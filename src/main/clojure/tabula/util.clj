(ns tabula.util)

(defn swap-lr!
  [atom f left right]
  (loop []
    (let [orig @atom
          result (f orig)]
      (if (compare-and-set! atom orig (left result orig))
        (right result)
        (recur)))))

(defn map-vals
  [f m]
  (reduce (fn [m [k v]]
            (conj m [k (f v)]))
          {}
          m))

(defn filter-by-vals
  [f m]
  (reduce (fn [m [k v]]
            (if (f v)
              (conj m [k v])
              m))
          {}
          m))

(defn contains-in?
  [m ks]
  (contains? (get-in m (drop-last ks)) (last ks)))

(defn max-by
  ([keyfn x]
   x)
  ([keyfn x y]
   (if (> (keyfn x) (keyfn y))
     x
     y))
  ([keyfn x y & more]
   (reduce (partial max-by keyfn) (max-by x y) more)))
