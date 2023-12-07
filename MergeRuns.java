import java.io.*;

/**
 * Kurtis-Rae Mokaraka  1256115
 * Cameron Pearce       1257230
 * 
 * Handles a k-way external merge sort from the standard input passed in
 */
public class MergeRuns {

    // Arrays for readers, and files for those readers
    private BufferedReader[] textReaderArray;
    private File[] fileArray;
    
    // Counter for the movement of whole runs
    private int runMovement;

    // Variable to store k number of runs
    private int runGroups;

    // And a heap to merge with
    private MyMinHeap heap;

    /**
     * Constructor for the MergeRuns Class
     * 
     * @param runNumber The k number of runs to merge at once
     */
    public MergeRuns(int runNumber){
        runGroups = runNumber;
        textReaderArray = new BufferedReader[runGroups];
        // We make the fileArray one larger, as the end spot is where we are going to store the file we write to
        fileArray = new File[runGroups + 1];
        heap = new MyMinHeap(runGroups < 0 ? 2 : runGroups);
        runMovement = 0;
    }

    /**
     * Entry to the program
     * Validates the needed run-group number, takes data from standard input,
     * distributes them into runs, then calls the merge method on them
     * 
     * @param args
     */
    public static void main(String[] args) {

        // Ensure the int is valid
        int temp = 0;
        try {
            temp = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            //System.err.println("The number entered was not a positive integer");
            return;
        }

        // Initialise the distributeRuns object
        DistributeRuns distributeObject = new DistributeRuns(temp);
        // Reader to read in from system input
        BufferedReader inputStream = new BufferedReader(new InputStreamReader(System.in));
        // Call the distribute method, passing the input stream
        distributeObject.distribute(inputStream);

        // After, create a merge object
        MergeRuns mergeObject = new MergeRuns(temp);
        // And MERGE
        mergeObject.merge();
    }

    /**
     * merge method
     * Contains the logic to perform a k-way external merge sort from a number of files 
     * that contain 1 or more runs each.
     * Outputs the result to standard output.
     * 
     */
    private void merge() {

        try {
            // Loop to initialise the File array with files created from DistributeRuns
            // And the scanners attached to each of those files
            for (int i = 0; i < runGroups; i++){
                fileArray[i] = new File(String.valueOf(i));
                textReaderArray[i] = new BufferedReader(new FileReader(fileArray[i]));
            }
            // An extra file to use to store our run output
            fileArray[runGroups] = new File(String.valueOf(runGroups));
            // Writer for the output file
            BufferedWriter outputStream = new BufferedWriter(new FileWriter(fileArray[runGroups]));
            
            // Initialise our string variable that we will use to read from our input files
            String lineIn = "";

            // Populate the heap with nodes that point to each file
            for (int i = 0; i < runGroups; i++){
                // Get first line of each file
                lineIn = textReaderArray[i].readLine();
                // Insert node with that first line, as well as reader index (file object descriptor) that points to its file
                heap.insert(new Node(lineIn, i));
            }
            
            // Variables to track how many files have reached their end of file (EOF), and which was most recent to reach EOF
            int endOfFileCount = 0;
            int endOfFileIndex;

            // Loop until break
            while(true){
                
                // Reset the EOF tracked
                endOfFileIndex = -1;
                
                // Merge for as long as there is more than one Node in the heap
                while(heap.getNodeCount() > 1){

                    // Get the root node's file scanner index
                    int rootIndex = heap.peek().getWriterIndex();
                    // Read the next line of the root node's file
                    lineIn = textReaderArray[rootIndex].readLine();
                    // Declare a node
                    Node node;

                    // If the next line is not an end of run delimiter
                    if (lineIn.compareTo(",,,,") != 0) {
                        // Replace the root with a node of the new value, same index
                        // And store the old root node
                        node = heap.replace(new Node(lineIn, rootIndex));
                        // Increment run move counter, as a whole run has now been moved
                        runMovement++;
                    }
                    // Otherwise, if we have reached the end of a run
                    else {
                        // Pop the root
                        node = heap.remove();
                        // Get the next line (skipping over the delimiter)
                        lineIn = textReaderArray[rootIndex].readLine();
                        // If not EOF, then will be apart of next run
                        if (lineIn != null){
                            // Backup the next line to the heap as a part of the next run
                            heap.backup(new Node(lineIn, rootIndex));
                        }
                        // Otherwise, we have reached EOF for this node
                        else {
                            // If so, do -nothing- to return the node to the heap. This way, the file that the 
                            // root node onced pointed to Leaves the heap, and will no longer be read from.
                            
                            // Increment the counter for the number of files emptied
                            endOfFileCount++;
                            // And store this now empty file for later use as output
                            endOfFileIndex = rootIndex;
                        }
                    }
                    // Whatever the result, output the value of the old root node
                    outputStream.write(node.getValue());
                    outputStream.newLine();
                }

                // When only one root remains, we write the rest of it's run
                // First pop the root and store it's reader index
                Node node = heap.remove();
                int lastRunIndex = node.getWriterIndex();
                // Output the node's value
                outputStream.write(node.getValue());
                outputStream.newLine();

                // Quick reset of lineIn
                lineIn = "";
                // While there are remaining lines of this run
                while (lineIn != null && lineIn.compareTo(",,,,") != 0){
                    // Read the file that the root pointed to
                    lineIn = textReaderArray[lastRunIndex].readLine();
                    // Output what we have
                    outputStream.write(lineIn);
                    outputStream.newLine();
                }

                // After writing the last of a run, increment counter to show run movement
                runMovement++;

                // After, we then need to check if there are still lines to read (not EOF)
                // So get a line
                lineIn = textReaderArray[lastRunIndex].readLine();
                // Check if its null (ie truly end of file)
                if (lineIn == null){
                    // Increment the end of file counter
                    endOfFileCount++;
                    // Select that file as the next output
                    endOfFileIndex = lastRunIndex;
                }
                // Otherwise, we've read a value, therefore there are more runs in this file
                else if (lineIn != null){
                    // So back it up to the heap as a new node
                    heap.backup(new Node(lineIn, lastRunIndex));
                }

                // Now check if we have only one file remaining (we have closed every starting file (rungroups) and are left only with the output file)
                if (endOfFileCount == runGroups){
                    // Flush and close the output
                    outputStream.flush();
                    outputStream.close();
                    // Now we need to write our combined merge from the output file to system out
                    outputStream = new BufferedWriter(new OutputStreamWriter(System.out));
                    BufferedReader reader = new BufferedReader(new FileReader(fileArray[runGroups]));

                    // Loop through the output file, writing to output
                    while ((lineIn = reader.readLine()) != null && lineIn.compareTo(",,,,") != 0){
                        outputStream.write(lineIn);
                        outputStream.newLine();
                    }
                    // Flush and close the IO's
                    outputStream.flush();
                    outputStream.close();
                    reader.close();
                    // And break, as we are done
                    break;
                }
                // Otherwise, check that a file has been closed (endOfFileIndex will not be -1 if a file has been closed)
                else if (endOfFileIndex != -1){
                    // Close the output
                    outputStream.flush();
                    outputStream.close();
                    // Close the reader for the recently emptied file
                    textReaderArray[endOfFileIndex].close();
                    // Pull out the output file into a temp variable
                    File tempFile = fileArray[runGroups];
                    // Set the output file to be the endOfFileIndex file
                    fileArray[runGroups] = fileArray[endOfFileIndex];
                    // Close the reader pointing to the endOfFileIndex
                    textReaderArray[endOfFileIndex].close();
                    // Then save the temp file in the place of the endOfFileIndex, making the old output file an input file
                    fileArray[endOfFileIndex] = tempFile;
                    // Make a new reader pointing to that file
                    textReaderArray[endOfFileIndex] = new BufferedReader(new FileReader(fileArray[endOfFileIndex]));
                    // Decrement end of File Count (as we have opened a new file after closing an old one, thus the number of open files remains the same)
                    endOfFileCount--;
                    // Make a new output stream writing to the file stored at the end of the fileArray (the new output file)
                    outputStream = new BufferedWriter(new FileWriter(fileArray[runGroups]));

                    // Then reintroduce a node into the heap with the new input file as it's reader index
                    heap.insert(new Node(textReaderArray[endOfFileIndex].readLine(), endOfFileIndex));

                }

                // Check there are nodes of next run stored in the array
                // If so, load them for merging
                if (!heap.isArrayEmpty()) 
                    heap.restore();
            }

            // Loop for closing out IO objects and deleting files
            for (int i = 0; i < runGroups; i++){
                textReaderArray[i].close();
                fileArray[i].delete();
            }
            // And also delete our extra output file
            fileArray[runGroups].delete();

            // Finally, print the runMovement counter to show the number of times a run moved between files
            System.err.println("Runs moved: " + runMovement);

        } catch (IOException e){
            System.out.print("MergeRuns file error: ");
            e.printStackTrace();
        }
    }

}