import edu.princeton.cs.algs4.*;

import java.util.Iterator;

public class Stack<Item> implements Iterable<Item>{

	private Item[] a;
	private int n;
	
	public Stack(){
		// Class initializer - Constructor
		a = (Item[]) new Object[2];
		n = 0;
	}

	public boolean isEmpty(){
		return n == 0;
	}

	public int size(){
		return n;
	}

	private void resize(int cap){
		// Resize my stack into a size cap
		Item[] t = (Item[]) new Object[cap];
		for (int i = 0; i < n; i++){
			t[i] = a[i];
		}
		a = t;
	}

	public void push(Item item){
		// Insert item in stack
		if (n == a.length)
			resize(2*a.length); // If stack is full

		a[n++] = item;
	}

	public Item pop(){
		// Pop item in the stack
		Item item = a[n-1];
		a[n-1] = null; // avoid loitering
		// Loitering é a reutilização de memoria pelo Java
		n -= 1;

		// Keeping at least 1/4 of the stack occupied
		if (n > 0 && n == a.length/2)
			resize(a.length/2);

		return item;
	}

	public Iterator<Item> iterator(){
		return new RerverseArrayIterator();
	}

	private class RerverseArrayIterator implements Iterator<Item>{
		private int t = n;

		public boolean hasNext(){
			return t > 0;
		}

		public Item next(){
			return a[t--];
		}

		public void remove(){
			throw new UnsupportedOperationException();
		}
	}

	public static void main(String[] args){
		// Using stuff

		Stack<String> s = new Stack<String>();

		while (!StdIn.isEmpty()){
			String item = StdIn.readString();
			if (!item.equals("-")) s.push(item);
			else if (!s.isEmpty()) StdOut.println(s.pop() + " ");
		}

		StdOut.println("\nConteúdo usando while (...): ");
		StdOut.println("----");
		Iterator<String> it = s.iterator();
		
		while (it.hasNext()) 
			StdOut.println(it.next());
		
		StdOut.println("----\n");
		StdOut.println("\nConteúdo usando foreach statement: ");
		StdOut.println("----");

		for (String bla: s) 
			StdOut.println(bla);
		
		StdOut.println("----\n");
		StdOut.println("(" + s.size() + " left on stack)");
	}
}