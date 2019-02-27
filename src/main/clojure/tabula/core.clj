(ns tabula.core)

;; -> Sampling -> Relevancy -> Selction

(defn get-ad
  [ads sample relevance select spec]
  (->> ads sample (sort-by (partial relevance spec)) select))
