(defn linear_operator
  [op]
  (fn [args1' args2']
    (mapv op args1' args2')))


(def v+ (linear_operator +))
(def v- (linear_operator -))
(def v* (linear_operator *))
(def vd (linear_operator /))


(defn scalar
  [v1' v2']
  (apply + (v* v1' v2')))


(defn vect
  [v1' v2']
  (vector (- (* (v1' 1) (v2' 2)) (* (v1' 2) (v2' 1)))
          (- (* (v1' 2) (v2' 0)) (* (v1' 0) (v2' 2)))
          (- (* (v1' 0) (v2' 1)) (* (v1' 1) (v2' 0)))))


(defn v*s
  [v' s]
  (mapv #(* s %) v'))


(defn m*s
  [m' s]
  (mapv #(v*s % s) m'))


(defn m*v
  [m' v']
  (mapv #(scalar % v') m'))


(defn transpose
  [m']
  (apply mapv vector m'))


(defn m*m
  [m1' m2']
  (transpose (mapv #(m*v m1' %) (transpose m2'))))


(def m+ (linear_operator v+))
(def m- (linear_operator v-))
(def m* (linear_operator v*))
(def md (linear_operator vd))

(def c+ (linear_operator m+))
(def c- (linear_operator m-))
(def c* (linear_operator m*))
(def cd (linear_operator md))

