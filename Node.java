/**
 * Kurtis-Rae Mokaraka 1256115
 * Cameron Pearce 1257230
 *
 * An element of the MyMinHeap data structure. Stores a string value and the
 * index of the file stream this node belongs to (used for merging).
 */
public class Node {

    /** The string value of the node */
    private String value;

    /** The index of the file stream this node belongs to. (Used for merging.) */
    private int writerIndex;

    /**
     * Creates a new node from the fields passed in
     * 
     * @param value       The string value of the node
     * @param writerIndex The index of the file stream this node belongs to
     */
    public Node(String value, int writerIndex) {
        this.value = value;
        this.writerIndex = writerIndex;
    }

    /**
     * Gets the string value of the node
     * 
     * @return The string value of the node
     */
    public String getValue() {
        return value;
    }

    /**
     * Gets the index of the file stream this node belongs to. (Used for merging)
     * 
     * @return The index of the file stream this node belongs to
     */
    public int getWriterIndex() {
        return writerIndex;
    }

}