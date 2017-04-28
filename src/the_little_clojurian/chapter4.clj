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

(with-test
  (def divide
    (fn [n m]
      (cond (zero? m) (throw (IllegalArgumentException.))
            (smaller-than n m) 0
            :else (add1 (divide (minuss n m) m)))))
  (is (= (divide 0 1) 0))
  (is (thrown? IllegalArgumentException (divide 1 0)))
  (is (= (divide 15 4) 3))
  (is (= (divide 15 3) 5))
  (is (= (divide 100 10) 10)))

(with-test
  (def length 
    (fn [lat] 
      (cond (null? lat) 0
            :else (add1 (length (cdr lat))))))

  (is (= (length '()) 0))
  (is (= (length '(hotdogs)) 1))
  (is (= (length '(hotdogs with mustard sauerkraut and pickles)) 6))
  (is (= (length '(ham and cheese on rye)) 5)))

(with-test 
  (def pick 
    (fn [n lat] 
      (cond (zero? n) nil
            (zero? (sub1 n)) (car lat)
            :else (pick (sub1 n) (cdr lat)))))

  (is (= (pick 0 '(apple)) nil))
  (is (= (pick 1 '()) nil))
  (is (= (pick 1 '(apple)) 'apple))
  (is (= (pick 2 '(apple bananas)) 'bananas))
  (is (= (pick 4 '(apple)) nil)))

(with-test
  (def rempick 
    (fn [n lat]
      (cond (zero? n) nil
            (zero? (sub1 n)) (cdr lat)
            :else (cons (car lat) 
                        (rempick (sub1 n) 
                                 (cdr lat))))))

  (is (= (rempick 0 '()) nil))
  (is (= (rempick 1 '(apple)) '()))
  (is (= (rempick 2 '(apple bananas gin)) '(apple gin))))


(with-test 
  (def no-nums 
    (fn [lat]
      (cond (null? lat) '()
            (number? (car lat)) (no-nums (cdr lat))
            :else (cons (car lat) 
                        (no-nums 
                         (cdr lat))))))

  (is (= (no-nums '()) '()))
  (is (= (no-nums '(1)) '()))
  (is (= (no-nums '(apple)) '(apple)))
  (is (= (no-nums '(5 pears 6 prunes 9 dates)) '(pears prunes dates))))

(with-test
  (def all-nums
    (fn [lat]
      (cond (null? lat) '()
            (number? (car lat)) (cons (car lat)
                                      (all-nums (cdr lat)))
            :else (all-nums (cdr lat)))))

  (is (= (all-nums '()) '()))
  (is (= (all-nums '(1)) '(1)))
  (is (= (all-nums '(apple)) '()))
  (is (= (all-nums '(1 apple 2 pears 3 bananas)) '(1 2 3))))

(with-test
  (def equan?
    (fn [a1 a2]
      (cond (and (number? a1) (number? a2)) (equal a1 a2)
            (or (number? a1) (number? a2)) false
            :else (eq? a1 a2))))
  
  (is (= (equan? 0 0) true))
  (is (= (equan? 0 1) false))
  (is (= (equan? 0 'a) false))
  (is (= (equan? 'a 'a) true))
  (is (= (equan? 'a 'b) false)))

(with-test
  (def occur
    (fn [a lat] 
      (cond (null? lat) 0
            (equan? (car lat) a) (add1 (occur a (cdr lat)))
            :else (occur a (cdr lat)))))

  (is (= (occur 'a '()) 0))
  (is (= (occur 'a '(a)) 1))
  (is (= (occur 'a '(b)) 0))
  (is (= (occur 'a '(a a)) 2))
  (is (= (occur 'a '(a b c a)) 2))
  (is (= (occur 'a '(b c d e)) 0)))

(with-test
  (def one?
    (fn [x] (equal x 1)))

  (is (= (one? 0) false))
  (is (= (one? 1) true))
  (is (= (one? 2) false)))
