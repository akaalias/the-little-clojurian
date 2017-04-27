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

(with-test
  (def multiply 
    (fn [n m]
      (cond (zero? m) 0
            :else (pluss n (multiply n (sub1 m))))))

  (is (= (multiply 0 0) 0))
  (is (= (multiply 1 1) 1))
  (is (= (multiply 1 0) 0))
  (is (= (multiply 5 3) 15))
  (is (= (multiply 13 4) 52)))

(with-test
  (def tup+ 
    (fn [tup1 tup2]
      (cond (null? tup1) tup2
            (null? tup2) tup1
            :else (cons (pluss (car tup1) (car tup2)) 
                        (tup+ 
                         (cdr tup1) (cdr tup2))))))

  (is (= (tup+ '() '()) '()))
  (is (= (tup+ '(1) '(1)) '(2)))
  (is (= (tup+ '(2 3) '(4 6)) '(6 9)))
  (is (= (tup+ '(3 6 9 11 4) '(8 5 2 0 7)) '(11 11 11 11 11)))
  (is (= (tup+ '(3 7) '(4 6 8 1)) '(7 13 8 1))))

(with-test
  (def greater-than
    (fn [n m] false
      (cond (zero? n) false
            (zero? m) true
            :else (greater-than (sub1 n) (sub1 m)))))

  (is (= (greater-than 0 0) false))
  (is (= (greater-than 1 0) true))
  (is (= (greater-than 12 133) false))
  (is (= (greater-than 120 11) true))
  (is (= (greater-than 4 6) false)))

(with-test
  (def smaller-than
    (fn [n m] 
      (cond (zero? m) false
            (zero? n) true
            :else (smaller-than (sub1 n) (sub1 m)))))
  
  (is (= (smaller-than 0 0) false))
  (is (= (smaller-than 0 1) true))
  (is (= (smaller-than 4 6))))

(with-test
  (def equal 
    (fn [n m] 
      (cond (smaller-than n m) false
            (greater-than n m) false
            :else true)))

  (is (= (equal 0 0) true))
  (is (= (equal 0 1) false))
  (is (= (equal 1 1) true))
  (is (= (equal 44 44) true)))

(with-test
  (def expt 
    (fn [n m]
      (cond (zero? m) 1
            :else (multiply n (expt n (sub1 m))))))

  (is (= (expt 1 1) 1))
  (is (= (expt 2 2) 4))
  (is (= (expt 2 3) 8))
  (is (= (expt 5 3) 125)))
