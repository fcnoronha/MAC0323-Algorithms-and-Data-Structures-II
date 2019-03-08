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

    // construct an empty deque
    public Deque(){
        nFront = nBack = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty(){
        return size == 0;
    }

    // return the number of items on the deque
    public int size(){
        return this.size;
    }

    // add the item to the front
    public void addFirst(Item item){

        if (nFront == null) {
            nFront = new Node();
            nFront.item = item;
            nBack = nFront;
            size++;

            return;
        }

        size++;
        Node aux = new Node();
        aux.item = item;
        nFront.next = aux;
        Node aux2 = nFront;
        nFront = aux;
        aux.prev = aux2;
    }

    // add the item to the back
    public void addLast(Item item){

        if (nBack == null) {
            nBack = new Node();
            nBack.item = item;
            nFront = nBack;
            size++;

            return;
        }

        size++;
        Node aux = new Node();
        aux.item = item;
        nBack.prev = aux;
        Node aux2 = nBack;
        nBack = aux;
        nBack.next = aux2;
    }

    // remove and return the item from the front
    public Item removeFirst(){

        if (size == 0)
            throw new NoSuchElementException();

        size--;
        Item ret = nFront.item;
        nFront = nFront.prev;

        return ret;
    }

    // remove and return the item from the back
    public Item removeLast(){

        if (size == 0)
            throw new NoSuchElementException();

        size--;
        Item ret = nBack.item;
        nBack = nBack.next;

        return ret;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator(){
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {

        Node current = nFront;

        public boolean hasNext() {
            return (current != null) ;
        }

        public Item next() {

            if (!hasNext())
                throw new NoSuchElementException();

            Item ret = current.item;
            current = current.prev;
            return ret;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    // unit testing (required)
    public static void main(String[] args){

        // javac-algs4 Deque.java && java-algs4 Deque

        Deque<Integer> teste = new Deque<Integer>();

        // Fazer o unit test ser um while infinito,
        // Tipo queries da maratona

        StdOut.println("\nComandos:\n'AF valor' - Adiciona valor na frente;\n"
        + "'AB valor' - Adiciona valor no começo;\n" +
        "'RF' - Printa e exclui valor do começo;\n" +
        "'RF' - Printa e exclui valor do começo;\n" +
        "'P' - Printa todos os elementos (Front -> Back);\n" +
        "'0' - Finaliza unit test.\n" +
        "OS VALORES INSERIDOS DEVEM SER INTEIROS.\n");

        while (true) {
            String op = StdIn.readString();

            if (op.equals("0"))
                break;

            if (op.equals("AF")){
                int v = StdIn.readInt();

                teste.addFirst(v);
            }

            else if (op.equals("AB")){
                int v = StdIn.readInt();

                teste.addLast(v);
            }

            else if (op.equals("RF")){
                Integer aux = teste.removeFirst();

                StdOut.println(aux);
            }

            else if (op.equals("RB")){
                Integer aux = teste.removeFirst();

                StdOut.println(aux);
            }

            else if (op.equals("P")){

                for (Integer p : teste)
                    StdOut.println(p);
            }
        }


    }

}
