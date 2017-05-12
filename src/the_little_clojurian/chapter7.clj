(ns the-little-clojurian.chapter7
  (:require [clojure.test :refer :all]
            [the-little-clojurian.chapter1 :refer :all]
            [the-little-clojurian.chapter2 :refer :all]
            [the-little-clojurian.chapter3 :refer :all]
            [the-little-clojurian.chapter4 :refer :all]
            [the-little-clojurian.chapter5 :refer :all]
            [the-little-clojurian.chapter7 :refer :all]))

(with-test
  (def set? 
    (fn [lat]
      (cond (null? lat) true
            (member? (car lat) (cdr lat)) false
            :else (set? (cdr lat)))))
 
  (is (= (set? '()) true))
  (is (= (set? '(apple peaches apple plum)) false))
  (is (= (set? '(apple 3 pear 4 9 apple 3 4)) false))
  (is (= (set? '(1 2 3 4 5 6 7 8)) true)))

(with-test
  (def makeset 
    (fn [lat] 
      (cond (null? lat) '()
            :else (cons (car lat) 
                        (makeset (multirember (car lat)
                                              (cdr lat)))))))

  (is (= (makeset '()) '()))
  (is (= (makeset '(apple)) '(apple)))
  (is (= (makeset '(apple apple)) '(apple)))
  (is (= (makeset '(apple peach apple)) '(apple peach)))
  (is (= (makeset '(apple peach pear peach plum apple lemon peach)) '(apple peach pear plum lemon)))
  (is (= (makeset '(apple 3 pear 4 9 apple 3 4)) '(apple 3 pear 4 9))))

(with-test
  (def subset? 
    (fn [set1 set2] 
      (cond (null? set1) true
            (member? (car set1) set2) (subset? (cdr set1) set2)
            :else false)))

  (is (= (subset? '() '()) true))
  (is (= (subset? '(1) '()) false))
  (is (= (subset? '(1) '(1)) true))
  (is (= (subset? '(1) '(2)) false))
  (is (= (subset? '(5 chicken wings) '(5 hamburgers 2 pieces fried chicken and light duckling wings)) true))
  (is (= (subset? '(4 pounds of horseradish) '(four pounds chicken and 5 ounces horseradish)) false)))

