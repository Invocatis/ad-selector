(ns tabula.parts.database-test
  (:require
    [clojure.test :refer :all]
    [tabula.parts.database :refer :all]))

(deftest test|create
  (testing "Execute Create Statement"
    (is (= {:state {:database {:what {1 {:asdf 1 :id 1}}}}}
           (execute {} [:create :what {:id 1 :entry {:asdf 1}}])))))

(deftest test|update
  (testing "Execute Update Statement"
    (is (= {:state {:database {:asdf {1 1}}}}
           (execute {} [:update :asdf {:id 1 :put 1}])))
    (is (= {:state {:database {:asdf {1 {1 1 2 2}}}}}
           (execute {:database {:asdf {1 {1 1}}}}
                    [:update :asdf {:id 1 :patch {2 2}}])))
    (is (= {:state {:database {:asdf {1 2}}}}
           (execute {:database {:asdf {1 1}}}
                    [:update :asdf {:id 1 :apply inc}])))))

(deftest test|query
  (testing "Query Statements"
    (let [state {:database {:asdf {1 {:name "QWER"}
                                   2 {:name "ZXCV"}}}}]
      (is (= {:return (list {:name "QWER"})}
             (query state [:query :asdf {:match {:name "QWER"}}])))
      (is (= {:return (list {:name "ZXCV"})}
             (query state [:query :asdf {:predicate #(= (:name %) "ZXCV")}])))
      (is (= {:return {:name "ZXCV"}}
             (query state [:query :asdf {:id 2}])))
      (is (= {:return (get-in state [:database :asdf])}
             (query state [:query :asdf]))))))
