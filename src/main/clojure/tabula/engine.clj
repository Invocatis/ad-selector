(ns tabula.engine
  (:require
    [tabula.util :refer [swap-lr!]]))

(defn holistic
  [& pipeline]
  (fn engine [state command]
    (loop [pipeline pipeline
           context {:state state :command command}]
      (let [{:keys [command state]} context]
        (if (or (empty? pipeline) (nil? context))
          context
          (if-let [context* (apply (first pipeline) [state command])]
            (if-let [[_ also] (find context* :also)]
              (recur (rest pipeline)
                     (-> context*
                         (dissoc :also)
                         (assoc :state
                                (reduce (fn [state command] (:state (engine state command)))
                                        (or (:state context*) state) also))))
              (recur (rest pipeline)
                     (merge-with #(or %2 %1) context context*)))
            (recur (rest pipeline) context)))))))

(defn atomistic
  [state & pipeline]
  (fn run-engine [command]
    (loop [pipeline pipeline
           context {:command command}]
      (let [{:keys [command]} context]
        (if (or (empty? pipeline) (nil? context))
          context
          (let [context (swap-lr! state #(apply (first pipeline) [% command]) #(or (:state %1) %2) identity)]
            (recur (rest pipeline) context)))))))
