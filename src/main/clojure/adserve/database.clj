(ns adserve.database
  (:require
    [tabula.core :as tabula]
    [tabula.parts.database :as db]
    [tabula.parts.id :as id]
    [tabula.util :refer [swap-lr!]]))

(def engine|execution (tabula/engine id/handler db/execute))

(defn execute
  [state command]
  (let [engine (get-in @state [:engines :execution])]
    (swap-lr! state #(engine % command)
              (fn [result orig] (or (:state result) orig))
              identity)))

(def engine|query (tabula/engine db/query))

(defn query
  [state command]
  (let [engine (get-in @state [:engines :query])]
    (:return (engine @state command))))
