(ns tabula.parts.sieve
  (:require
    [tabula.util :refer [filter-by-vals]]))

(defn sieve
  [what predicate]
  (fn [state command]
    {:state
     (update-in state [:database what]
                (partial filter-by-vals predicate))}))

(defn parametrized-sieve
  [what predicate]
  (fn [state command]
    (when-let [p (predicate command)]
      {:state
       (update-in state [:database what]
                  (partial filter-by-vals p))})))
