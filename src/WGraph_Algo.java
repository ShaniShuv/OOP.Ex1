package ex1.src;

import ex1.src.node_info;
import ex1.src.weighted_graph;
import ex1.src.weighted_graph_algorithms;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class WGraph_Algo implements weighted_graph_algorithms {
    private weighted_graph g;
    private HashMap<Integer, node_info> shortestPathRevers;

    public WGraph_Algo(){
        this.g= new WGraph_DS();
        this.shortestPathRevers = new HashMap<Integer, node_info>();
    }
    /**
     * Init the graph on which this set of algorithms operates on.
     * @param g
     */
    public void init(weighted_graph g){
        this.g=g;
    }

    /**
     * Return the underlying graph of which this class works.
     * @return
     */
    public weighted_graph getGraph(){
        return this.g;
    }
    /**
     * Compute a deep copy of this weighted graph.
     * @return
     */
    public weighted_graph copy(){
        return new WGraph_DS(this.g);
    }
    /**
     * There is a Boolean method that checks if the g of this GraphAlgo is connected.
     * This method uses the bfs algorithm by sending a random node.
     * If the bfs return the number of nodes that there are in the src.ex1.src.weighted_graph, then the src.ex1.src.weighted_graph is connected,
     * if not that it returns false because we didn't succeed to reach all of the nodes from one random node.
     */
    public boolean isConnected(){
            if(g.getV().size()==0 ||g.getV().size()==1)return true;
            if(g.edgeSize()==0 || g.nodeSize()>g.edgeSize()+1)
                return false;
            int x = 0;
            for (node_info n1 : this.g.getV()) {
                x = this.bfs(n1);
                break;
            }
            if (x==g.nodeSize()) return true;
            return false;

    }
    /**
     * There is a method shortestPathDist that returns the smallest counter of edge's weight between two give nodes.
     * It sends one of them to the bfs algorithm, and return the tag of the other.
     * If there is no path then the default number will be -1, because the bfs didn't reach that node.
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    public double shortestPathDist(int src, int dest){
        node_info n1 = this.g.getNode(src);
        node_info n2 = this.g.getNode(dest);

        if(n1==null || n2 == null) return -1;
        int x = bfs(n1);
        return n2.getTag();
    }
    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * see: https://en.wikipedia.org/wiki/Shortest_path_problem
     * Note if no such path --> returns null;
     * @param src - start node
     * @param dest - end (target) node
     * @return
     *
     * The method shortestPath that returns a list of all the nodes between two given node's keys. If there are no nodes
     * with those keys in the src.ex1.src.weighted_graph it returns null, if there is then if there is no path it would return an
     * empty list, if they have a path if would send one to the bfs algorithm, and in a loop would start from the other
     * node  and add every node that have the next number of tag- by that we go throw the other node, then the next in
     * the src.ex1.src.weighted_graph, then the next until we reach the first node.
     */
    public List<node_info> shortestPath(int src, int dest){
        node_info n1 = this.g.getNode(src);
        node_info n2 = this.g.getNode(dest);
        if(n1== null || n2==null) return null;
        LinkedList<node_info> neibL = new LinkedList<node_info>();
        double pathDist = shortestPathDist(src, dest);
        if(pathDist == -1){
            return null;
        }
        neibL.addFirst(n2);
        if (pathDist == 0) return neibL;
        node_info nextOne = shortestPathRevers.get(n2.getKey());
        while(nextOne != n1){
            neibL.addFirst(nextOne);
            nextOne = shortestPathRevers.get(nextOne.getKey());
        }
        neibL.addFirst(n1);
        int i=0;
        return neibL;
    }

    /**
     * Saves this weighted (undirected) graph to the given
     * file name
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    public boolean save(String file){

        try {
                PrintWriter pw = new PrintWriter(new File(file));
                StringBuilder nodeS = new StringBuilder();
            for (node_info n : this.g.getV()) {
                    nodeS.append(n.getKey());
                    nodeS.append(",");
                    nodeS.append(n.getTag());
                    nodeS.append(",");
                    nodeS.append(n.getInfo());
                    nodeS.append("\n");
                    pw.write(nodeS.toString());
                    nodeS.setLength(0);

                }
                nodeS.append("endOfNodesLoading");
            nodeS.append("\n");
            pw.write(nodeS.toString());
                nodeS.append("\n");
                nodeS.setLength(0);
                for (node_info n : this.g.getV()) {
                    nodeS.append(n.getKey());
                    nodeS.append(",");
                    if(this.g.getV(n.getKey()).size()!=0) {
                        for (node_info neib : this.g.getV(n.getKey())) {
                            nodeS.append(neib.getKey());
                            nodeS.append(",");
                            nodeS.append(this.g.getEdge(n.getKey(), neib.getKey()));
                            nodeS.append(",");
                        }

                        nodeS.append("\n");
                        pw.write(nodeS.toString());
                        nodeS.setLength(0);
                    }
                }
                pw.close();


        } catch (FileNotFoundException e) {

            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     * @param file - file name
     * @return true - iff the graph was successfully loaded.
     */
    public boolean load(String file)  {
        String line= "";
        this.g = new WGraph_DS();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            while (!(line=br.readLine()).contains("endOfNodesLoading")){
                String[] nInfo =line.split(",");
                int key= Integer.parseInt(nInfo[0]);
                g.addNode(key);
                node_info currNI = g.getNode(key);
                currNI.setTag(Double.parseDouble(nInfo[1]));
                currNI.setInfo(nInfo[2]);
                int i=0;
            }
            while ((line=br.readLine())!= null){
                String[] nNeib =line.split(",");
                int key = Integer.parseInt(nNeib[0]);
                node_info n = g.getNode(key);
                for (int i = 1; i<nNeib.length-1; i+=2){
                    int neibKey=Integer.parseInt(nNeib[i]);
                    node_info neib = g.getNode(neibKey);
                    double w = Double.parseDouble(nNeib[i+1]);
                    g.connect(key, neibKey, w);
                    int i1=0;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * First of all, there is a method that didn't come from the interface, but if helps the method that the interface does contain.
     * This method named bfs, it gets a node named node from the src.ex1.src.weighted_graph, first we change all of the tags of the nodes in
     * the src.ex1.src.weighted_graph to -1, because the weight of each node is a positive value.
     * Then, by using a queue named q, we change the tag of a node to the smallest number of weughts that separate him
     * from the node we got in the method. Every time the bfs changes a node's tag is add it to the q. A node may get in
     * the q twice if we find a smaller number of edges that separate him from the original node.
     * By that we make sure that eventually every node will get the smallest number of edges in his tag.
     * We start with the original node, put it in the q and that start a loop going throw all of his neighbors and set
     * their tag to be the value of the edge's weight that separate them, adding them to the q, we keep a counter that
     * represent the number of nodes we have seen so far. At the end of the original node neighbors, we take him out of the q,
     * and start going throw the next node in the q's neighbors. We do so again and again, keep setting the tags and counting
     * the nodes we have seen until the q is empty, then we know we have reached every node possible.
     * Then the bfs algorithm ends and return the number of nodes we saw.
     * @param node
     * @return
     */
    public int bfs(node_info node){
        this.shortestPathRevers= new HashMap<Integer, node_info>();
        for (node_info n : this.g.getV()) {
            n.setTag(-1);
            this.shortestPathRevers.put(n.getKey(), null);

        }
        int counter = 0;
        node_info temp = null;
        Queue <node_info>q = new LinkedList<node_info>() ;
        q.add(node);
        node.setTag(0);
        counter ++;
        while(!q.isEmpty()){
            if(q.peek()!=null){
                temp = q.poll();
            }
            for (node_info n2 : this.g.getV(temp.getKey())) {
                double w = this.g.getEdge(temp.getKey(), n2.getKey());
                if (n2.getTag() == -1) {
                    counter++;
                    q.add(n2);
                    n2.setTag(temp.getTag() +w);
                    this.shortestPathRevers.remove(n2.getKey());
                    this.shortestPathRevers.put(n2.getKey(), temp);
                }
                else if (n2.getTag() > temp.getTag() + w){
                    n2.setTag(temp.getTag() + w);
                    q.add(n2);
                    this.shortestPathRevers.remove(n2.getKey());
                    this.shortestPathRevers.put(n2.getKey(), temp);
                }
            }
        }
        return counter;
    }

    public boolean equals(weighted_graph_algorithms p){
        WGraph_DS tempG = (WGraph_DS)this.g;
        if(tempG.equals(p.getGraph()))
            return true;
        return false;
    }

    /**
     * this method returns a String that contains all of the information on this src.ex1.src.WGraph_Algo's graph data.
     * @return
     */
    @Override
    public String toString(){
        return this.g.toString();
    }

}