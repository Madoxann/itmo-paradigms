package queue;

public class LinkedQueue extends AbstractQueue {
    private Node head;
    private Node tail = new Node(null, null);

    @Override
    public void push(Object elem) {
        if (size == 1) {
            head = new Node(tail, elem);
            return;
        }
        tail.value = elem;
        tail.next = new Node(tail, null);
        tail = tail.next;
    }

    @Override
    public Object getHead() {
        return head.value;
    }

    @Override
    public void popHead() {
        head = head.next;
    }

    @Override
    public void clearImpl() {
        tail.next = null;
        tail.value = null;
    }

    private static class Node {
        private Node next;
        private Object value;

        public Node(Node link, Object val) {
            next = link;
            value = val;
        }
    }
}
