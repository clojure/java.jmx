
(deftest dynamic-mbean-from-compiled-class
  (let [mbean-name "clojure.java.test_jmx:name=Foo"]
    (jmx/register-mbean
     (jmx/create-bean
      (ref {:string-attribute "a-string"}))
     mbean-name)
    (are [result expr] (= result expr)
         "a-string" (jmx/read mbean-name :string-attribute)
         {:string-attribute "a-string"} (jmx/mbean mbean-name)
         )))

(deftest test-getAttribute
  (doseq [reftype [ref atom agent]]
    (let [state (reftype {:a 1 :b 2})
          bean (jmx/create-bean state)]
      (testing (str "accessing values from a " (class state))
               (are [result expr] (= result expr)
                    1 (.getAttribute bean "a"))))))

(deftest test-bean-info
  (let [state (ref {:a 1 :b 2})
        bean (jmx/create-bean state)
        info (.getMBeanInfo bean)]
    (testing "accessing info"
             (are [result expr] (= result expr)
                  "clojure.java.jmx.Bean" (.getClassName info)))))

(deftest test-getAttributes
  (let [bean (jmx/create-bean (ref {:r 5 :d 4}))
        atts (.getAttributes bean (into-array ["r" "d"]))]
    (are [x y] (= x y)
         AttributeList (class atts)
         [5 4] (seq atts))))

(deftest test-guess-attribute-typename
  (are [x y] (= x (@#'jmx/guess-attribute-typename y))
       "long" 10
       "boolean" false
       "java.lang.String" "foo"
       "long" (Long/valueOf (long 10))))



