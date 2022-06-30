package queue;

import java.util.function.Predicate;

// I: for i = 0..n-1 : a[i] != null
// Let Immutable (Imm) (x) : for i = 0..n: a'[i] = a[i]
public interface Queue {
        // P: elem != null
        // Q: n' = n + 1 && Imm(n \ 0) && a[0] = elem
        void enqueue(Object elem);

        // P: n >= 1
        // Q: R = queue[n] && n' = n && Imm(n)
        Object element();

        // P: n >= 1
        // Q: n' = n - 1 && Imm(n \ n-1) && R == a[n]
        Object dequeue();

        // P: true
        // Q: R = n && n' = n && Imm(n)
        int size();

        // P: true
        // Q: R = (n == 0) && n' = n && Imm(n)
        boolean isEmpty();

        // P: true
        // Q: n' = 0
        void clear();

        // P: true
        // Q: R = число элементов, удовлетворяющих предикату && Imm(n)
        int countIf(Predicate<Object> predicate);
}
