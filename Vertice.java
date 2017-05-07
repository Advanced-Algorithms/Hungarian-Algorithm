import java.util.HashMap;


public class Vertice
{
	private String name;
	private int label;
	public HashMap<Vertice, Integer> neighbors;
	private int maxEdgeWeight;
	private Vertice maxEdgeNeighbor;
	public String color = "white" ;//for dfs
	public String prev = "null";
	
	public Vertice(String name, int label){
		this.name = name;
		this.label = label;
		neighbors = new HashMap<Vertice, Integer>();
	}

	public String getName(){
		return name;
	}

	public int getLabel(){
		return label;
	}

	public void setLabel(int label)
	{
		this.label = label;
	}
	
	public int getMaxEdgeWeight(){
		return maxEdgeWeight;
	}
	public Vertice getMaxEdgeNeighbor(){
		return maxEdgeNeighbor;
	}

	public void addNeighbor(Vertice v, int edgeweight){
		neighbors.put(v, edgeweight);
		//keep track of maximim weight for labeling step (feasible labeling)
		if(edgeweight>this.maxEdgeWeight){
			this.maxEdgeWeight = edgeweight;
			this.maxEdgeNeighbor = v;
		}
	}

	public HashMap<Vertice, Integer> getNeighbors()
	{
		return this.neighbors;
	}




}