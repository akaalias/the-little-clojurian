# The Little Clojurian

A TDD approach to following along "The Little Schemer"'s conversational question/answer style using Clojure's `with-test` macro.


```clojure
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

## Full transcript

## The Little Schemer in Clojure – Chapter 1 Recap

This is a series of small blog posts about following along the book 'The Little Schemer' using Clojure.

### Why
This book is great fun to read and it is easy to understand. The format is a kind conversation between the author and reader. And finally, the book even has dedicated pages for jelly-stains! It teaches programming small but realistic applications with the least amount of jargon and concepts. I think it is a perfect place to start learning to program if you haven't done so yet. It also helps seasoned developers like myself get back to the fundamentals of how to think about computation. 

For me, the challenge was to follow along in the book via programming in Clojure, a recent JVM-based Lisp. I recently worked on a side-project where the most smartest solution would have been a recursive algorithm but I wasn't able to solve it without head-scratching all the way through. With this series I want to recap my recursion skills and learn them through Clojure, my favorite programming language.

### How
I'm currently using a very barebones setup to follow along: I used Leiningen to create a standalone Clojure project `lein new the-little-clojurian`. I use GNU Emacs 25 with Cider on a 3-year old Macbook Air. 

My workflow is as follows: 

Once...
1. I open `project.clj` in a buffer and 
2. run `M-x cider-jack-in` to get a REPL

Then, I...
1. Write the test as it appears in the book,
2. use `C-c k` to compile the file and 
3. `C-c ,` to run the tests. 

When I have a failing test, I fix the implementation only as much as to get the test to pass. Following the book, I continue this pattern until the chapter is over.

I am using the `with-test` macro and write my tests and implementation all in the main core namespace. Normally, we tend to write our tests in separate files but I've grown fond of `with-test` because of 

1. less switching between buffers
2. having all code right in front of me.

I like it.

In a way, the book is written in a conversational, assumptions-first, almost test-driven way, so I write the assertion first and then gradually implement the function itself. 

The function-definition, including tests for `lat?` looks like this for example: 

```clojure
(ns the-little-clojurian.chapter2
  (:require [clojure.test :refer :all]
            [the-little-clojurian.chapter1 :refer :all]))

(with-test 
  ;; Function-definition
  (def lat? 
    (fn [l]
      (cond (null? l) true
            (atom? (car l)) (lat? (cdr l))
            :else false)))
  ;; Tests
  (testing "returns true"
    (is (= (lat? '(Jack Sprat could eat no chicken fat)) true))
    (is (= (lat? '()) true))
    (is (= (lat? '(bacon and eggs)) true)))
  (testing "returns false"
    (is (= (lat? '((Jack) Sprat could eat no chicken fat)) false))
    (is (= (lat? '(Jack (Sprat could) eat no chicken fat)) false))
    (is (= (lat? '(bacon (and eggs))) false))))
```

The full transcript of Chapter 1 in Clojure is:

```
(ns the-little-clojurian.chapter1
  (:require [clojure.test :refer :all]))

(declare listp? atom? s-expression? car cdr conss null? eq?)

(with-test 
  (def atom? 
    (fn [a] 
      (not (listp? a))))
  (testing "returns true"
    (is (= true (atom? 'atom)))
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
```

## The Little Schemer in Clojure – Chapter 2 Recap

Yesterday I made it through Chapter 2 and as before I've enjoyed it. I have come to copy the question/answer style from the book into the tests: 

First I write the application of the function under test, then the expectation such as:

```
(is (= (lat? '(Jack Sprat could eat no chicken fat)) true))
```

I understand that calling functions recursively, as done in the book, isn't idiomatic in Clojure.  

```
(def lat? 
    (fn [l]
      (cond (null? l) true
            (atom? (car l)) (lat? (cdr l))
            :else false)))
```

There's `recur` for that. For now, I like to continue with calling functions regardless. The data-size is small and, since I have tests in place, once I'm through with the book, I can imagine a refactor using `recur`.

On the same note, I recognize that Clojure has great helper functions such as `every?` that I could leverage directly. For example, the above mentioned function `lat?` could become a one-liner such as by asking "is every expression in the list not a list itself".

```
(def lat? 
  (fn [l] 
    (every? #(not (list? %))) l)))
```

This is much cleaner (and every? Itself is a recursive function) but in the spirit of the book, I'm going to stick with the functions as they are discussed. Again, my test-suite will allow for refactoring as needed later.

Lastly, I am getting into the habit of tiny, regular commits. I now make a commit when I have fully tested a function just before moving on to the next one. I can see even smaller commits on every red-to-green test cycle.

If you are interested, the full listing for Chapter 2 is as follows:

```
(ns the-little-clojurian.chapter2
  (:require [clojure.test :refer :all]
            [the-little-clojurian.chapter1 :refer :all]))

(with-test 
  (def lat? 
    (fn [l]
      (cond (null? l) true
            (atom? (car l)) (lat? (cdr l))
            :else false)))
  (testing "returns true"
    (is (= (lat? '(Jack Sprat could eat no chicken fat)) true))
    (is (= (lat? '()) true))
    (is (= (lat? '(bacon and eggs)) true)))
  (testing "returns false"
    (is (= (lat? '((Jack) Sprat could eat no chicken fat)) false))
    (is (= (lat? '(Jack (Sprat could) eat no chicken fat)) false))
    (is (= (lat? '(bacon (and eggs))) false))))

(deftest boolean-operators
  (is (= (or (null? '())
             (atom? '(d e f g)))
         true))
  (is (= (or (null? '(a b c))
             (null? '()))
         true))
  (is (= (or (null? '(a b c))
             (null? '(atom)))
         false)))

(with-test 
  (def member? 
    (fn [a lat]
      (cond (null? lat) false
            :else (or (eq? (car lat) a) 
                      (member? a (cdr lat))))))
  (testing "evaluates to true"
    (is (= (member? 'meat '(meat)) true))
    (is (= (member? 'tea '(coffee tea and milk))))
    (is (= (member? 'meat '(mashed potatoes and meat gravy)) true)))
  (testing "evaluates to false"
    (is (= (member? 'meat '()) false))
    (is (= (member? 'liver '(bagels and lox)) false))
    (is (= (member? 'poached '(fried eggs and scrambled eggs))))))
```

## The Little Schemer in Clojure – Chapter 3 Recap

**Cons the magnificent**

In this chapter I learned how to create lists using `cons` and recursion.

The first function discussed was `rember`, which removes the first occurrence of `a` in `lat` and returns the result as a new list.

As you can see from below, the function asks three questions:

1. Is `lat` null?, or empty. If so, return the empty list `()
2. Is `(car lat)` equal to the atom we're looking to drop? If so, return the rest of the list `(cdr lat)` and effectively drop `(car lat)` from the list, implementing the requirement of "remove member".
3. If neither of the above is true, `(cons (car lat))` onto the natural recursion of `rember` with `a` and the rest of `lat`, which is `(cdr lat)`.

*Note that I am using `cones` in my listings to stay consistent with the book.

```
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
```

I have begun to always start my tests with the null-case, here it's...

```
(is (= (rember 'and '()) '()))
```
...which drives out the question #1.

Then I write the minimal test that drives out the main behavior without the need for recursion...

```
(is (= (rember 'and '(and)) '()))
```
...which drives out the question #2

And then finally I write a test that drives out question #3 such as...
```
(is (= (rember 'and '(bacon lettuce and tomato)) '(bacon lettuce tomato)))
```

As the books describes in the beginning, over time, patterns emerge from this simple example.

The second function I learned was `firsts` which takes the first s-expression of each nested list in the list.

This time there are only two questions we ask: 
1. Is the list `null?`, then return the empty list `(). This is the necessary terminal clause that will cause a recursion to stop. 
2. Else, `(cons (car (car l))` onto the natural recursion of `firsts` with `(cdr l)`.

So, for example
`(firsts '((a b) (c d) (e f)))` evaluates to `(a c e)`.

Here, again, I started with the simplest case with `'()` for `l` and worked through the rest of the example step by step following the book.

```
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
```

After that I wrote `insertR` which inserts a new s-expression to the right of an the first occurrence of an existing s-expression inside a list.

The questions this function needs to ask and answer are:

1. Is `lat` null? If so, return the empty list `().
2. Is `(car lat)` equal to `old` and if so, `(cons old (cons new (cdr lat)))` which basically says append `new`, then `old` onto the rest.
3. Else `(cons (car lat))` onto the natural recursion of this function with `new`, `old` and `(cdr lat)`.

```
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
```

Now, the next function, `insertL` inserts a new element to the left of an existing one. The only difference here was what we do in case `(eq? (car lat) old)` namely `(cons new lat)`

```
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
```

Now up to this point the functions only replace or insert or remove the _first occurence_ of whatever the matching function was. What about multiple occurrences and manipulations?

When you look at `multimember` you'll notice that we drop the current occurrence of `a` in `lat` and recur via `(multirember a (cdr lat)` which will drop any following occurrences as well.

```
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
```

This pattern persisted for the following, improved versions of `insertL`, `insertR` and `subst`:

```
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
```

All in all I had great fun really digging into it. The question/answer style still lends itself to writing the test first and then following along with the implementation.

## The Little Schemer in Clojure – Chapter 4

**Number Games**

I've enjoyed working through chapter 4. It's simpler and more dense at the same time since it's all about numbers. Over the course of this chapter I (re)implemented several arithmetic basic functions such as `add1`, `sub1` – Those were rather helper functions – and moved on to implementing `pluss` and `minuss`, `multiply` and `divide` (I added the extra "s" to pluss and minuss to avoid redefining built-in functions). 

The cool thing about this chapter is that I was able to implement these functions only in terms of very simple operations (add1, sub1) and recursion.

For example, `pluss` works as follows:

Given `n` and `m`, I want to evaluate their sum. For that to happen, I need to ask two questions:

* Is `m` equal to 0? If so, return `n`. This would make an addition such as 1 + 0 work straight away.
* Else, `add1` to the natural recursion of `pluss` with `n` and `(sub1 m)`. This counts `m` down with each recursion until it reaches 0. 

If we were to add 1 and 3, the result of 4 would be calculated as:

```
(pluss 1 3)
1 + (pluss 1 2)
1 + 1 + (pluss 1 1)
1 + 1 + 1 (pluss 1 0)
1 + 1 + 1 + 1
```

Pretty cool.

```
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
```

For every function I write, I also make sure I start with the smallest possible test-case and then only add the code necessary to fix that test. I quiet enjoy it because it gives me confidence that, once I refactor, the functions still work.

For example, take `occur` which takes an atom `a` and a list `lat` as its arguments and returns the count of `a` in `lat`.

To build the simple algorithm, I start out writing the bare minimum setup for the first, smallest test:

```
(with-test
  (def occur
    (fn [a lat]))

  (is (= (occur 'a '()) 0)))
```

Running the test will fail since the function currently, and intentionally, only returns `nil` and not 0 when given `'a` and `'()` as its inputs.

I can fix this situation by writing the smallest possible solution which is to return 0, hardcoded:

```
(with-test
  (def occur
    (fn [a lat] 0))

  (is (= (occur 'a '()) 0)))
```

Our first test will now pass. We could, if we wanted to, make a commit at this point. On to the next.

```
(with-test
  (def occur
    (fn [a lat] 0))

  (is (= (occur 'a '()) 0))
  (is (= (occur 'a '(a)) 1)))
```

Running the tests will now fail for scenario 2 when we expect to receive 1 as the value and not 0. The smallest fix to solve for this is to introduce a conditional.

```
(with-test
  (def occur
    (fn [a lat]
      (cond (null? lat) 0
            :else 1)))

  (is (= (occur 'a '()) 0))
  (is (= (occur 'a '(a)) 1)))
```

Great! All tests are passing now. Now, on to the next, simplest test-case where `(occur 'a '(b))` should evaluate to 0.

```
(with-test
  (def occur
    (fn [a lat]
      (cond (null? lat) 0
            :else 1)))

  (is (= (occur 'a '()) 0))
  (is (= (occur 'a '(a)) 1))
  (is (= (occur 'a '(b)) 0)))
```

The third test fails naturally. To fix it, I now have to introduce a new condition to check if `(car lat)` is equal to `a`:

```
(with-test
  (def occur
    (fn [a lat]
      (cond (null? lat) 0
            (equan? (car lat) a) 1
            :else 0)))

  (is (= (occur 'a '()) 0))
  (is (= (occur 'a '(a)) 1))
  (is (= (occur 'a '(b)) 0)))
```

Now, let's test for multiple occurrences of `a` in `lat`: `(is (= (occur 'a '(a a)) 2))`

```
(with-test
  (def occur
    (fn [a lat]
      (cond (null? lat) 0
            (equan? (car lat) a) 1
            :else 0)))

  (is (= (occur 'a '()) 0))
  (is (= (occur 'a '(a)) 1))
  (is (= (occur 'a '(b)) 0))
  (is (= (occur 'a '(a a)) 2)))
```

The test will fail when run. Now we have to fix it by introducing adding 1 to the natural recursion of the function as follows: ```(add1 (occur a (cdr lat)))```

```
(with-test
  (def occur
    (fn [a lat]
      (cond (null? lat) 0
            (equan? (car lat) a) (add1 (occur a (cdr lat)))
            :else 0)))

  (is (= (occur 'a '()) 0))
  (is (= (occur 'a '(a)) 1))
  (is (= (occur 'a '(b)) 0))
  (is (= (occur 'a '(a a)) 2)))
```

Splendid, all tests are passing.

Now, I want to make sure that we get the right result, when the list is mixed, such as `(is (= (occur 'a '(a b c a)) 2))`

```
(with-test
  (def occur
    (fn [a lat]
      (cond (null? lat) 0
            (equan? (car lat) a) (add1 (occur a (cdr lat)))
            :else 0)))

  (is (= (occur 'a '()) 0))
  (is (= (occur 'a '(a)) 1))
  (is (= (occur 'a '(b)) 0))
  (is (= (occur 'a '(a a)) 2))
  (is (= (occur 'a '(a b c a)) 2)))
```

Adding the test makes it fail of course, since we're not accounting for this scenario. It's a simple fix, we need to allow our `:else` branch to continue searching for `a`s with `(occur a (cdr lat))`. The final solution to `occur` is

```
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
  (is (= (occur 'a '(a b c a)) 2)))
```

I think this is pretty cool. We've got a first set of reasonable inputs and outputs covered. This would be a great time to run the full test-suite with `lein test`, and if everything passes, make a commit and push to master. For this I usually make myself a tiny script named `ship.sh` that runs the test-suite one final time and does the git push for me:

ship.sh
```
#!/bin/bash

lein test && git push origin master
```

Nothing fancy, but it keeps me from habitually doing a `git push origin master` without running all tests beforehand. 

Anyways, I enjoyed showing you how I build my functions with TDD and baby-steps to arrive at a good place to commit and push. The full listing of my (un-refactored) chapter 4 is as follows:

```
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
            (one? n) (car lat)
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
            (one? n) (cdr lat)
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
```

## The Little Schemer in Clojure – Chapter 5

**Oh My Gawd, It's Full Of Stars**

### Dealing With Nested Lists

Learned how to deal with nested lists by asking at least 3 questions:

* Is the list null? If so, return the empty element (an empty list if the value of the function is to be a list, or 0 if you're evaluating to a number)
* Is the first element in the list an atom? – If so, operate on that atom and cons it onto the natural recursion of the function.
* Else, cons the natural recursion of the function of `car list` onto the natural recursion of the function of `cdr list`

You can see this pattern in action on `rember*` which removes any occurrence of `a` in `l` regardless of how deep `a`s are hidden in the nested list. (*, or star is added to the function name to denote that it's recurring on both, `car` and `cdr`.)

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

The same applies to `insertR and insertL*` which respectively return a new list with `new` inserted next to to `old` in `l`.

The questions are again: 

* null? – return an empty list that can be `cons`-ed onto from previous calls
* atom? – checks if `car l` is what we're looking for and if so, adds a new element
* else – recurs on both `car l` and `cdr l`

```
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
```

```
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
```

Then, the same pattern applies to `subst*` which substitutes `new` with `old` in `l`

```
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
```

Then, `member*` returns `true` if `a` can be found in `l`, otherwise it returns `false`:

Note that instead of returning a list on `(null l)` it returns `false` which represents that the element couldn't be found and we've reached the end where we can't look no further.

```
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
```

## Traversing a Tree

A personal favorite of mine is `leftmost` which returns the left-most element in `l`. It's a bit simpler than the functions before. It recurs **only** on `(car l)` unless it is an atom:

```
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
```

### Testing equality

At the end of the chapter, the authors use recurring on a nested list to test for equality of any s-expression:

```
(with-test
  (def eqlist?
    (fn [l1 l2] 
      (cond (and (null? l1) (null? l2)) true
            (or (null? l1) (null? l2)) false
            :else (and (equal? (car l1) (car l2))
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
  (def equal? 
    (fn [s1 s2]
      (cond (and (atom? s1) (atom? s2)) (equan? s1 s2)
            (or (atom? s1) (atom? s2)) false
            :else (eqlist? s1 s2))))
  
  (is (= (equal? 'a 'a) true))
  (is (= (equal? 'a 'b) false))
  (is (= (equal? 'a '(a)) false))
  (is (= (equal? '(a) 'a) false))
  (is (= (equal? '(a) '(a)) true))
  (is (= (equal? '(a) '(b)) false)))
```

I am starting to realize how great it is to have a set of tests right away with any function definition. Refactoring will be easy and give me confidence that everything still works. Pretty stoked!

