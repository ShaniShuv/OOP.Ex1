The class ex1.src.WGraph_DS creates a type of graph that impliments the interface ex1.src.weighted_graph.
The method getNode gets integer and if there is a node in the graph with that key, it returns it, if not then it returns null.
The method hasEdge gets two keys returns true if there is edge between two nodes with that key in the graph.

The method gesEdge gets two keys and if there are two nodes with that key, it return the edge weight.

The method addNode gets integer, and if the graph didn't already contain a node with that key, if adds it.

The method connect, connect between two given node's kye, if they already has an edge it update the weight of the edge to the given double value.

The method getV returns all the nodes in the graph as a Collection.

The method getV get a node's key and returns all the nodes that are connected to this key in the graph as a Collection.

The method removeNode gets a node's key and remove that node from the graph, it returns the node at the end.

The methode removeEdge between two given node's keys.

The method nodeSize returns the number of nodes in the graph.

The method edgeSize returns the number of edges in the graph.

The method getMC returns the number of changes in the graph.

The boolean method equals compare between one given ex1.src.weighted_graph to other ex1.src.WGraph_DS it returns true if they are equal.

The method toString make a String made of all of the data in a weighted graph.

NodeInfo is a private class that create the nodes of the graph, it has method to get or set a node's values

The class ex1.src.WGraph_Algo impliments the interface ex1.src.weighted_graph_algorithms

The method init save a given graph as this ex1.src.WGraph_Algo's graph.

The method getGraph return the underlying graph of which this class works.

The method copy compute a deep copy of this weighted graph.

The method shortest path returns the the shortest path between src to dest - as an ordered List of nodes.
If no such path it returns null.

The method shortestPathDist returns the length of the shortest path between src to dest, if there's no path it returns -1.

The boolean method isConnected returns true if and only if there is a valid path from EVREY node to each.

There is a method save which creates a new file and save the data of a wieghted graph in that file.
It return true - iff the graph was successfully saved in the file.

There is a method named load which creates a graph to this graph algorithm by the data that was saved in the 'save' method.
It return true - iff the graph was successfully loaded.

