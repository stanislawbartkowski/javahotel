(ns logical.expression)

(defn subexpr 
  "assumes the ( (opening bracket) has just been read;
   returns the position of the ) closing bracket"
  [li] 
  (loop [pos 0 level 1]
    (if (= level 0) pos
    (recur (+ pos 1) (cond 
                       (= (nth li pos) "(") (+ level 1)
                       (= (nth li pos) ")") (- level 1)
                       :else level
                     )
    ))
  )
)

(defn getelembound 
  "recognizes the beginning and ending index of the current operand,
   returns two element vector with both indexes,
   in case of single operand the beginning and ending are equal
   in case of subexpression the beginning and ending of the opening and closing brackets () are returned"

  [li] 
  (if (= (first li) "(") 
      [0 (subexpr ( next li)) ]
      [0 0]
  )
)


(defn analyzeoper
  "analyze current operand, atomic or subexpression
   return four element vector
   [0 (if atomic) operand operator rest]
   [1 (if subexpression) operand (subexpression) operator rest]"

    [li]
    ( let [ firsto (if (= (first li) "~") "~" nil)
            ; if first element is negation then simply keep negation and analyze the rest
            body (if (= firsto "~") (next li) li)
            pos (getelembound body)
            beg (first pos)
            end (fnext pos)
            rest (next (nthnext body end))
            oper (drop-last (- -1 (count body) end) body )
          ]
      (if (= beg end) 
         [0 firsto (first oper) rest]
         [1 firsto (drop-last (next oper)) rest]        
      )
    )
)


(defn parse 
  "parse string expression
   returns three element vector (Polish notation: (operator, left operand, right operand)
   left/right operand can be an atomic operand (variable) or subexpression (another tripple vector)"
      [str]
      ; tokenize str (expression) to the list of tokens
      (let 
         [seqw (re-seq #"[A-Za-z][\w]*|[\^\(\)&~]|=>|==" str)]
         (declare expression)
         (defn opera [pos]
            (let [ type (first pos)
                   neg (second pos)
                   ope (nth pos 2)
                   operand (if (= type 0) ope (expression ope)) 
                 ]
             (if (= neg "~") ["~" operand nil] operand )
           )
         )
         (defn expression [li]
            (let [ pos (analyzeoper li)
                   rest (nth pos 3)
                   operator (first rest)
                   rightopera ( if (nil? operator) nil (analyzeoper (next rest)))
                 ]
             [operator (opera pos) ( if ( nil? rightopera) nil (opera rightopera))]              
           )
         )
         (expression seqw)
      )
)

(defn evaluate 
    [expr val]

"evaluate (calculate value) of the parsed expression using current values (as hash-map)
 returns true or false
"
    (defn getval [subexpr]
    (let 
      [operator (first subexpr)
       first (fnext subexpr)
       second (nth subexpr 2)
       firstval (if (vector? first) (getval first) (get val first))
       secondval (if (vector? second) (getval second) (get val second))
      ] 
     (cond 
        (= operator nil) firstval
        (= operator "&") (and firstval secondval)
        (= operator "^") (or firstval secondval)
        (= operator "==") (= firstval secondval)
        (= operator "=>") (if (and firstval (not secondval)) false true)
        (= operator "~") (not firstval)
        :else nil
     )
     )
     )
     (getval expr)
)

(defn calculateall

"calculates combination of all logical values in logical expression
 str : expression, tautology
 returns: collection of two element vectors:
 [ result(true or values) (map of values)]
"

  [str]

  (defn genvalues [lvals num ]
     (defn getsinglevalue [k]
        [ (nth lvals k) (odd? (bit-shift-right num k)) ]
     )
     (map getsinglevalue (range 0 (count lvals)))
  )
  (defn verif [e avals]
    (let [valh (apply hash-map (flatten avals))]
    [ valh (logical.expression/evaluate e valh)]
    )
   )   

  (let [ e (parse str)
         operands (distinct (filter #(and (not (nil? %)) (Character/isLetter  (first %))) (flatten e)))
         rlist ( range 0 (int (Math/pow 2 (count operands))))
       ]
    (map #(verif e (genvalues operands %)) rlist)
  )
)

(defn varifytautology

"verify if logical expression is tautology
 returns the collection of values giving false result
 if list is empty then expression is tautology
"

  [str]
  (let [ vals (calculateall str)]
;    (seq (for [x vals] (println x)))
    (reduce #( if (not (second %2 )) (conj %1 %2) %1) [] vals)
  )
)

(ns unit.tests
 (:require [logical.expression :as le])
)

(use 'clojure.test)

(deftest parse-test1
  (let [expr (le/parse "a ^ b")
       ]
  (is (logical.expression/evaluate expr {"a"  true "b"  true}))
  (is (logical.expression/evaluate expr {"a"  false "b"  true}))
  (is (not (logical.expression/evaluate expr {"a"  false "b"  false})))
  )
)

(deftest parse-test2
  (let [expr (le/parse "a & b")
       ]
  (is (logical.expression/evaluate expr {"a"  true "b"  true}))
  (is (not (logical.expression/evaluate expr {"a"  false "b"  true})))
  )
)

(deftest parse-test3
  (let [expr (le/parse "a == b")
       ]
  (is (logical.expression/evaluate expr {"a"  true "b"  true}))
  (is (logical.expression/evaluate expr {"a"  false "b"  false}))
  (is (not (logical.expression/evaluate expr {"a"  false "b"  true})))
  )
)

(deftest parse-test4
  (let [expr (le/parse "a=>b")
       ]
  (is (logical.expression/evaluate expr {"a"  true "b"  true}))
  (is (logical.expression/evaluate expr {"a"  false "b"  false}));
  (is (not (logical.expression/evaluate expr {"a"  true "b"  false})))
  )
)

(deftest parse-test5
  (let [expr (le/parse "(a=>b)")
       ]
  (is (logical.expression/evaluate expr {"a"  true "b"  true}))
  (is (logical.expression/evaluate expr {"a"  false "b"  false}))
  (is (not (logical.expression/evaluate expr {"a"  true "b"  false})))
  )
)

(deftest parse-test6
  (let [expr (le/parse "((a & b) ^ c)")
       ]
  (is (logical.expression/evaluate expr {"a"  true "b"  true "c" true}))
  (is (not (logical.expression/evaluate expr {"a"  false "b"  true "c" false })))
  )
)

(deftest parse-test7
  (let [expr (le/parse "~a")
       ]
  (is (logical.expression/evaluate expr {"a"  false}))
  (is (not (logical.expression/evaluate expr {"a"  true })))
  )
)

(deftest parse-test8
  (let [expr (le/parse "(~a)")
       ]
  (is (logical.expression/evaluate expr {"a"  false}))
  (is (not (logical.expression/evaluate expr {"a"  true })))
  )
)

(deftest parse-test9
  (let [expr (le/parse "~(a & b)")
       ]
  (is (logical.expression/evaluate expr {"a"  false "b" false}))
  (is (not (logical.expression/evaluate expr {"a"  true "b" true})))
  )

)

(deftest parse-test10
  (let [expr (le/parse "~a & b")
       ]
  (is (not (logical.expression/evaluate expr {"a"  false "b" false})))
  (is (not (logical.expression/evaluate expr {"a"  true "b" true})))
  )
)

(deftest parse-test11
  (let [expr (le/parse "a & ~b")
       ]
  (is (not (logical.expression/evaluate expr {"a"  false "b" false})))
  (is (not (logical.expression/evaluate expr {"a"  true "b" true})))
  )
)

(deftest parse-taut1
  (let [sent "((q & ~p) ^ (r & s)) => (p => ((r & q) & s))"
        lfalse (logical.expression/varifytautology sent)
       ]
;   (println lfalse)   
    (is (= (count lfalse) 1))
  )
)
  
; DeMorgan's Low
(deftest parse-taut2
  (let [sent "(~(p & q) == (~p ^ ~q))"
        lfalse (logical.expression/varifytautology sent)
       ]
;   (println lfalse)   
    (is (empty? lfalse))
  )
)

; DeMorgan's Low
(deftest parse-taut3
  (let [sent "(~(p ^ q) == (~p & ~q))"
        lfalse (logical.expression/varifytautology sent)
       ]
;   (println lfalse)   
    (is (empty? lfalse))
  )
)

(deftest parse-taut4
  (let [sent "((a&b)=>c) == (a=>(b=>c))))"
        lfalse (logical.expression/varifytautology sent)
       ]
;   (println lfalse)   
    (is (empty? lfalse))
  )
)

; the Distribution Law
(deftest parse-taut5
  (let [sent "(p^(q&r)) == ((p^q)&(p^r))"
        lfalse (logical.expression/varifytautology sent)
       ]
;   (println lfalse)   
    (is (empty? lfalse))
  )
)


(run-tests)