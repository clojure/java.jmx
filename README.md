clojure.java.jmx
========================================

Produce and consume JMX beans from Clojure.

Releases and Dependency Information
========================================

This project follows the version scheme MAJOR.MINOR.PATCH where each component provides some relative indication of the size of the change, but does not follow semantic versioning. In general, all changes endeavor to be non-breaking (by moving to new names rather than by breaking existing names).

Latest stable release: 1.1.1

* [All Released Versions](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.clojure%22%20AND%20a%3A%22java.jmx%22)

* [Development Snapshot Versions](https://oss.sonatype.org/index.html#nexus-search;gav%7Eorg.clojure%7Ejava.jmx%7E%7E%7E)

[CLI/`deps.edn`](https://clojure.org/reference/deps_edn) dependency information:
```clojure
org.clojure/java.jmx {:mvn/version "1.1.1"}
```

[Leiningen](https://github.com/technomancy/leiningen) dependency information:

    [org.clojure/java.jmx "1.1.1"]

[Maven](https://maven.apache.org/) dependency information:

    <dependency>
      <groupId>org.clojure</groupId>
      <artifactId>java.jmx</artifactId>
      <version>1.1.1</version>
    </dependency>

Usage
========================================

[Full Documentation](https://clojure.github.io/java.jmx/)

```clojure
(require '[clojure.java.jmx :as jmx])
```

What beans do I have?

```clojure
(jmx/mbean-names "*:*")
-> #<HashSet [java.lang:type=MemoryPool,name=CMS Old Gen,
              java.lang:type=Memory, ...]
```

What attributes does a bean have?

```clojure
(jmx/attribute-names "java.lang:type=Memory")
-> (:Verbose :ObjectPendingFinalizationCount
    :HeapMemoryUsage :NonHeapMemoryUsage)
```

What is the value of an attribute?

```clojure
(jmx/read "java.lang:type=Memory" :ObjectPendingFinalizationCount)
-> 0
```

Give me all the attributes:

```clojure
(jmx/mbean "java.lang:type=Memory")
-> {:NonHeapMemoryUsage
     {:used 16674024, :max 138412032, :init 24317952, :committed 24317952},
    :HeapMemoryUsage
     {:used 18619064, :max 85393408, :init 0, :committed 83230720},
    :ObjectPendingFinalizationCount 0,
    :Verbose false}
```

Find an invoke an operation:

```clojure
(jmx/operation-names "java.lang:type=Memory")
-> (:gc)
(jmx/invoke "java.lang:type=Memory" :gc)
-> nil
```

Conneting to another process? Just run *any* of the above code
inside a with-connection:

```clojure
(jmx/with-connection {:host "localhost", :port 3000}
  (jmx/mbean "java.lang:type=Memory"))
-> {:ObjectPendingFinalizationCount 0,
    :HeapMemoryUsage ... etc.}
```

Serve your own beans. Drop a Clojure ref into create-bean
to expose read-only attributes for every key/value pair
in the ref:

```clojure
(jmx/register-mbean
  (jmx/create-bean
  (ref {:string-attribute "a-string"}))
  "my.namespace:name=Value")}
```

Developer Information
========================================

* [GitHub project](https://github.com/clojure/java.jmx)
* [Bug Tracker](https://clojure.atlassian.net/browse/JMX)
* [Continuous Integration](https://github.com/clojure/java.jmx/actions/workflows/test.yml)

Building
====================

0. Clone the repo
1. Make sure you have maven installed
2. Run the maven build: `mvn install` will produce a JAR file in the
target directory, and run all tests with the most recently-released build
of Clojure.

## License

Copyright © Stuart Halloway, Rich Hickey & contributors.

Licensed under the EPL. (See the file epl.html.)
