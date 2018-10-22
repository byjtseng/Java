import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.io.File;

public class Dijkstra {
    private static HashSet<Vertex> visited;
    private static PriorityQueue<Vertex> heap;
    private static Vertex[] vertices;

    public static void main(String[] args) {
        vertices = new Vertex[200];

        try {
            Scanner in = new Scanner(new File("dijkstraData.txt"));

            while (in.hasNext()) {
                Scanner line = new Scanner(in.nextLine());
                Vertex v = new Vertex();
                int v_id = line.nextInt()-1;
                v.edges = new ArrayList<>();

                while (line.hasNext()) {
                    Edge e = new Edge();
                    String[] s = line.next().split(",");
                    e.tail_id = Integer.parseInt(s[0])-1;
                    e.length = Integer.parseInt(s[1]);

                    v.edges.add(e);
                }

                vertices[v_id] = v;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        visited = new HashSet<>();
        heap = new PriorityQueue<>(new VertexComparator());
        heap.add(vertices[0]);

        while (visited.size() < vertices.length) {
            find_next();
        }

        for (int i = 0; i < vertices.length; i++) {
            Vertex v = vertices[i];
            System.out.println((i+1) + ": " + v.min_length);
        }
    }

    private static void find_next() {
        Vertex v = heap.poll();

        if (v != null) {
            visited.add(v);

            for (Edge e : v.edges) {
                Vertex n = vertices[e.tail_id];
                if (visited.contains(n)) continue;

                if (heap.contains(n)) {

                    heap.remove(n);
                    n.min_length = Math.min(v.min_length + e.length, n.min_length);
                    heap.add(n);
                } else {
                    n.min_length = v.min_length + e.length;
                    heap.add(n);
                }
            }
        }
    }

    static class Vertex {
        int min_length;
        ArrayList<Edge> edges;
    }

    static class Edge {
        int tail_id;
        int length;
    }

    static class VertexComparator implements Comparator<Vertex> {
        public int compare(Vertex v1, Vertex v2) {
            if (v1.min_length > v2.min_length) {
                return 1;
            }
            else if (v1.min_length < v2.min_length) {
                return -1;
            }
            return 0;
        }
    }
}

