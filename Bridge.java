public class Bridge {
	// will keep track of the number of bridges
	private int bridges;
	private int count;
	// dfs will examine v in preorder order
	private int[] preOrder;
	// this is the lowest preorder of any vertex connected to v
	private int[] lowOrder;

	public Bridge(Graph G) {
		// initialize the length of the low order and the pre order array to
		// number of vertices in the graph
		lowOrder = new int[G.V()];
		preOrder = new int[G.V()];
		// mark every single entry as unvisited
		for (int v = 0; v < G.V(); v++) {
			lowOrder[v] = -1;
		}
		for (int v = 0; v < G.V(); v++) {
			preOrder[v] = -1;
		}
		// loop through the vertices and call the recursive dfs when a vertex is
		// marked as unvisited
		for (int v = 0; v < G.V(); v++) {

			if (preOrder[v] == -1) {
				dfs(G, v, v);
			}
		}
	}

	// dfs implementation
	// dfs in pre order which means that the furthtest bridge will be displayed
	// first
	private void dfs(Graph G, int u, int v) {
		// the count will be used to identify the vertexes
		preOrder[v] = count++;
		lowOrder[v] = preOrder[v];
		// iterate through every single adjacent vertex to v
		for (int w : G.adj(v)) {
			// if it is unvisited run dfs recursively
			// this will run until we have both low order and post order
			// filled with vertices and we have ran through all vertices
			if (preOrder[w] == -1) {
				dfs(G, v, w);
				// will take the lowest value of the low orders
				lowOrder[v] = Math.min(lowOrder[v], lowOrder[w]);

				if (lowOrder[w] == preOrder[w]) {
					// we have found a bridge
					StdOut.println(v + "-" + w + " is a bridge");
					// increment the bridge counter
					bridges++;
				}
				// update low number - ignore reverse of edge leading to v
			} else if (w != u) {
				lowOrder[v] = Math.min(lowOrder[v], preOrder[w]);
			}
		}
	}

	public int components() {
		return bridges;
	}
	
	public static void main(String[] args) {
		In in = new In(args[0]);
		Graph G = new Graph(in);
		StdOut.println(G);
		Bridge bridge = new Bridge(G);
		StdOut.println("Number of Bridges = " + bridge.components());
	}

}