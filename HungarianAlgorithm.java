import java.util.Set;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;



public class HungarianAlgorithm
{
	public static BipartiteGraph graph;
	public static BipartiteGraph feasible_labelled;
	public static BipartiteGraph initial_match;
	public static Set<Vertice> s;
	public static Set<Vertice> t;
	public static Set<Vertice> neighbors_of_s;
	public static Set<Vertice> nos_minus_t;
	public static Vertice picked_u;
	public static Vertice start;
	public static ArrayList<String> order = new ArrayList<String>(); 
	public static ArrayList<String> order_x = new ArrayList<String>(); 
	public static ArrayList<String> order_y = new ArrayList<String>(); 

	public static void main(String[] args)
	{
		// to keep it random, do this: 
		// recommended max size is 5 vertices to avoid possible stack overflow 
		// BipartiteGraph g = new BipartiteGraph(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
		// g.populateEdges(); 

		// example1 for testing purposes 
		BipartiteGraph g = new BipartiteGraph(5, 15); //debugging purposes
		g.addEdge("x1", "y1", 1);
		g.addEdge("x1", "y2", 6);
		g.addEdge("x2", "y2", 8);
		g.addEdge("x2", "y3", 6);
		g.addEdge("x3", "y1", 4);
		g.addEdge("x3", "y3", 1);
		g.addEdge("x4", "y4", 15);
		g.addEdge("x5", "y2", 9);
		g.addEdge("x2", "y5", 6);
		g.addEdge("x5", "y4", 14);

		// another example for test
		// BipartiteGraph g = new BipartiteGraph(3, 8);
		// g.addEdge("x1", "y1", 1);
		// g.addEdge("x1", "y3", 4);
		// g.addEdge("x2", "y1", 6);
		// g.addEdge("x2", "y2", 8);
		// g.addEdge("x3", "y2", 6);
		// g.addEdge("x3", "y3", 1);

		graph=g;
		graph.printGraph();

		HungarianAlgorithm();
	}

	public static void HungarianAlgorithm()
	{
		System.out.println("In HungarianAlgorithm");

		feasible_labelled = feasibleLabel() ;
		System.out.println("=================Printing Feasibly labelled graph======================");
		feasible_labelled.printGraph();

		initial_match = initial_match(feasible_labelled);
		System.out.println("=================Printing initial match graph======================");
		initial_match.printGraph();

		//LabelOriginalGraph
		for (Vertice v: graph.xverts)
		{
			v.setLabel(v.getMaxEdgeWeight());
		}
		for (Vertice v: graph.yverts)
		{
			v.setLabel (0);
		}

		// now we have our initial feasible label and initial matching
		// move onto step 2
		doStep2(graph.size);
	}

	// purpose of this method:
	// to check to see if max size perfect matching has been achieved
	// if not, find a vertex not in the matching to find it a matching 
	public static void doStep2(int graphsize)
	{
		System.out.println("STEP 2========================");

		s = new HashSet<Vertice>();
		t = new HashSet<Vertice>();

		if (initial_match.numberOfEdges()==graphsize)
		{
			initial_match.printGraph();
			System.out.println("WE ARE DONE!!!!!!!!!!!!!!!!!!!!!! Algorithm complete, found max size perfect matching ");	
		}
		else
		{
			boolean found = false;
			for (Vertice x : initial_match.xverts)
			{
				if (x.neighbors.size()==0) //use initial match to find free vertice
				{
					for (Vertice x1: feasible_labelled.xverts)
					{
						if (x.getName().equals(x1.getName()))
						{
							// add free vertex x1 from feasible_labelled graph to s 
							s.add(x1); 
							// u is the same free vertex x from inital_match graph 
							picked_u = x;

							break;
						}
						
					}
					found = true;
				}
				if (found)
				{ 
					break;
				}
			}
			if (recomputeNoS()) {
				doStep3();
			}
			else {
				doStep4(); 
			}
		}
	}

	// purpose of this method:
	// to check whether vert is in s or t 
	public static boolean checkKey(String a, boolean s_or_t) 
	{
		// if s_or_t is true: 
		// return true if vert a is in s
		// return false if vert a is not in s
		// if s_or_t is false:
		// return true if vert a is in t 
		// return false if vert a is not in t  
		if (s_or_t)
		{
			for (Vertice v: s)
			{
				if (v.getName().equals(a)){
					return true;
				}
			}
			return false;
		}
		else
		{
			for (Vertice v: t)
			{
				if (v.getName().equals(a))
				{
					return true;
				}
			}
			return false;
		}
	}


	// purpose of this method:
	// to update the labels, forcing neighbors of s in feasible_labelling graph to not equal t 
	public static void doStep3()
	{

		System.out.println("Doing step 3=======================");

		int min = Integer.MAX_VALUE;

		for (Vertice v : s)
		{
			String name = v.getName();

			for (Vertice y: graph.yverts)
			{
				if (!checkKey(y.getName(),false)) 
				{
					// this is the case we want -- x in s and y not in t 
					int weight = graph.edgeWeight(v.getName(), y.getName());
					//System.out.println("weight is: " + weight);
					if (weight > 0)
					{
						int newValue = v.getLabel() + y.getLabel() - weight;
						if (newValue<min)
						{
							min = newValue;
						}
					}
					
				}
			}
		}
		System.out.println("Min Calculated is " + min);

		// update labels 
		for (Vertice x : graph.xverts)
		{
			if (checkKey(x.getName(),true))
			{
				x.setLabel(x.getLabel()-min);
			}
		}
		for (Vertice y : graph.yverts)
		{
			if (checkKey(y.getName(), false))
			{
				y.setLabel(y.getLabel()+min);
			}
		}
		updateFeasibleLabel();
		recomputeNoS();
		doStep4();
	}

	// purpose of this method:
	// update the feasible labelling on feasible_labelled graph 
	public static void updateFeasibleLabel()
	{
		System.out.println("UPDATING FEASIBLE LABEL");

		BipartiteGraph new_el = new BipartiteGraph(graph.size, graph.maxWeight);
		for (Vertice x: graph.xverts)
		{
			new_el.setLabel(x.getName(), x.getLabel());
		}

		for (Vertice y: graph.yverts)
		{
			new_el.setLabel(y.getName(), y.getLabel());
		}

		for (Vertice x: graph.xverts)
		{
			for(Vertice y: x.neighbors.keySet())
			{
				for(Vertice yvert : graph.yverts)
				{
					if(yvert.getName().equals(y.getName()))
					{
						if(x.getLabel()+ yvert.getLabel()==x.neighbors.get(y))
						{
							new_el.addEdge(x.getName(), y.getName(), x.neighbors.get(y));
						}
					}
				}
				
			}
		}
		feasible_labelled = new_el;
		feasible_labelled.printGraph();
		recomputeNoS();
	}

	// purpose of this method:
	// to update nos_minus_t 
	public static boolean recomputeNoS()
	{		
		Set<Vertice> new_s = new HashSet<Vertice>();
		for(Vertice v : s)
		{
			for (Vertice new_v : feasible_labelled.xverts)
			{
				if(new_v.getName().equals(v.getName()))
				{
					new_s.add(new_v);
				}
			}
		}
		s = new_s; 
		Set<Vertice> new_t = new HashSet<Vertice>();
		for(Vertice v : t)
		{
			for (Vertice new_v : feasible_labelled.yverts)
			{
				if(new_v.getName().equals(v.getName()))
				{
					new_t.add(new_v);
				}
			}
		}
		t = new_t;

		neighbors_of_s = new HashSet<Vertice>();

		for(Vertice vert: s)
		{
			for(Vertice neighbor : vert.neighbors.keySet())
			{
				neighbors_of_s.add(neighbor);
			}
		}

		boolean neighbors_of_s_equals_t = true;

		nos_minus_t = new HashSet<Vertice>(); //Neighbors of S minus T

		for (Vertice y : neighbors_of_s) {
			if (!checkKey(y.getName(),false)) {
				neighbors_of_s_equals_t = false; 
				nos_minus_t.add(y); 
			}
		}

		return neighbors_of_s_equals_t;
	}
	
	// purpose of this method:
	// to see whether the new y is matched or unmatched
	// if unmatched, then we find the augmenting path
	// if matched, we have to go back to step 3 and update labels 
	public static void doStep4()
	{		
		recomputeNoS();
		
		System.out.println("Doing step 4====================");
		Vertice y=null;
		System.out.println(picked_u.getName() +" is picked_u");
		boolean yIsMatched = false; 

		// pick a y 
		for (Vertice picked: nos_minus_t)
		{
			y=picked;
			System.out.println("Picked y is : " + y.getName()); 
			break;
		}

		// make y point to vert from initial_match 
		for (Vertice picked: initial_match.yverts)
		{		
			if (picked.getName().equals(y.getName()))
			{
				y=picked;
			}
		}

		if (initial_match.matched(y.getName()))
		{
			// if in this if statement we know y is matched in initial_matching graph 
			for (Vertice k: y.neighbors.keySet())
			{
				s.add(k);
			}
			t.add(y); 
			recomputeNoS();
			doStep3();
		}
		else
		{
			int weight=0;
			System.out.println(y.getName() + " is free");

			ArrayList<String> path  = dfs(picked_u.getName(), y.getName());
			System.out.println(path);
			for (int i = 0;i<path.size()-1; i++)
			{ 
				if (initial_match.edgeWeight(path.get(i), path.get(i+1))!=0)
				{
					// remove all the matches from initial_match graph 
					initial_match.removeEdge(path.get(i),path.get(i+1));
				}
				else
				{
					// add all the new matches to initial_match graph 
					int edge = feasible_labelled.edgeWeight(path.get(i), path.get(i+1));
					initial_match.addEdge(path.get(i), path.get(i+1), edge);
				}
			}
			initial_match.printGraph();
			doStep2(graph.size);			
		}
	}
	
	// purpose of this method:
	// generate new feasible label graph 
	public static BipartiteGraph feasibleLabel()
	{
		BipartiteGraph new_graph = new BipartiteGraph(graph.size,graph.maxWeight);
		for (Vertice v: graph.xverts)
		{
			new_graph.setLabel(v.getName(), v.getMaxEdgeWeight());
			new_graph.addEdge(v.getName(), v.getMaxEdgeNeighbor().getName(), v.getMaxEdgeWeight());
		}
		for (Vertice v: graph.yverts)
		{
			new_graph.setLabel(v.getName(),0);
		}
		return new_graph;
	}

	// purpose of this method:
	// generate new initial match 
	public static BipartiteGraph initial_match(BipartiteGraph graph)
	{
		BipartiteGraph new_graph = new BipartiteGraph(graph.size,graph.maxWeight);
		for(Vertice v: graph.xverts)
		{
			for(Vertice key: v.neighbors.keySet())
			{

				new_graph.addEdge( v.getName(), key.getName(), v.neighbors.get(key));
			}
			new_graph.setLabel(v.getName(), v.getMaxEdgeWeight());
		}
		ArrayList<String> toRemove = new ArrayList<String>();

		for (Vertice v: new_graph.yverts)
		{
			
			if (v.neighbors.size()>1)
			{
				Set<Vertice> keyset=v.neighbors.keySet();
				int count=1;
				HashMap<Vertice,Integer> new_1 = new HashMap<Vertice,Integer>();
				for (Vertice k: keyset )
				{
					if (count<2){
						count++;
						new_1.put(k, v.neighbors.get(k));
					}
					else
					{
						String key_name = k.getName();
						for (Vertice w: new_graph.xverts)
						{
							if (w.getName().equals(key_name))
							{
								w.neighbors.remove(v);
							}
						}
					}
					
				}
				v.neighbors = new_1;
			}
		}
		return new_graph;
	}

	// purpose of this method: 
	// to find the augmenting path
	public static ArrayList<String> dfs(String source, String dest)
	{
		ArrayList<String> path = new ArrayList<String>();
		if (dfsAux(path,source,dest))
		{
			return path;
		}
		else 
		{
			return new ArrayList<String>();
		}
	}

	// purpose of this method:
	// to find the augmenting path 
	public static boolean dfsAux(ArrayList<String> path, String curr, String dest)
	{
		Vertice cur1 = null;
		ArrayList<Vertice> toExplore;
		if (curr.startsWith("x"))
		{
			toExplore = feasible_labelled.xverts;
		}
		else
		{
			toExplore = feasible_labelled.yverts;
		}

		for (Vertice v: toExplore)
		{
			if (v.getName().equals(curr))
			{
				cur1 = v;
				break;
			}
			
		}
		if (cur1.color.equals("white"))
		{
			path.add(cur1.getName());
			cur1.color = "black";
		}
		else
		{
			return false;
		}
		if(cur1.getName().equals(dest))
		{
			return true;
		}
		boolean check = false;
		for(Vertice next: cur1.neighbors.keySet())
		{
			if(!dfsAux(path,next.getName(),dest))
			{
				check = false;
			}
			else
			{
				check=true;
				break;
			}
		}
		if (!check)
		{
			path.remove(path.size()-1);
			return false;
		}
		else 
		{
			return true;
		}
	}
}
