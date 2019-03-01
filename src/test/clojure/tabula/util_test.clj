(ns tabula.util-test
  (:require
    [clojure.test :refer :all]
    [tabula.util :refer :all]))


(deftest test|swap-lr!
  (testing "swap-lr!"
    (let [a (atom 10)]
      (is (= (swap-lr! a (partial * 2) (fn [new orig] (inc new)) dec) 19))
      (is @a 21))))
