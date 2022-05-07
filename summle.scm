;; https://summle.net/

;; remove consecutive duplicates in a sorted list
(define (remove-dups lst . cmp)
  (let ((proc (if (null? cmp) equal? (car cmp))))
    (cond
     ((null? lst) '())
     ((null? (cdr lst)) lst)
     ((proc (car lst) (car (cdr lst))) (remove-dups (cdr lst) proc))
     (else (cons (car lst) (remove-dups (cdr lst) proc))))))

;; return list of combinations of k elements from ls
(define (combinations k ls)
  (cond
   ((zero? k) '())
   ((= 1 k) (map list ls))
   (else
    (fold (lambda (el res)
            (append res (map (lambda (comb) (cons el comb))
                             (combinations (-1+ k) (cdr (member el ls))))))
          '()
          ls))))
(assert (equal? (combinations 2 '(1 2 3)) '((1 2) (1 3) (2 3))))
(assert (equal? (combinations 2 '(1 2)) '((1 2))))
(assert (equal? (combinations 1 '(1 2)) '((1) (2))))
(assert (equal? (combinations 0 '(1 2)) '()))

(define-structure (state (type list)) numbers operations)

;; apply op to elements of pair, or return #f is impossible
(define (op-apply op pair)
  (let ((a (apply max pair))
        (b (apply min pair)))
    (case op
      ((+) (+ a b))
      ((-) (and (not (= a b)) (- a b)))
      ((*) (* a b))
      ((/) (and (integer? (/ a b)) (/ a b))))))
(assert (= 3 (op-apply '+ '(1 2))))
(assert (= 1 (op-apply '- '(1 2))))
(assert (not (op-apply '- '(2 2))))
(assert (= 2 (op-apply '* '(1 2))))
(assert (= 2 (op-apply '/ '(1 2))))
(assert (not (op-apply '/ '(3 2))))

;; remove 1st occurence of el from ls
(define (delete-first el ls)
  (cond
   ((null? ls) '())
   ((= el (car ls)) (cdr ls))
   (else
    (cons (car ls) (delete-first el (cdr ls))))))
(assert (equal? '() (delete-first 1 '())))
(assert (equal? '() (delete-first 1 '(1))))
(assert (equal? '(2) (delete-first 1 '(2 1))))
(assert (equal? '(2 1 3) (delete-first 1 '(1 2 1 3))))

;; return new state after applying op on elements in pair, or #f if impossible
(define (state-next state op pair)
  (let ((res (op-apply op pair)))
    (if res
        ;; operation worked: make new state with the result
        (make-state (cons res (delete-first (first pair)
                                            (delete-first (second pair) (state-numbers state))))
                    (cons (list op (first pair) (second pair)) (state-operations state)))
        ;; operation failed, no new state
        #f)))
(define *test-state-1* (make-state '(1 2 3) '()))
(assert (equal? (state-next *test-state-1* '+ '(2 3))
                '((5 1) ((+ 2 3)))))
(assert (not (state-next *test-state-1* '/ '(2 3))))

;; return all possible next states
(define (get-next-states state)
  ;; loop on possible pairs of elements
  (fold (lambda (pair res)
          ;; loop on possible operations
          (append res (fold (lambda (op res)
                              (let ((next (state-next state op pair)))
                                (if next
                                    (cons next res)
                                    res)))
                            '()
                            '(+ - * /))))
        '()
        (combinations 2 (state-numbers state))))
(assert (= 11 (length (get-next-states *test-state-1*))))

;; return list of winning states for result
(define (find-winning-states state result)
  (cond
   ((member result (state-numbers state)) (list state))   ;; state is a winner
   ((< (length (state-numbers state)) 2) '())             ;; no pair left
   (else
    ;; recurse in the tree of next states
    (fold (lambda (next winners)
            (append winners (find-winning-states next result)))
          '()
          (get-next-states state)))))
(assert (null? (find-winning-states (make-state '(1) '()) 123)))
(define *test-state-3* (make-state '(123) '()))
(assert (equal? (list *test-state-3*) (find-winning-states *test-state-3* 123)))
(assert (null? (find-winning-states (make-state '(1 2 3) '()) 123)))
(define *test-state-2* (make-state '(1 123 2) '()))
(assert (equal? (list *test-state-2*) (find-winning-states *test-state-2* 123)))
(assert (equal? (list (make-state '(1 123 2) '())) (find-winning-states *test-state-2* 123)))

;; display first shortest solution
(define (find-best-solution numbers result)
  (let ((winners (find-winning-states (make-state numbers '()) result)))
    (if (null? winners)
        (warn "no solution found")
        (map warn
             (reverse (state-operations (car (sort winners
                                                   (lambda (x y)
                                                     (< (length (state-operations x))
                                                        (length (state-operations y))))))))))))
(find-best-solution '(1 4 5 5 9) 864) ;; solution in 4
(find-best-solution '(3 4 5) 13)      ;; no solution
