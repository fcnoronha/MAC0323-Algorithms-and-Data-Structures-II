
/****************************************************************
    Nome: Felipe Castro de Noronha
    NUSP: 10737032

    Ao preencher esse cabeçalho com o meu nome e o meu número USP,
    declaro que todas as partes originais desse exercício programa (EP)
    foram desenvolvidas e implementadas por mim e que portanto não
    constituem desonestidade acadêmica ou plágio.
    Declaro também que sou responsável por todas as cópias desse
    programa e que não distribui ou facilitei a sua distribuição.
    Estou ciente que os casos de plágio e desonestidade acadêmica
    serão tratados segundo os critérios divulgados na página da
    disciplina.
    Entendo que EPs sem assinatura devem receber nota zero e, ainda
    assim, poderão ser punidos por desonestidade acadêmica.

    Abaixo descreva qualquer ajuda que você recebeu para fazer este
    EP.  Inclua qualquer ajuda recebida por pessoas (inclusive
    monitoras e colegas). Com exceção de material de MAC0323, caso
    você tenha utilizado alguma informação, trecho de código,...
    indique esse fato abaixo para que o seu programa não seja
    considerado plágio ou irregular.


    Descrição de ajuda ou indicação de fonte:

    Se for o caso, descreva a seguir 'bugs' e limitações do seu programa:

****************************************************************/

import java.lang.IllegalArgumentException;
import java.lang.UnsupportedOperationException;

import java.util.NoSuchElementException;
import java.util.Iterator;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private int size; // Size of RandomizedQueue

    // Node used in linked-list
    private class Node {
        private Item item;
        private Node next;
    }

    private Node head; // Head of ll

    // Construct an empty randomized queue
    public RandomizedQueue() {
        head = null;
        size = 0;
    }

    // Is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // Return the number of items on the randomized queue
    public int size() {
        return this.size;
    }

    // Add the item
    public void enqueue(Item item) {

        size++;

        Node add = new Node();
        add.item = item;
        add.next = head;
        head = add;
    }

    // Remove and return a random item
    public Item dequeue() {

        if (size == 0)
            throw new NoSuchElementException();

        // Generate random number [0, size[
        int until = StdRandom.uniform(size);

        size--;

        // Head were choosen
        if (until == 0) {
            Item ret = head.item;
            head = head.next;

            return ret;
        }

        Node walk = head;
        for (int i = 0; i < until - 1; i++)
            walk = walk.next;

        Item ret = walk.next.item;
        // Connecting
        walk.next = walk.next.next;

        return ret;
    }

    // Return a random item (but do not remove it)
    public Item sample() {

        if (size == 0)
            throw new NoSuchElementException();

        // Generate random number [0, size[
        int until = StdRandom.uniform(size);

        Node walk = head, aux;
        for (int i = 0; i < until; i++)
            walk = walk.next;

        return walk.item;
    }

    // Return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RQIterator();
    }

    private class RQIterator implements Iterator<Item> {

        int cnt = size;
        int[] untils = null;

        public boolean hasNext() {
            return (cnt > 0);
        }

        public Item next() {

            if (!hasNext())
                throw new NoSuchElementException();

            if (untils == null) {
                // Creates array that will store the random order for
                // visiting each block of linked list

                untils = new int[size];
                for (int i = 0; i < size; i++)
                    untils[i] = i;

                StdRandom.shuffle(untils);
            }

            // Wich block is the actual for visiting
            int actu = untils[cnt - 1];
            cnt--;

            // visiting it
            Node walk = head, aux;
            for (int i = 0; i < actu; i++)
                walk = walk.next;

            return walk.item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // Unit testing (required)
    public static void main(String[] args) {

        // javac-algs4 RandomizedQueue.java && java-algs4 RandomizedQueue

        RandomizedQueue<Integer> teste = new RandomizedQueue<Integer>();

        StdOut.println("\nComandos:\n'ADD valor' - Adiciona valor;\n" + "'DQ' - Mostra e remove valor aleatorio;\n"
                + "'SP' - Mostra um valor aleatorio;\n" + "'P' - Printa todos os elementos;\n"
                + "'0' - Finaliza unit test.\n" + "OS VALORES INSERIDOS DEVEM SER INTEIROS.\n");

        // User iterative test
        while (true) {
            String op = StdIn.readString();

            if (op.equals("0")) {
                StdOut.println("Tamaho final: " + teste.size());
                break;
            }

            if (op.equals("ADD")) {
                int v = StdIn.readInt();

                teste.enqueue(v);
            }

            else if (op.equals("DQ")) {
                StdOut.println(teste.dequeue());
            }

            else if (op.equals("SP")) {
                StdOut.println(teste.sample());
            }

            else if (op.equals("P")) {

                for (Integer p : teste)
                    StdOut.println(p);
            }
        }
    }
}
