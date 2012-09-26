clojure.java.jmx
========================================

Produce and consume JMX beans from Clojure.

Releases and Dependency Information
========================================

Latest stable release: 0.2.0

* [All Released Versions](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.clojure%22%20AND%20a%3A%22java.jmx%22)

* [Development Snapshot Versions](https://oss.sonatype.org/index.html#nexus-search;gav%7Eorg.clojure%7Ejava.jmx%7E%7E%7E)

[Leiningen](https://github.com/technomancy/leiningen) dependency information:

    [org.clojure/java.jmx "0.2.0"]

[Maven](http://maven.apache.org/) dependency information:

    <dependency>
      <groupId>org.clojure</groupId>
      <artifactId>java.jmx</artifactId>
      <version>0.2.0</version>
    </dependency>

Usage
========================================

[Full Documentation](http://clojure.github.com/java.jmx/)

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

Give me all the attributes:

    (jmx/mbean \"java.lang:type=Memory\")
    -> {:NonHeapMemoryUsage
         {:used 16674024, :max 138412032, :init 24317952, :committed 24317952},
        :HeapMemoryUsage
         {:used 18619064, :max 85393408, :init 0, :committed 83230720},
        :ObjectPendingFinalizationCount 0,
        :Verbose false}

Find an invoke an operation:

    (jmx/operation-names \"java.lang:type=Memory\")
    -> (:gc)
    (jmx/invoke \"java.lang:type=Memory\" :gc)
    -> nil

Conneting to another process? Just run *any* of the above code
inside a with-connection:

    (jmx/with-connection {:host \"localhost\", :port 3000}
      (jmx/mbean \"java.lang:type=Memory\"))
    -> {:ObjectPendingFinalizationCount 0,
        :HeapMemoryUsage ... etc.}

Serve your own beans. Drop a Clojure ref into create-bean
to expose read-only attributes for every key/value pair
in the ref:

    (jmx/register-mbean
       (jmx/create-bean
       (ref {:string-attribute \"a-string\"}))
       \"my.namespace:name=Value\")"}

Developer Information
========================================

* [GitHub project](https://github.com/clojure/java.jmx)

* [Bug Tracker](http://dev.clojure.org/jira/browse/JMX)

* [Continuous Integration](http://build.clojure.org/job/java.jmx/)

* [Compatibility Test Matrix](http://build.clojure.org/job/java.jmx-test-matrix/)

Building
====================

0. Clone the repo
1. Make sure you have maven installed
2. Run the maven build: `mvn install` will produce a JAR file in the
target directory, and run all tests with the most recently-released build
of Clojure.

## License

Copyright © Stuart Halloway

Licensed under the EPL. (See the file epl.html.)
