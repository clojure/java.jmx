{:namespaces
 ({:source-url
   "https://github.com/clojure/java.jmx/blob/e5cc32e281de76849648897600655b9a9138b4de/src/main/clojure/clojure/java/jmx.clj",
   :wiki-url
   "http://clojure.github.com/java.jmx/clojure.java.jmx-api.html",
   :name "clojure.java.jmx",
   :author "Stuart Halloway",
   :doc
   "JMX support for Clojure\n\nUsage\n  (require '[clojure.java.jmx :as jmx])\n\nWhat beans do I have?\n\n  (jmx/mbean-names \"*:*\")\n  -> #<HashSet [java.lang:type=MemoryPool,name=CMS Old Gen, \n                java.lang:type=Memory, ...]\n\nWhat attributes does a bean have?\n\n  (jmx/attribute-names \"java.lang:type=Memory\")\n  -> (:Verbose :ObjectPendingFinalizationCount \n      :HeapMemoryUsage :NonHeapMemoryUsage)\n\nWhat is the value of an attribute? \n\n  (jmx/read \"java.lang:type=Memory\" :ObjectPendingFinalizationCount)\n  -> 0\n\nCan't I just have *all* the attributes in a Clojure map?\n\n  (jmx/mbean \"java.lang:type=Memory\")\n  -> {:NonHeapMemoryUsage\n       {:used 16674024, :max 138412032, :init 24317952, :committed 24317952},\n      :HeapMemoryUsage\n       {:used 18619064, :max 85393408, :init 0, :committed 83230720},\n      :ObjectPendingFinalizationCount 0,\n      :Verbose false}\n\nCan I find and invoke an operation?\n\n  (jmx/operation-names \"java.lang:type=Memory\")\n  -> (:gc)  \n  (jmx/invoke \"java.lang:type=Memory\" :gc)\n  -> nil\n\nWhat about some other process? Just run *any* of the above code\ninside a with-connection:\n\n  (jmx/with-connection {:host \"localhost\", :port 3000} \n    (jmx/mbean \"java.lang:type=Memory\"))\n  -> {:ObjectPendingFinalizationCount 0, \n      :HeapMemoryUsage ... etc.}\n\nCan I serve my own beans?  Sure, just drop a Clojure ref\ninto an instance of clojure.java.jmx.Bean, and the bean\nwill expose read-only attributes for every key/value pair\nin the ref:\n\n  (jmx/register-mbean\n     (create-bean\n     (ref {:string-attribute \"a-string\"}))\n     \"my.namespace:name=Value\")"}),
 :vars
 ({:name "*connection*",
   :namespace "clojure.java.jmx",
   :source-url
   "https://github.com/clojure/java.jmx/blob/e5cc32e281de76849648897600655b9a9138b4de/src/main/clojure/clojure/java/jmx.clj#L76",
   :dynamic true,
   :raw-source-url
   "https://github.com/clojure/java.jmx/raw/e5cc32e281de76849648897600655b9a9138b4de/src/main/clojure/clojure/java/jmx.clj",
   :wiki-url
   "http://clojure.github.com/java.jmx//clojure.java.jmx-api.html#clojure.java.jmx/*connection*",
   :doc
   "The connection to be used for JMX ops. Defaults to the local process.",
   :var-type "var",
   :line 76,
   :file "src/main/clojure/clojure/java/jmx.clj"}
  {:arglists ([object-name attr-name]),
   :name "attribute-info",
   :namespace "clojure.java.jmx",
   :source-url
   "https://github.com/clojure/java.jmx/blob/e5cc32e281de76849648897600655b9a9138b4de/src/main/clojure/clojure/java/jmx.clj#L216",
   :raw-source-url
   "https://github.com/clojure/java.jmx/raw/e5cc32e281de76849648897600655b9a9138b4de/src/main/clojure/clojure/java/jmx.clj",
   :wiki-url
   "http://clojure.github.com/java.jmx//clojure.java.jmx-api.html#clojure.java.jmx/attribute-info",
   :doc "Get the MBeanAttributeInfo for an attribute.",
   :var-type "function",
   :line 216,
   :file "src/main/clojure/clojure/java/jmx.clj"}
  {:arglists ([n]),
   :name "attribute-names",
   :namespace "clojure.java.jmx",
   :source-url
   "https://github.com/clojure/java.jmx/blob/e5cc32e281de76849648897600655b9a9138b4de/src/main/clojure/clojure/java/jmx.clj#L253",
   :raw-source-url
   "https://github.com/clojure/java.jmx/raw/e5cc32e281de76849648897600655b9a9138b4de/src/main/clojure/clojure/java/jmx.clj",
   :wiki-url
   "http://clojure.github.com/java.jmx//clojure.java.jmx-api.html#clojure.java.jmx/attribute-names",
   :doc "All attribute names available on an MBean.",
   :var-type "function",
   :line 253,
   :file "src/main/clojure/clojure/java/jmx.clj"}
  {:arglists ([state-ref]),
   :name "create-bean",
   :namespace "clojure.java.jmx",
   :source-url
   "https://github.com/clojure/java.jmx/blob/e5cc32e281de76849648897600655b9a9138b4de/src/main/clojure/clojure/java/jmx.clj#L299",
   :raw-source-url
   "https://github.com/clojure/java.jmx/raw/e5cc32e281de76849648897600655b9a9138b4de/src/main/clojure/clojure/java/jmx.clj",
   :wiki-url
   "http://clojure.github.com/java.jmx//clojure.java.jmx-api.html#clojure.java.jmx/create-bean",
   :doc
   "Expose a reference as a JMX bean. state-ref should be a Clojure\nreference (ref, atom, agent) containing a map.",
   :var-type "function",
   :line 299,
   :file "src/main/clojure/clojure/java/jmx.clj"}
  {:arglists ([n op & args]),
   :name "invoke",
   :namespace "clojure.java.jmx",
   :source-url
   "https://github.com/clojure/java.jmx/blob/e5cc32e281de76849648897600655b9a9138b4de/src/main/clojure/clojure/java/jmx.clj#L264",
   :raw-source-url
   "https://github.com/clojure/java.jmx/raw/e5cc32e281de76849648897600655b9a9138b4de/src/main/clojure/clojure/java/jmx.clj",
   :wiki-url
   "http://clojure.github.com/java.jmx//clojure.java.jmx-api.html#clojure.java.jmx/invoke",
   :doc "Invoke an operation an an MBean.",
   :var-type "function",
   :line 264,
   :file "src/main/clojure/clojure/java/jmx.clj"}
  {:arglists ([] [overrides]),
   :name "jmx-url",
   :namespace "clojure.java.jmx",
   :source-url
   "https://github.com/clojure/java.jmx/blob/e5cc32e281de76849648897600655b9a9138b4de/src/main/clojure/clojure/java/jmx.clj#L83",
   :raw-source-url
   "https://github.com/clojure/java.jmx/raw/e5cc32e281de76849648897600655b9a9138b4de/src/main/clojure/clojure/java/jmx.clj",
   :wiki-url
   "http://clojure.github.com/java.jmx//clojure.java.jmx-api.html#clojure.java.jmx/jmx-url",
   :doc "Build a JMX URL from options.",
   :var-type "function",
   :line 83,
   :file "src/main/clojure/clojure/java/jmx.clj"}
  {:arglists ([n]),
   :name "mbean",
   :namespace "clojure.java.jmx",
   :source-url
   "https://github.com/clojure/java.jmx/blob/e5cc32e281de76849648897600655b9a9138b4de/src/main/clojure/clojure/java/jmx.clj#L274",
   :raw-source-url
   "https://github.com/clojure/java.jmx/raw/e5cc32e281de76849648897600655b9a9138b4de/src/main/clojure/clojure/java/jmx.clj",
   :wiki-url
   "http://clojure.github.com/java.jmx//clojure.java.jmx-api.html#clojure.java.jmx/mbean",
   :doc
   "Like clojure.core/bean, but for JMX beans. Returns a read-only map of\na JMX bean's attributes. If an attribute it not supported, value is\nset to the exception thrown.",
   :var-type "function",
   :line 274,
   :file "src/main/clojure/clojure/java/jmx.clj"}
  {:arglists ([n]),
   :name "mbean-names",
   :namespace "clojure.java.jmx",
   :source-url
   "https://github.com/clojure/java.jmx/blob/e5cc32e281de76849648897600655b9a9138b4de/src/main/clojure/clojure/java/jmx.clj#L248",
   :raw-source-url
   "https://github.com/clojure/java.jmx/raw/e5cc32e281de76849648897600655b9a9138b4de/src/main/clojure/clojure/java/jmx.clj",
   :wiki-url
   "http://clojure.github.com/java.jmx//clojure.java.jmx-api.html#clojure.java.jmx/mbean-names",
   :doc
   "Finds all MBeans matching a name on the current *connection*.",
   :var-type "function",
   :line 248,
   :file "src/main/clojure/clojure/java/jmx.clj"}
  {:file nil,
   :raw-source-url nil,
   :source-url nil,
   :wiki-url
   "http://clojure.github.com/java.jmx//clojure.java.jmx-api.html#clojure.java.jmx/objects->data",
   :namespace "clojure.java.jmx",
   :var-type "function",
   :arglists ([_]),
   :doc
   "Convert JMX object model into data. Handles CompositeData, TabularData, maps, and atoms.",
   :name "objects->data"}
  {:arglists ([n]),
   :name "operation-names",
   :namespace "clojure.java.jmx",
   :source-url
   "https://github.com/clojure/java.jmx/blob/e5cc32e281de76849648897600655b9a9138b4de/src/main/clojure/clojure/java/jmx.clj#L259",
   :raw-source-url
   "https://github.com/clojure/java.jmx/raw/e5cc32e281de76849648897600655b9a9138b4de/src/main/clojure/clojure/java/jmx.clj",
   :wiki-url
   "http://clojure.github.com/java.jmx//clojure.java.jmx-api.html#clojure.java.jmx/operation-names",
   :doc "All operation names available on an MBean.",
   :var-type "function",
   :line 259,
   :file "src/main/clojure/clojure/java/jmx.clj"}
  {:arglists ([n]),
   :name "operations",
   :namespace "clojure.java.jmx",
   :source-url
   "https://github.com/clojure/java.jmx/blob/e5cc32e281de76849648897600655b9a9138b4de/src/main/clojure/clojure/java/jmx.clj#L227",
   :raw-source-url
   "https://github.com/clojure/java.jmx/raw/e5cc32e281de76849648897600655b9a9138b4de/src/main/clojure/clojure/java/jmx.clj",
   :wiki-url
   "http://clojure.github.com/java.jmx//clojure.java.jmx-api.html#clojure.java.jmx/operations",
   :doc "All oeprations available on an MBean.",
   :var-type "function",
   :line 227,
   :file "src/main/clojure/clojure/java/jmx.clj"}
  {:arglists ([n attr]),
   :name "raw-read",
   :namespace "clojure.java.jmx",
   :source-url
   "https://github.com/clojure/java.jmx/blob/e5cc32e281de76849648897600655b9a9138b4de/src/main/clojure/clojure/java/jmx.clj#L187",
   :raw-source-url
   "https://github.com/clojure/java.jmx/raw/e5cc32e281de76849648897600655b9a9138b4de/src/main/clojure/clojure/java/jmx.clj",
   :wiki-url
   "http://clojure.github.com/java.jmx//clojure.java.jmx-api.html#clojure.java.jmx/raw-read",
   :doc
   "Read an mbean property. Returns low-level Java object model for\ncomposites, tabulars, etc. Most callers should use read.",
   :var-type "function",
   :line 187,
   :file "src/main/clojure/clojure/java/jmx.clj"}
  {:file "src/main/clojure/clojure/java/jmx.clj",
   :raw-source-url
   "https://github.com/clojure/java.jmx/raw/e5cc32e281de76849648897600655b9a9138b4de/src/main/clojure/clojure/java/jmx.clj",
   :source-url
   "https://github.com/clojure/java.jmx/blob/e5cc32e281de76849648897600655b9a9138b4de/src/main/clojure/clojure/java/jmx.clj#L193",
   :wiki-url
   "http://clojure.github.com/java.jmx//clojure.java.jmx-api.html#clojure.java.jmx/read",
   :namespace "clojure.java.jmx",
   :line 193,
   :var-type "var",
   :doc "Read an mbean property.",
   :name "read"}
  {:arglists ([n attr]),
   :name "readable?",
   :namespace "clojure.java.jmx",
   :source-url
   "https://github.com/clojure/java.jmx/blob/e5cc32e281de76849648897600655b9a9138b4de/src/main/clojure/clojure/java/jmx.clj#L222",
   :raw-source-url
   "https://github.com/clojure/java.jmx/raw/e5cc32e281de76849648897600655b9a9138b4de/src/main/clojure/clojure/java/jmx.clj",
   :wiki-url
   "http://clojure.github.com/java.jmx//clojure.java.jmx-api.html#clojure.java.jmx/readable?",
   :doc "Is attribute readable?",
   :var-type "function",
   :line 222,
   :file "src/main/clojure/clojure/java/jmx.clj"}
  {:arglists ([mbean mbean-name]),
   :name "register-mbean",
   :namespace "clojure.java.jmx",
   :source-url
   "https://github.com/clojure/java.jmx/blob/e5cc32e281de76849648897600655b9a9138b4de/src/main/clojure/clojure/java/jmx.clj#L243",
   :raw-source-url
   "https://github.com/clojure/java.jmx/raw/e5cc32e281de76849648897600655b9a9138b4de/src/main/clojure/clojure/java/jmx.clj",
   :wiki-url
   "http://clojure.github.com/java.jmx//clojure.java.jmx-api.html#clojure.java.jmx/register-mbean",
   :doc "Register an mbean with the current *connection*.",
   :var-type "function",
   :line 243,
   :file "src/main/clojure/clojure/java/jmx.clj"}
  {:arglists ([opts & body]),
   :name "with-connection",
   :namespace "clojure.java.jmx",
   :source-url
   "https://github.com/clojure/java.jmx/blob/e5cc32e281de76849648897600655b9a9138b4de/src/main/clojure/clojure/java/jmx.clj#L171",
   :raw-source-url
   "https://github.com/clojure/java.jmx/raw/e5cc32e281de76849648897600655b9a9138b4de/src/main/clojure/clojure/java/jmx.clj",
   :wiki-url
   "http://clojure.github.com/java.jmx//clojure.java.jmx-api.html#clojure.java.jmx/with-connection",
   :doc
   "Execute body with JMX connection specified by opts. opts can also\ninclude an optional :environment key which is passed as the\nenvironment arg to JMXConnectorFactory/connect.",
   :var-type "macro",
   :line 171,
   :file "src/main/clojure/clojure/java/jmx.clj"}
  {:arglists ([n attr value]),
   :name "write!",
   :namespace "clojure.java.jmx",
   :source-url
   "https://github.com/clojure/java.jmx/blob/e5cc32e281de76849648897600655b9a9138b4de/src/main/clojure/clojure/java/jmx.clj#L208",
   :raw-source-url
   "https://github.com/clojure/java.jmx/raw/e5cc32e281de76849648897600655b9a9138b4de/src/main/clojure/clojure/java/jmx.clj",
   :wiki-url
   "http://clojure.github.com/java.jmx//clojure.java.jmx-api.html#clojure.java.jmx/write!",
   :doc "Write an attribute value.",
   :var-type "function",
   :line 208,
   :file "src/main/clojure/clojure/java/jmx.clj"})}
