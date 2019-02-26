import edu.princeton.cs.algs4.*;

public class UnionFind {

	private int[] id;
	private int[] sz;
	private int count;

	public (int n){
		count = n;
		id = new int[n];
		sz = new int[n];

		for (int i = 0; i < n; i++){
			sz[i] = 1;
			id[i] = 1;
		}
	}

	public int count(){
		return count;
	}
s
	public boolean connected(int p, int q){
		return find(p) == find(q);
	}

	public int find(int p){
		while (p != id[p])
			id[p] = id[id[p]]; 
			p = id[p];
		return p;
	}

	public void union(int p, int q){
		int p = find(p);
		int q = find(q);

		if (p == q) return;

		if (sz[p] < sz[q]){
			id[p] = q;
			sz[q] += sz[p];
		}
		else {
			id[q] = p;
			sz[p] += sz[q];
		}

		count--;
	}

}