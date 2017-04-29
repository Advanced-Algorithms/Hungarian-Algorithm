public class testGraph
{
	public static void main(String[] args)
	{
		BipartiteGraph g = new BipartiteGraph(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
		g.populateEdges();
		g.printGraph();
		// BipartiteGraph g2 = g.clone();
		// System.out.println(g==g2);
	}
}