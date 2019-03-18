(ns tabula.parts.id)

(defn handle|create
  [state [op what params]]
  {:effects [[:APPLY [:database :ids what] #(inc (or % 0))]]
   :command [op what (assoc params :id (or (get-in state [:database :ids what]) 1))]})

(defn handler
  [state [op :as command]]
  (condp = op
    :create (handle|create state command)
    nil))
