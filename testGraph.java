public class testGraph
{
	public static void main(String[] args)
	{
		BipartiteGraph g = new BipartiteGraph(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
		g.populateEdges();
		g.printGraph();

		BipartiteGraph mygraph = new BipartiteGraph(5, 15);
		mygraph.addEdge("x1", "y1", 12);
		mygraph.addEdge("x1", "y3", 10);
		mygraph.addEdge("x2", "y4", 2);
		mygraph.addEdge("x2", "y5", 12);
		mygraph.addEdge("x3", "y2", 8);
		mygraph.addEdge("x3", "y3", 7);
		mygraph.addEdge("x4", "y1", 5);
		mygraph.addEdge("x5", "y2", 9);
		mygraph.addEdge("x4", "y4", 6);
		mygraph.addEdge("x5", "y4", 14);
		// BipartiteGraph g2 = g.clone();
		// System.out.println(g==g2);
	}
}