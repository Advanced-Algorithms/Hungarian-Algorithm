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
	public static BipartiteGraph e_l;
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
		BipartiteGraph g = new BipartiteGraph(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
		// graph = g;
		 //g.populateEdges();
		// g.printGraph();
		BipartiteGraph mygraph = new BipartiteGraph(5, 15); //debugging purposes
		mygraph.addEdge("x1", "y1", 1);
		mygraph.addEdge("x1", "y2", 6);
		mygraph.addEdge("x2", "y2", 8);
		mygraph.addEdge("x2", "y3", 6);
		mygraph.addEdge("x3", "y1", 4);
		mygraph.addEdge("x3", "y3", 1);

		 mygraph.addEdge("x4", "y4", 15);
		 mygraph.addEdge("x5", "y2", 9);
		 mygraph.addEdge("x2", "y5", 6);
		//mygraph.addEdge("x5", "y4", 14);
		graph=mygraph;
		// graph = g;
		graph.printGraph();

		HungarianAlgorithm();
	}

	//public static void HungarianAlgorithm(BipartiteGraph graph)
	public static void HungarianAlgorithm()
	{
		System.out.println("In HungarianAlgorithm");
		feasible_labelled = feasibleLabel() ;//graph);
		System.out.println("=================Printing Feasibly labelled graph======================");
		feasible_labelled.printGraph();
		initial_match = initial_match(feasible_labelled);
		System.out.println("=================Printing initial match graph======================");
		initial_match.printGraph();
		//LabelOriginalGraph
		for (Vertice v: graph.xverts)
		{
			v.setLabel(v.getMaxEdgeWeight());
			//System.out.println(v.getLabel());

		}
		for (Vertice v: graph.yverts)
		{
			v.setLabel (0);
		}
		//graph.printLabel();
		doStep2(graph.size);
	}

	

	public static void doStep2(int graphsize)
	{
		System.out.println("STEP 2========================");
		s = new HashSet<Vertice>();
		t = new HashSet<Vertice>();
		if(initial_match.numberOfEdges()==graphsize)
		{
			initial_match.printGraph();
			System.out.println("WE ARE DONE!!!!!!!!!!!!!!!!!!!!!!");			
		}
		else
		{
			boolean found = false;
			for (Vertice x : initial_match.xverts)
			{
				if(x.neighbors.size()==0) //use initial match to find free vertice
				{
					for (Vertice x1: feasible_labelled.xverts)
					{
						if(x.getName().equals(x1.getName())){
							s.add(x1); //add the vertice from the feasible labelled not the initial match
							//order.add(x1.getName());
							//order_x.add(x1.getName());
							picked_u = x;
							//System.out.println("FREE VERTICE: "+ picked_u.getName());
							break;
						}
						
					}
					found = true;
				}
				if(found){break;};
			}

			figureOutNextStep();
		}
	}

	public static void figureOutNextStep()
	{
		neighbors_of_s = new HashSet<Vertice>();
		for(Vertice vert: s)
		{
			//System.out.println("Printing neighbors of S");
			for(Vertice neighbor : vert.neighbors.keySet())
			{
				neighbors_of_s.add(neighbor);
				//System.out.println(neighbor.getName());
			}
		}
		boolean neighbors_of_s_equals_t = true;
		nos_minus_t = new HashSet<Vertice>(); //Neighbors of S minus T
		if(neighbors_of_s.size()==t.size())
		{
			//System.out.println(" NoS & T SizeEqual, Printing neighbors of S minus t");

			for(Vertice y: neighbors_of_s)
			{
				if(t.contains(y))
				{
					//System.out.println("Set T contains: "+ y.getName());
					continue;
				}
				else
				{
					neighbors_of_s_equals_t = false;
					nos_minus_t.add(y);
					//System.out.println(y.getName());
					//System.out.println(nos_minus_t.size());;
				}
			}
		}
		else
		{
			//System.out.println(" NoS & T Size NOT Equal, Printing neighbors of S minus t");

			neighbors_of_s_equals_t=false;
			for(Vertice y: neighbors_of_s)
			{
				if(t.contains(y))
				{
					//System.out.println("Set T contains: "+ y.getName());
					continue;
				}
				else
				{
					nos_minus_t.add(y);
					//System.out.println(nos_minus_t.size());;
				}
			}
			//System.out.println(nos_minus_t.size());;
		}

		if(neighbors_of_s_equals_t)
		{
			

			doStep3(); //feasible_labelled,graph,s,t);
		}
		else{
			

			doStep4();
		}


	}

	//boolean used to switch b/w s atr t

	public static boolean checkKey(String a, boolean s_or_t) 
	{
		if(s_or_t)
		{
			for(Vertice v: s)
			{
				if(v.getName().equals(a)){
					return true;
				}
			}
			return false;
		}
		else
		{
			for(Vertice v: t)
			{
				if(v.getName().equals(a)){
					return true;
				}
			}
			return false;
		}
	}


	//public static void doStep3(BipartiteGraph feasible_labelled, BipartiteGraph graph, Set<Vertice> s, Set<Vertice> t)
	public static void doStep3()
	{
		System.out.println("Doing step 3=======================");
		int min = Integer.MAX_VALUE;
		for (Vertice v : s)
		{
			String name = v.getName();
			//System.out.println(name + "From set S");
			for (Vertice y: graph.yverts)
			{
				if(!checkKey(y.getName(),false)) 
				{
					//System.out.println(y.getName() +" Not in Set T");
					int weight=graph.edgeWeight(v.getName(), y.getName());
					if(weight!=0)
					{
						//System.out.println("weight of edge is "+ weight);
						int newValue = v.getLabel() + y.getLabel() - weight;
						if(newValue<min)
						{
							min = newValue;
						}
					}
					
				}
			}
		}
		System.out.println("Min Calculated is " + min);

		//label update
		for(Vertice x : graph.xverts)
		{
			if(checkKey(x.getName(),true))
			{
				x.setLabel(x.getLabel()-min);
			}
			if(checkKey(x.getName(),false))
			{
				x.setLabel(x.getLabel()+min);
			}
		}
		for(Vertice y : graph.yverts)
		{
			if(checkKey(y.getName(),true))
			{
				y.setLabel(y.getLabel()-min);
			}
			if(checkKey(y.getName(), false))
			{
				y.setLabel(y.getLabel()+min);
			}
		}
		//graph.printLabel();
		UpdatefeasibleLabel();
		//feasible_labelled.printGraph();
		recomputeNoS();
		doStep4();

	}

	// public static void UpdateGraphLabel(int value)
	// {
	// 	for (Vertice x: graph.xverts)
	// 	{
			
	// 		if(checkKey(x.getName(),true))
	// 		{
	// 			x.setLabel(x.getLabel() - value);
	// 		}
	// 		else
	// 		{
	// 			if(checkKey(x.getName(), false))
	// 			{
	// 				x.setLabel(x.getLabel() + value);
	// 			}
	// 		}
	// 	}

	// 	for (Vertice y: graph.yverts)
	// 	{
			
	// 		if(checkKey(y.getName(),true))
	// 		{
	// 			y.setLabel(y.getLabel() - value);
	// 		}
	// 		else
	// 		{
	// 			if(checkKey(y.getName(), false))
	// 			{
	// 				y.setLabel(y.getLabel() + value);
	// 			}
	// 		}
	// 	}
	// }

	//public static void UpdatefeasibleLabel(BipartiteGraph graph)
	public static void UpdatefeasibleLabel()
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

	public static boolean recomputeNoS()
	{
		neighbors_of_s = new HashSet<Vertice>();
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
		s=new_s; //Do this because s was pointing to vertices in previous e_l before;
		
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
		t=new_t;

		for(Vertice vert: s)
		{
			for(Vertice neighbor : vert.neighbors.keySet())
			{
				neighbors_of_s.add(neighbor);
			}
		}
		boolean neighbors_of_s_equals_t = true;
		nos_minus_t = new HashSet<Vertice>(); //Neighbors of S minus T
		if(neighbors_of_s.size()==t.size())
		{
			//System.out.println("size Neighbors of S and  set T  equal");
			for(Vertice y: neighbors_of_s)
			{
				//if(t.contains(y))
				if(checkKey(y.getName(),false))
				{
					continue;
				}
				else
				{
					neighbors_of_s_equals_t = false;
					nos_minus_t.add(y);
				}
			}
		}
		else
		{
			//System.out.println(" NoS & T Size NOT Equal, Printing neighbors of S minus t");

			neighbors_of_s_equals_t=false;
			for(Vertice y: neighbors_of_s)
			{
				if(t.contains(y))
				{
					//System.out.println("Set T contains: "+ y.getName());
					continue;
				}
				else
				{
					nos_minus_t.add(y);
					//System.out.println(nos_minus_t.size());;
				}
			}
			//System.out.println(nos_minus_t.size());;
		}

		return neighbors_of_s_equals_t;

	}
	public static void doStep4()
	{
		System.out.println("Doing step 4====================");
		Vertice y=null;
		System.out.println(picked_u.getName() +" is picked_u");

		//System.out.println(nos_minus_t.size());
		//System.out.println("First examine the matchgraph");
		//initial_match.printGraph();
		for(Vertice picked: nos_minus_t)
		{
			y=picked;
			//System.out.println(y.getName());

			break;
		}

		for(Vertice picked: initial_match.yverts)
		{
			
			if(picked.getName().equals(y.getName()))
			{
				y=picked;
			}
			//System.out.println(picked.getName() + "Neighbors Size " + picked.neighbors.size());

			//break;
		}
		//System.out.println("picked y from Neighbors of S minus t = " + y.getName());
		if(initial_match.matched(y.getName()))
		{
			System.out.println("Y matched");
			//System.out.println(y.neighbors.size());
			String toAdd1 = y.getName();
			 String toAdd2 = "dummy" ;
			// //order.add(y.getName());
			
			for(Vertice k: y.neighbors.keySet())
			{
				s.add(k);
				toAdd2 = k.getName();
				//order_x.add(k.getName());
				//System.out.println("Y is matched to "+ k.getName());
			}
			t.add(y);
			//order_y.add(y.getName());
			
			//s.add()
			recomputeNoS();
			doStep3();

		}
		else
		{
			
			int weight=0;
			System.out.println(y.getName()+ " is free");
			//order.add(y.getName());
			//order_y.add(y.getName());
			//System.out.println("*******************************");
			//feasible_labelled.printGraph();
			//System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&");
			//initial_match.printGraph();
			//initial_match.printLabel();
			ArrayList<String> path  = dfs2(picked_u.getName(), y.getName());
			System.out.println (path);
			for(int i = 0;i<path.size()-1; i++)
			{
				//System.out.println(initial_match.edgeWeight(path.get(i), path.get(i+1)));
				if(initial_match.edgeWeight(path.get(i), path.get(i+1))!=0)
				{
					//System.out.println("WE are here!");
					//System.out.println(initial_match.edgeWeight(path.get(i), path.get(i+1)));
					initial_match.removeEdge(path.get(i),path.get(i+1));
				}
				else
				{
					//System.out.println("got to else");
					int edge = feasible_labelled.edgeWeight(path.get(i), path.get(i+1));
					initial_match.addEdge(path.get(i), path.get(i+1), edge);
				}
			}
			initial_match.printGraph();
			// for(Vertice x: feasible_labelled.xverts)
			// {
			// 	if(x.getName().equals(picked_u.getName()))
			// 	{
			// 		for(Vertice y1: x.neighbors.keySet())
			// 		{
			// 			if( y1.getName().equals(y.getName()))
			// 			{
			// 				weight = x.neighbors.get(y1);
			// 			}
			// 		}
			// 	}
			// }
			//initial_match.addEdge(picked_u.getName(), y.getName(), weight);
			//System.out.println(order_x.toString());
			//System.out.println(order_y.toString());
			doStep2(graph.size);
			//find the augmenting path
			
		}
	}

	
	//Feasibly Label the graph
	//public static BipartiteGraph feasibleLabel(BipartiteGraph graph)
	public static BipartiteGraph feasibleLabel()

	{
		//System.out.println("In feasibleLabel");
		BipartiteGraph new_graph = new BipartiteGraph(graph.size,graph.maxWeight);
		for (Vertice v: graph.xverts)
		{
			//System.out.println(v.getName()+" "+ v.getMaxEdgeWeight() + " "+ v.getMaxEdgeNeighbor());
			new_graph.setLabel(v.getName(), v.getMaxEdgeWeight());
			new_graph.addEdge(v.getName(),v.getMaxEdgeNeighbor().getName(),v.getMaxEdgeWeight());
		}
		for (Vertice v: graph.yverts)
		{
			new_graph.setLabel (v.getName(),0);
		}
		return new_graph;
	}

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

		for(Vertice v: new_graph.yverts)
		{
			
			if(v.neighbors.size()>1)
			{
				//toRemove.add(v.getName());
				Set<Vertice> keyset=v.neighbors.keySet();
				int count=1;
				HashMap<Vertice,Integer> new_1 = new HashMap<Vertice,Integer>();
				for(Vertice k: keyset )
				{
					if(count<2){
						count++;
						new_1.put(k, v.neighbors.get(k));
					}
					else
					{
						String key_name = k.getName();
						for (Vertice w: new_graph.xverts)
						{
							if(w.getName().equals(key_name))
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
	public static ArrayList<String> dfs2(String source, String dest)
	{
		ArrayList<String> path = new ArrayList<String>();
		if(dfs22(path,source,dest))
		{
			return path;
		}
		else{
			return new ArrayList<String>();
		}
	}

	public static boolean dfs22(ArrayList<String> path, String curr, String dest)
	{
		//System.out.println(curr);
		Vertice cur1 = null;
		ArrayList<Vertice> toExplore;
		if(curr.startsWith("x"))
		{
			toExplore = feasible_labelled.xverts;
		}
		else{
			toExplore = feasible_labelled.yverts;
		}

		for (Vertice v: toExplore)
		{
			//System.out.println(v.getName() + " vs " + curr);
			if(v.getName().equals(curr))
			{
				cur1 = v;
				break;
			}
			
		}
		//System.out.println(cur1.getName() + " with color " + cur1.color);
		if(cur1.color.equals("white"))
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
			if(!dfs22(path,next.getName(),dest))
			{
				check = false;
			}
			else{
				check=true;
				break;
			}
		}
		if(!check)
			{
				path.remove(path.size()-1);
				return false;
			}
			else{
				return true;
			}
	}
}
