package queue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

// I: for i = 0..n-1 : a[i] != null
// Let Immutable (Imm) (x) : for i = 0..n: a'[i] = a[i]

public class ArrayQueueADT {
    private Object[] queue = new Object[5];
    private int size = 5, head = 0, tail = 0;
    private HashMap<Object, Integer> elements = new LinkedHashMap<>();

    // P: elem != null
    // Q: n' = n + 1 && Imm(n \ 0) && a[0] = elem
    public static void enqueue(final ArrayQueueADT queueADT, Object elem) {
        ensureCapacity(queueADT, (queueADT.tail + 1) % queueADT.size);
        queueADT.elements.putIfAbsent(elem, 0);
        queueADT.elements.put(elem, queueADT.elements.get(elem) + 1);
        queueADT.queue[queueADT.tail] = elem;
        queueADT.tail = (queueADT.tail + 1) % queueADT.size;
    }

    // P: n >= 1
    // Q: R = queue[n] && n' = n && Imm(n)
    public static Object element(final ArrayQueueADT queueADT) {
        return queueADT.queue[queueADT.head];
    }

    // P: n >= 1
    // Q: R = queue[n] && n' = n && Imm(n)
    public static Object dequeue(final ArrayQueueADT queueADT) {
        if (isEmpty(queueADT)) return null;
        Object ret = queueADT.queue[queueADT.head];
        queueADT.elements.put(queueADT.queue[queueADT.head], queueADT.elements.get(queueADT.queue[queueADT.head]) - 1);
        queueADT.head = (queueADT.head + 1) % queueADT.size;
        return ret;
    }

    // P: true
    // Q: R = n && n' = n && Imm(n)
    public static int size(final ArrayQueueADT queueADT) {
        if (queueADT.head > queueADT.tail) return queueADT.size - queueADT.head + queueADT.tail;
        return queueADT.tail - queueADT.head;
    }

    // P: true
    // Q: R = (n == 0) && n' = n && Imm(n)
    public static boolean isEmpty(final ArrayQueueADT queueADT) {
        return  queueADT.head == queueADT.tail;
    }

    // P: true
    // Q: n' = 0
    public static void clear(final ArrayQueueADT queueADT) {
        queueADT.queue = new Object[5];
        queueADT.elements = new HashMap<>();
        queueADT.size = 5;
        queueADT.head = 0;
        queueADT.tail = 0;
    }

    private static void ensureCapacity(final ArrayQueueADT queueADT, int ind) {
        if (queueADT.head == ind) {
            Object[] newQueue = new Object[queueADT.size*2];
            System.arraycopy(queueADT.queue, queueADT.head, newQueue, 0, queueADT.size - queueADT.head);
            System.arraycopy(queueADT.queue, 0, newQueue, queueADT.size - queueADT.head, queueADT.head);
            queueADT.head = 0;
            queueADT.tail = queueADT.size - 1;
            queueADT.size *= 2;
            queueADT.queue = newQueue;
        }
    }

    // P: true
    // Q: R = число вхождений элемента
    public static int count(final ArrayQueueADT queueADT, Object elem) {
        Integer retVal = queueADT.elements.get(elem);
        return retVal == null ? 0 : retVal;
    }
}