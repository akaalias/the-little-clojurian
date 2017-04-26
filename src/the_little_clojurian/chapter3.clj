(ns the-little-clojurian.chapter3
  (:require [clojure.test :refer :all]
            [the-little-clojurian.chapter1 :refer :all]
            [the-little-clojurian.chapter2 :refer :all]))

(with-test
  (def rember 
    (fn [a lat] 
      (cond (null? lat) '()
            (eq? (car lat) a) (cdr lat)
            :else (conss (car lat) 
                        (rember a 
                                (cdr lat))))))
  
  (is (= (rember 'and '(bacon lettuce and tomato)) '(bacon lettuce tomato)))
  (is (= (rember 'mint '(lamb chops and mint jelly)) '(lamb chops and jelly)))
  (is (= (rember 'mint '(lamb chops and mint flavored mint jelly)) '(lamb chops and flavored mint jelly))))


(with-test
  (def firsts 
    (fn [l]
      (cond (null? l) '()
            :else (cons (car (car l))
                        (firsts (cdr l))))))

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
           (no))))
  (is (= (firsts '())
         '())))
