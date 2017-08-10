package com.mygdx.game;

import java.util.LinkedList;
import java.util.List;

public class Graph {

    /*
     * This class implements a common graph
     *
     * It will compute a minimum spanning tree. In doing so it uses an implementation of the algorithm of Prim
     *
     * source: https://de.wikipedia.org/wiki/Algorithmus_von_Prim
     */

    private class Edge {

        /*
         * simple edge class with weight and target node
         */

        private final Node target;

        private final int weight;

        private Edge(final Node target, final int weight) {
            this.target = target;
            this.weight = weight;
        }
    }

    public class MST {

        /*
         * the minimum spanning tree
         */

        private class MSTNode {

            /*
             * node in the minimum spanning tree
             */

            // key/identity of the node
            private final int key;

            // previous node of this node
            private MSTNode previous;

            // value stored in the node
            private int value = Integer.MAX_VALUE;

            private MSTNode(final int key) {
                this.key = key;
            }

            public void setPrevious(final MSTNode previous) {
                this.previous = previous;
            }

            public void setValue(final int value) {
                this.value = value;
            }

        }

        private MSTNode current;

        private Graph inputGraph;

        private List<MSTNode> list = new LinkedList<MSTNode>();

        private Graph outputGraph;

        private List<MSTNode> rList = new LinkedList<MSTNode>();

        private MST(Graph iGraph, int startNode) {
            inputGraph = iGraph;
            outputGraph = new Graph(inputGraph.getNumberOfNodes());
            for (int i = 0; i < inputGraph.getNumberOfNodes(); i++) {
                MSTNode n = new MSTNode(i);
                rList.add(n);
                list.add(n);
            }
            list.get(startNode).setValue(0);
            current = poll();
        }

        // creates a minimum spanning tree
        private Graph create() {
            while (!rList.isEmpty()) {
                update();
                current = poll();
                outputGraph.insertEdge(current.previous.key, current.key, current.value);
            }
            return outputGraph;
        }

        private MSTNode poll() {
            MSTNode p = rList.get(0);
            for (MSTNode n : rList) {
                if (n.value < p.value) {
                    p = n;
                }
            }
            rList.remove(p);
            return p;
        }

        // updates every node in the minimum spanning tree
        private void update() {
            for (int i = 0; i < inputGraph.getNumberOfNodes(); i++) {
                // check if the value of the Node should be updated
                // if so, update value and update predecessor of the Node
                if (inputGraph.getWeight(current.key, i) < list.get(i).value && inputGraph.isConnected(current.key, i)) {
                    list.get(i).setValue(inputGraph.getWeight(current.key, i));
                    list.get(i).setPrevious(current);
                }
            }
        }
    }

    private class Node {

        /*
         * simple node class
         */

        private LinkedList<Edge> edges;

        public Node() {
            edges = new LinkedList<Edge>();
        }

        private Edge getEdgeTo(final Node t) {
            Edge e = null;
            for (int i = 0; i < edges.size(); i++) {
                if (edges.get(i).target == t) {
                    e = edges.get(i);
                }
            }
            return e;
        }

        private void setEdge(Edge e) {
            edges.add(e);
        }
    }

    private final LinkedList<Node> nodes = new LinkedList<Node>();

    public Graph(final int size) {
        for (int i = 0; i < size; i++) {
            insertNode();
        }
    }

    public Graph createMinimumSpanningTree(int startNode) {
        if (startNode < 0 || startNode >= nodes.size()) {
            throw new IllegalArgumentException();
        }
        for (Node n : nodes) {
            if (n.edges.size() == 0) {
                throw new IllegalStateException();
            }
        }
        MST mst = new MST(this, startNode);
        Graph spanningTree = mst.create();
        return spanningTree;
    }

    public int[][] getAdjacencyMatrix() {
        int[][] g = new int[nodes.size()][nodes.size()];
        for (int i = 0; i < g.length; i++) {
            for (int j = 0; j < g.length; j++) {
                // fill matrix with the weight of the edge connecting the current nodes
                // if nodes aren't connected, write -1
                g[i][j] = isConnected(i, j) ? getWeight(i, j) : -1;
            }
        }
        return g;
    }

    public int getNumberOfNodes() {
        return nodes.size();
    }

    public int getWeight(int src, int target) {
        return isConnected(src, target) ? nodes.get(src).getEdgeTo(nodes.get(target)).weight : -1;
    }

    // connect two nodes with a specific weight
    public void insertEdge(final int src, final int target, final int weight) {
        if (src == target || src >= nodes.size() || target >= nodes.size() || src < 0 || target < 0 || weight < 0) {
            throw new IllegalArgumentException();
        }
        if (isConnected(src, target)) {
            throw new IllegalStateException();
        }
        nodes.get(src).setEdge(new Edge(nodes.get(target), weight));
        nodes.get(target).setEdge(new Edge(nodes.get(src), weight));
    }

    public void insertNode() {
        nodes.add(new Node());
    }

    public boolean isConnected(final int src, final int target) {
        if (src < 0 || target < 0 || src >= getNumberOfNodes() || target >= getNumberOfNodes()) {
            throw new IllegalArgumentException();
        }
        return nodes.get(src).getEdgeTo(nodes.get(target)) != null;
    }
}
