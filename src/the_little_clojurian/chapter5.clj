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

(with-test
  (def insertR*
    (fn [new old l]
      (cond (null? l) '()
            (atom? (car l)) (cond (eq? (car l) old) (cons old 
                                                          (cons new (insertR* new old 
                                                                              (cdr l))))

                                  :else (cons (car l) 
                                              (insertR* new old 
                                                        (cdr l))))
            :else (cons (insertR* new old (car l))
                        (insertR* new old (cdr l))))))
  
  (is (= (insertR* 'new 'old '()) '()))
  (is (= (insertR* 'new 'old '(old)) '(old new)))
  (is (= (insertR* 'new 'old '((old))) '((old new))))
  (is (= (insertR* 'new 'old '((these) old ((shoes old) perfume))) '((these) old new ((shoes old new) perfume)))))

(with-test
  (def occur*
    (fn [a l] 
      (cond (null? l) 0
            (atom? (car l)) (cond (eq? (car l) a) (add1 (occur* a (cdr l)))
                                  :else (occur* a (cdr l)))
            :else (pluss (occur* a (car l))
                         (occur* a (cdr l))))))

  (is (= (occur* 'beer '()) 0))
  (is (= (occur* 'beer '(beer)) 1))
  (is (= (occur* 'beer '(coffee)) 0))
  (is (= (occur* 'beer '((beer))) 1))
  (is (= (occur* 'orange '((orange (beer)))) 1))
  (is (= (occur* 'orange '((banana split) and (orange) with sherbet (((and orange peanut) butter)))) 2)))

(with-test 
  (def subst*
    (fn [new old l] 
      (cond (null? l) '()
            (atom? (car l)) (cond (eq? (car l) old) (cons new (subst* new old (cdr l)))
                                  :else (cons (car l) (subst* new old (cdr l))))
            :else (cons (subst* new old (car l))
                        (subst* new old (cdr l))))))

  (is (= (subst* 'orange 'banana '()) '()))
  (is (= (subst* 'orange 'banana '(banana)) '(orange)))
  (is (= (subst* 'orange 'banana '((banana))) '((orange))))
  (is (= (subst* 'cup 'mug '((a mug) in the (((kitchen (mug)))))) '((a cup) in the (((kitchen (cup))))))))

(with-test
  (def insertL*
    (fn [new old l] 
      (cond (null? l) '()
            (atom? (car l)) (cond (eq? (car l) old) (cons new (cons old (insertL* new old (cdr l))))
                                  :else (cons (car l) (insertL* new old (cdr l))))
            :else (cons (insertL* new old (car l))
                        (insertL* new old (cdr l))))))

  (is (= (insertL* 'new 'old '()) '()))
  (is (= (insertL* 'new 'old '(old)) '(new old)))
  (is (= (insertL* 'new 'old '((old))) '((new old))))
  (is (= (insertL* 'new 'old '((these) old ((shoes old) perfume))) '((these) new old ((shoes new old) perfume)))))

(with-test
  (def member* 
    (fn [a l] 
      (cond (null? l) false
            (atom? (car l)) (or (eq? (car l) a)
                                (member* a (cdr l)))
            :else (or (member* a (car l))
                      (member* a (cdr l))))))

  (is (= (member* 'foo '()) false))
  (is (= (member* 'foo '(foo)) true))
  (is (= (member* 'foo '(bar)) false))
  (is (= (member* 'foo '((foo))) true))
  (is (= (member* 'foo '((the quick) ((((brown (springy foo)) jumps over)) the dog))))))

(with-test
  (def leftmost 
    (fn [l]
      (cond (null? l) nil
            (atom? (car l)) (car l)
            :else (leftmost (car l)))))
  
  (is (= (leftmost '()) nil))
  (is (= (leftmost '(apple)) 'apple))
  (is (= (leftmost '((apple))) 'apple))
  (is (= (leftmost '(((hot cider) with (green) tea))) 'hot)))

(with-test
  (def eqlist?
    (fn [l1 l2] 
      (cond (and (null? l1) (null? l2)) true
            (or (null? l1) (null? l2)) false
            (and (atom? (car l1)) (atom? (car l2))) (and (equan? (car l1) (car l2))
                                                         (eqlist? (cdr l1) (cdr l2)))
            (or (atom? (car l1)) 
                (atom? (car l2))) false
            :else (and (eqlist? (car l1) (car l2))
                       (eqlist? (cdr l1) (cdr l2))))))
  
  (is (= (eqlist? '() '()) true))
  (is (= (eqlist? '() '(foot)) false))
  (is (= (eqlist? '(foot) '()) false))
  (is (= (eqlist? '(foot) '(foot)) true))
  (is (= (eqlist? '(foot rub) '(foot sub)) false))
  (is (= (eqlist? '(strawberry ice cream) '(strawberry ice cream)) true))
  (is (= (eqlist? '(strawberry ice cream) '(strawberry cream ice)) false))
  (is (= (eqlist? '((coffee) (cup)) '((coffee) (cup))) true)))


(with-test 
  (def replace* 
    (fn [new old l] 
      (cond (null? l) '()
            (atom? (car l)) (cond (eq? (car l) old) (cons new (replace* new old (cdr l)))
                                  :else (cons (car l) (replace* new old (cdr l))))
            :else (cons (replace* new old (car l))
                        (replace* new old (cdr l))))))

  (is (= (replace* 'foo 'bar '()) '()))
  (is (= (replace* 'foo 'bar '(bar)) '(foo)))
  (is (= (replace* 'foo 'bar '((bar))) '((foo))))
  (is (= (replace* 'foo 'bar '((quux bar))) '((quux foo))))
  (is (= (replace* 'apple 'orange '(a (deeply (nested (juice of orange) and orange boxes)))) 
         '(a (deeply (nested (juice of apple) and apple boxes))))))
