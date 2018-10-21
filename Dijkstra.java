import java.util.ArrayList;
import java.util.HashMap;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.io.File;

public class Dijkstra {
    private static HashSet<Vertex> visited;
    private static PriorityQueue<Vertex> heap;
    private static HashMap <Integer, Vertex> vertices;

    public static void main(String[] args) {
        vertices = new HashMap<>();

        try {
            Scanner in = new Scanner(new File("dijkstraData.txt"));

            while (in.hasNext()) {
                Scanner line = new Scanner(in.nextLine());
                Vertex v = new Vertex();
                int id = line.nextInt();
                v.edges = new ArrayList<>();

                while (line.hasNext()) {
                    Edge e = new Edge();
                    String[] s = line.next().split(",");
                    e.tail_id = Integer.parseInt(s[0]);
                    e.length = Integer.parseInt(s[1]);

                    v.edges.add(e);
                }

                vertices.put(id, v);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        visited = new HashSet<>();
        heap = new PriorityQueue<>(new VertexComparator());
        heap.add(vertices.get(1));

        while (visited.size() < vertices.size()) {
            add_vertex();
        }

        for (int i = 1; i < vertices.size(); i++) {
            Vertex v = vertices.get(i);
            System.out.println(i + ": " + v.min_length);
        }
    }

    private static void add_vertex() {
        Vertex v = heap.poll();

        if (v != null) {
            visited.add(v);

            for (Edge e : v.edges) {
                Vertex n = vertices.get(e.tail_id);
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
}

class Vertex {
    int min_length;
    ArrayList<Edge> edges;
}

class Edge {
    int tail_id;
    int length;
}

class VertexComparator implements Comparator<Vertex> {
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
