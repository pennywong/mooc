import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int N;
    private Node<Item> head;
    private Node<Item> tail;

    private static class Node<Item> {
        private Item item;
        private Node<Item> pre;
        private Node<Item> next;
    }

    // construct an empty deque
    public Deque() {
        N = 0;
        head = null;
        tail = null;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return N == 0;
    }

    // return the number of items on the deque
    public int size() {
        return N;
    }

    // insert the item at the front
    public void addFirst(Item item) {
        Node<Item> node = getNode(item);

        if (isEmpty())
            tail = node;
        else
            head.pre = node;

        node.next = head;
        head = node;

        N++;
    }

    // insert the item at the end
    public void addLast(Item item) {
        Node<Item> node = getNode(item);

        if (isEmpty())
            head = node;
        else
            tail.next = node;

        node.pre = tail;
        tail = node;

        N++;
    }

    private Node<Item> getNode(Item item) {
        if (item == null)
            throw new NullPointerException("Null item");

        Node<Item> node = new Node<Item>();
        node.item = item;
        node.pre = null;
        node.next = null;

        return node;
    }

    // delete and return the item at the front
    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException("Empty Deque");

        Item item = head.item;
        head = head.next;

        N--;

        if (isEmpty())
            tail = null;
        else
            head.pre = null;

        return item;
    }

    // delete and return the item at the end
    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException("Empty Deque");

        Item item = tail.item;
        tail = tail.pre;

        N--;

        if (isEmpty())
            head = null;
        else
            tail.next = null;

        return item;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new ListIterator<Item>(head);
    }

    @SuppressWarnings("hiding")
    private class ListIterator<Item> implements Iterator<Item> {
        private Node<Item> current;

        public ListIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();

            Item item = current.item;
            current = current.next;
            return item;
        }
    }

}
