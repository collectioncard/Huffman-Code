import java.util.*;
/**
 * This class handles the actual compression and other operations required for this project. It requires the Main class
 * in order to operate.
 *
 * This project 'compresses' text and stores it inside of a tree.
 * It then 'decompresses' the text and relays the information back to the user.
 *
 * This class takes in a string from a user, shows the huffman code and finally prints the original string back out.
 *
 * 2020 Thomas Wessel
 */
public class HuffmanEncoding {

    /////Global Variables/////

    private final Node rootNode;
    private final Map<Character, String> codeTable = new TreeMap<>();
    private final String encodedMessage;

    /////Constructors/////

    /**
     * creates a HuffmanEncoding object and encodes the string provided.
     * @param toBeEncoded The string to be encoded
     */
    public HuffmanEncoding(char[] toBeEncoded){
        /*algorithm:
       1. add values to a map to get the number of times that character appears in the char array. This will be the 'frequency' of the character in the Node object
       2. Once all values are added, create node objects out of each entry in the map. Add these nodes to a priority queue.
       3. The smaller frequency nodes will be at the front (hopefully) Remove two nodes and make a tree from them. The root node will have a blank char field and a frequency of the sum of its children
       4. Insert this tree back into the queue
       5. repeat steps 3 & 4 until the queue has only one item left in it. This will be the complete tree.
        */

        Map<Character, Integer> frequencyMap = new HashMap<>();
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();

        //step 1
        for(char character : toBeEncoded){
            //getOrDefault returns the value that a char has or 0 if not found. Very helpful.
            frequencyMap.put(character, frequencyMap.getOrDefault(character, 0) + 1);
        }
        //step 2
        for(Map.Entry<Character, Integer> entry : frequencyMap.entrySet()){
            priorityQueue.add(new Node(entry.getValue(), entry.getKey()));
        }
        //step 3 - 5
        while(priorityQueue.size() > 1){
            Node temp1 = priorityQueue.remove();
            Node temp2 = priorityQueue.remove();
            Node parent = new Node();
            parent.frequency = temp1.frequency + temp2.frequency;
            parent.setLeftLink(temp1);
            parent.setRightLink(temp2);
            priorityQueue.add(parent);
        }
        //now the queue only has one item. We can go ahead and change the root reference to it
        rootNode = priorityQueue.remove();

        //we also need to generate the code table at this point - this is recursive, so this will be the helper method
        generateCodeTable(rootNode, "");

        //finally, encode the message
        StringBuilder finishedString= new StringBuilder();
        for(char chars : toBeEncoded){
            finishedString.append(codeTable.get(chars));
        }
        encodedMessage = finishedString.toString();

    }

    /**
     * Allows this object to be constructed with a string. Calls other constructor in this class
     * @param toBeEncoded The string to be encoded.
     */
    public HuffmanEncoding(String toBeEncoded){
        this(toBeEncoded.toCharArray());
    }

    /////Private Methods/////

    //This method, while mostly my own, did have some help from stack overflow
    private void generateCodeTable(Node temp, String codeBuffer){

        if(temp.getData() == '\u0000'){

            codeBuffer += "0";
            generateCodeTable(temp.getLeftLink(), codeBuffer);
            codeBuffer = codeBuffer.substring(0, codeBuffer.length() - 1);

            codeBuffer += "1";
            generateCodeTable(temp.getRightLink(),codeBuffer);
            codeBuffer = codeBuffer.substring(0, codeBuffer.length() - 1);

        }else{
            codeTable.put(temp.getData(), codeBuffer);
        }
    }

    /////Public Methods/////

    /**
     * Returns a decoded string passed into it if the string only consists of the letters used to instantiate this object.
     * @param encodedString The string to decode in binary
     * @return A decoded string
     * @throws IllegalArgumentException This method can only take a string of binary 1s and 0s in
     */
    public String decode(String encodedString) throws IllegalArgumentException{
        StringBuilder result = new StringBuilder();
        Node temp = rootNode;

        for(int i = 0; i < encodedString.length();){
            //if we see data in the current node, then it is what we need to add to the string
            if(temp.getData() != '\u0000'){
                result.append(temp.getData());
                //we also need to reset the tempNode to root
                temp = rootNode;

            }else{
                if(encodedString.charAt(i) == '0'){
                    temp = temp.getLeftLink();
                }else if(encodedString.charAt(i) == '1'){
                    temp = temp.getRightLink();
                }else{
                    throw new IllegalArgumentException("Encoded string must only be comprised of 1s or 0s");
                }
                i++;
            }
        }
        //we also need to add the final character
        result.append(temp.getData());
        //at this point, we should have a complete decoded string.
        return result.toString();
    }

    /**
     * Returns the encoded version of the string the object was instantiated with.
     * @return a text string
     */
    public String getEncodedMessage(){
        return encodedMessage;
    }

    /**
     * Prints the code table in the system output stream.
     */
    public void printCodeTable(){
        System.out.println("\nCode table:");
        System.out.println("*****************");
        for(Map.Entry<Character, String> entry : codeTable.entrySet()){
            System.out.println("Character: " + entry.getKey() + " has a value of: " + entry.getValue());
        }
        System.out.println("*****************\n");
    }


    /////Node Class/////

    /**
     * This class implements Comparable because we need to be able to use the default priority queue provided by
     * java util. We are required to add a method that compares the data contained within to another Node object
     */
    private class Node implements Comparable<Object>{
        private Node leftLink;
        private Node rightLink;
        private char data;
        private int frequency;

        public Node(int frequency, char data){
            this.frequency = frequency;
            this.data = data;
        }

        public Node(){

        }

        ///*** setters and getters ***///
        public Node getLeftLink() {
            return leftLink;
        }

        public Node getRightLink() {
            return rightLink;
        }

        public char getData() {
            return data;
        }

        public void setData(char data) {
            this.data = data;
        }

        public void setLeftLink(Node leftLink) {
            this.leftLink = leftLink;
        }

        public void setRightLink(Node rightLink) {
            this.rightLink = rightLink;
        }

        @Override
        public int compareTo(Object o) {
            Node node = (Node) o;
            return (this.frequency - node.frequency);
        }
    }
}
