package queue;

import java.util.function.Predicate;

public abstract class AbstractQueue implements Queue {
    protected int size;

    @Override
    public void enqueue(Object elem) {
        size++;
        push(elem);
    }

    public abstract void push(Object elem);

    @Override
    public Object element() {
        return getHead();
    }

    public abstract Object getHead();

    @Override
    public Object dequeue() {
        Object result = getHead();
        size--;
        popHead();
        return result;
    }

    public abstract void popHead();

    @Override
    public int countIf(Predicate<Object> predicate) {
        int cnt = 0;
        for (int  i = 0; i < size; i++) {
            Object tmp = dequeue();
            if (predicate.test(tmp)) {
                cnt++;
            }
            enqueue(tmp);
        }
        return cnt;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    
    @Override
    public void clear() {
        size  = 0;
        clearImpl();
    }

    protected abstract void clearImpl();

    @Override
    public int size() {
        return size;
    }
}
