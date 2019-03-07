(ns tabula.core
  (:require
    [clojure.core.async :refer [go]]
    [tabula.engine :as engine]
    [tabula.matching.sampling :as sampling]
    [tabula.matching.relevancy :as relevancy]
    [tabula.matching.selection :as selection]
    [tabula.parts.sieve :refer [parametrized-sieve]]
    [tabula.parts.database :as db]
    [tabula.parts.id :as id]
    [tabula.parts.cache :refer [cache]]
    [tabula.util :refer [swap-lr! contains-in?]]))


;; Filtering -> Sampling -> Relevancy -> Selection ->

(defn parameter-filter
  [ks]
  (fn [[op what params :as ad]]
    (when (contains-in? params ks)
      (fn [ad] (= (get-in params ks) (get-in ad ks))))))

(def engine|execution (engine/holistic id/handler db/execute))

(def engine|query (engine/holistic db/query))

(def engine|matching (engine/holistic (cache (parametrized-sieve :ad (parameter-filter [:language])) #{:query})
                                      (cache (parametrized-sieve :ad (parameter-filter [:country])) #{:query})
                                      (sampling/part (partial sampling/random 4))
                                      (relevancy/part relevancy/random)
                                      (selection/part selection/random)
                                      db/query))

(defonce state (atom {:engines {:execution engine|execution
                                :query engine|query
                                :matching engine|matching}}))

(defn execute
  [command]
  (let [engine (get-in @state [:engines :execution])]
    (swap-lr! state #(engine % command)
              (fn [result orig] (or (:state result) orig))
              identity)))

(defn query
  [command]
  (let [engine (get-in @state [:engines :query])]
    (:return (engine @state command))))

(defn match
  [spec]
  (let [engine (get-in @state [:engines :matching])
        {:keys [effects return]} (engine @state [:query :ad spec])]
    (go (swap! state #(reduce engine/commit % effects)))
    return))
