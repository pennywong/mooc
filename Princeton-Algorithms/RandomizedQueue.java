import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int N;

    // construct an empty randomized queue
    @SuppressWarnings("unchecked")
    public RandomizedQueue() {
        items = (Item[]) new Object[2];
    }

    // is the queue empty?
    public boolean isEmpty() {
        return N == 0;
    }

    // return the number of items on the queue
    public int size() {
        return N;
    }

    public void enqueue(Item item) // add the item
    {
        if (item == null)
            throw new NullPointerException("Null item");

        if (N == items.length)
            resize(items.length * 2);

        items[N++] = item;
    }

    // delete and return a random item
    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException("Empty queue");

        int p = StdRandom.uniform(N);
        Item result = items[p];

        items[p] = items[N - 1];
        items[N - 1] = null;
        N--;

        if (N > 0 && N == items.length / 4)
            resize(items.length / 2);

        return result;
    }

    // return (but do not delete) a random item
    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException("Empty queue");

        int p = StdRandom.uniform(N);
        return items[p];
    }

    @SuppressWarnings("unchecked")
    private void resize(int capacity) {
        assert capacity >= N;
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++) {
            temp[i] = items[i];
        }
        items = temp;
    }

    // return an independent iterator over
    // items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int index;
        private int[] array;

        public RandomizedQueueIterator() {
            index = 0;
            array = new int[N];
            for (int i = 0; i < N; i++)
                array[i] = i;

            StdRandom.shuffle(array);
        }

        @Override
        public boolean hasNext() {
            return index < N;
        }

        @Override
        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();

            Item item = items[array[index]];
            index++;

            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
