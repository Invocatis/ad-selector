(ns tabula.parts.id-test
  (:require
    [clojure.test :refer :all]
    [tabula.parts.id :refer :all]))

(deftest test|handler
  (testing "Id part handler"
    (is (= {:command [:create :test {:id 1}]
            :state {:database {:ids {:test 1}}}}
           (handler {} [:create :test {}])))
    (is (= {:command [:create :test {:id 10}]
            :state {:database {:ids {:test 10}}}}
           (handler {:database {:ids {:test 9}}} [:create :test {}])))
    (is (= nil (handler {} [:not-create :asdf {}])))))
