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
  {'+      add
   '-      subtract
   '*      multiply
   '/      divide
   'negate negate
   'exp    exp
   'ln     ln})


(defn parseFunction
  [args]
  (let [expression (read-string (str args))]
    (if (number? expression)
      (constant expression)
      (if (symbol? expression)
        (variable (str expression))
        (apply (mapFun (first expression)) (map parseFunction (rest expression)))))))


(load-file "proto.clj")

(def toString (method :toString))
(def evaluate (method :evaluate))
(def diff (method :diff))
(def toStringSuffix (method :toStringSuffix))

(def m_val (field :val))


(defn _Constant
  [this val]
  (assoc this
         :val val))


(declare Constant)


(def ConstantPrototype
  {:toString      (fn [this] (str (m_val this)))
   :evaluate      (fn [this args] (read-string (str (m_val this))))
   :diff          (fn [whyDoINeedThisHere args] (Constant 0))
   :toStringSuffix (fn [this] (toString this))})


(def Constant (constructor _Constant ConstantPrototype))

(def m_varName (field :varName))


(defn _Variable
  [this val]
  (assoc this
         :varName val))


(def VariablePrototype
  {:toString      (fn [this] (str (m_varName this)))
   :evaluate      (fn [this args] (args (clojure.string/lower-case (nth (m_varName this) 0))))
   :diff          (fn [this args]
                    (if (= args (m_varName this))
                      (Constant 1)
                      (Constant 0)))
   :toStringSuffix (fn [this] (toString this))})


(def Variable (constructor _Variable VariablePrototype))

(def m_funcSign (field :funcSign))
(def m_evalFunc (field :evalFunc))
(def m_argsFunc (field :argsFunc))
(def m_diffFunc (field :diffFunc))


(defn _VarargExpression
  [funcSign evalFunc diffFunc this & argsFunc]
  (assoc this
         :funcSign funcSign
         :evalFunc evalFunc
         :diffFunc diffFunc
         :argsFunc argsFunc))


(def VarargPrototype
  {:toString      (fn [this] (str "(" (m_funcSign this) " " (clojure.string/join " " (map #(toString %) (m_argsFunc this))) ")"))
   :evaluate      (fn [this args] (apply (m_evalFunc this) (map #(evaluate % args) (m_argsFunc this))))
   :diff          (fn [this arg] ((m_diffFunc this) this arg))
   :toStringSuffix (fn [this] (str "(" (clojure.string/join " " (map #(toStringSuffix %) (m_argsFunc this))) " " (m_funcSign this) ")"))})


(defn VarargExpression
  [funcSign evalFunc diffFunc]
  (constructor (partial _VarargExpression funcSign evalFunc diffFunc) VarargPrototype))


(def Add (VarargExpression "+" + (fn [this arg] (Add (diff (first (m_argsFunc this)) arg) (diff (second (m_argsFunc this)) arg)))))
(def Subtract (VarargExpression "-" - (fn [this arg] (Subtract (diff (first (m_argsFunc this)) arg) (diff (second (m_argsFunc this)) arg)))))
(def Multiply (VarargExpression "*" * (fn [this arg] (Add (Multiply (diff (first (m_argsFunc this)) arg) (second (m_argsFunc this))) (Multiply (diff (second (m_argsFunc this)) arg) (first (m_argsFunc this)))))))
(def Divide (VarargExpression "/" #(/ (double %1) %2) (fn [this arg] (Divide (Subtract (Multiply (diff (first (m_argsFunc this)) arg) (second (m_argsFunc this))) (Multiply (diff (second (m_argsFunc this)) arg) (first (m_argsFunc this)))) (Multiply (second (m_argsFunc this)) (second (m_argsFunc this)))))))
(def Negate (VarargExpression "negate" - (fn [this arg] (Multiply (Constant -1) (diff (first (m_argsFunc this)) arg)))))
(def Exp (VarargExpression "exp" #(Math/exp %) (fn [this arg] (Multiply (Exp (first (m_argsFunc this))) (diff (first (m_argsFunc this)) arg)))))
(def Ln (VarargExpression "ln" #(Math/log %) (fn [this arg] (Divide (diff (first (m_argsFunc this)) arg) (first (m_argsFunc this))))))


(def mapObj
  {'+      Add
   '-      Subtract
   '*      Multiply
   '/      Divide
   'negate Negate
   'exp    Exp
   'ln     Ln})


(defn parseObject
  [args]
  (let [expression (read-string (str args))]
    (if (number? expression)
      (Constant expression)
      (if (symbol? expression)
        (Variable (str expression))
        (apply (mapObj (first expression)) (map parseObject (rest expression)))))))


(load-file "parser.clj")

(def *space (+char " \t\n\r"))
(def *ws (+ignore (+star *space)))

(def *digit (+char "0123456789"))
(def *number (+map Constant (+str (+seq *ws (+opt (+char "-")) *ws (+str (+plus *digit)) (+opt (+char ".")) (+opt (+str (+plus *digit))) *ws))))
(def *var (+map Variable (+str (+seq *ws (+str (+plus (+char "xyzXYZ"))) *ws))))


(def mapPar
  {"+"      Add
   "-"      Subtract
   "*"      Multiply
   "/"      Divide
   "negate" Negate})


(def operations  (+char "+-*/"))
(def unaryOp (+str (+seq (+char "n") (+char "e") (+char "g") (+char "a") (+char "t") (+char "e"))))


(def parseObjectSuffix
  (letfn [(*expression
            []
            (+map #(apply (mapPar (str (last %))) (butlast %))
                  (+or
                    (+seq *ws (+opt (+ignore (+char "("))) *ws (*value) *ws unaryOp *ws (+opt (+ignore (+char ")"))) *ws)
                    (+seq *ws (+opt (+ignore (+char "("))) *ws (*value) *ws *ws (*value) *ws operations *ws (+opt (+ignore (+char ")"))) *ws))))
          (*value
            []
            (+or
              *var
              *number
              (delay (*expression))))]
    (+parser (+seqn 0 *ws (delay (*value)) *ws))))

