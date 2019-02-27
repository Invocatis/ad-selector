(ns tabula.core)

<<<<<<< HEAD
;; -> Sampling -> Relevancy -> Selction

(defn get-ad
  [ads sample relevance select spec]
  (->> ads sample (sort-by (partial relevance spec)) select))
=======

;; -> Sampling -> Relevancy -> Selection ->

(defn index
  [index ky vl]
  (update index ky #(conj (or % #{}) vl)))
>>>>>>> 3d8a7874a5f17e86640c7a9cf14f57b2386ba824
