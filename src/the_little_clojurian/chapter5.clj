(ns the-little-clojurian.chapter5
  (:require [clojure.test :refer :all]
            [the-little-clojurian.chapter1 :refer :all]
            [the-little-clojurian.chapter2 :refer :all]
            [the-little-clojurian.chapter3 :refer :all]
            [the-little-clojurian.chapter4 :refer :all]))

(with-test
  (def rember* 
    (fn [a l]
      (cond (null? l) '()
            (atom? (car l)) (cond (eq? (car l) a) (rember* a (cdr l))
                                  :else (cons (car l)
                                              (rember* a (cdr l))))
            :else (cons (rember* a (car l))
                        (rember* a (cdr l))))))
 
  (is (= (rember* 'cup '()) '()))
  (is (= (rember* 'cup '(coffee)) '(coffee)))
  (is (= (rember* 'cup '(cup)) '()))
  (is (= (rember* 'cup '(coffee cup)) '(coffee)))
  (is (= (rember* 'cup '((cup))) '(())))
  (is (= (rember* 'cup '(coffee (cup) and (another) cup)) '(coffee () and (another))))
  (is (= (rember* 'sauce '(((tomato sauce)) ((bean) sauce) (and ((flying)) sauce))) '(((tomato)) ((bean)) (and ((flying)))))))
