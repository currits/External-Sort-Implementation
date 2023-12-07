import java.io.*;

/**
 * Kurtis-Rae Mokaraka  1256115
 * Cameron Pearce       1257230
 * 
 * Uses Replacement selection to sort lines of System input into a number of
 * lexographically sorted runs. Outputs the sorted runs to System output with a
 * delimiter
 */
public class CreateRuns {

    /** The delimiter symbol/line that separates runs */
    private static final String DELIMITER = ",,,,";

    /** Entry point into the program */
    public static void main(String[] args) {

        // Usage statement
        if (!(args.length == 1)) {
            System.out.println("Usage: CreateRuns <heapSize>");
            System.out.println("heapSize: The maximum size of the heap");
            return;
        }

        // Create a variable to store the heap size passed in
        int heapSize;

        // Try to convert the param to an int
        try {
            heapSize = Integer.parseInt(args[0]);
        }

        // Otherwise, print an error and return
        catch (Exception e) {
            System.out.println("The parameter entered was not an integer");
            return;
        }

        try {

            // Create a reader and writer for standard input and output
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

            // Get rid of the pesky "?" at the start of the stream for some reason...
            // (Maybe get rid of this later...?)
            //reader.read();

            // Create a heap with the size passed in
            MyMinHeap heap = new MyMinHeap(heapSize < 0 ? 31 : heapSize);

            // Populate the heap with lines from the stream, maintaining heap order
            heap.load(reader);

            // Create a string to hold the most recent line printed to the output
            String recentOut = null;

            // While the heap is not empty
            while (!heap.isEmpty()) {

                // If "recent = null", or if "top of heap >= recent", then print top of heap
                if (recentOut == null || heap.peek().getValue().compareTo(recentOut) >= 0) {

                    // Pop the node from the stack, feeding in a new line from the stream
                    Node node = heap.replace(new Node(reader.readLine(), 0));

                    // Write the node's value to the output stream...
                    writer.write(node.getValue());

                    // ... with a line break
                    writer.newLine();

                    // Make recent out the value that was just printed to output
                    recentOut = node.getValue();
                }

                // Otherwise, save the node to the heap
                else
                    heap.backup(heap.remove());

                // If the heap has run out of nodes
                if (heap.isEmpty()) {

                    // Load the saved nodes
                    heap.restore();

                    // Print the delimiter...
                    writer.write(DELIMITER);

                    // ... with a line break
                    writer.newLine();

                    // Make recent out null
                    recentOut = null;
                }
            }

            // Flush the stream writer
            writer.flush();

            // Be a tidy kiwi
            reader.close();
            writer.close();
        }

        // Upon exception, display a nice error message
        catch (Exception e) {
            System.out.print("CreateRuns error: ");
            e.printStackTrace();
        }
    }
}