import java.io.BufferedReader;

/**
 * Kurtis-Rae Mokaraka   1256115
 * Cameron Pearce        1257230
 * 
 * A data structure that can hold strings as nodes in heap order
 */
public class MyMinHeap {

    /** An array of nodes, in heap order */
    private Node[] heapArray;

    /** The position of the next available spot in the heap */
    private int nextPos;

    /** The next available position a node can be "saved" to */
    private int nextSave;

    /**
     * Initialises a min heap with the size passed in
     * 
     * @param size The maximum size of the heap
     */
    public MyMinHeap(int size) {
        heapArray = new Node[size + 1];
        nextPos = 1;
        nextSave = size;
    }

    /**
     * Adds a new node, without maintaining heap order
     * 
     * @param node The node to be added to the heap
     */
    private void add(Node node) {

        // If the input is invalid, or there is no room, don't do anything
        if (node == null || node.getValue() == null || nextPos > nextSave)
            return;

        // Insert the node into the heap
        heapArray[nextPos] = node;

        // Increment the "next position"
        nextPos++;
    }

    /**
     * Adds a new node, while maintaining heap order
     * 
     * @param node The node to be added to the heap
     */
    public void insert(Node node) {

        // Add the node to the heap
        add(node);

        // Upheap the node into the correct position
        upheap(nextPos - 1);
    }

    /**
     * Removes the root, and maintains heap order
     * 
     * @return The root node
     */
    public Node remove() {

        // If the heap is empty, return null
        if (nextPos == 1)
            return null;

        // Else, save the root to be returned later
        Node root = heapArray[1];

        // Swap the root with the last node
        swap(1, nextPos - 1);

        // Delete the last node (old root)
        heapArray[nextPos - 1] = null;

        // Decrement the "next position"
        nextPos--;

        // Downheap the new node into the correct position
        downheap(1);

        // Return the old root
        return root;
    }

    /**
     * Removes the root, inserts a new node, maintains heap order, and returns the
     * previous node
     * 
     * @param node The node to be pushed to the heap
     */
    public Node replace(Node node) {

        // If the input is invalid, just pop
        if (node == null || node.getValue() == null)
            return remove();

        // Get the root node to be returned later
        Node root = heapArray[1];

        // Insert the node into the heap
        heapArray[1] = node;

        // Downheap the node into the correct position
        downheap(1);

        // Return the original root
        return root;
    }

    /**
     * Returns the root of the heap
     * 
     * @return The root of the heap
     */
    public Node peek() {
        return heapArray[1];
    }

    /**
     * Fills the heap with lines from a stream until heap full, or stream empty
     * 
     * @param reader The input stream to read lines from
     */
    public void load(BufferedReader reader) {

        try {

            // Get the next line from the reader
            String line = "";

            // While the next line in the stream is not null, and the heap is not full
            while (line != null && nextPos <= nextSave) {

                // Get the next line
                line = reader.readLine();

                // Add the node to the heap
                heapArray[nextPos] = new Node(line, 0);

                // Increment the "next position"
                nextPos++;
            }
        }

        // Upon exception, display a nice error message
        catch (Exception e) {
            System.out.print("CreateRuns file error: ");
            e.printStackTrace();
        }

        // Reheap the heap into heap order
        reheap();
    }

    /** Reheaps the heap into heap order */
    public void reheap() {

        // Downheap every node, starting from the last to the root
        for (int i = (nextPos - 1) / 2; i > 0; i--)
            downheap(i);
    }

    /** Saves the passed node to the end of the heap for use later 
     * 
     * @param node The node to save to the end of the heap
    */
    public void backup(Node node) {

        // Save the passed root to the next available save position after the heap
        heapArray[nextSave] = node;

        // Decrement the next save
        nextSave--;
    }

    /** Loads the saved nodes back into the heap, and reheaps */
    public void restore() {

        // While next save is not at the end of the heap array
        while (nextSave < heapArray.length - 1) {

            // Free up the space of the last saved node
            nextSave++;

            // Add the node to the heap
            add(heapArray[nextSave]);
        }

        // Reheap the heap into heap order
        reheap();
    }

    /**
     * Checks if the heap is empty
     * 
     * @return True if the heap is empty, otherwise false
     */
    public boolean isEmpty() {
        return nextPos == 1;
    }

    /**
     * Upheaps starting from the node at the position passed in
     *
     * @param currPos
     */
    private void upheap(int currPos) {

        // While the current node is not the root
        while (currPos != 1) {

            // If the parent is smaller or equal to the current node, return
            if (currPos / 2 == getSmaller(currPos / 2, currPos))
                return;

            // Else, swap the child with its parent
            swap(currPos, currPos / 2);

            // Make the parent node the current node
            currPos /= 2;
        }
    }

    /** Downheaps to maintain heap order, starting from the root node */
    private void downheap(int currPos) {

        // Starting at the root, iterate through itself and its smallest children
        while (true) {

            // Get the position of the smallest of the current node and its children
            int smallPos = getSmaller(getSmaller(currPos, currPos * 2), currPos * 2 + 1);

            // If the current node is smaller or equal to its smallest child, return
            if (currPos == smallPos)
                return;

            // Else, swap the parent with its smallest child
            swap(currPos, smallPos);

            currPos = smallPos;
        }
    }

    /**
     * Compares the values of two nodes in the heap, and returns the position of the
     * "lowest". If the values are identical, or if pos2 is invalid, pos1 is
     * returned. Assumes that pos1 is always valid (smaller than nextPos, but not 0)
     * 
     * @param pos1 The position of the first node (usually a parent node)
     * @param pos2 The position of the second node (usually a child node)
     * @return The position of the first valid node with the smallest string
     */
    private int getSmaller(int pos1, int pos2) {

        // If pos2 is not valid, return pos1
        if (pos2 >= nextPos)
            return pos1;

        // If the node at pos1 <= the node at pos2, return pos1
        if (pos2 >= nextPos || heapArray[pos1].getValue().compareTo(heapArray[pos2].getValue()) <= 0)
            return pos1;

        // Else, return pos2
        return pos2;
    }

    /**
     * Swaps the nodes at the positions passed in
     * 
     * @param pos1 The position of the first node to be swapped
     * @param pos2 The position of the second node to be swapped
     */
    private void swap(int pos1, int pos2) {

        // Swap the two nodes at the given positions, using heapArray[0] as a temp
        heapArray[0] = heapArray[pos1];
        heapArray[pos1] = heapArray[pos2];
        heapArray[pos2] = heapArray[0];
    }

        /**
     * Gets the number of nodes currently in the heap
     * 
     * @return The number of nodes in the heap
     */
    public int getNodeCount() {
        return nextPos - 1;
    }

    /**
     * Checks if the heap is empty AND there are no saved nodes
     * 
     * @return True if the heap array is empty, otherwise false
     */
    public boolean isArrayEmpty() {
        return nextPos == 1 && nextSave == heapArray.length - 1;
    }
}