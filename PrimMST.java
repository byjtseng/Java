import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.io.File;

public class PrimMST {
    private static PriorityQueue<Vertex> heap;
    private static HashSet<Vertex> visited;
    private static Vertex[] vertices;

    public static void main(String[] args) {
        try {
            Scanner in = new Scanner(new File("edges.txt"));
            vertices = new Vertex[in.nextInt()];
            for (int i=0; i<vertices.length; i++) {
                vertices[i] = new Vertex();
            }

            in.nextInt();

            while (in.hasNext()) {
                int v1i = in.nextInt()-1;
                int v2i = in.nextInt()-1;
                int l = in.nextInt();

                vertices[v1i].edges.add(new Edge(v2i, l));
                vertices[v2i].edges.add(new Edge(v1i, l));
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        visited = new HashSet<>();
        heap = new PriorityQueue<>(new VertexComparator());
        int total_length = 0;

        Vertex v = vertices[0];
        visited.add(v);
        for (Edge e : v.edges) {
            vertices[e.tail_id].key = e.length;
            heap.add(vertices[e.tail_id]);
        }

        while (visited.size() < vertices.length) {
            v = heap.poll();
            visited.add(v);
            total_length += v.key;

            for (Edge e : v.edges) {
                if (!visited.contains(vertices[e.tail_id])) {
                    heap.remove(vertices[e.tail_id]);
                    vertices[e.tail_id].key = Math.min(e.length, vertices[e.tail_id].key);
                    heap.add(vertices[e.tail_id]);
                }
            }
        }

        System.out.println(total_length);
    }

    static class Vertex {
        int key;
        ArrayList<Edge> edges;

        Vertex() {
            edges = new ArrayList<>();
            key = Integer.MAX_VALUE;
        }
    }

    static class Edge {
        int tail_id;
        int length;

        Edge(int t, int l) {
            tail_id = t;
            length = l;
        }
    }

    static class VertexComparator implements Comparator<Vertex> {
        public int compare(Vertex v1, Vertex v2) {
            if (v1.key > v2.key) {
                return 1;
            }
            else if (v1.key < v2.key) {
                return -1;
            }
            return 0;
        }
    }
}
