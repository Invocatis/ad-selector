(ns tabula.sampling)

;; Logic for selecting a relatively small sample from the whole of advertisements
;; Various functions for differing strategires

(defn random
  "Selects a random sample of size n from the collection coll

   Runs in linear time, should be optimized"
  [n coll]
  (->> coll shuffle (take n)))
