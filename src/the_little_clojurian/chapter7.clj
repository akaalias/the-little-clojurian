(ns the-little-clojurian.chapter7
  (:require [clojure.test :refer :all]
            [the-little-clojurian.chapter1 :refer :all]
            [the-little-clojurian.chapter2 :refer :all]
            [the-little-clojurian.chapter3 :refer :all]
            [the-little-clojurian.chapter4 :refer :all]
            [the-little-clojurian.chapter5 :refer :all]
            [the-little-clojurian.chapter7 :refer :all]))

(with-test
  (def sett?
    (fn [lat]
      (cond (null? lat) true
            (member? (car lat) (cdr lat)) false
            :else (sett? (cdr lat)))))

  (is (= (sett? '()) true))
  (is (= (sett? '(apple peaches apple plum)) false))
  (is (= (sett? '(apple 3 pear 4 9 apple 3 4)) false))
  (is (= (sett? '(1 2 3 4 5 6 7 8)) true)))

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
            :else (and (member? (car set1) set2)
                       (subset? (cdr set1) set2)))))

  (is (= (subset? '() '()) true))
  (is (= (subset? '(1) '()) false))
  (is (= (subset? '(1) '(1)) true))
  (is (= (subset? '(1) '(2)) false))
  (is (= (subset? '(5 chicken wings) '(5 hamburgers 2 pieces fried chicken and light duckling wings)) true))
  (is (= (subset? '(4 pounds of horseradish) '(four pounds chicken and 5 ounces horseradish)) false)))

(with-test
  (def eqset?
    (fn [set1 set2]
      (and (subset? set1 set2)
           (subset? set2 set1))))

  (is (= (eqset? '() '()) true))
  (is (= (eqset? '(1) '()) false))
  (is (= (eqset? '(1) '(1)) true))
  (is (= (eqset? '(1) '(2)) false))
  (is (= (eqset? '(1 2) '(1 2)) true))
  (is (= (eqset? '(1 2) '(2 1)) true))
  (is (= (eqset? '(1 2 3 4) '(1 2 3 4)) true))
  (is (= (eqset? '(1 2 3 4) '(4 3 2 1)) true))
  (is (= (eqset? '(:foo :bar :baz) '(:baz :bar :foo)) true)))

(with-test
  (def intersect?
    (fn [set1 set2]
      (cond (null? set1) false
            :else (or (member? (car set1) set2)
                      (intersect? (cdr set1) set2)))))

  (is (= (intersect? '() '()) false))
  (is (= (intersect? '(1) '(1)) true))
  (is (= (intersect? '(1) '(2)) false))
  (is (= (intersect? '(stewed tomatoes and macaroni) '(macaroni and cheese)) true)))

(with-test
  (def intersect
    (fn [set1 set2]
      (cond (null? set1) '()
            (member? (car set1) set2) (cons (car set1)
                                            (intersect (cdr set1) set2))
            :else (intersect (cdr set1) set2))))

  (is (= (intersect '() '()) '()))
  (is (= (intersect '(1) '(1)) '(1)))
  (is (= (intersect '(1) '(2)) '()))
  (is (= (intersect '(1 2) '(2 3)) '(2)))
  (is (= (intersect '(1 2 3 4) '(4 3 2 1)) '(1 2 3 4)))
  (is (= (intersect '(stewed tomatoes and macaroni) '(macaroni and cheese)) '(and macaroni))))

(with-test
  (def union
    (fn [set1 set2]
      (cond (null? set1) set2
            (member? (car set1) set2) (union (cdr set1) set2)
            :else (cons (car set1) (union (cdr set1) set2)))))

  (is (= (union '() '()) '()))
  (is (= (union '(1) '(1)) '(1)))
  (is (= (union '(1) '(1 2)) '(1 2)))
  (is (= (union '(1 2 3) '(4 5 6)) '(1 2 3 4 5 6)))
  (is (= (union '(stewed tomatoes and macaroni casserole) '(macaroni and cheese)) '(stewed tomatoes casserole macaroni and cheese))))

(with-test
  (def difference
    (fn [set1 set2]
      (cond (null? set1) '()
            (member? (car set1) set2) (difference (cdr set1) set2)
            :else (cons (car set1)
                        (difference (cdr set1) set2)))))

  (is (= (difference '() '()) '()))
  (is (= (difference '(:foo) '()) '(:foo)))
  (is (= (difference '(:bar) '()) '(:bar)))
  (is (= (difference '(:foo :bar) '(:foo)) '(:bar)))
  (is (= (difference '(:foo) '(:bar)) '(:foo)))
  (is (= (difference '(1 2 3) '(1 2 3)) '()))
  (is (= (difference '(:a :b :c) '(:d :e :f)) '(:a :b :c))))

(with-test
  (def intersect-all
    (fn [l-set]
      (cond (null? (cdr l-set)) (car l-set)
            :else (intersect (car l-set)
                             (intersect-all (cdr l-set))))))

  (is (= (intersect-all '(())) '()))
  (is (= (intersect-all '((a) (a))) '(a)))
  (is (= (intersect-all '((b) (b))) '(b)))
  (is (= (intersect-all '((a 1) (a 2) (a 3))) '(a)))
  (is (= (intersect-all '((a b c) (a b c d) (a b c d e))) '(a b c))))

(with-test
  (def a-pair?
    (fn [x]
      (cond (atom? x) false
            (empty? x) false
            (null? (cdr x)) false
            (null? (cdr (cdr x))) true
            :else false)))

  (is (= (a-pair? '()) false))
  (is (= (a-pair? '(:foo :bar)) true))
  (is (= (a-pair? '(:foo)) false))
  (is (= (a-pair? :foo)) false)
  (is (= (a-pair? '(:foo (:bar))) true))
  (is (= (a-pair? '(:foo :bar :baz)) false))
  (is (= (a-pair? '((:foo) (:bar))) true)))
