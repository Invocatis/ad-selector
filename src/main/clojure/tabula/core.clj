(ns tabula.core)


;; -> Sampling -> Relevancy -> Selection ->

(defn get-ad
  [ads sample relevance select spec]
  (->> ads sample (sort-by (partial relevance spec)) select))

(defn index
  [index ky vl]
  (update index ky #(conj (or % #{}) vl)))
