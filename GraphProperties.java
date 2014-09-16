import java.util.HashMap;

public class GraphProperties {
	private Graph g;
	// will hold the diameter value
	int diameter = Integer.MIN_VALUE;
	// will hold the radius value
	int radius = Integer.MAX_VALUE;
	// will hold the center value (note it will return a single center)
	// in some cases where can be multiple centers in which case if we want to
	// get all of them this value must be an array
	int center = Integer.MAX_VALUE;
	// will hold the eccentricity values of the vertices
	HashMap<Integer, Integer> eccentricity = new HashMap<>();

	public GraphProperties(Graph g) {
		CC cc = new CC(g);
		// throw an exception if graph is not connected
		// if the connected components are more than one
		if (cc.count() != 1)
			throw new IllegalArgumentException();
		this.g = g;

		// go through the vertices, calculating their eccentricity and figuring
		// out
		// the diameter, center and radius
		// O(V(V + E)) complexity because we need to execute BFS for each of the
		// vertices
		for (int i = 0; i < g.V(); i++) {
			System.out.println("eccentricity of vertex" + i + ":"
					+ eccentricity(i));
			// add the eccenticity of the vertex to the array of eccentricities
			eccentricity.put(i, eccentricity(i));
			// if it is bigger then the current max then we override
			// basically we find the biggest eccentricity of all vertices which
			// will be the diameter
			if (eccentricity(i) > diameter) {

				diameter = eccentricity(i);
			}
			// if it is lower then the current min then we override
			// basically we find the smallest eccentricity of all vertices which
			// will be the radius
			if (eccentricity(i) < radius) {

				radius = eccentricity(i);
			}
			// if the eccentricity of a vertex equals the radius then we return
			// the vertex
			if (eccentricity(i) == radius) {

				center = i;
			}
		}
//		System.out.println("Diameter:" + diameter + ", Radius:" + radius
//				+ ", Center: " + center);
	}

	// will return the diameter in constant time because its value has already
	// been calculated and we just need to return it
	public int getDiameter() {

		return diameter;
	}

	// will return the radius in constant time
	public int getRadius() {

		return radius;
	}

	// will return the center in constant time
	public int getCenter() {

		return center;
	}

	// return the eccentricity of a vertex
	public int getEccentricity(int vertex) {

		return eccentricity.get(vertex);
	}

	public int eccentricity(int v) {
		// use BFS to find the shortest paths from a vertex to all other
		// vertices
		BreadthFirstPaths bfp = new BreadthFirstPaths(g, v);
		int max = -1;
		// for each of the vertices
		for (int i = 0; i < g.V(); i++) {
			// if it is the same vertex we skip the iteration
			if (i == v) {
				continue;
			}
			// Returns the number of edges in a shortest path between the source
			// vertex s which in our case is v vertex v which in our case is i
			int dist = bfp.distTo(i);
			// if it is bigger than the current max we override
			if (dist > max) {
				max = dist;
			}
		}
		return max;
	}

	public static void main(String[] args) {
		In in = new In(args[0]);
		Graph g = new Graph(in);
		GraphProperties gp = new GraphProperties(g);
		// it will return the valus in constant time because they are already
		// calculated when the gp instance was created, which will preprocess
		// the graph
		System.out.println("Eccentricity of vertex 1:" + gp.getEccentricity(5)
				+ ", Diamter:" + gp.getDiameter() + ", Radius:"
				+ gp.getRadius() + ", Center:" + gp.getCenter());

	}
}