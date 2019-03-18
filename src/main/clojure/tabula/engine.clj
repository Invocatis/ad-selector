(ns tabula.engine
  (:require
    [tabula.util :refer [swap-lr!]]))

(defn commit|put
  [state [_ where value]]
  (assoc-in state where value))

(defn commit|patch
  [state [_ where patch]]
  (update-in state where merge patch))

(defn commit|apply
  [state [_ where f]]
  (update-in state where f))

(defn commit|remove
  [state [_ where]]
  (condp = (count where)
    0 state
    1 (dissoc state (first where))
    (update-in state (drop-last where) dissoc (last where))))

(defn commit
  [state [op :as command]]
  (condp = op
    :PUT (commit|put state command)
    :PATCH (commit|patch state command)
    :APPLY (commit|apply state command)
    :REMOVE (commit|remove state command)))

(defn engine
  [& pipeline]
  (fn engine [state command]
    (loop [pipeline pipeline
           context {:state state :command command :effects []}]
      (let [{:keys [command state]} context]
        (if (or (empty? pipeline) (nil? context) (nil? command))
          context
          (if-let [context* (apply (first pipeline) [state command])]
            (if-let [[_ effects] (find context* :effects)]
              (recur (rest pipeline)
                     (-> context*
                         (assoc :state
                                (reduce commit
                                        (or (:state context*) (:state context))
                                        effects))
                         (assoc :effects (into (:effects context) (:effects context*)))
                         (assoc :command (or (:command context*) (:command context)))))
              (recur (rest pipeline)
                     (merge-with #(or %2 %1) context context*)))
            (recur (rest pipeline) context)))))))
