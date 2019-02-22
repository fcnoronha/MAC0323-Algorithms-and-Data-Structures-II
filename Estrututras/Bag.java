import java.util.Iterator; // To make iterator

public class Bag<Item> implements Iterable<Item>{

	// Bag is a data structure where you can put stuff and
	// iterate through them. It's implementations is almost 
	// the same as a linked list.
	private Node first;
	private int n;
	private Node current;

	private class Node {
		private Item item;
		private Node next;
	}

	public Bag(){ 
		// Constructor
		first = null;
	}

	public void add(Item item){
		// Add element to the bag
		Node oldFirst = first;
		first = new Node();
		first.item = item;
		first.next = oldFirst;
		n++;
	}

	public int size(){
		// Return the size of the bag
		return n;
	}

	public boolean isEmpty(){
		// Return true if there is any item on it
		return n == 0;
	}

	public Iterator<Item> iterator(){
		// Iterator method
		return new BagIterator();
	}

	private class BagIterator implements Iterator<Item>{
		// Iterator class
		private Node current = first;

		public boolean hasNext(){
			return current != null;
		}

		public Item next(){
			Item item = current.item;
			current = current.next;
			return item;
		}

		public void remove(){
			throw new UnsupportedOperationException();
		}
	}
}