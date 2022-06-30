package queue;

import java.util.function.Predicate;

public class ArrayQueue extends AbstractQueue {
    private Object[] queue = new Object[5];
    private int capacity = 5, head = 0, tail = 0;

    @Override
    public void push(Object elem) {
        ensureCapacity((tail + 1) % capacity);
        queue[tail] = elem;
        tail = (tail + 1) % capacity;
    }

    @Override
    public Object getHead() {
        return queue[head];
    }

    @Override
    public void popHead() {
        head = (head + 1) % capacity;
    }

    @Override
    public void clearImpl() {
        queue = new Object[5];
        capacity = 5;
        head = 0;
        tail = 0;
    }

    private void ensureCapacity(int ind) {
        if (head == ind) {
            Object[] newQueue = new Object[capacity *2];
            System.arraycopy(queue, head, newQueue, 0, capacity - head);
            System.arraycopy(queue, 0, newQueue, capacity - head, head);
            head = 0;
            tail = capacity - 1;
            capacity *= 2;
            queue = newQueue;
        }
    }

    public int count(Object elem) {
        Predicate<Object> predicate = o -> o.equals(elem);
        return countIf(predicate);
    }
}