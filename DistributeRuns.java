import java.io.*;

/**
 * Kurtis-Rae Mokaraka  1256115
 * Cameron Pearce       1257230
 *
 * Takes a stream of sorted runs separated by a delimiter, and distributes them
 * into "run groups"
 */
public class DistributeRuns {

    /** The number of groups to distribute runs to */
    private int numGroups;

    /** An array of writers to write to temporary files */
    private BufferedWriter[] writerArray;

    /** The index of the current writer writing to file */
    private int writerIndex;

    /**
     * Creates a new DistributeRuns object from the parameters passed in
     * 
     * @param numRuns the number of groups to distribute runs into
     */
    public DistributeRuns(int numRuns) {
        this.numGroups = numRuns;
        writerArray = new BufferedWriter[numRuns];
        writerIndex = 0;
    }

    public static void main(String[] args){
        // Usage statement
        if (!(args.length == 1)) {
            System.out.println("Usage:  DistributeRuns <numGroups>");
            System.out.println("numGroups:  The number of groups to separate runs into.");
            System.out.println("Note, needs data from standard input.");
            return;
        }

        // Create a distributeRuns object
        DistributeRuns distributeObject = new DistributeRuns(Integer.parseInt(args[0]));
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        // call distribute on data from system input
        distributeObject.distribute(reader);
    }

    /**
     * Distributes runs from an input stream, and distributes them across a certain
     * number of run group files, as determined by numRuns
     * 
     * @param reader The input stream to read the lines of data from
     */
    public void distribute(BufferedReader reader) {

        try {

            // Populate the writer array with writers
            for (int i = 0; i < numGroups; i++)
                writerArray[i] = new BufferedWriter(new FileWriter(Integer.toString(i)));

            // Create a string to store the next line from input
            String lineIn;

            // While there is still more data to be read
            while ((lineIn = reader.readLine()) != null) {

                // Write the current line to file using the current writer...
                writerArray[writerIndex].write(lineIn);

                // ... with a line break
                writerArray[writerIndex].newLine();

                // If the current line is the delimiter, then switch writer
                if (lineIn.compareTo(",,,,") == 0) {
                    writerIndex++;
                    if (writerIndex == numGroups)
                        writerIndex = 0;
                }
            }

            // Flush the writers and be a tidy kiwi
            for (BufferedWriter writer : writerArray) {
                writer.flush();
                writer.close();
            }
            reader.close();
        }

        // Upon exception, display a nice error message
        catch (IOException e) {
            System.out.print("DistributeRuns file error: ");
            e.printStackTrace();
        }
    }
}