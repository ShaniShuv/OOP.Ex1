package ex1.tests;

import ex1.src.WGraph_DS;
import ex1.src.weighted_graph;
import org.junit.jupiter.api.Test;
import ex1.src.node_info;

import java.util.Random;


import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class WGraph_DSTest {
    public static weighted_graph wg;
    public static weighted_graph wg1 = new WGraph_DS();
    static int seed = 31;
    static int v_size = 10;
    static int e_size = v_size * 3;
    static Random _rnd = new Random(seed);

    private static void execute() {

    }


    public weighted_graph wgEmpty(int nodes) {
        wg1 = new WGraph_DS();
        for (int i = 0; i < nodes; i++) {
            wg1.addNode(i);
        }
        return wg1;
    }

    public weighted_graph wgWhole(int nodes) {
        wg1 = new WGraph_DS();
        for (int i = 0; i < nodes; i++) {
            wg1.addNode(i);
        }
        for (int i = 0; i < nodes - 1; i++) {
            for (int j = i + 1; j < nodes; j++) {
                wg1.connect(i, j, 5);
            }

        }
        return wg1;
    }

    public weighted_graph wgRandCreator(int nodes, int edges) {
        wg1 = wgEmpty(nodes);
        int maxE = (nodes * (nodes - 1) / 2);
        for (int j = 0; j < Math.min(edges, maxE); j++) {
            int a = nextRnd(0, nodes - 1);
            int b = nextRnd(0, nodes - 1);

            while (wg1.hasEdge(a, b) || a == b) {
                a = nextRnd(0, nodes - 1);
                b = nextRnd(0, Math.abs(nodes - a));

            }
            double w = nextRnd(1, (a + b) * 2);
            wg1.connect(a, b, w);
        }
        return wg1;
    }
    public weighted_graph wgRealRand(int nodes, int edges) {
        wg1 = wgEmpty(nodes);
        int maxE = (nodes * (nodes - 1) / 2);
        for (int j = 0; j < Math.min(edges, maxE); j++) {
            int a = nextRnd(0, nodes - 1);
            int b = nextRnd(0, nodes - 1);

            double w = nextRnd(1, (a + b) * 2);
            wg1.connect(a, b, w);
        }
        return wg1;
    }

    public weighted_graph wgSpecific() {
        wg = new WGraph_DS();
        for (int i = 0; i < 11; i++) {
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
        return wg;
    }


    @Test
    void hasEdge(){
        wg = wgEmpty(5);
        assertFalse(wg.hasEdge(0, 1));
        wg.connect(0, 4, 0.3);
        assertTrue(wg.hasEdge(0, 4));
    }
    @Test
    void getEdge() {
        wg = wgWhole(v_size);
        int a = nextRnd(0, v_size / 2);
        int b = nextRnd((v_size / 2 + 1), v_size - 1);
        double d = wg.getEdge(a, b);
        assertNotEquals(-1, d);
        wg.removeEdge(a, b);
        d = wg.getEdge(b, a);
        assertEquals(-1, d);
        d = wg.getEdge(a, v_size + 1);
        assertEquals(-1, d);

        wg.connect(a, b, 0);
        assertEquals(0, d);

    }

    @Test
    void addNode() {
        weighted_graph wg = wgWhole(v_size);
        wg.addNode(20);
        boolean b = false;
        if (wg.getNode(20) != null) {
            b = true;
        }
        assertEquals(true, b);
    }

    @Test
    void connect() {
        wg = wgWhole(v_size);
        wg.connect(1, 2, 0.7);
        boolean b = false;
        if (wg.getEdge(1, 2) == 0.7) {
            b = true;
        }

        assertEquals(true, b);
        b = false;
        if (wg.getEdge(2, 1) == 0.7) {
            b = true;
        }

        assertEquals(true, b);
    }




    @Test

    void getV() {
        wg = wgEmpty(v_size);
        assertEquals(v_size, wg.nodeSize());

        for(int i=0; i<v_size; i++){
            node_info temp = wg.getNode(i);
            boolean b = temp ==null;
            assertFalse(b);
        }
    }

    @Test
    void testGetV() {
        wg = wgSpecific();
        node_info temp = null;
        Collection<node_info> col = wg.getV(1);
        assertTrue(col.contains(wg.getNode(10)));

        col = wg.getV(10);
        assertTrue(col.contains(wg.getNode(1)));
        assertTrue(col.contains(wg.getNode(9)));

        col = wg.getV(9);
        assertTrue(col.contains(wg.getNode(10)));
        assertTrue(col.contains(wg.getNode(2)));

        col = wg.getV(2);
        assertTrue(col.contains(wg.getNode(9)));
        assertTrue(col.contains(wg.getNode(7)));

        col = wg.getV(7);
        assertTrue(col.contains(wg.getNode(2)));
        assertTrue(col.contains(wg.getNode(5)));

        col = wg.getV(5);
        assertTrue(col.contains(wg.getNode(7)));
        assertTrue(col.contains(wg.getNode(8)));

        col = wg.getV(8);
        assertTrue(col.contains(wg.getNode(5)));
        assertTrue(col.contains(wg.getNode(4)));

        col = wg.getV(4);
        assertTrue(col.contains(wg.getNode(8)));
        assertTrue(col.contains(wg.getNode(6)));

        col = wg.getV(6);
        assertTrue(col.contains(wg.getNode(4)));
        assertTrue(col.contains(wg.getNode(3)));

        wg.removeEdge(1, 10);

        col = wg.getV(1);
        assertFalse(col.contains(wg.getNode(10)));

    }

    @Test
    void removeNode() {
        wg =wgWhole(v_size);
        int key = nextRnd(0, v_size-1);
        node_info removedNode = wg.getNode(key);
        node_info temp = wg.removeNode(key);
        assertEquals(removedNode, temp);
        boolean b = (wg.getNode(key)==null);
        assertTrue(b);
    }

    @Test
    void removeEdge() {
        wg =wgEmpty(v_size);
        int key1 = nextRnd(0, v_size/2);
        int key2 = nextRnd(v_size/2 +1,v_size-1);

        boolean b = wg1.hasEdge(key1, key2);
        assertEquals(false, b);
        wg.connect(key1, key2, 6);

        b = wg1.hasEdge(key1, key2);
        assertEquals(true, b);
    }

    @Test
    void nodeSize() {
        wg =wgWhole(v_size);
        boolean b = (wg.getV().size() == v_size);
        assertTrue(b);
        wg.removeNode(1);
        b = (wg.getV().size() == v_size-1);
        assertTrue(b);
    }

    @Test
    void edgeSize() {
        wg =wgWhole(v_size);
        boolean b = (wg.edgeSize() == (v_size*(v_size-1)/2));
        assertTrue(b);
        wg.removeEdge(0, v_size-1);
        b = (wg.edgeSize() == (v_size*(v_size-1)/2)-1);
        assertTrue(b);

        wg.connect(0, 1, 0);
        b = (wg.edgeSize() == (v_size*(v_size-1)/2)-1);
        assertTrue(b);

        wg.connect(0, 1, 3);
        b = (wg.edgeSize() == (v_size*(v_size-1)/2)-1);
        assertTrue(b);

        int edgesSize = nextRnd(1, (v_size*(v_size-1)/2)-1);
        wg = wgRandCreator(v_size, edgesSize);
        b = (wg.edgeSize() == edgesSize);
        assertTrue(b);

        wg = wgEmpty(v_size);
        b = (wg.edgeSize() == 0);
        assertTrue(b);
        wg.connect(v_size, v_size+1, 0);
        b = (wg.edgeSize() == 0);
        assertTrue(b);

        wg.connect(v_size-1, v_size+1, 0);
        b = (wg.edgeSize() == 0);
        assertTrue(b);

    }
    @Test
    void testEquals() {
        wg =wgWhole(v_size);
        WGraph_DS wg1 = new WGraph_DS(wg);
        boolean b = wg1.equals((WGraph_DS)wg);
        assertTrue(b);
        wg1.removeNode(1);
        b=wg1.equals(wg);
        assertFalse(b);
    }
    @Test
    void getMC(){
        wg = wgEmpty(v_size);
        int tempMC = wg.getMC();
        wg.connect(0, v_size-1, 5);
        assertNotEquals(tempMC, wg.getMC());

        tempMC = wg.getMC();
        wg.connect(0, v_size-1, 4);
        assertNotEquals(tempMC, wg.getMC());

        tempMC = wg.getMC();
        wg.addNode(v_size);
        assertNotEquals(tempMC, wg.getMC());

        tempMC = wg.getMC();
        wg.removeEdge(0, v_size-1);
        assertNotEquals(tempMC, wg.getMC());

        tempMC = wg.getMC();
        wg.removeNode( v_size);
        assertNotEquals(tempMC, wg.getMC());

    }

    @Test
    void creatingTime(){
        int vNum = 1000000;
            long startEmpty = System.currentTimeMillis();
            wg = wgEmpty(vNum);
            long endEmpty= System.currentTimeMillis();
            long emptyTime = (startEmpty-endEmpty)/1000;
            assertTrue(emptyTime<10);

        long startRand = System.currentTimeMillis();
        wg = wgRealRand(vNum, vNum*10);
        long endRand= System.currentTimeMillis();
        long randTime = (startRand-endRand)/1000;
        assertTrue(randTime<40);

//        long startWhole = System.currentTimeMillis();
//        wg = wgWhole(vNum);
//        long endWhole= System.currentTimeMillis();
//        long wholeTime = (startWhole-endWhole)/1000;
//        assertTrue(wholeTime<40);
    }
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