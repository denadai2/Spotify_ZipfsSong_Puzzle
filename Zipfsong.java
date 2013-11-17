

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 *
 * @author denadai2
 */
public class Zipfsong {

    private static Scanner stdin = null;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int n, m;

        /**
         * Assumes all input is valid, as no error handling requirements were
         * stated. Removed Scanner and regular expressions for performance
         * reasons.
         */
        try {
            /**
             * Variables: - line contain a line in this format "[number]
             * [string/number]" ex: "30 one" - stdin is the standard input
             */
            String[] line;
            stdin = new Scanner(System.in);

            /**
             * Read from stdin
             */
            line = getInputLine();
            n = Integer.parseInt(line[0]);
            m = Integer.parseInt(line[1]);

            /**
             * Read from stdin and calculating the songs' quality
             */
            PriorityQueue<Song> heap = new PriorityQueue<>(n, songComparator);
            long q;
            for (int i = 0; i < n; i++) {
                line = getInputLine();
                q = Long.parseLong(line[0]) * (i + 1);
                heap.add(new Song(i, line[1], q));
            }

            /**
             * Output
             */
            StringBuilder output = new StringBuilder();
            int i = 0;

            while (i < m) {
                Song extracted = heap.poll();

                /**
                 * If there are songs with the same quality, we need to
                 * take care about the initial order of the songs, so it is
                 * needed to create another priority queue which takes care of
                 * that.
                 */
                PriorityQueue<Song> sameQualityHeap = new PriorityQueue<>(11, sameQualitySongComparator);
                while (heap.size() > 0 && extracted.quality == heap.peek().quality) {
                    sameQualityHeap.add(heap.poll());
                }

                if (sameQualityHeap.size() > 0) {
                    sameQualityHeap.add(extracted);
                    while (sameQualityHeap.size() > 0 && i < m) {
                        output.append(sameQualityHeap.poll().name).append('\n');
                        i++;
                    }
                } else {
                    output.append(extracted.name).append('\n');
                    i++;
                }
            }

            System.out.print(output.substring(0, output.length() - 1));


        } catch (Exception e) {
            System.out.println("Exception caught");
            System.out.println(e.getStackTrace());
        } finally {
            if (stdin != null) {
                stdin.close();
            }
        }


    }

    /**
     * Returns the two objects read from stdin
     *
     * @return two strings who correspond to the two object read from stdin
     */
    private static String[] getInputLine() {
        String line;
        String[] tokens = new String[2];

        if (stdin.hasNextLine() && !(line = stdin.nextLine()).equals("")) {
            tokens = line.split(" ");
        }

        return tokens;
    }

    /**
     * Class that stores all the interesting properties of a Song
     */
    public static class Song {

        public long quality;
        public int index;
        public String name;

        public Song(int i, String name, long q) {
            super();
            this.index = i;
            this.quality = q;
            this.name = name;
        }
    }
    /**
     * Comparator to compare two Songs
     */
    public static Comparator<Song> songComparator = new Comparator<Zipfsong.Song>() {
        @Override
        public int compare(Song p1, Song p2) {
            if (p1.quality == p2.quality) {
                return (p1.index < p2.index) ? 1 : (p1.index > p2.index ? -1 : 0);
            }

            return (p1.quality < p2.quality) ? 1 : -1;
        }
    };
    /**
     * Comparator to compare two Songs with the same quality
     */
    public static Comparator<Song> sameQualitySongComparator = new Comparator<Zipfsong.Song>() {
        @Override
        public int compare(Song p1, Song p2) {
            return (p1.index > p2.index) ? 1 : ((p1.index < p2.index) ? -1 : 0);
        }
    };
}
