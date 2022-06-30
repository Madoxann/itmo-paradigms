package queue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

// I: for i = 0..n-1 : a[i] != null
// Let Immutable (Imm) (x) : for i = 0..n: a'[i] = a[i]

public class ArrayQueueModule {
    private static Object[] queue = new Object[5];
    private static int size = 5, head = 0, tail = 0;
    private static HashMap<Object, Integer> elements = new LinkedHashMap<>();

    // P: elem != null
    // Q: n' = n + 1 && Imm(n \ 0) && a[0] = elem
    public static void enqueue(Object elem) {
        ensureCapacity((tail + 1) % size);
        queue[tail] = elem;
        elements.putIfAbsent(elem, 0);
        elements.put(elem, elements.get(elem) + 1);
        tail = (tail + 1) % size;
    }

    // P: n >= 1
    // Q: R = queue[n] && n' = n && Imm(n)
    public static Object element() {
        return queue[head];
    }

    // P: n >= 1
    // Q: n' = n - 1 && Imm(n \ n-1) && R == a[n]
    public static Object dequeue() {
        if (isEmpty()) return null;
        Object ret = queue[head];
        elements.put(queue[head], elements.get(queue[head]) - 1);
        head = (head + 1) % size;
        return ret;
    }

    // P: true
    // Q: R = n && n' = n && Imm(n)
    public static int size() {
        if (head > tail) return size - head + tail;
        return tail - head;
    }

    // P: true
    // Q: R = (n == 0) && n' = n && Imm(n)
    public static boolean isEmpty() {
        return head == tail;
    }

    // P: true
    // Q: n' = 0
    public static void clear() {
        queue = new Object[5];
        elements = new HashMap<>();
        size = 5;
        head = 0;
        tail = 0;
    }

    private static void ensureCapacity(int ind) {
        if (head == ind) {
            Object[] newQueue = new Object[size*2];
            System.arraycopy(queue, head, newQueue, 0, size - head);
            System.arraycopy(queue, 0, newQueue, size - head, head);
            head = 0;
            tail = size - 1;
            size *= 2;
            queue = newQueue;
        }
    }

    private static void getArr() {
        int cnt = 0;
        for (Object item : queue) {
            System.err.println(cnt++ + " " + item);
        }
    }

    // P: true
    // Q: R = число вхождений элемента
    public static int count(Object elem) {
        Integer retVal = elements.get(elem);
        return retVal == null ? 0 : retVal;
    }
}