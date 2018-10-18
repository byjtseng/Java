import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

public class Karger {
    public static void main (String[] args) {
        Scanner in, line;
        int e2 = 0;
        ArrayList<ArrayList<Integer>> verts = new ArrayList<>();

        // stores the adjacency list as a list of lists, where first index is vertex # and items are adjacent vertices
        try {
            File in_file = new File("kargerMinCut.txt");
            in = new Scanner(in_file);

            while (in.hasNext()) {
                line = new Scanner(in.nextLine());

                ArrayList<Integer> edges = new ArrayList<>();
                line.nextInt();

                while (line.hasNext()) {
                    edges.add(line.nextInt() - 1);
                    e2++;
                }

                verts.add(edges);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // repeatedly runs the contraction algorithm and remembers the min number of cuts
        int min = Integer.MAX_VALUE;

        for(int iteration = 0; iteration < 200; iteration++) {
            ArrayList<ArrayList<Integer>> v = new ArrayList<>();

            // copies the original list into a new one so it can be contracted
            for (ArrayList<Integer> e : verts) {
                v.add(new ArrayList<>(e));
            }

            // finds the number of cuts in a randomly contracted graph, remembers it if it beats the min
            int cuts = kargerMinCut(v, e2);
            if (cuts  < min) min = cuts;
        }

        System.out.println(min);
    }

    private static int kargerMinCut(ArrayList< ArrayList<Integer> > v, int e2) {
        int vertices = v.size();

        // continues contracting until there are only 2 vertices, every contraction removes 1 vertex
        // e2 is always double the amount of edges since the input list is bi-directional redundant
        while (vertices > 2) {
            e2 = randomContract(v, e2);
            vertices--;
        }

        return e2/2;
    }

    // e2 is initial amount of edges and returns the new amount of edges
    private static int randomContract(ArrayList <ArrayList <Integer> > v, int e2) {
        // finds a random edge and stores the 2 vertices connected by that edge as v1 and v2 where v1 < v2
        int e_num = (int)(Math.random()*e2);
        int current_e = 0;
        int current_v = 0;
        int v1, v2;

        // walks forward until it passes e_num number of edges
        while (current_e < e_num) {
            current_e += v.get(current_v).size();
            current_v++;
        }

        // if current_e landed right on the right one, just takes the next non-empty vertex's edge
        if (current_e == e_num) {
            while (v.get(current_v).size() == 0)
                current_v++;

            v1 = current_v;
            v2 = v.get(current_v).get(0);
        }
        // if it overshoots, steps back a vertex and finds the edge that far away from the end of the edge list
        else {
            current_v--;
            v1 = current_v;
            v2 = v.get(current_v).get(v.get(current_v).size() - (current_e - e_num));
        }

        // points all edges that used to point to v2 to v1 and makes v1 now point to them
        for (int e = 0; e < v.size(); e++) {
            for (int i = 0; i < v.get(e).size(); i++) {
                if (v.get(e).get(i) == v2) {
                    v.get(e).set(i, v1);
                    v.get(v1).add(e);
                }
            }
        }

        // sets v2 to be empty
        v.set(v2, new ArrayList<>());

        //removes self loops, subtracting from the total number of edges
        for (int i = v.get(v1).size()-1; i>=0; i--) {
            if (v.get(v1).get(i) == v1) {
                v.get(v1).remove(i);
                e2--;
            }
        }

        return e2;
    }
}