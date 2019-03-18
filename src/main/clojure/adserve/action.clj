(ns adserve.action
  (:require
    [adserve.matching :refer [match]]
    [tabula.core :as tabula]
    [tabula.engine :as engine]
    [tabula.parts.database :as db]
    [tabula.parts.id :as id]
    [tabula.util :refer [swap-lr!]]))

(def engine|execution (tabula/engine id/handler db/execute))

(defn execute
  [state command]
  (engine|execution state command))

(defn execute-all
  [state commands]
  (let [execs (map (partial engine|execution state) commands)]
    {:effects (->> execs (map :effects) (reduce into []))
     :return (-> execs last :return)}))

(def engine|query (tabula/engine db/query))

(defn query
  [state command]
  (engine|query state command))

(defn serve-ad
  [state {:keys [channel-id] :as spec}]
  (let [{:keys [id] :as ad} (:return (match state spec))
        {:keys [effects]} (execute state
                            [:create [:services id channel-id]
                                     {:entry {:time (java.util.Date.)}}])]
    (when-not (empty? ad)
      {:return ad
       :effects effects})))

(defn exec
  [action state & params]
  (locking state
    (let [s @state
          {:keys [return effects] :as asdf} (apply action (into [s] params))]
      (reset! state (reduce engine/commit s effects))
      return)))
