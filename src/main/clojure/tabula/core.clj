(ns tabula.core)


;; -> Sampling -> Relevancy -> Selection ->

(defn index
  [index ky vl]
  (update index ky #(conj (or % #{}) vl)))
