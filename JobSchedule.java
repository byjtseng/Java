import java.io.File;
import java.util.Scanner;
import java.util.Arrays;

public class JobSchedule {
    public static void main(String[] args) {
        Job[] jobs;

        try {
            Scanner in = new Scanner(new File("jobs.txt"));
            jobs = new Job[in.nextInt()];

            for (int i=0; i<jobs.length; i++) {
                jobs[i] = new Job();
                jobs[i].weight = in.nextInt();
                jobs[i].length = in.nextInt();
            }

            System.out.println(jobs[0].weight + " " + jobs[0].length);
            Arrays.sort(jobs);

            for (Job j : jobs) {
                System.out.println(j.weight + " " + j.length);
            }
            long sum = 0;
            long time = 0;

            for (Job j : jobs) {
                time += j.length;
                sum += j.weight * time;
            }

            System.out.println(sum);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    static class Job implements Comparable<Job>{
        int weight;
        int length;

        int dif() {
            return weight - length;
        }
        double ratio() {
            return (double)weight/length;
        }

        /*
        // non-optimal difference solution
        public int compareTo(Job j) {
            if (this.dif() < j.dif()) {
                return 1;
            } else if (this.dif() > j.dif()) {
                return -1;
            }
            else if (this.weight > j.weight) {
                return -1;
            }
            else if (this.weight < j.weight) {
                return 1;
            }
            return 0;
        }
        */
        public int compareTo(Job j) {
            if (this.ratio() < j.ratio()) {
                return 1;
            } else if (this.ratio() > j.ratio()) {
                return -1;
            }
            else if (this.weight > j.weight) {
                return -1;
            }
            else if (this.weight < j.weight) {
                return 1;
            }
            return 0;
        }
    }
}
