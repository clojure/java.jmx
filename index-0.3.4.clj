{:namespaces
 ({:doc
   "JMX support for Clojure\n\nUsage\n  (require '[clojure.java.jmx :as jmx])\n\nWhat beans do I have?\n\n  (jmx/mbean-names \"*:*\")\n  -> #<HashSet [java.lang:type=MemoryPool,name=CMS Old Gen,\n                java.lang:type=Memory, ...]\n\nWhat attributes does a bean have?\n\n  (jmx/attribute-names \"java.lang:type=Memory\")\n  -> (:Verbose :ObjectPendingFinalizationCount\n      :HeapMemoryUsage :NonHeapMemoryUsage)\n\nWhat is the value of an attribute?\n\n  (jmx/read \"java.lang:type=Memory\" :ObjectPendingFinalizationCount)\n  -> 0\n  (jmx/read \"java.lang:type=Memory\" [:HeapMemoryUsage :NonHeapMemoryUsage])\n  ->\n  {:NonHeapMemoryUsage\n    {:used 16674024, :max 138412032, :init 24317952, :committed 24317952},\n   :HeapMemoryUsage\n    {:used 18619064, :max 85393408, :init 0, :committed 83230720}}\n\nCan't I just have *all* the attributes in a Clojure map?\n\n  (jmx/mbean \"java.lang:type=Memory\")\n  -> {:NonHeapMemoryUsage\n       {:used 16674024, :max 138412032, :init 24317952, :committed 24317952},\n      :HeapMemoryUsage\n       {:used 18619064, :max 85393408, :init 0, :committed 83230720},\n      :ObjectPendingFinalizationCount 0,\n      :Verbose false}\n\nCan I find and invoke an operation?\n\n  (jmx/operation-names \"java.lang:type=Memory\")\n  -> (:gc)\n  (jmx/invoke \"java.lang:type=Memory\" :gc)\n  -> nil\n\nWhat about some other process? Just run *any* of the above code\ninside a with-connection:\n\n  (jmx/with-connection {:host \"localhost\", :port 3000}\n    (jmx/mbean \"java.lang:type=Memory\"))\n  -> {:ObjectPendingFinalizationCount 0,\n      :HeapMemoryUsage ... etc.}\n\nCan I serve my own beans?  Sure, just drop a Clojure ref\ninto an instance of clojure.java.jmx.Bean, and the bean\nwill expose read-only attributes for every key/value pair\nin the ref:\n\n  (jmx/register-mbean\n     (create-bean\n     (ref {:string-attribute \"a-string\"}))\n     \"my.namespace:name=Value\")",
   :author "Stuart Halloway",
   :name "clojure.java.jmx",
   :wiki-url
   "http://clojure.github.com/java.jmx/clojure.java.jmx-api.html",
   :source-url
   "https://github.com/clojure/java.jmx/blob/f7fad3b9d8a376d3d7efe2b7cfcacc5b1ec5e585/src/main/clojure/clojure/java/jmx.clj"}),
 :vars
 ({:raw-source-url
   "https://github.com/clojure/java.jmx/raw/f7fad3b9d8a376d3d7efe2b7cfcacc5b1ec5e585/src/main/clojure/clojure/java/jmx.clj",
   :name "->Bean",
   :file "src/main/clojure/clojure/java/jmx.clj",
   :source-url
   "https://github.com/clojure/java.jmx/blob/f7fad3b9d8a376d3d7efe2b7cfcacc5b1ec5e585/src/main/clojure/clojure/java/jmx.clj#L335",
   :line 335,
   :var-type "function",
   :arglists ([state-ref]),
   :doc "Positional factory function for class clojure.java.jmx.Bean.",
   :namespace "clojure.java.jmx",
   :wiki-url
   "http://clojure.github.com/java.jmx//clojure.java.jmx-api.html#clojure.java.jmx/->Bean"}
  {:raw-source-url
   "https://github.com/clojure/java.jmx/raw/f7fad3b9d8a376d3d7efe2b7cfcacc5b1ec5e585/src/main/clojure/clojure/java/jmx.clj",
   :name "attribute-names",
   :file "src/main/clojure/clojure/java/jmx.clj",
   :source-url
   "https://github.com/clojure/java.jmx/blob/f7fad3b9d8a376d3d7efe2b7cfcacc5b1ec5e585/src/main/clojure/clojure/java/jmx.clj#L292",
   :line 292,
   :var-type "function",
   :arglists ([n]),
   :doc "All attribute names available on an MBean.",
   :namespace "clojure.java.jmx",
   :wiki-url
   "http://clojure.github.com/java.jmx//clojure.java.jmx-api.html#clojure.java.jmx/attribute-names"}
  {:raw-source-url
   "https://github.com/clojure/java.jmx/raw/f7fad3b9d8a376d3d7efe2b7cfcacc5b1ec5e585/src/main/clojure/clojure/java/jmx.clj",
   :name "create-bean",
   :file "src/main/clojure/clojure/java/jmx.clj",
   :source-url
   "https://github.com/clojure/java.jmx/blob/f7fad3b9d8a376d3d7efe2b7cfcacc5b1ec5e585/src/main/clojure/clojure/java/jmx.clj#L374",
   :line 374,
   :var-type "function",
   :arglists ([state-ref]),
   :doc
   "Expose a reference as a JMX bean. state-ref should be a Clojure\nreference (ref, atom, agent) containing a map.\n\nUsing an agent for the state-ref is not recommended when the bean may\nbe modified with the setAttribute(s) methods. The setAttribute(s) methods\nwill block on the agent to complete all submitted actions (via await).",
   :namespace "clojure.java.jmx",
   :wiki-url
   "http://clojure.github.com/java.jmx//clojure.java.jmx-api.html#clojure.java.jmx/create-bean"}
  {:raw-source-url
   "https://github.com/clojure/java.jmx/raw/f7fad3b9d8a376d3d7efe2b7cfcacc5b1ec5e585/src/main/clojure/clojure/java/jmx.clj",
   :name "invoke",
   :file "src/main/clojure/clojure/java/jmx.clj",
   :source-url
   "https://github.com/clojure/java.jmx/blob/f7fad3b9d8a376d3d7efe2b7cfcacc5b1ec5e585/src/main/clojure/clojure/java/jmx.clj#L322",
   :line 322,
   :var-type "function",
   :arglists ([n op & args]),
   :doc "Invoke an operation an an MBean. See also: invoke-signature",
   :namespace "clojure.java.jmx",
   :wiki-url
   "http://clojure.github.com/java.jmx//clojure.java.jmx-api.html#clojure.java.jmx/invoke"}
  {:raw-source-url
   "https://github.com/clojure/java.jmx/raw/f7fad3b9d8a376d3d7efe2b7cfcacc5b1ec5e585/src/main/clojure/clojure/java/jmx.clj",
   :name "invoke-signature",
   :file "src/main/clojure/clojure/java/jmx.clj",
   :source-url
   "https://github.com/clojure/java.jmx/blob/f7fad3b9d8a376d3d7efe2b7cfcacc5b1ec5e585/src/main/clojure/clojure/java/jmx.clj#L305",
   :line 305,
   :var-type "function",
   :arglists ([n op signature & args]),
   :doc
   "Invoke an operation an an MBean. You must also supply\nthe signature of the operation. This is useful in cases\nwhere the operation is overloaded. Otherwise you should\nuse the 'invoke' operation which will determine the\nsignature for you.\n\nThe signature parameter is a sequence of strings that\ndescribes the method parameter types in order.",
   :namespace "clojure.java.jmx",
   :wiki-url
   "http://clojure.github.com/java.jmx//clojure.java.jmx-api.html#clojure.java.jmx/invoke-signature"}
  {:raw-source-url
   "https://github.com/clojure/java.jmx/raw/f7fad3b9d8a376d3d7efe2b7cfcacc5b1ec5e585/src/main/clojure/clojure/java/jmx.clj",
   :name "mbean",
   :file "src/main/clojure/clojure/java/jmx.clj",
   :source-url
   "https://github.com/clojure/java.jmx/blob/f7fad3b9d8a376d3d7efe2b7cfcacc5b1ec5e585/src/main/clojure/clojure/java/jmx.clj#L327",
   :line 327,
   :var-type "function",
   :arglists ([n]),
   :doc
   "Like clojure.core/bean, but for JMX beans. Returns a read-only map of\na JMX bean's attributes. If an attribute it not supported, value is\nset to the exception thrown.",
   :namespace "clojure.java.jmx",
   :wiki-url
   "http://clojure.github.com/java.jmx//clojure.java.jmx-api.html#clojure.java.jmx/mbean"}
  {:raw-source-url
   "https://github.com/clojure/java.jmx/raw/f7fad3b9d8a376d3d7efe2b7cfcacc5b1ec5e585/src/main/clojure/clojure/java/jmx.clj",
   :name "mbean-names",
   :file "src/main/clojure/clojure/java/jmx.clj",
   :source-url
   "https://github.com/clojure/java.jmx/blob/f7fad3b9d8a376d3d7efe2b7cfcacc5b1ec5e585/src/main/clojure/clojure/java/jmx.clj#L287",
   :line 287,
   :var-type "function",
   :arglists ([n]),
   :doc
   "Finds all MBeans matching a name on the current *connection*.",
   :namespace "clojure.java.jmx",
   :wiki-url
   "http://clojure.github.com/java.jmx//clojure.java.jmx-api.html#clojure.java.jmx/mbean-names"}
  {:raw-source-url
   "https://github.com/clojure/java.jmx/raw/f7fad3b9d8a376d3d7efe2b7cfcacc5b1ec5e585/src/main/clojure/clojure/java/jmx.clj",
   :name "operation-names",
   :file "src/main/clojure/clojure/java/jmx.clj",
   :source-url
   "https://github.com/clojure/java.jmx/blob/f7fad3b9d8a376d3d7efe2b7cfcacc5b1ec5e585/src/main/clojure/clojure/java/jmx.clj#L299",
   :line 299,
   :var-type "function",
   :arglists ([n]),
   :doc "All operation names available on an MBean.",
   :namespace "clojure.java.jmx",
   :wiki-url
   "http://clojure.github.com/java.jmx//clojure.java.jmx-api.html#clojure.java.jmx/operation-names"}
  {:raw-source-url
   "https://github.com/clojure/java.jmx/raw/f7fad3b9d8a376d3d7efe2b7cfcacc5b1ec5e585/src/main/clojure/clojure/java/jmx.clj",
   :name "read",
   :file "src/main/clojure/clojure/java/jmx.clj",
   :source-url
   "https://github.com/clojure/java.jmx/blob/f7fad3b9d8a376d3d7efe2b7cfcacc5b1ec5e585/src/main/clojure/clojure/java/jmx.clj#L224",
   :line 224,
   :var-type "var",
   :arglists nil,
   :doc "Read one or more mbean properties.",
   :namespace "clojure.java.jmx",
   :wiki-url
   "http://clojure.github.com/java.jmx//clojure.java.jmx-api.html#clojure.java.jmx/read"}
  {:raw-source-url
   "https://github.com/clojure/java.jmx/raw/f7fad3b9d8a376d3d7efe2b7cfcacc5b1ec5e585/src/main/clojure/clojure/java/jmx.clj",
   :name "readable?",
   :file "src/main/clojure/clojure/java/jmx.clj",
   :source-url
   "https://github.com/clojure/java.jmx/blob/f7fad3b9d8a376d3d7efe2b7cfcacc5b1ec5e585/src/main/clojure/clojure/java/jmx.clj#L254",
   :line 254,
   :var-type "function",
   :arglists ([n attr]),
   :doc "Is attribute readable?",
   :namespace "clojure.java.jmx",
   :wiki-url
   "http://clojure.github.com/java.jmx//clojure.java.jmx-api.html#clojure.java.jmx/readable?"}
  {:raw-source-url
   "https://github.com/clojure/java.jmx/raw/f7fad3b9d8a376d3d7efe2b7cfcacc5b1ec5e585/src/main/clojure/clojure/java/jmx.clj",
   :name "register-mbean",
   :file "src/main/clojure/clojure/java/jmx.clj",
   :source-url
   "https://github.com/clojure/java.jmx/blob/f7fad3b9d8a376d3d7efe2b7cfcacc5b1ec5e585/src/main/clojure/clojure/java/jmx.clj#L277",
   :line 277,
   :var-type "function",
   :arglists ([mbean mbean-name]),
   :doc "Register an mbean with the current *connection*.",
   :namespace "clojure.java.jmx",
   :wiki-url
   "http://clojure.github.com/java.jmx//clojure.java.jmx-api.html#clojure.java.jmx/register-mbean"}
  {:raw-source-url
   "https://github.com/clojure/java.jmx/raw/f7fad3b9d8a376d3d7efe2b7cfcacc5b1ec5e585/src/main/clojure/clojure/java/jmx.clj",
   :name "unregister-mbean",
   :file "src/main/clojure/clojure/java/jmx.clj",
   :source-url
   "https://github.com/clojure/java.jmx/blob/f7fad3b9d8a376d3d7efe2b7cfcacc5b1ec5e585/src/main/clojure/clojure/java/jmx.clj#L282",
   :line 282,
   :var-type "function",
   :arglists ([mbean-name]),
   :doc
   "Unregister mbean named mbean-name with the current *connection*.",
   :namespace "clojure.java.jmx",
   :wiki-url
   "http://clojure.github.com/java.jmx//clojure.java.jmx-api.html#clojure.java.jmx/unregister-mbean"}
  {:raw-source-url
   "https://github.com/clojure/java.jmx/raw/f7fad3b9d8a376d3d7efe2b7cfcacc5b1ec5e585/src/main/clojure/clojure/java/jmx.clj",
   :name "with-connection",
   :file "src/main/clojure/clojure/java/jmx.clj",
   :source-url
   "https://github.com/clojure/java.jmx/blob/f7fad3b9d8a376d3d7efe2b7cfcacc5b1ec5e585/src/main/clojure/clojure/java/jmx.clj#L187",
   :line 187,
   :var-type "macro",
   :arglists ([opts & body]),
   :doc
   "Execute body with a JMX connection created based on opts. opts can include [default]:\n\n:host        The host to connect to [localhost]\n:port        The port to connect to [3000]\n:jndi-path   The jndi-path to use [jmxuri]\n:url         The full url (as a String) to use instead of generating a rmi url from\n             the above options [nil]\n:environment A map representing the environment used for the connection.\n             See JMXConnectorFactory/connect for details [{}]",
   :namespace "clojure.java.jmx",
   :wiki-url
   "http://clojure.github.com/java.jmx//clojure.java.jmx-api.html#clojure.java.jmx/with-connection"}
  {:raw-source-url
   "https://github.com/clojure/java.jmx/raw/f7fad3b9d8a376d3d7efe2b7cfcacc5b1ec5e585/src/main/clojure/clojure/java/jmx.clj",
   :name "write!",
   :file "src/main/clojure/clojure/java/jmx.clj",
   :source-url
   "https://github.com/clojure/java.jmx/blob/f7fad3b9d8a376d3d7efe2b7cfcacc5b1ec5e585/src/main/clojure/clojure/java/jmx.clj#L239",
   :line 239,
   :var-type "function",
   :arglists ([n attr value]),
   :doc "Write an attribute value.",
   :namespace "clojure.java.jmx",
   :wiki-url
   "http://clojure.github.com/java.jmx//clojure.java.jmx-api.html#clojure.java.jmx/write!"}
  {:name "Bean",
   :var-type "type",
   :namespace "clojure.java.jmx",
   :arglists nil,
   :wiki-url
   "http://clojure.github.com/java.jmx//clojure.java.jmx-api.html#clojure.java.jmx/Bean",
   :source-url nil,
   :raw-source-url nil,
   :file nil}
  {:raw-source-url
   "https://github.com/clojure/java.jmx/raw/f7fad3b9d8a376d3d7efe2b7cfcacc5b1ec5e585/src/main/clojure/clojure/java/jmx.clj",
   :name "Destract",
   :file "src/main/clojure/clojure/java/jmx.clj",
   :source-url
   "https://github.com/clojure/java.jmx/blob/f7fad3b9d8a376d3d7efe2b7cfcacc5b1ec5e585/src/main/clojure/clojure/java/jmx.clj#L129",
   :line 129,
   :var-type "protocol",
   :arglists nil,
   :doc nil,
   :namespace "clojure.java.jmx",
   :wiki-url
   "http://clojure.github.com/java.jmx//clojure.java.jmx-api.html#clojure.java.jmx/Destract"}
  {:name "objects->data",
   :doc
   "Convert JMX object model into data. Handles CompositeData, TabularData, maps, and atoms.",
   :var-type "function",
   :namespace "clojure.java.jmx",
   :arglists ([_]),
   :wiki-url
   "http://clojure.github.com/java.jmx//clojure.java.jmx-api.html#clojure.java.jmx/objects->data",
   :source-url nil,
   :raw-source-url nil,
   :file nil})}
