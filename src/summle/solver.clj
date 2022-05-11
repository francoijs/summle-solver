(ns summle.solver
  (:require [clojure.math.combinatorics :as combo]
            [clojure.test :refer :all]
            ))

;; apply op to elements of pair, or return false is impossible
(with-test
  (defn- op-apply [op pair]
    (let [a (apply max pair)
          b (apply min pair)]
      (case op
        + (+ a b)
        - (and (not (= a b)) (- a b))
        * (* a b)
        / (and (integer? (/ a b)) (/ a b))
        false)))
  
  (is (= 3 (op-apply '+ '(1 2))))
  (is (= 1 (op-apply '- '(1 2))))
  (is (not (op-apply '- '(2 2))))
  (is (= 2 (op-apply '* '(1 2))))
  (is (= 2 (op-apply '/ '(1 2))))
  (is (not (op-apply '/ '(3 2))))
  )
(test #'op-apply)

;; remove 1st occurence of el from ls
(with-test
  (defn- delete-first [el ls]
    (let [[n m] (split-with
                 (partial not= el)
                 ls)]
      (concat n (rest m))))
  
  (is (= '() (delete-first 1 '())))
  (is (= '() (delete-first 1 '(1))))
  (is (= '(2) (delete-first 1 '(2 1))))
  (is (= '(2 1 3) (delete-first 1 '(1 2 1 3))))
  )
(test #'delete-first)


(defrecord State [numbers operations])
(defn make-state [numbers] (->State numbers (vector)))

(def ^:dynamic *test-state-1* (make-state '(1 2 3)))
(def ^:dynamic *test-state-2* (make-state '(1 123 2)))
(def ^:dynamic *test-state-3* (make-state '(123)))

;; return new state after applying op on elements in pair,
;; or false if impossible
(with-test
  (defn- state-next [state op pair]
    (let [res (op-apply op pair)]
      (if res
        ;; operation worked: make new state with the result
        (->State (cons res (delete-first (first pair)
                                         (delete-first (second pair) (:numbers state))))
                 (conj (:operations state) (list op (first pair) (second pair))))
        ;; operation failed, no new state
        false)))

  (is (= (state-next *test-state-1* '+ '(2 3))
         (->State '(5 1) '[(+ 2 3)])))
  (is (= (state-next (->State '(5 1) '[(+ 2 3)]) '* '(5 1))
         (->State '(5) '[(+ 2 3) (* 5 1)])))
  (is (not (state-next *test-state-1* '/ '(2 3))))
  )
(test #'state-next)

;; return list of possible next states
(with-test
  (defn- state-next-all [state]
    ;; loop on possible pairs of elements
    (reduce
     (fn [res pair]
       ;; loop on possible operations
       (concat res (reduce
                    (fn [res op]
                      (let [next (state-next state op pair)]
                        (if next
                          (cons next res)
                          res)))
                    '()
                    '(+ - * /))))
     '()
     (combo/combinations (:numbers state) 2)))
  
  (is (= 11 (count (state-next-all *test-state-1*))))
  )
(test #'state-next-all)

;; build and search the tree of states, starting from 'state'
;; return the list of winning states that match value 'result'
(with-test
  (defn- find-winning-states [state result]
    (cond
      ;; state is a winner?
      (some #{result} (:numbers state)) (list state)
      ;; no pair left?
      (< (count (:numbers state)) 2) '()
      :else
      ;; recurse in the tree of next states
      (reduce
       (fn [winners next]
         (concat winners (find-winning-states next result)))
       '()
       (state-next-all state))))

  (is (= (list *test-state-2*) (find-winning-states *test-state-2* 123)))
  (is (= (list (make-state '(1 123 2))) (find-winning-states *test-state-2* 123)))
  (is (= (list *test-state-3*) (find-winning-states *test-state-3* 123)))
  (is (empty? (find-winning-states (make-state '(1 2 3)) 123)))
  )
(test #'find-winning-states)

;; return first shortest solution
(with-test
  (defn solve [numbers result]
    (let [winners (find-winning-states (make-state numbers) result)]
      (if (empty? winners) false
          (:operations
           (first (sort (fn [x y]
                          (< (count (:operations x))
                             (count (:operations y))))
                        winners))))))

  ;; no solution
  (is (not (solve '(3 4 5) 13)))
  ;; solution in 4
  (is (= 4 (count (solve '(1 4 5 5 9) 864))))
  )
(test #'solve)
