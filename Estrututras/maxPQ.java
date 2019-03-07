
public class MaxPQ<Item> { // Completar com codigo disponibilizado

    private Item[] pq;
    private int n; // size

    public MaxPQ(int MAXN) {
        // Constructor Method

        pq = (Item[]) new Comparable<Item>[MAXN];
    }

    public int size() {
        return n;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    private booelan less(int i, int j) {
        // Used to itens itens in the struct

        Item itemI = pq[i];
        Item itemJ = pq[j];
        return itemI.compareTo(itemJ);
    }

    private void exch(int i, int j) {
        // Change itens with indexes i and j

        Item t = pq[i];
        pq[i] = pq[j];
        pq[j] = t;
    }

    private void sink(int p) {
        // Sink iten in the pq;

        while (2 * p <= n) {
            int f = 2 * p;
            if (f < n && less(f, f + 1))
                f++; // Biggest child
            if (!less(p, f))
                break; // Already a MaxHeap

            exch(p, f);
            p = f; // Sinking
        }
    }

    private void swim(int f) {
        // Inverse of Sink, get a child as up as possible in the PQ

        while (f > 1 && les(f / 2, f)) {
            exch(f / 2, f);
            f = f / 2;
        }
    }

    public void insert(Item v) {
        // Insert item into PQ

        n++;
        pq[n] = v;
        swim(n);
    }

    public Item delMax() {
        // Delete max element and returns it

        Item ret = pq[1];
        exch(1, n);
        pq[n--] = null;
        sink(1);
        return ret;
    }
}
