(ns the-little-clojurian.chapter1
  (:require [clojure.test :refer :all]))

(declare listp? atom? s-expression? car cdr conss null? eq?)

(with-test 
  (def atom? 
    (fn [a] 
      (not (listp? a))))
  (testing "returns true"
    (is (= (atom? 'atom) true))
    (is (= true (atom? 'turkey)))
    (is (= true (atom? '1492)))
    (is (= true (atom? 'u)))
    (is (= true (atom? '*abc$)))
    (is (= (atom? 'Harry) true))
    (is (= (atom? (car '(Harry had a heap of apples))) true))
    (is (= (atom? (car (cdr '(swing low sweet cherry oat)))) true)))
  (testing "returns false"
    (is (= false (atom? '())))
    (is (= false (atom? '(a b c))))
    (is (= (atom? '(Harry had a heap of apples)) false))
    (is (= (atom? (cdr '(Harry had a heap of apples))) false))
    (is (= (atom? (car (cdr '(swing (low sweet) cherry oat)))) false))))

(with-test 
  (def listp? 
    (fn [s] 
      (list? s)))
  (testing "returns true"
    (is (= true (listp? '(atom))))
    (is (= true (listp? '(atom? turkey or))))
    (is (= true (listp? '((atom turkey) or))))
    (is (= true (listp? '())))
    (is (= true (listp? '(() () () ())))))
  (testing "returns false"
    (is (= false (listp? 'atom)))
    (is (= false (listp? nil)))))

(with-test
  (def s-expression? 
    (fn [x] 
      (not (nil? x))))
  (testing "returns true"
    (is (= true (s-expression? 'xyz)))
    (is (= true (s-expression? '(x y z))))
    (is (= true (s-expression? '((x y) z))))
    (is (= true (s-expression? '(how are you doing so far))))
    (is (= true (s-expression? '(((how) are) ((you) (doing so)) far)))))
  (testing "returns false"
    (is (= false (s-expression? nil)))))

(with-test
  (def car 
    (fn [x] 
      (first x)))
  (testing "returns s-expression"
    (is (= 'a (car '(a b c))))
    (is (= '(a b c) (car '((a b c) x y z))))
    (is (= '((hotdogs)) (car '(((hotdogs)) (and) (pickle) relish))))
    (is (= '(hotdogs) (car (car '(((hotdogs)) (and)))))))
  (testing "returns nil"
    (is (nil? (car '()))))
  (testing "throws Exception"
    (is (thrown? IllegalArgumentException  (car 'hotdog)))))

(with-test 
  (def cdr 
    (fn [l] 
      (rest l)))
  (testing "returns list"
    (is (= '(b c) (cdr '(a b c))))
    (is (= '(x y z) (cdr '((a b c) x y z))))
    (is (= '() (cdr '(hamburger))))
    (is (= '(t r) (cdr '((x) t r))))
    (is (= '() (cdr '())))
    (is (= (cdr nil) '())))
  (testing "throws Exception"
    (is (thrown? IllegalArgumentException (cdr 'hotdogs)))))

(deftest car-and-cdr-tests
  (is (= '(x y) (car (cdr '((b) (x y) (()))))))
  (is (= '(((c))) (cdr (cdr '((b) (x y) ((c)))))))
  (is (thrown? IllegalArgumentException (cdr (car '(a (b (c)) d))))))

(with-test 
  (def conss 
    (fn [a l] 
      (cons a l)))
  (testing "returns the consed list"
    (is (= '(peanut butter and jelly) (conss 'peanut '(butter and jelly))))
    (is (= '((banana and) peanut butter and jelly) (conss '(banana and) '(peanut butter and jelly))))
    (is (= '(((help) this) is very ((hard) to learn)) (conss '((help) this) '(is very ((hard) to learn)))))
    (is (= '((a b (c))) (conss '(a b (c)) '())))
    (is (= '(a) (conss 'a '()))))
  (testing "throws Exception"
    (is (thrown? IllegalArgumentException (conss '() 'a)))
    (is (thrown? IllegalArgumentException (conss 'a 'b)))))

(deftest cons-and-car-and-cdr-tests
  (is (= '(a b) (conss 'a (car '((b) c d)))))
  (is (= '(a c d) (conss 'a (cdr '((b) c d))))))

(with-test 
  (def null? 
    (fn [x] 
      (empty? x)))
  (testing "returns true"
    (is (= true (null? '())))
    (is (= (null? (quote ())) true)))
  (testing "returns false"
    (is (= (null? '(a b c)) false)))
  (testing "throws Exception"
    (is (thrown? IllegalArgumentException (null? 'spaghetti)))))

(with-test
  (def eq? 
    (fn [x y] 
      (if (or
           (and (atom? x)
                (listp? y))
           (and (listp? x)
                (atom? y)))
        (throw (IllegalArgumentException.)) 
        (= x y))))
  (testing "returns true"
    (is (= (eq? 'Harry 'Harry) true))
    (is (= (eq? '() '(strawberry))) true)
    (is (= (eq? (car '(Mary had a little lamb)) 'Mary)))
    (is (= (eq? (car '(beans beans)) (car (cdr '(beans beans)))) true)))
  (testing "returns false"
    (is (= (eq? 'margarine 'butter) false))
    (is (= (eq? 6 7)) false))
  (testing "throws Exception"
    (is (thrown? IllegalArgumentException (eq? (cdr '(soured milk)) 'milk)))
    (is (thrown? IllegalArgumentException (eq? 'milk (cdr '(soured milk)))))))
