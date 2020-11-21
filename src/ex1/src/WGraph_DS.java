package ex1.src;

import ex1.src.node_info;
import ex1.src.weighted_graph;

import java.util.Collection;
import java.util.HashMap;

/**
 * This class creates a type of graph that impliments the interface src.ex1.src.weighted_graph
 *This class contains a private class named NodeInfo, that impliments the interface src.src.node_info.
 *The new type contains HashMap named allNodes, of NodeInfo as value and integer as key, which holds
 * within in all the NodeInfos that that src.ex1.src.weighted_graph holds, while creating a new src.ex1.src.weighted_graph the class initialized the HashMap to be empty.
 * It also saves two integers named edgeNum – which keep track of the number of edges in the src.ex1.src.weighted_graph, each edge represents
 * a connection between two NodeInfos, the second integer named changesNum – it keeps track about the number of changes that the src.ex1.src.weighted_graph has been throw.
 * While creating a new src.ex1.src.weighted_graph both of these counters are given the value 0.
 *It alse has a HashMap named nodesNeib that contains a src.src.node_info as a key,
 * the value of this HashMap is another hashMap that contains an integer as a key-
 * it's the node's neighbor, and the value of this inside HashMap is src.src.node_info.
 * In this order it's easier to go throw one node's neighbors.
 * The third HashMap is for quick access to edges between two nodes.
 */

public class WGraph_DS implements weighted_graph {
    private HashMap<Integer, node_info> allNodes;
    public  HashMap<node_info, HashMap<Integer, node_info>> nodesNeib;
    private HashMap<Integer, HashMap<Integer, Double>> edgesWeight;

    private int changesNum = 0, edgeNum = 0;

    public WGraph_DS(){
        this.allNodes= new HashMap<Integer, node_info>();
        this.nodesNeib = new HashMap<node_info, HashMap<Integer, node_info>>();
        this.edgesWeight= new HashMap<Integer, HashMap<Integer, Double>>();
    }

    /**
     * There is a constructor to the src.ex1.src.weighted_graph, a copy constructer (which get a src.ex1.src.weighted_graph to copy from, named gCop,
     * and a src.ex1.src.weighted_graph to copy into).
     * There is a method named getNode, that while given a key value, checks first if there is a NodeInfo that
     * contains that key- if not than it returns null, and if the
     * src.ex1.src.weighted_graph contains a NodeInfo with that key, than it gets it from the HashMap allNodes and return it.     *
     * @param p
     */
    public WGraph_DS(weighted_graph p){
        this.allNodes = new HashMap<Integer, node_info>() ;
        this.nodesNeib = new HashMap<node_info, HashMap<Integer, node_info>>();
        this.edgesWeight= new HashMap<Integer, HashMap<Integer, Double>>();

        for (node_info node: p.getV()) {
            node_info temp = new NodeInfo(node);
            this.addNode(temp.getKey());
        }
        for (node_info nodeP: p.getV()) {
            node_info nodeG = this.allNodes.get(nodeP.getKey());
            for (node_info neibP : p.getV(nodeP.getKey())) {
                node_info neibG = this.allNodes.get(neibP.getKey());
                double w =p.getEdge(nodeP.getKey(), neibP.getKey());
                connect(neibG.getKey(), nodeG.getKey(),w);
            }
        }
        this.changesNum= p.getMC();
    }

    /**
     * the method getNode gets integer and if there is a node in the graph with that key, it returns it, if not then it returns null.
     * @param key - the node_id
     * @return
     */
    @Override
    public node_info getNode(int key) {
        if(!this.allNodes.containsKey(key))
                return null;
        return this.allNodes.get(key);
    }

    /**
     * the method hasEdge gets two keys returns true if there is edge between two nodes with that key in the graph
     * @param key1
     * @param key2
     * @return
     */
    @Override
    public boolean hasEdge(int key1, int key2) {
        if(!this.allNodes.containsKey(key1)||!this.allNodes.containsKey(key1))
              return false;
        return nodesNeib.get(allNodes.get(key1)).containsKey(key2);
    }

    /**
     * the method gesEdge gets two keys and if there are two nodes with that key, it return the edge weight.
     * @param key1
     * @param key2
     * @return
     */
    @Override
    public double getEdge(int key1, int key2) {
        if(!this.allNodes.containsKey(key1)||!this.allNodes.containsKey(key1))
            return -1;
        HashMap<Integer, node_info> temp = nodesNeib.get(allNodes.get(key1));
        if(!temp.containsKey(key2)) return -1;
        return edgesWeight.get(key1).get(key2);

    }

    /**
     * the method addNode gets integer, and if the graph didn't already contain a node with that key, if adds it.
     * @param key
     */
    @Override
    public void addNode(int key) {
        if(this.allNodes.containsKey(key))
            return;
        node_info node = new NodeInfo(key);
        allNodes.put(key, node);
        nodesNeib.put(node, new HashMap<Integer, node_info>());
        edgesWeight.put(key, new HashMap<Integer, Double>());
        changesNum++;
    }

    /**
     * the method connect, connect between two given node's kye
     * if they already has an edge it update the weight of the edge to the given double value
     * @param key1
     * @param key2
     * @param w
     */
    @Override
    public void connect(int key1, int key2, double w) {
        if(!this.allNodes.containsKey(key1)||!this.allNodes.containsKey(key2)||key1==key2)
            return;
        node_info n1 = allNodes.get(key1);
        node_info n2 = allNodes.get(key2);
        if (edgesWeight.get(key1).containsKey(key2)){
            if (edgesWeight.get(key1).get(key2) == w){
                return;
            }
            else {
                edgesWeight.get(key1).remove(key2);
                nodesNeib.get(n1).remove(key2);
                edgesWeight.get(key2).remove(key1);
                nodesNeib.get(n2).remove(key1);
                edgeNum--;
            }
        }
        HashMap<Integer, node_info> temp1 = nodesNeib.get(n1);
        HashMap<Integer, node_info> temp2 = nodesNeib.get(n2);
        temp1.put(key2, n2);
        temp2.put(key1, n1);
        edgesWeight.get(key1).put(key2, w);
        edgesWeight.get(key2).put(key1, w);
        this.edgeNum++;
        this.changesNum++;
    }

    /**
     * There is a getV method that returns a collection of NodeInfos that the src.ex1.src.weighted_graph contains.
     * There is other method with the same name that gets an integer named key, first the method checks if
     * the src.ex1.src.weighted_graph contains a NodeInfo with that key, if not than it returns null, if it contains it
     * then it returns all of this NodeInfo neib collection, using the method getNi from NodeInfo.
     */
    @Override
    public Collection<node_info> getV() {
        return allNodes.values();
    }
    /**
     * the method getV get a node's key and returns all the nodes that are connected to this key in the graph as a Collection
     * @return
     */
    @Override
    public Collection<node_info> getV(int key) {
        if(!this.allNodes.containsKey(key))
            return null;
        node_info n = allNodes.get(key);
        return nodesNeib.get(n).values();
    }

    /**
     * Other method named removeNode gets a key value, we use the method get from NodeInfo class to get the node
     * we want to remove, and save it. By using foreach it passes throw all of that node neighbors'' and remove it from their neib,
     * the method does it by using the removeNode method from NodeInfo class.
     * At the end we remove that node from the HashMap allNodes of the src.ex1.src.weighted_graph, and return the removed node.
     * If the node doesn't exist then we return null because there is nothing to remove.
     * @param key
     * @return
     */
    @Override
    public node_info removeNode(int key) {
        if(!this.allNodes.containsKey(key))
            return null;
        node_info removedN = allNodes.get(key);
        for (node_info neibN: this.getV(key)){
            this.nodesNeib.get(neibN).remove(key, removedN);
            this.edgesWeight.get(neibN.getKey()).remove(key);
            this.edgeNum-=1;
        }
        allNodes.remove(key, removedN);
        nodesNeib.remove(removedN);
        edgesWeight.remove(key);
        this.changesNum++;
        return removedN;
    }

    /**
     * the methode removeEdge between two given node's keys
     * @param key1
     * @param key2
     */
    @Override
    public void removeEdge(int key1, int key2) {
        if(!this.allNodes.containsKey(key1) || !this.allNodes.containsKey(key2))
            return;
        if(!edgesWeight.get(key1).containsKey(key2))
            return;
        node_info n1 = allNodes.get(key1);
        node_info n2 = allNodes.get(key2);
        this.nodesNeib.get(n1).remove(key2);
        this.nodesNeib.get(n2).remove(key1, n1);
        this.edgesWeight.get(key1).remove(key2);
        this.edgesWeight.get(key2).remove(key1);
        this.edgeNum-=1;
        changesNum++;
    }

    /**
     * the method nodeSize returns the number of nodes in the graph.
     * @return
     */
    @Override
    public int nodeSize() {
        return allNodes.size();
    }
    /**
     * the method edgeSize returns the number of edges in the graph.
     * @return
     */
    @Override
    public int edgeSize() {
        return edgeNum;
    }
    /**
     * the method getMC returns the number of changes in the graph.
     * @return
     */
    @Override
    public int getMC() {
        return this.changesNum;
    }

    /**
     * the boolean method equals compare between one given src.ex1.src.weighted_graph to other src.ex1.src.WGraph_DS
     * it returns true if they are equal.
     * @param
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(obj instanceof weighted_graph){
            return obj.toString().equals(this.toString());
        }
        return false;
    }
    public boolean equals(weighted_graph p){
        if(p.nodeSize()==0){
            if(this.nodeSize()==0){
                return true;
            }
            else{
                return false;
            }
        }
        for (node_info nP : p.getV()) {
            node_info nG = this.getNode(nP.getKey());
            if (nG == null) {
                return false;
            }
            if(nG.getKey() != nP.getKey()){
                return false;
            }
            if(nG.getTag() != nP.getTag()) {
                return false;
            }
            if(nG.getInfo().compareTo(nP.getInfo()) != 0){
                return false;
            }
            for (node_info neibP : p.getV(nP.getKey())) {
                node_info neibG = this.allNodes.get(neibP.getKey());
                if(neibG==null) {
                    return false;
                }
                if(this.getEdge(neibG.getKey(), nG.getKey()) != p.getEdge(neibP.getKey(), nP.getKey())) return false;
            }
        }
        return true;
    }

    /**
     * This method make a String made of all of the data in a weighted graph.
     * @return
     */
    public String toString(){
        StringBuilder nodeS = new StringBuilder();
            for (node_info n : this.getV()) {
                    nodeS.append(n.getKey());
                    nodeS.append(n.getTag());
                    nodeS.append(n.getInfo());
                }
            nodeS.append("endOfNodesLoading");
                for (node_info n : this.getV()) {
                    nodeS.append(n.getKey());
                    nodeS.append(",");
                    if (this.getV(n.getKey()).size() != 0) {
                        for (node_info neib : this.getV(n.getKey())) {
                            nodeS.append(neib.getKey());
                            nodeS.append(this.getEdge(n.getKey(), neib.getKey()));
                        }
                    }
                }
        return nodeS.toString();

    }

    /**
     * NodeInfo is a private class that create the nodes of the graph
     * it has method to get or set a node's values
     */
    private static class NodeInfo implements node_info {
        private static int keyCounter = 0;
        private int key;
        private double tag = 0;
        private String info;

        private NodeInfo() {
            this.key = keyCounter;
            this.info = "" + keyCounter;
            keyCounter++;
        }

        private NodeInfo(int key) {
            this.key = key;
            this.info = "" + key;
            if (keyCounter < key)
                keyCounter = key;
        }

        private NodeInfo(node_info nCop) {
            this.info = nCop.getInfo();
            this.key = nCop.getKey();
            this.tag = nCop.getTag();
        }

        private NodeInfo(String s) {
            String[] nInfo = s.split(",");
            this.key = Integer.parseInt(nInfo[0]);
            this.tag = Integer.parseInt(nInfo[1]);
            this.info = nInfo[2];
        }

        @Override
        public int getKey() {
            return this.key;
        }

        @Override
        public String getInfo() {
            return this.info;
        }

        @Override
        public void setInfo(String s) {
            this.info = s;
        }

        @Override
        public double getTag() {
            return this.tag;
        }

        @Override
        public void setTag(double t) {
            this.tag = t;
        }

        public boolean equals(node_info nC) {
            if (this == null) {
                if (nC == null)
                    return true;
                return false;
            } else if (nC == null)
                return false;
            if (this.key != nC.getKey()) return false;
            if (this.tag != nC.getTag()) return false;
            if (this.info.equals(nC.getInfo())) return false;
            return true;
        }
    }
}
