(ns the-little-clojurian.chapter2
  (:require [clojure.test :refer :all]
            [the-little-clojurian.chapter1 :refer :all]))

(with-test 
  (def lat? 
    (fn [l]
      (cond (null? l) true
            (atom? (car l)) (lat? (cdr l))
            :else false)))
  (testing "returns true"
    (is (= (lat? '(Jack Sprat could eat no chicken fat)) true))
    (is (= (lat? '()) true))
    (is (= (lat? '(bacon and eggs)) true)))
  (testing "returns false"
    (is (= (lat? '((Jack) Sprat could eat no chicken fat)) false))
    (is (= (lat? '(Jack (Sprat could) eat no chicken fat)) false))
    (is (= (lat? '(bacon (and eggs))) false))))

(deftest boolean-operators
  (is (= (or (null? '())
             (atom? '(d e f g)))
         true))
  (is (= (or (null? '(a b c))
             (null? '()))
         true))
  (is (= (or (null? '(a b c))
             (null? '(atom)))
         false)))

(with-test 
  (def member? 
    (fn [a lat]
      (cond (null? lat) false
            :else (or (eq? (car lat) a) 
                      (member? a (cdr lat))))))
  (testing "evaluates to true"
    (is (= (member? 'meat '(meat)) true))
    (is (= (member? 'tea '(coffee tea and milk))))
    (is (= (member? 'meat '(mashed potatoes and meat gravy)) true)))
  (testing "evaluates to false"
    (is (= (member? 'meat '()) false))
    (is (= (member? 'liver '(bagels and lox)) false))
    (is (= (member? 'poached '(fried eggs and scrambled eggs))))))
