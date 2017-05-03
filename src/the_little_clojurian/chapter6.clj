(ns the-little-clojurian.chapter6
  (:require [clojure.test :refer :all]
            [the-little-clojurian.chapter1 :refer :all]
            [the-little-clojurian.chapter2 :refer :all]
            [the-little-clojurian.chapter3 :refer :all]
            [the-little-clojurian.chapter4 :refer :all]
            [the-little-clojurian.chapter5 :refer :all]))

(with-test 
  (def numbered?
    (fn [aexp] 
      (cond (atom? aexp) (number? aexp)
            :else (and (numbered? (car aexp))
                       (numbered? (car (cdr (cdr aexp))))))))
  
  (is (= (numbered? 1) true))
  (is (= (numbered? 'a) false))
  (is (= (numbered? '(3 + 1)) true))
  (is (= (numbered? '(3 + (4 * 7))))))

(with-test
  (def value-infix
    (fn [u] 
      (cond (atom? u) u
            (eq? (car (cdr u)) '+) (+ (value-infix (car u))
                                          (value-infix (car (cdr (cdr u)))))
            (eq? (car (cdr u)) '*) (- (value-infix (car u))
                                           (value-infix (car (cdr (cdr u)))))
            :else (int (java.lang.Math/pow (value-infix (car u))
                                           (value-infix (car (cdr (cdr u)))))))))

  (is (= (value-infix 13) 13))
  (is (= (value-infix '(1 + 3)) 4))
  (is (= (value-infix '(1 + (3 pow 4))) 82)))

