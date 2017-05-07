# The Little Clojurian

A TDD approach to following along "The Little Schemer"'s conversational question/answer style using Clojure's `with-test` macro.


```
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
```

## Usage

### Testing

All tests are written in-line using the `with-test` macro. It's a lot of fun. Run all tests with `lein run`

### Shipping

Run `./ship.sh` to ensure all tests are passing locally.

## License

Copyright © 2017 Alexis Rondeau

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
