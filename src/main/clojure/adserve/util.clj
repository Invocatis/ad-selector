(ns adserve.util
  (:require
    [tabula.util :refer [contains-in?]]))

(defn parameter-filter
  [ks]
  (fn [[op what params :as ad]]
    (when (contains-in? params ks)
      (fn [ad] (= (get-in params ks) (get-in ad ks))))))
