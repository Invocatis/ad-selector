(ns tabula.core
  (:require
    [tabula.engine :as engine]
    [tabula.matching.sampling :as sampling]
    [tabula.parts.database :as db]
    [tabula.parts.id :as id]
    [tabula.util :refer [swap-lr!]]))


;; Filtering -> Sampling -> Relevancy -> Selection ->

(defonce state (atom {}))

(def engine|execution (engine/holistic id/handler db/execute))

(def engine|query (engine/holistic db/query))

(def engine|matching (engine/holistic (sampling/part (partial sampling/random 20)) db/query))

(defn execute
  [command]
  (swap-lr! state #(engine|execution % command)
            (fn [result orig] (or (:state result) orig))
            identity))

(defn query
  [command]
  (:return (engine|query @state command)))
