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
            (eq? (infix-operator u) '+) (+ (value-infix (infix-first-sub-exp u))
                                          (value-infix (infix-second-sub-exp u)))
            (eq? (infix-operator u) '*) (- (value-infix (infix-first-sub-exp u))
                                           (value-infix (infix-second-sub-exp u)))
            :else (int (java.lang.Math/pow (value-infix (infix-first-sub-exp u))
                                           (value-infix (infix-second-sub-exp u)))))))

  (is (= (value-infix 13) 13))
  (is (= (value-infix '(1 + 3)) 4))
  (is (= (value-infix '(1 + (3 pow 4))) 82)))

(with-test
  (def infix-operator
    (fn [aexp]
      (car (cdr aexp))))

  (is (= (infix-operator '()) nil))
  (is (= (infix-operator '(1)) nil))
  (is (= (infix-operator '(1 + )) '+))
  (is (= (infix-operator '(1 + 2)) '+)))

(with-test
  (def infix-first-sub-exp
    (fn [aexp]
      (car aexp)))

  (is (= (infix-first-sub-exp '()) nil))
  (is (= (infix-first-sub-exp '(1)) 1))
  (is (= (infix-first-sub-exp '(1 + 2)) 1))
  (is (= (infix-first-sub-exp '(2 + 1)) 2)))

(with-test
  (def infix-second-sub-exp
    (fn [aexp] (car (cdr (cdr aexp)))))

  (is (= (infix-second-sub-exp '()) nil))
  (is (= (infix-second-sub-exp '(1)) nil))
  (is (= (infix-second-sub-exp '(1 +)) nil))
  (is (= (infix-second-sub-exp '(1 + 2)) 2))
  (is (= (infix-second-sub-exp '(1 + 3)) 3)))

(with-test
  (def operator
    (fn [aexp]
      (car aexp)))

  (is (= (operator '()) nil))
  (is (= (operator '(+)) '+))
  (is (= (operator '(+ 1)) '+)))

(with-test
  (def first-sub-exp
    (fn [aexp]
      (car (cdr aexp))))

  (is (= (first-sub-exp '()) nil))
  (is (= (first-sub-exp '(+)) nil))
  (is (= (first-sub-exp '(+ 1)) 1))
  (is (= (first-sub-exp '(+ 1 2)) 1)))

(with-test
  (def second-sub-exp
    (fn [aexp]
      (car (cdr (cdr aexp)))))

  (is (= (second-sub-exp '()) nil))
  (is (= (second-sub-exp '(+)) nil))
  (is (= (second-sub-exp '(+ 1)) nil))
  (is (= (second-sub-exp '(+ 1 2)) 2))
  (is (= (second-sub-exp '(+ 1 2 3)) 2)))

(with-test
  (def value-prefix
    (fn [nexp]
      (cond (atom? nexp) nexp
            (eq? (operator nexp) '+) (+ (value-prefix (first-sub-exp nexp))
                                         (value-prefix (second-sub-exp nexp)))
            (eq? (operator nexp) '*) (* (value-prefix (first-sub-exp nexp))
                                        (value-prefix (second-sub-exp nexp)))
            :else (int (java.lang.Math/pow (value-prefix (first-sub-exp nexp))
                                           (value-prefix (second-sub-exp nexp)))))))

  (is (= (value-prefix 1) 1))
  (is (= (value-prefix '(+ 1 3)) 4))
  (is (= (value-prefix '(+ 1 (* 2 2))) 5))
  (is (= (value-prefix '(+ 1 (pow 3 4))) 82)))

(with-test
  (def sero? 
    (fn [n] (null? n)))

  (is (= (sero? '()) true))
  (is (= (sero? '(())) false)))

(with-test
  (def edd1
    (fn [n]
      (cons '() n)))
  (is (= (edd1 '()) '(())))
  (is (= (edd1 '(())) '(() ())))
  (is (= (edd1 '(() ())) '(() () ()))))

(with-test
  (def zub1
    (fn [n] (cdr n)))

  (is (= (zub1 '()) '()))
  (is (= (zub1 '(())) '()))
  (is (= (zub1 '(() ())) '(()))))

(with-test
  (def plusss (fn [n m] 
                (cond (sero? m) n
                      :else (edd1 (plusss n (zub1 m))))))

  (is (= (plusss '() '()) '()))
  (is (= (plusss '(()) '())) '(()))
  (is (= (plusss '(() ()) '(())) '(() () ()))))

;; The Eight Commandment
;;
;; USE HELP FUNCTIONS TO ABSTRACT FROM REPRESENTATIONS
;;
;;
