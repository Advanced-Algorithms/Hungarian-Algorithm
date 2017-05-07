import java.util.*; 

public class testGraph
{
	public static BipartiteGraph mygraph;
	public static void main(String[] args)
	{
		BipartiteGraph g = new BipartiteGraph(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
		//g.populateEdges();
		//g.printGraph();


		 mygraph = new BipartiteGraph(5, 15);
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
		mygraph.printGraph();
		// BipartiteGraph g2 = g.clone();
		// System.out.println(g==g2);
		ArrayList<String> path =  dfs2("y2","y4");
		System.out.println(path.toString());
	}
	public static ArrayList dfs(String start, String vert_name)
	{
		for(Vertice x : mygraph.xverts)
		{
			x.color = "white";

		}
		for(Vertice y : mygraph.yverts)
		{
			y.color = "white";
			
		}
		for( Vertice x: mygraph.xverts)
		{
			if(x.getName().equals(start))
			{
				 ArrayList<String> path_so_far = new ArrayList<String>();
				 path_so_far.add(x.getName()); 
				 System.out.println(x.color);
				 return dfsVisit(x,vert_name,path_so_far);
			}
		}
		return new ArrayList<String>();
	}
	public static ArrayList dfsVisit(Vertice x,String vert_name,ArrayList path_so_far)
	{
		
		for(Vertice v: x.neighbors.keySet())
		{
			
			if(v.color.equals("white"))
			{
				System.out.println("examining vertice : "+ v.getName() + "with color : "+ v.color);
				v.color = "black";
				path_so_far.add(v.getName());
				System.out.println(path_so_far.toString());
				//v.prev = x.getName();
				if(v.getName().equals(vert_name))
				{
					System.out.println("Did we ever get here?");
					return path_so_far;
				}
				else
				{
					return dfsVisit(v,vert_name,path_so_far);
				}
			}
		}
		return new ArrayList<String>();
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
			toExplore = mygraph.xverts;
		}
		else{
			toExplore = mygraph.yverts;
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
				//System.out.pr
				//path.remove(path.size()-1);
			}
			else{
				check=true;
				break;
			}
		}
		if(!check)
			{
				//System.out.println(path);
				path.remove(path.size()-1);
				return false;
			}
			else{
				//System.out.println("gpt here");
				//System.out.println(path);
				return true;
			}
		//return false;
	}
}