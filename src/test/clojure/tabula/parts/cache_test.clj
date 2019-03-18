(ns tabula.parts.cache-test
  (:require
    [clojure.test :refer :all]
    [tabula.parts.cache :refer :all]))

(def times-executed (atom 0))

(defn part
  [& _]
  (swap! times-executed inc)
  {})

(def cached {:tabula.parts.cache/cache {part {[:query] 1}}})

(def information-content-cached
  {:tabula.parts.cache/cache {part {1 1, 2 2, 3 3, 4 4}}})

(deftest test|cache
  (testing "Testing Cache"
    (is (do (apply (cache part #{:query}) [{} [:query]])
            (apply (cache part #{:query}) [cached [:query]])
            (= @times-executed 1)))
    (reset! times-executed 0)
    (let [part (cache part #{:query} second)]
      (is (= 1 (get-in (apply part [information-content-cached [:query 1]]) [:state :database])))
      (is (= 2 (get-in (apply part [information-content-cached [:query 2]]) [:state :database])))
      (is (= 3 (get-in (apply part [information-content-cached [:query 3]]) [:state :database])))
      (apply part [information-content-cached [:query 10]])
      (is (= @times-executed 1)))))
