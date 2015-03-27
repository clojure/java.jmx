;; Copyright (c) Stuart Halloway. All rights reserved.  The use
;; and distribution terms for this software are covered by the Eclipse
;; Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this
;; distribution.  By using this software in any fashion, you are
;; agreeing to be bound by the terms of this license.  You must not
;; remove this notice, or any other, from this software.


(ns ^{:author "Stuart Halloway"
       :doc "JMX support for Clojure

  Usage
    (require '[clojure.java.jmx :as jmx])

  What beans do I have?

    (jmx/mbean-names \"*:*\")
    -> #<HashSet [java.lang:type=MemoryPool,name=CMS Old Gen,
                  java.lang:type=Memory, ...]

  What attributes does a bean have?

    (jmx/attribute-names \"java.lang:type=Memory\")
    -> (:Verbose :ObjectPendingFinalizationCount
        :HeapMemoryUsage :NonHeapMemoryUsage)

  What is the value of an attribute?

    (jmx/read \"java.lang:type=Memory\" :ObjectPendingFinalizationCount)
    -> 0
    (jmx/read \"java.lang:type=Memory\" [:HeapMemoryUsage :NonHeapMemoryUsage])
    ->
    {:NonHeapMemoryUsage
      {:used 16674024, :max 138412032, :init 24317952, :committed 24317952},
     :HeapMemoryUsage
      {:used 18619064, :max 85393408, :init 0, :committed 83230720}}

  Can't I just have *all* the attributes in a Clojure map?

    (jmx/mbean \"java.lang:type=Memory\")
    -> {:NonHeapMemoryUsage
         {:used 16674024, :max 138412032, :init 24317952, :committed 24317952},
        :HeapMemoryUsage
         {:used 18619064, :max 85393408, :init 0, :committed 83230720},
        :ObjectPendingFinalizationCount 0,
        :Verbose false}

  Can I find and invoke an operation?

    (jmx/operation-names \"java.lang:type=Memory\")
    -> (:gc)
    (jmx/invoke \"java.lang:type=Memory\" :gc)
    -> nil

  What about some other process? Just run *any* of the above code
  inside a with-connection:

    (jmx/with-connection {:host \"localhost\", :port 3000}
      (jmx/mbean \"java.lang:type=Memory\"))
    -> {:ObjectPendingFinalizationCount 0,
        :HeapMemoryUsage ... etc.}

  Can I serve my own beans?  Sure, just drop a Clojure ref
  into an instance of clojure.java.jmx.Bean, and the bean
  will expose read-only attributes for every key/value pair
  in the ref:

    (jmx/register-mbean
       (create-bean
       (ref {:string-attribute \"a-string\"}))
       \"my.namespace:name=Value\")"}
  clojure.java.jmx
  (:refer-clojure :exclude [read])
  (:use [clojure.walk :only [postwalk]])
  (:import [clojure.lang Associative]
           java.lang.management.ManagementFactory
           [javax.management Attribute AttributeList DynamicMBean MBeanInfo
            ObjectName RuntimeMBeanException MBeanAttributeInfo]
           [javax.management.remote JMXConnectorFactory JMXServiceURL]))

(def ^{:dynamic true
       :doc "The connection to be used for JMX ops. Defaults to the local process."
       :skip-wiki true}
  *connection*
  (ManagementFactory/getPlatformMBeanServer))

(declare jmx->clj)

(defn ^{:skip-wiki true} jmx-url
  "Build a JMX URL from options."
  ([] (jmx-url {}))
  ([overrides]
     (let [opts (merge {:host "localhost", :port "3000", :jndi-path "jmxrmi"} overrides)]
       (format "service:jmx:rmi:///jndi/rmi://%s:%s/%s" (opts :host) (opts :port) (opts :jndi-path)))))

(defprotocol CoercionImpl
  (as-object-name [_]))

(extend-protocol CoercionImpl
  String
  (as-object-name [_] (ObjectName. _))

  ObjectName
  (as-object-name [_] _))

(defn- maybe-keywordize
  "Convert a string key to a keyword, leaving other types alone. Used to
   simplify keys in the tabular data API."
  [s]
  (if (string? s) (keyword s) s))

(defn- maybe-atomize
  "Convert a list of length 1 into its contents, leaving other things alone.
  Used to simplify keys in the tabular data API."
  [k]
  (if (and (instance? java.util.List k)
           (= 1 (count k)))
    (first k)
    k))

(def ^{:private true} simplify-tabular-data-key
  (comp maybe-keywordize maybe-atomize))

(defprotocol ^{:skip-wiki true} Destract
  (objects->data [_] "Convert JMX object model into data. Handles CompositeData, TabularData, maps, and atoms."))

(extend-protocol Destract
  javax.management.openmbean.CompositeData
  (objects->data
   [cd]
   (into {}
         (map (fn [attr] [(keyword attr) (objects->data (.get cd attr))])
              (.. cd getCompositeType keySet))))

  javax.management.openmbean.TabularData
  (objects->data
   [td]
   (into {}
         (map (fn [k]
                [(simplify-tabular-data-key k) (objects->data (.get td (into-array k)))])
              (.keySet td))))
  clojure.lang.Associative
  (objects->data
   [m]
   (into {} (zipmap (keys m) (map objects->data (vals m)))))

  java.lang.Boolean
  (objects->data [b] (boolean b))
  Object
  (objects->data [obj] obj)
  nil
  (objects->data [_] nil))

(def ^{:private true} guess-attribute-map
     {"java.lang.Integer" "int"
      "java.lang.Boolean" "boolean"
      "java.lang.Long" "long"
      })

(defn- guess-attribute-typename
  "Guess the attribute typename for MBeanAttributeInfo based on the attribute value."
  [value]
  (let [classname (.getName (class value))]
    (get guess-attribute-map classname classname)))

(defn- build-attribute-info
  "Construct an MBeanAttributeInfo. Normally called with a key/value pair from a Clojure map."
  ([attr-name attr-value]
     (build-attribute-info
      (name attr-name)
      (guess-attribute-typename attr-value)
      (name attr-name) true false false))
  ([name type desc readable? writable? is?] (MBeanAttributeInfo. name type desc readable? writable? is? )))

(defn- map->attribute-infos
  "Construct an MBeanAttributeInfo[] from a Clojure associative."
  [attr-map]
  (into-array MBeanAttributeInfo
              (map (fn [[attr-name value]] (build-attribute-info attr-name value))
                   attr-map)))

(defmacro with-connection
  "Execute body with a JMX connection created based on opts. opts can include [default]:

     :host        The host to connect to [localhost]
     :port        The port to connect to [3000]
     :jndi-path   The jndi-path to use [jmxuri]
     :url         The full url (as a String) to use instead of generating a rmi url from
                  the above options [nil]
     :environment A map representing the environment used for the connection.
                  See JMXConnectorFactory/connect for details [{}]"
  [opts & body]
  `(let [opts# ~opts
         env# (get opts# :environment {})
         opts# (dissoc opts# :environment)]
     (with-open [connector# (javax.management.remote.JMXConnectorFactory/connect
                             (JMXServiceURL. (:url opts# (jmx-url opts#)))
                             env#)]
       (binding [*connection* (.getMBeanServerConnection connector#)]
         ~@body))))

(defn ^{:skip-wiki true} mbean-info [n]
  (.getMBeanInfo *connection* (as-object-name n)))

(defn ^{:skip-wiki true} raw-read
  "Read a list of mbean properties. Returns low-level Java object
   models for composites, tabulars, etc. Most callers should use
   read."
  [n attrs]
  (if (sequential? attrs)
    (into {}
          (map (fn [attr] [(keyword (.getName attr)) (.getValue attr)])
               (.getAttributes *connection*
                               (as-object-name n)
                               (into-array (map name attrs)))))
    (.getAttribute *connection* (as-object-name n) (name attrs))))

(def ^{:doc "Read one or more mbean properties."}
  read
  (comp objects->data raw-read))

(defn- read-supported
  "Calls read to read an mbean property, *returning* unsupported
   operation exceptions instead of throwing them. Used to keep mbean
   from blowing up. Note: There is no good exception that aggregates
   unsupported operations, hence the overly-general catch block."
  [n attr]
  (try
   (read n attr)
   (catch Exception e
     e)))

(defn write!
  "Write an attribute value."
  [n attr value]
  (.setAttribute
   *connection*
   (as-object-name n)
   (Attribute. (name attr) value)))

(defn ^{:skip-wiki true} attribute-info
  "Get the MBeanAttributeInfo for an attribute."
  [object-name attr-name]
  (first
    (filter #(= (name attr-name) (.getName %))
            (.getAttributes (mbean-info object-name)))))

(defn readable?
  "Is attribute readable?"
  [n attr]
  (.isReadable (attribute-info n attr)))

(defn ^{:skip-wiki true} operations
  "All operations available on an MBean."
  [n]
  (.getOperations (mbean-info n)))

(defn- operation
  "The MBeanOperationInfo for operation op on mbean n. Used by invoke."
  [n op]
  (first  (filter #(= (-> % .getName keyword) op) (operations n))))

(defn- op-param-types
  "The parameter types (as class name strings) for operation op on n.
   Used for invoke."
  [n op]
  (map #(-> % .getType) (.getSignature (operation n op))))

(defn register-mbean
  "Register an mbean with the current *connection*."
  [mbean mbean-name]
  (.registerMBean *connection* mbean (as-object-name mbean-name)))

(defn mbean-names
  "Finds all MBeans matching a name on the current *connection*."
   [n]
  (.queryNames *connection* (as-object-name n) nil))

(defn attribute-names
  "All attribute names available on an MBean."
  [n]
  (doall (map #(-> % .getName keyword)
              (.getAttributes (mbean-info n)))))

(defn operation-names
  "All operation names available on an MBean."
  [n]
  (doall (map #(-> % .getName keyword) (operations n))))

(defn invoke-signature
  "Invoke an operation an an MBean. You must also supply
  the signature of the operation. This is useful in cases
  where the operation is overloaded. Otherwise you should
  use the 'invoke' operation which will determine the
  signature for you.

  The signature parameter is a sequence of strings that
  describes the method parameter types in order."
  [n op signature & args]
  (if ( seq args)
    (.invoke *connection* (as-object-name n) (name op)
             (into-array Object args)
             (into-array String signature))
    (.invoke *connection* (as-object-name n) (name op)
             nil nil)))

(defn invoke
  "Invoke an operation an an MBean. See also: invoke-signature"
  [n op & args]
  (apply invoke-signature n op (op-param-types n op) args))

(defn mbean
  "Like clojure.core/bean, but for JMX beans. Returns a read-only map of
   a JMX bean's attributes. If an attribute it not supported, value is
   set to the exception thrown."
  [n]
  (into {} (map (fn [attr-name] [(keyword attr-name) (read-supported n attr-name)])
                (attribute-names n))))

(deftype Bean [state-ref]
  DynamicMBean
  (getMBeanInfo [_]
                (MBeanInfo. (.. _ getClass getName)                             ; class name
                            "Clojure Dynamic MBean"                             ; description
                            (map->attribute-infos @state-ref)                   ; attributes
                            nil                                                 ; constructors
                            nil                                                 ; operations
                            nil))
  (getAttribute [_ attr]
                (@state-ref (keyword attr)))
  (getAttributes [_ attrs]
                 (let [result (AttributeList.)]
                   (doseq [attr attrs]
                     (.add result (Attribute. attr (.getAttribute _ attr))))
                   result)))

(defn create-bean
  "Expose a reference as a JMX bean. state-ref should be a Clojure
   reference (ref, atom, agent) containing a map."
  [state-ref]
  (Bean. state-ref))
