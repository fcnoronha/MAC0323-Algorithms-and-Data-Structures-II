import edu.princeton.cs.algs4.*;

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
		n -= 1;

		// Keeping at least 1/4 of the stack occupied
		if (n > 0 && n < a.length/2)
			resize(a.length/2);

		return item;
	}
}