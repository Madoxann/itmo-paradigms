(defn function
  [f]
  (fn appFn
    [& args]
    (fn ev
      [xyz]
      (apply f (map #(% xyz) args)))))


(defn constant
  [val]
  #(do (comment %) val))


(defn variable
  [xyz]
  #(% xyz))


(def add (function +))
(def subtract (function -))
(def multiply (function *))
(def divide (function #(/ (double %1) %2)))
(def negate (function -))
(def exp (function #(Math/exp %)))
(def ln (function #(Math/log %)))


(def mapFun
  {'+ add
   '- subtract
   '* multiply
   '/ divide
   'negate negate
   'exp exp
   'ln ln})


(defn parseFunction
  [args]
  (let [expression (read-string (str args))]
    (if (number? expression)
      (constant expression)
      (if (symbol? expression)
        (variable (str expression))
        (apply (mapFun (first expression)) (map parseFunction (rest expression)))))))

