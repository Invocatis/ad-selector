(ns tabula.parts.id)

(defn handle|create
  [state [op what params]]
  (let [state (update-in state [:database :ids what] #(inc (or % 0)))]
    {:state state
     :command [op what (assoc params :id (get-in state [:database :ids what]))]}))

(defn handler
  [state [op :as command]]
  (condp = op
    :create (handle|create state command)
    nil))
