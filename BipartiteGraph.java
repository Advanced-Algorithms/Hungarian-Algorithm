import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collection;
import java.util.Random;

/**
 * An undirected, unweighted, simple graph.  The vertex set is static; edges
 * may be added but not removed.
 *
 * @param K the type of the vertices
 * @author Mohammed Ibrahim
 * @version 0.1 2017-4-21
 */

public class BipartiteGraph 
{
    public int maxWeight;
    public int size;
    /**
     * This graph's X vertex set.
     */
    public ArrayList<Vertice> xverts;

    /**
     * This graph's Y vertex set.
     */
    public ArrayList<Vertice> yverts;
    
    /**
     * Creates a graph with two sets of vertices objects.
     *
     * @param size: size of each vertice set
     */
    public BipartiteGraph(int size,int maxWeight)
	{
	    this.maxWeight = maxWeight;
        this.size = size;
	    xverts = new ArrayList<Vertice>();

	    yverts = new ArrayList<Vertice>();
	    
	    for (int i = 1; i<=size; i++)
	    {
	    	xverts.add(new Vertice("x"+i, 0));
	    	yverts.add(new Vertice("y"+i, 0));
	    }
	}

     public void printLabel(){
        System.out.println("LABELS ARE:");
        for (Vertice x: this.xverts)
        {
            System.out.println(x.getName() + " has label " + x.getLabel());
        }
        for (Vertice y: this.yverts)
        {
            System.out.println(y.getName() + " has label " + y.getLabel());
        }
     }

    public void addEdge(String xvertName, String yvertName, int weight)
    {
		//System.out.println("in addedge");
        for (Vertice temp : this.xverts) {
        	//System.out.println(temp);
        	if(temp.getName().equals(xvertName)){
        		for (Vertice temp2 : this.yverts) {
        			if(temp2.getName().equals(yvertName))
        			{
        				temp2.addNeighbor(temp, weight);
        				temp.addNeighbor(temp2,weight);
        			}
	        	}
    	 	}
    	}
    }


    public void setLabel(String vertice, int label)
    {
        if(vertice.startsWith("x"))
        {
            //System.out.println(vertice);
            for(Vertice v : this.xverts)
            {
                //System.out.println(v);
                if(v.getName().equals(vertice))
                {
                    v.setLabel(label);
                }
            }
        }
        else
        {
            for(Vertice v : this.yverts)
            {
                if(v.getName().equals(vertice))
                {
                    v.setLabel(label);
                }
            }
        }
    }

    public void populateEdges()
    {
    	Random rand = new Random();
    	for (Vertice temp : xverts) {
    		int randomInt = rand.nextInt(yverts.size());
    		Vertice target = yverts.get(randomInt);
    		int weightToUse = rand.nextInt(maxWeight)+1;
    		temp.addNeighbor(target,weightToUse);
    		target.addNeighbor(temp,weightToUse);
            int randomInt2 = rand.nextInt(yverts.size());
            while (randomInt2==randomInt)
            {
                randomInt2 = rand.nextInt(yverts.size());
            }
            weightToUse = rand.nextInt(maxWeight)+1;
            target = yverts.get(randomInt2);
            temp.addNeighbor(target,weightToUse);
            target.addNeighbor(temp,weightToUse);
    	}
    	for (Vertice temp : yverts) {
    		if(temp.getNeighbors().size()==0){
    			int randomInt = rand.nextInt(xverts.size());
    			Vertice target = xverts.get(randomInt);
    			int weightToUse = rand.nextInt(maxWeight)+1;
    			temp.addNeighbor (target,weightToUse);
                target.addNeighbor(temp,weightToUse);
                int randomInt2 = rand.nextInt(yverts.size());
                while (randomInt2==randomInt)
                {
                    randomInt2 = rand.nextInt(yverts.size());
                }
                weightToUse = rand.nextInt(maxWeight)+1;
                target = xverts.get(randomInt2);
                temp.addNeighbor(target,weightToUse);
                target.addNeighbor(temp,weightToUse);
    		}

    	}
	}
    public int numberOfEdges()
    {
        int count = 0;
        for(Vertice x : xverts)
        {
            count+=x.neighbors.size();
        }
        return count;
    }

    public int edgeWeight(String x, String y)
    {
        if(x.startsWith("y"))
        {
            String z = y;
            y=x;
            x=z;
        }
        //System.out.println("Got to edgeWeight");
       //  System.out.println(x +"   "+ y);
        for(Vertice v: this.xverts){
            if(v.getName().equals(x))
            {
                // System.out.println("Step 1 done");
                 //System.out.println(v.neighbors.size());
                for(Vertice k: v.neighbors.keySet())
                {
                    // System.out.println(k.getName());
                    if(k.getName().equals(y))
                    {
                        //System.out.println(v.neighbors.get(k));
                        return v.neighbors.get(k);
                    }
                }
            }
        }
        return 0;
    }

    public void removeEdge(String x, String y)
    {
        if(x.startsWith("y"))
        {
            String z = y;
            y=x;
            x=z;
        }
       //System.out.println("Got to removeEdge");
       //System.out.println(x +"   "+ y);
       for(Vertice v: xverts){
            if(v.getName().equals(x))
            {
                for(Vertice k: v.neighbors.keySet())
                {
                    if(k.getName().equals(y))
                    {
                        v.neighbors.remove(k);
                        break;
                    }
                }
            }
        }
        for(Vertice v: yverts){
            if(v.getName().equals(y))
            {
                for(Vertice k: v.neighbors.keySet())
                {
                    if(k.getName().equals(x))
                    {
                        v.neighbors.remove(k);
                        break;
                    }
                }
            }
        }
    }

    //check if a particular vertice is matched
    public boolean matched(String vertice)
    {
       // this.printGraph();
        if(vertice.startsWith("x"))
        {
            for (Vertice x : this.xverts)
            {
                if(x.getName().equals(vertice))
                {
                    if(x.neighbors.size()==1)
                    {
                        return true;
                    }
                }
            }
            //return false;
        }
        else
        {
           //System.out.println(vertice);
           for (Vertice y : this.yverts)
            {
                //System.out.println("Vertice :" + y.getName() + " with neighbors size :" + y.neighbors.size());
                if(y.getName().equals(vertice))
                {
                    if(y.neighbors.size()==1)
                    {
                        return true;
                    }
                }
            } 
           // return false;
        }
        return false;
    }
    public void printGraph()
    {
		for (Vertice temp : xverts) {
			HashMap<Vertice,Integer> neighbors = temp.getNeighbors();
            //System.out.println("XVertice label is" + temp.getLabel());
			if(neighbors.keySet().size()!=0)
            {
                for(Vertice key: neighbors.keySet())
    			{

    				System.out.println( temp.getName() + "==>" + key.getName() + " :: " + neighbors.get(key));
                    //System.out.println("YVertice label is" + key.getLabel());

    			}
            }
            else{
                System.out.println( temp.getName() + "==>" + "nothing" + " :: " + "nothing");
            }
		}
        for (Vertice temp: yverts)
        {
            HashMap<Vertice,Integer> neighbors = temp.getNeighbors();
            if(neighbors.keySet().size()==0)
            {
                System.out.println( "nothing" + "==>" + temp.getName() + " :: " + "nothing");
            }
        }
    }
}