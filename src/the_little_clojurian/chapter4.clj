(ns the-little-clojurian.chapter4
  (:require [clojure.test :refer :all]
            [the-little-clojurian.chapter1 :refer :all]
            [the-little-clojurian.chapter2 :refer :all]
            [the-little-clojurian.chapter3 :refer :all]))

(with-test 
  (def add1 
    (fn [x] (+ x 1)))
  (is (= (add1 0) 1))
  (is (= (add1 67) 68))
  (is (= (add1 68) 69)))

(with-test
  (def sub1
    (fn [x] (- x 1)))
  (is (= (sub1 0) -1))
  (is (= (sub1 1) 0))
  (is (= (sub1 2) 1))
  (is (= (sub1 3) 2)))

(with-test
  (def pluss
    (fn [n m]
      (cond (zero? m) n
            :else (add1 (pluss n (sub1 m))))))

  (is (= (pluss 0 0) 0))
  (is (= (pluss 1 0) 1))
  (is (= (pluss 0 1) 1))
  (is (= (pluss 1 1) 2))
  (is (= (pluss 1 2) 3))
  (is (= (pluss 46 12) 58)))

(with-test
  (def minuss 
    (fn [n m]
      (cond 
       (zero? m) n
       :else (sub1 (minuss n (sub1 m))))))

  (is (= (minuss 0 0) 0))
  (is (= (minuss 1 1) 0))
  (is (= (minuss 2 1) 1))
  (is (= (minuss 3 2) 1))
  (is (= (minuss 100 1) 99)))

(with-test
  (def tup?
    (fn [l] 
      (cond (null? l) true
            (not (number? (car l))) false
            :else (tup? (cdr l)))))

  (is (= (tup? '()) true))
  (is (= (tup? '(a)) false))
  (is (= (tup? '(1)) true))
  (is (= (tup? '(1 a)) false))
  (is (= (tup? '(1 2)) true))
  (is (= (tup? '(2 11 3 79 47 6))))
  (is (= (tup? '(8 55 5 555))))
  (is (= (tup? '(1 2 8 apple 4 3)) false))
  (is (= (tup? '(3 (7 4) 13 9)) false)))

(with-test
  (def addtup 
    (fn [tup] 
      (cond (null? tup) 0
            :else (pluss (car tup) (addtup (cdr tup))))))

  (is (= (addtup '()) 0))
  (is (= (addtup '(1)) 1))
  (is (= (addtup '(1 2)) 3))
  (is (= (addtup '(1 2 3)) 6))
  (is (= (addtup '(3 5 2 8)) 18))
  (is (= (addtup '(15 6 7 12 3)) 43)))
