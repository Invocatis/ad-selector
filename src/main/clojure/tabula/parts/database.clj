(ns tabula.parts.database)

(defn execute|create
  [state [_ what {:keys [id entry]}]]
  (let [what (if (vector? what) what [what])
        entry (if (map? entry) (assoc entry :id id) entry)]
    [[:PUT (conj (into [:database] what) id) entry]]))

(defn execute|update
  [state [_ what {:keys [id put patch apply]}]]
  (let [what (if (vector? what) what [what])
        where (if id (conj (into [:database] what) id) (into [:database] what))]
    (cond
      put   [[:PUT where put]]
      patch [[:PATCH where patch]]
      apply [[:APPLY where apply]])))

(defn execute
  [state [op :as command]]
  {:effects
    (condp = op
      :create (execute|create state command)
      :update (execute|update state command))})

(defn query
  [state [_ what {:keys [id match predicate]}]]
  {:return
    (let [coll (vals (get-in state [:database what]))]
      (cond
        predicate (into [] (filter predicate coll))
        id (get-in state [:database what id])
        :else (get-in state [:database what])))})
