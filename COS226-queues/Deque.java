import java.lang.IllegalArgumentException;
import java.lang.UnsupportedOperationException;

import java.util.NoSuchElementException;
import java.util.Iterator;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

public class Deque<Item> implements Iterable<Item> {

    private int size; // Size of deque

    // Node used in linked-list
    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }

    private Node nFront; // Front (End of ll)
    private Node nBack; // Back (Start of ll)

    // Construct an empty deque
    public Deque() {

        nFront = nBack = null;
        size = 0;
    }

    // Is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // Return the number of items on the deque
    public int size() {
        return this.size;
    }

    // Add the item to the front
    public void addFirst(Item item) {

        size++;
        if (size == 1) {
            nFront = new Node();
            nFront.item = item;
            nBack = nFront;

            return;
        }

        Node aux = new Node();
        aux.item = item;
        nFront.next = aux;
        Node aux2 = nFront;
        nFront = aux;
        aux.prev = aux2;
    }

    // Add the item to the back
    public void addLast(Item item) {

        size++;
        if (size == 1) {
            nBack = new Node();
            nBack.item = item;
            nFront = nBack;

            return;
        }

        Node aux = new Node();
        aux.item = item;
        nBack.prev = aux;
        Node aux2 = nBack;
        nBack = aux;
        nBack.next = aux2;
    }

    // Remove and return the item from the front
    public Item removeFirst() {

        if (size == 0)
            throw new NoSuchElementException();

        size--;
        Item ret = nFront.item;
        nFront = nFront.prev;
        if (nFront != null)
            nFront.next = null;

        if (size == 0)
            nFront = nBack = null;

        return ret;
    }

    // Remove and return the item from the back
    public Item removeLast() {

        if (size == 0)
            throw new NoSuchElementException();

        size--;
        Item ret = nBack.item;
        nBack = nBack.next;
        if (nBack != null)
            nBack.prev = null;

        if (size == 0)
            nFront = nBack = null;

        return ret;
    }

    // Return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {

        // 'Head' of linked list
        Node current = nFront;

        public boolean hasNext() {
            return (current != null);
        }

        public Item next() {

            if (!hasNext())
                throw new NoSuchElementException();

            // Going backwards
            Item ret = current.item;
            current = current.prev;
            return ret;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    // Unit testing
    public static void main(String[] args) {

        // javac-algs4 Deque.java && java-algs4 Deque

        Deque<Integer> teste = new Deque<Integer>();

        StdOut.println("\nComandos:\n'AF valor' - Adiciona valor na frente;\n"
                + "'AB valor' - Adiciona valor no começo;\n" + "'RF' - Printa e exclui valor do fim;\n"
                + "'RB' - Printa e exclui valor do começo;\n" + "'P' - Printa todos os elementos (Front -> Back);\n"
                + "'0' - Finaliza unit test.\n" + "OS VALORES INSERIDOS DEVEM SER INTEIROS.\n");

        // Userr iterative test
        while (true) {
            String op = StdIn.readString();

            if (op.equals("0")) {
                StdOut.println("Tamaho final: " + teste.size());
                break;
            }

            if (op.equals("AF")) {
                int v = StdIn.readInt();

                teste.addFirst(v);
            }

            else if (op.equals("AB")) {
                int v = StdIn.readInt();

                teste.addLast(v);
            }

            else if (op.equals("RF")) {
                Integer aux = teste.removeFirst();

                StdOut.println(aux);
            }

            else if (op.equals("RB")) {
                Integer aux = teste.removeLast();

                StdOut.println(aux);
            }

            else if (op.equals("P")) {

                for (Integer p : teste)
                    StdOut.println(p);
            }
        }
    }
}
