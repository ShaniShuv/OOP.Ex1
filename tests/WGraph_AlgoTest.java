package ex1.tests;

import ex1.src.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;



import static org.junit.jupiter.api.Assertions.*;

class WGraph_AlgoTest {
    public static weighted_graph wg = new WGraph_DS();
    public static weighted_graph_algorithms wga = new WGraph_Algo();
    public static weighted_graph wg1 = new WGraph_DS();
    public static weighted_graph_algorithms wga1 = new WGraph_Algo();
    static int seed = 31;
    static int v_size = 10;
    static int e_size = v_size*3;
    static Random _rnd = new Random(seed);
//    static src.ex1.src.weighted_graph g0 = new src.ex1.src.WGraph_DS(), g1;
//    static src.ex1.src.weighted_graph_algorithms ga;

    public weighted_graph wgSpecific(){
        wg = new WGraph_DS();
        for(int i=0; i<11; i++){
            wg.addNode(i);
        }
        wg.connect(1, 10, 1);
        wg.connect(9, 10, 1);
        wg.connect(9, 2, 1);
        wg.connect(7, 2, 1);
        wg.connect(7, 5, 1);
        wg.connect(8, 5, 1);
        wg.connect(8, 4, 8);
        wg.connect(4, 6, 8);
        wg.connect(3, 6, 8);
        wga= new WGraph_Algo();
        wga.init(wg);
        return wg;
    }

    public weighted_graph wgEmpty(int nodes) {
        wg1 = new WGraph_DS();
        for (int i = 0; i < nodes; i++) {
            wg1.addNode(i);
        }
        return wg1;
    }

    public weighted_graph wgWhole(int nodes){
        wg1 = new WGraph_DS();
        for (int i = 0; i < nodes; i++) {
            wg1.addNode(i);
        }
        for (int i = 0; i < nodes-1; i++) {
            for (int j = i+1; j < nodes; j++) {
                wg1.connect(i, j, 5);
            }

        }
        return wg1;
    }
    public weighted_graph wgRandCreator(int nodes, int edges) {
        wg1 = wgEmpty(nodes);
        int maxE = (nodes * (nodes - 1) / 2);
        for (int j = 0; j < Math.min(edges,maxE); j++) {
            int a = nextRnd(0, nodes- 1);
            int b = nextRnd(0, nodes- 1);

            while (wg1.hasEdge(a,b) || a==b) {
                a = nextRnd(0, nodes- 1);
                b = nextRnd(0, Math.abs(nodes- a));

            }
            double w = 100 +nextRnd(1, (a + b)*2);
            wg1.connect(a, b, w);
        }
        return wg1;
    }
    public double shortestSU(weighted_graph wg, int []a){
//        double [] d = new double[a.length-1];
        double d =0;
        for (int i=0; i<a.length-1; i++){
            node_info n1 =wg.getNode(a[i]);
            node_info n2 = wg.getNode(a[i+1]);
//            d[i]=nextRnd(0.01, 1);
            double w = nextRnd(0.01, 1);
            d+=w;
            wg.connect(a[i], a[i+1], w);
        }
        return d;
    }
    @Test
    void init() {
        wga1 = new WGraph_Algo();
        wg= wgRandCreator(v_size, e_size);
        wga1.init(wg);
        wga.init(wg);
        wg1 = (WGraph_DS)wga1.getGraph();
        wg= wga.getGraph();
        assertTrue(wg.equals(wga1.getGraph()));

         wg = new WGraph_DS();
         wga.init(wg);
        assertFalse(wg.equals(wga1.getGraph()));
    }

    @Test
    void getGraph() {
        wg = wgSpecific();
        wga.init(wg);
        Object temp = (Object)wg;
        boolean b = temp.equals(wga.getGraph());
        assertTrue(b);
        wg= wgEmpty(1);
        temp = (Object)wg;
        b = temp.equals(wga.getGraph());
        assertFalse(b);
    }

    @Test
    void copy() {
        wg = wgRandCreator(v_size, e_size);
        wga.init(wg);
        wga1.init(wga.copy());
        assertTrue(wga.toString().equals(wga1.toString()));
        wga1.init(new WGraph_DS());
        assertFalse(wga.toString().equals(wga1.toString()));
    }

    @Test
    void isConnected() {
        wg = wgEmpty(v_size);
        wga.init(wg);

        boolean b = wga.isConnected();
        assertFalse(b);

        wg = wgRandCreator(v_size, v_size-2);
        wga.init(wg);

        b = wga.isConnected();
        assertFalse(b);

        wg = wgWhole(v_size);
        wga.init(wg);

        b = wga.isConnected();
        assertTrue(b);

        wg.addNode(v_size);
        wga.init(wg);

        b = wga.isConnected();
        assertFalse(b);

//        assertNotEquals(true, false);
    }

    @Test
    void shortestPathDist() {
        wg = wgWhole(50);
        wga.init(wg);
        int[] arr= {1, 6, 25, 19, 23,43,37 };
//        int a = nextRnd(0, v_size-2);
//        int b = nextRnd(0, v_size-1);
//        while (a==b){
//            a = nextRnd(0, b);
//        }
        double d=shortestSU(wg, arr);
        double sP = wga.shortestPathDist(1, 37);
        boolean b = d==sP;
        b &= d==wga.getGraph().getNode(37).getTag();
        assertTrue(b);
        int key = nextRnd(0, v_size-1);
        assertEquals(0, wga.shortestPathDist(0,0));

    }

    @Test
    void shortestPath() {
        int []arr = {1, 7, 9, 23, 13, 19, 49, 0};
        wg = wgWhole(50);
        wga.init(wg);
        double d=shortestSU(wg, arr);
        List<node_info> sP = wga.shortestPath(1,0);
        boolean b = sP.size()==arr.length;
        assertTrue(b);
        if(b){
            int i=0;
            for(node_info curr: sP) {
                b &=curr.getKey()==arr[i];
                i++;
            }
        }
        assertTrue(b);



    }

    @Test
    void save() {
        boolean b = wga.save("123456789.txt");
        assertTrue(b);
//        assertTrue();
    }

    @Test
    void load() {
        wg = wgRandCreator(v_size, e_size);
        wga.init(wg);
        wga.save("file.txt");
        wga1.load("C:\\Users\\abhau\\IdeaProjects\\ex1\\file.txt");
        WGraph_DS temp = (WGraph_DS)wga.getGraph();
        boolean b = temp.equals(wga1.getGraph());
        assertTrue(b);
    }

    @Test
    void bfs(){
        wg = wgSpecific();
        wga.init(wg);
        wga.shortestPathDist(1, 4);
        boolean b = wga.getGraph().getNode(1).getTag()==0;
        b &= wga.getGraph().getNode(2).getTag()==3;
        b &= wga.getGraph().getNode(3).getTag()==23;
        b &= wga.getGraph().getNode(4).getTag()==7;
        b &= wga.getGraph().getNode(5).getTag()==5;
        b &= wga.getGraph().getNode(6).getTag()==15;
        b &= wga.getGraph().getNode(7).getTag()==4;
        b &= wga.getGraph().getNode(8).getTag()==6;
        b &= wga.getGraph().getNode(9).getTag()==2;
        b &= wga.getGraph().getNode(10).getTag()==1;

    }


//    @Test
//    void equals() {
//        src.ex1.src.WGraph_Algo wga2 = new src.ex1.src.WGraph_Algo();
//        src.ex1.src.WGraph_Algo wga1 = (src.ex1.src.WGraph_Algo) wga;
//        wga1.init(wgSpecific());
//
//        boolean b = wga1.equals(wga2);
//        assertFalse(b);
//
////        wga2.init(wga1.getGraph());
//        wga2.init(wgSpecific());
//        b = wga1.equals(wga2);
//        assertTrue(b);
//
//        wga2.init(wga1.copy());
//        b = wga1.equals(wga2);
//        assertTrue(b);
////        int key = (nextRnd(1, 10));
////        wga.getGraph().getNode(key).setTag(-30);
//
//
//    }
    public static int nextRnd(int min, int max) {
        double v = nextRnd(0.0+min, (double)max);
        int ans = (int)v;
        return ans;
    }
    public static double nextRnd(double min, double max) {
        double d = _rnd.nextDouble();
        double dx = max-min;
        double ans = d*dx+min;
        return ans;
    }
}