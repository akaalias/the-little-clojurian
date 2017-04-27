(ns the-little-clojurian.chapter3
  (:require [clojure.test :refer :all]
            [the-little-clojurian.chapter1 :refer :all]
            [the-little-clojurian.chapter2 :refer :all]))

(with-test
  (def rember 
    (fn [a lat] 
      (cond (null? lat) '()
            (eq? (car lat) a) (cdr lat)
            :else (conss (car lat) (rember a 
                                           (cdr lat))))))

  (is (= (rember 'and '()) '()))
  (is (= (rember 'and '(and)) '()))
  (is (= (rember 'and '(bacon lettuce and tomato)) '(bacon lettuce tomato)))
  (is (= (rember 'mint '(lamb chops and mint jelly)) '(lamb chops and jelly)))
  (is (= (rember 'mint '(lamb chops and mint flavored mint jelly)) '(lamb chops and flavored mint jelly))))


(with-test
  (def firsts 
    (fn [l]
      (cond (null? l) '()
            :else (cons (car (car l))
                        (firsts (cdr l))))))

  (is (= (firsts '()) '()))
  (is (= (firsts '((apple peach pumpkin)
                   (plum pear cherry)
                   (grape raisin pea)
                   (bean carrot eggplant)))
         '(apple plum grape bean)))
  (is (= (firsts '((a b) (c d) (e f)))
         '(a c e)))
  (is (= (firsts '((five plums)
                   (four)
                   (eleven green oranges)))
         '(five four eleven)))
  (is (= (firsts '(((five plums) four)
                   (eleven green oranges)
                   ((no) more)))
         '((five plums)
           eleven
           (no)))))

(with-test
  (def insertR 
    (fn [new old lat]
      (cond (null? lat) '()
            (eq? (car lat) old) (cons old
                                      (cons new (cdr lat)))
            :else (cons (car lat)
                        (insertR new old 
                                 (cdr lat))))))

  (is (= (insertR 'topping 'fudge '())
         '()))
  (is (= (insertR 'topping 'fudge '(ice cream with fudge for dessert))
         '(ice cream with fudge topping for dessert)))
  (is (= (insertR 'jalapeno 'and '(tacos tamales and salsa))
         '(tacos tamales and jalapeno salsa)))
  (is (= (insertR 'e 'd '(a b c d f g d h))
         '(a b c d e f g d h))))

(with-test
  (def insertL
    (fn [new old lat]
      (cond (null? lat) '()
            (eq? (car lat) old) (conss new lat)
            :else (cons (car lat) (insertL new old (cdr lat))))))
  
  (is (= (insertL 'topping 'fudge '())
         '()))
  (is (= (insertL 'topping 'fudge '(ice cream with fudge for dessert))
         '(ice cream with topping fudge for dessert))))

(with-test
  (def subst
    (fn [new old lat]
      (cond (null? lat) '()
            (eq? (car lat) old) (conss new (cdr lat))
            :else (conss (car lat)
                        (subst new old (cdr lat))))))

  (is (= (subst 'topping 'fudge '())
         '()))
  (is (= (subst 'topping 'fudge '(ice cream with fudge for dessert))
         '(ice cream with topping for dessert))))

(with-test
  (def subst2 
    (fn [new o1 o2 lat]
      (cond (null? lat) '()
            (or (eq? (car lat) o1)
                (eq? (car lat) o2)) (conss new (cdr lat))
            :else (conss (car lat) (subst2 new o1 o2 (cdr lat))))))

  (is (= (subst2 'vanilla 'chocolate 'banana '())
         '()))
  (is (= (subst2 'vanilla 'chocolate 'banana '(banana ice cream with chocolate topping))
         '(vanilla ice cream with chocolate topping))))


(with-test
  (def multirember 
    (fn [a lat]
      (cond (null? lat) '()
            (eq? (car lat) a) (multirember a (cdr lat))
            :else (conss (car lat) (multirember a (cdr lat))))))
  (is (= (multirember 'cup '())
         '()))
  (is (= (multirember 'cup '(coffee cup tea cup and hick cup))
         '(coffee tea and hick))))

(with-test
  (def multiinsertR
    (fn [new old lat]
      (cond (null? lat) '()
            (eq? (car lat) old) (conss old (conss new (multiinsertR new old (cdr lat))))
            :else (conss (car lat) (multiinsertR new old (cdr lat))))))

  (is (= (multiinsertR 'new 'old '())
         '()))
  (is (= (multiinsertR 'new 'old '(old socks old beer))
         '(old new socks old new beer))))

(with-test
  (def multiinsertL
    (fn [new old lat]
      (cond (null? lat) '()
            (eq? (car lat) old) (conss new (conss old (multiinsertL new old (cdr lat))))
            :else (cons (car lat) (multiinsertL new old (cdr lat))))))

  (is (= (multiinsertL 'new 'old '())
         '()))
  (is (= (multiinsertL 'new 'old '(old socks old beer))
         '(new old socks new old beer))))

(with-test
  (def multisubst
    (fn [new old lat]
      (cond (null? lat) '()
            (eq? (car lat) old) (conss new (multisubst new old (cdr lat)))
            :else (conss (car lat) (multisubst new old (cdr lat))))))
  (is (= (multisubst 'new 'old '())
         '()))
  (is (= (multisubst 'new 'old '(frog))
         '(frog)))
  (is (= (multisubst 'new 'old '(old))
         '(new)))
  (is (= (multisubst 'new 'old '(old old old))
         '(new new new))))
