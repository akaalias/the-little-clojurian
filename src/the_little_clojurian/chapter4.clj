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
