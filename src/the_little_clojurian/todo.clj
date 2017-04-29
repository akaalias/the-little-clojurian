(ns the-little-clojurian.todo
  (:require [clojure.test :refer :all]))

(declare complete-map* complete-list* complete-node* complete-nodes*)

(with-test
  (def leftmost-node
    (fn [root]
      (cond (nil? root) nil
            (empty? (:nodes root)) root
            :else (leftmost-node (first (:nodes root))))))
  
  (is (= (leftmost-node nil) nil))
  (is (= (leftmost-node {:id 1}) {:id 1}))
  (is (= (leftmost-node {:id 1 :nodes '({:id 2})}) {:id 2}))
  (is (= (leftmost-node {:id 1 :nodes '({:id 2 :nodes ({:id 3})})}) {:id 3}))
  (is (= (leftmost-node {:id 1 :nodes '({:id 2} {:id 3})})) {:id 2}))

(with-test 
  (def complete-nodes* 
    (fn [id nodes] 
      (cond (empty? nodes) '()
            :else (cons (complete-node* id (first nodes)) (complete-nodes* id (rest nodes))))))

  (is (= (complete-nodes* 1 '())
         '()))

  (is (= (complete-nodes* 1 '({:id 1}))
         '({:id 1 :complete true})))

  (is (= (complete-nodes* 1 '({:id 1} {:id 1}))
         '({:id 1 :complete true} {:id 1 :complete true})))

  (is (= (complete-nodes* 1 '({:id 1} {:id 2}))
         '({:id 1 :complete true} {:id 2})))

  (is (= (complete-nodes* 2 '({:id 1} {:id 2}))
         '({:id 1} {:id 2 :complete true})))
  
  (is (= (complete-nodes* 2 '({:id 1 :nodes ({:id 2})}))
         '({:id 1 :nodes ({:id 2 :complete true})}))))

(with-test 
  (def complete-node* 
    (fn [id m] 
      (cond (nil? m) {}
            (empty? m) {}
            (nil? (:id m)) m
            (= (:id m) id) (assoc m :complete true)
            (not (empty? (:nodes m))) {:id (:id m) :nodes (complete-nodes* id (:nodes m))}
            :else m)))

  (is (= (complete-node* 1 nil) 
         {}))

  (is (= (complete-node* 1 {})
         {}))

  (is (= (complete-node* 1 {:foo "bar"})
         {:foo "bar"}))

  (is (= (complete-node* 1 {:id 1})
         {:id 1 :complete true}))

  (is (= (complete-node* 2 {:id 1})
         {:id 1}))

  (is (= (complete-node* 2 {:id 2})
         {:id 2 :complete true}))
  
  (is (= (complete-node* 1 {:id 1 :nodes ()})
         {:id 1 :complete true :nodes ()}))

  (is (= (complete-node* 1 {:id 1 :nodes '({:id 2})}))
      {:id 1 :complete true :nodes '({:id 2})})

  (is (= (complete-node* 2 {:id 1 :nodes '({:id 2})})
         {:id 1 :nodes '({:id 2 :complete true})}))

  (is (= (complete-node* 3 {:id 1 :nodes '({:id 2 :nodes ({:id 3})})})
         {:id 1 :nodes '({:id 2 :nodes ({:id 3 :complete true})})}))

  (is (= (complete-node* 4 {:id 1 :nodes '({:id 2} {:id 3} {:id 4})})
         {:id 1 :nodes '({:id 2} {:id 3} {:id 4 :complete true})}))
  
  (is (= (complete-node* 2 {:id 1 :nodes '({:id 2 :nodes ({:id 3})})})
         {:id 1 :nodes '({:id 2 :complete true :nodes ({:id 3})})})))

(with-test
  (def complete-list* 
    (fn [id l] 
      "Looks for maps with :id equal to id and adds :completed true to those maps that match including those that are inside the :nodes list of of a map"
      (cond (empty? l) '()
            (= (:id (first l)) id) (cond (empty? (:nodes (first l))) (cons {:id id :complete true} 
                                                                           (complete-list* id (rest l)))
                                         :else (cons {:id id :complete true :nodes (cons (complete-map* id (first (:nodes (first l))))
                                                                                         (complete-list* id (rest (:nodes (first l)))))}
                                                     (complete-list* id (rest l))))

            :else (cons (complete-map* id (first l)) (complete-list* id (rest l))))))

  (is (= (complete-list* 1 '())
         '()))

  (is (= (complete-list* 1 '({:id 1}))
         '({:id 1 :complete true})))

  (is (= (complete-list* 1 '({:id 2}))
         '({:id 2})))

  (is (= (complete-list* 2 '({:id 1} {:id 2}))
         '({:id 1} {:id 2 :complete true})))

  (is (= (complete-list* 1 '({:id 1} {:id 1}))
         '({:id 1 :complete true} {:id 1 :complete true})))

  (is (= (complete-list* 1 '({:id 1} {:id 2})) 
         '({:id 1 :complete true} {:id 2})))

  (is (= (complete-list* 2 '({:id 1} {:id 2}))
         '({:id 1} {:id 2 :complete true})))

  (is (= (complete-list* 1 '({:id 1 :nodes ({:id 1})}))
         '({:id 1 :complete true :nodes ({:id 1 :complete true})})))

  (is (= (complete-list* 1 '({:id 1 :nodes ({:id 2 :nodes ({:id 1})})}))
         '({:id 1 :complete true :nodes ({:id 2 :nodes ({:id 1 :complete true})})})))

  (is (= (complete-list* 2 '({:id 1 :nodes ({:id 2})}))
         '({:id 1 :nodes ({:id 2 :complete true})})))

  (is (= (complete-list* 2 '({:id 1 :nodes ({:id 2} {:id 2})}))
         '({:id 1 :nodes ({:id 2 :complete true} {:id 2 :complete true})})))

  (is (= (complete-list* 5 '({:id 1 :nodes ({:id 2 :nodes ({:id 3} {:id 4 :nodes ({:id 5} {:id 6})})})}))
         '({:id 1 :nodes ({:id 2 :nodes ({:id 3} {:id 4 :nodes ({:id 5 :complete true} {:id 6})})})}))))

(with-test 
  (def complete-map*
    (fn [id m]
      (cond (nil? m) nil
            (= (:id m) id) (assoc m :complete true)
            (empty? (:nodes m)) m
            :else (assoc m :nodes (complete-list* id (:nodes m))))))

  (is (= (complete-map* 1 nil)
         nil))

  (is (= (complete-map* 1 {:id 1}) 
         {:id 1 :complete true}))

  (is (= (complete-map* 2 {:id 2}) 
         {:id 2 :complete true}))

  (is (= (complete-map* 1 {:id 2})
         {:id 2}))

  (is (= (complete-map* 2 {:id 1 :nodes ()}) 
         {:id 1 :nodes ()}))

  (is (= (complete-map* 2 {:id 1 :nodes '({:id 2})}) 
         {:id 1 :nodes '({:id 2 :complete true})}))

  (is (= (complete-map* 3 {:id 1 :nodes '({:id 2} {:id 3})}) 
         {:id 1 :nodes '({:id 2} {:id 3 :complete true})}))

  (is (= (complete-map* 4 {:id 1 :nodes '({:id 2} {:id 3 :nodes ({:id 4})})}) 
         {:id 1 :nodes '({:id 2} {:id 3 :nodes ({:id 4 :complete true})})}))

  (is (= (complete-map* 3 {:id 1 :nodes '({:id 2 :nodes ({:id 3})})}) 
         {:id 1 :nodes '({:id 2 :nodes ({:id 3 :complete true})})}))

  (is (= (complete-map* 5 {:id 1 :nodes '({:id 2 :nodes ({:id 3 :nodes ({:id 4 :nodes ({:id 5} {:id 6})})})})}))
       {:id 1 :nodes '({:id 2 :nodes ({:id 3 :nodes ({:id 4 :nodes ({:id 5 :complete true} {:id 6})})})})}))
