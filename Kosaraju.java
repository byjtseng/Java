import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collections;
import java.io.File;
import java.util.Scanner;
import java.util.LinkedList;

public class Kosaraju {
    private static int n;
    private static HashSet<Integer> visited;
    private static HashSet<Integer> addplz;

    public static void main(String[] args) {
        Vertex[] graph = new Vertex[1];
        Vertex[] graph_rev = new Vertex[1];

        // reads in a file with a list of directed edges in numerical order
        try {
            Scanner max = new Scanner(new File("SCC.txt"));
            while (max.hasNext()) {
                int v_start = max.nextInt();
                int v_end = max.nextInt();

                int v_max = Math.max(v_start, v_end);
                n = Math.max(n, v_max);
            }

            graph = new Vertex[n];
            graph_rev = new Vertex[n];

            Scanner in = new Scanner(new File("SCC.txt"));
            while (in.hasNext()) {
                int v_start = in.nextInt()-1;
                int v_end = in.nextInt()-1;

                // adds to the graph
                if (graph[v_start] == null) {
                    graph[v_start] = new Vertex();
                    ArrayList<Integer> e1 = new ArrayList<>();
                    e1.add(v_end);
                    graph[v_start].edges = e1;
                }
                else {
                    graph[v_start].edges.add(v_end);
                }

                // adds to the reverse graph
                if (graph_rev[v_end] == null) {
                    graph_rev[v_end] = new Vertex();
                    ArrayList<Integer> er = new ArrayList<>();
                    er.add(v_start);
                    graph_rev[v_end].edges = er;
                }
                else {
                    graph_rev[v_end].edges.add(v_start);
                }

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(n + " total vertices");

        visited = new HashSet<>();
        addplz = new HashSet<>();
        int [] order = new int [n];
        int pos = 0;

        // iterating backwards through the HashMap of graph_rev
        for (int i = n-1; i >= 0; i--) {
            if (!visited.contains(i)) {
                // finds the finishing order until it cannot continue
                ArrayList<Integer> small_order = find_order(graph_rev, i);

                // adds that partial ordering to the total ordering
                for (int  v : small_order) {
                    order[pos] = v;
                    pos++;
                }
            }
        }

        visited.clear();
        ArrayList<Integer> scc_size = new ArrayList<>();

        // iterating backwards through the array of finishing orders
        for (int i = order.length-1; i >= 0; i--) {
            int current = order[i];

            // if the next vertex in the finishing order hasn't been visited, it's the start of a new SCC
            // finds the length and adds the length to the list of lengths of SCCs
            if (!visited.contains(current)) {
                scc_size.add(find_scc(graph, current));
            }
        }

        // sorts and prints the 5 largest SCCs
        Collections.sort(scc_size);
        for (int i=scc_size.size()-1; i>=0 && i>=scc_size.size()-5; i--)
            System.out.println(scc_size.get(i));
    }

    // DFS through the reverse graph to find finishing orders
    private static ArrayList<Integer> find_order(Vertex[] g, int v_start) {
        LinkedList <Integer> s = new LinkedList<>();
        ArrayList <Integer> order = new ArrayList<>();
        s.push(v_start);

        while (!s.isEmpty()) {
            int current = s.pop();

            // ignore previously visited vertices, but add them to the finished ordering if they were waiting
            // to be added while backtracking, also remembering to not add it to the order a second time if
            // the vertex is encountered along a different path
            if (visited.contains(current)) {
                if (addplz.contains(current)) {
                    addplz.remove(current);
                    order.add(current);
                }
                continue;
            }

            visited.add(current);

            // adds the vertex back to the stack so it will be encountered while backtracking.
            // also notes that this vertex needs to be added to the finishing order once it is
            // found while backtracking
            addplz.add(current);
            s.push(current);

            // adds unvisited neighbors to the stack
            if (g[current] != null) {
                for (int v : g[current].edges) {
                    if (!visited.contains(v)) {
                        s.push(v);
                    }
                }
            }
        }

        return order;
    }

    // DFS to find the size of the various strongly connected components
    private static int find_scc(Vertex[] g, int v_start) {
        LinkedList <Integer> s = new LinkedList<>();
        int scc = 0;
        s.push(v_start);

        while (!s.isEmpty()) {
            int current = s.pop();

            if (visited.contains(current)) continue;

            // increments the size of the scc for each vertex visited
            visited.add(current);
            scc++;

            // adds unvisited neighbors to the stack
            if (g[current] != null) {
                for (int v : g[current].edges) {
                    if (!visited.contains(v)) {
                        s.push(v);
                    }
                }
            }
        }

        return scc;
    }

    private static class Vertex {
        ArrayList <Integer> edges;
    }
}

