(ns tabula.util)

(defn swap-lr!
  [atom f left right]
  (loop []
    (let [orig @atom
          result (f orig)]
      (if (compare-and-set! atom orig (left result orig))
        (right result)
        (recur)))))
