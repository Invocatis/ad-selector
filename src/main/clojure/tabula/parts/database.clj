(ns tabula.parts.database
  (:require
    [motif.core :as motif]))

(defn execute|create
  [state [_ what {:keys [id entry]}]]
  (let [entry (if (map? entry) (assoc entry :id id) entry)]
    (assoc-in state [:database what id] entry)))

(defn execute|update
  [state [_ what {:keys [id put patch apply]}]]
  (let [where (if id [what id] [what])]
    (cond
      put (assoc-in state [:database what id] put)
      patch (update-in state (into [:database] where) #(merge % patch))
      apply (update-in state (into [:database] where) apply))))

(defn execute
  [state [op :as command]]
  {:state
    (condp = op
      :create (execute|create state command)
      :update (execute|update state command))})

(defn query
  [state [_ what {:keys [id match predicate]}]]
  {:return
    (let [coll (vals (get-in state [:database what]))]
      (cond
        match (into [] (filter (motif/matches? match) coll))
        predicate (into [] (filter predicate coll))
        id (get-in state [:database what id])
        :else (get-in state [:database what])))})
