/***************************************************

Project name: huffman tree

Filename: h_rybacki_prj.java

Student Name:

Date:

Tasks

1. Build and display a complete Huffman Tree

2. Encode 'tea' and display the resulting bits

3. Decode the bits associated with 'tea' and print the resulting string

Computing requirements:

Language: Java

Platform: Linux

Submission requirements: Tested on UNCG's linux server. One file h_rybacki_prj.java

***************************************************/
/**
 * @TODO:   refactor two arraylists into one
 */

import java.util.ArrayList;
import java.util.PriorityQueue;

public class h_rybacki_prj {

    public static class HuffNode implements Comparable<HuffNode> {
        // fields
        public int value;
        public int weight;
        public HuffNode leftTree;
        public HuffNode rightTree;
        public HuffNode parent;

        // constructors
        public HuffNode() {
            parent = null;
        }

        public HuffNode( int v, int w, HuffNode lTree, HuffNode rTree, HuffNode par ) {
            value = v;
            weight = w;
            leftTree = lTree;
            rightTree = rTree;
            parent = par;
         }

        @Override
        public int compareTo(HuffNode rhs) {
            return weight - rhs.weight;
        }

        @Override
        public String toString() {
            String str = "";
            str += this.value;
            return str;
        }
    }

    // object representing a huffman tree
    public static class HuffTree {
        // fields
        private int size = 0;
        private HuffNode root = new HuffNode();
        private PriorityQueue<HuffNode> huffQueue = new PriorityQueue();
        int tableSize = 0;
        public ArrayList<String> pathTable = new ArrayList();
        public ArrayList<Character> valueTable = new ArrayList();

        // constructor
        public HuffTree(int[] freq, char[] code) {
            // get the counts
            this.size = freq.length;

            // throw exception if arrays are different sizes
            if (freq.length != code.length) {
            throw new UnsupportedOperationException("Error: Character and code length mismatch.");
            }

            // build huffQueue from frequencies given
            for (int i = 0; i < this.size; i++) {
                huffQueue.offer(new HuffNode(code[i], freq[i], null, null, null));
            }

            // build huffman tree from queue
            createTree();

            // build code table from huffman tree
            createTable(this.root, "");
        }

        // setters/getters

        /**
         * creates Huffman Tree from frequencies and values
         *
         * @param null
         */
        private void createTree() {
            // while elements remain in huffQueue, add to tree
            while (huffQueue.size() > 1) {
                // pop off two minimum elements in huffQueue
                HuffNode tempL = huffQueue.poll();
                HuffNode tempR = huffQueue.poll();
                // create root for two minimum elements and build tree
                HuffNode parent = new HuffNode(0, tempL.weight+tempR.weight, tempL, tempR, null);
                tempL.parent = parent;
                tempR.parent = parent;
                // add new tree back in huffQueue
                huffQueue.offer(parent);
                this.size++;
            }
            // set HuffTree root to remaning element in huffQueue
            this.root = huffQueue.peek();
        }

        /**
         * creates code table for a huffman tree
         * @param HuffNode -- root for tree, string -- for building paths
         */
        private void createTable(HuffNode curr, String str) {
            // if iterator is null, return
            if (curr == null) return;
            // else if leaf, display path and value
            if (curr.leftTree == null && curr.rightTree == null) {
                //System.out.println(str +  ":" + curr.value);
                char tempChar;
                if (curr.value == 32)
                    tempChar = ' ';
                if (curr.value == 10)
                    tempChar = 'n';
                else 
                    tempChar = (char)curr.value;
                this.valueTable.add(tempChar);
                this.pathTable.add(str);
                this.tableSize++;
            }

            // add 0 if before moving to left child
            str += "0";
            // recursively call in pre-order
            createTable(curr.leftTree, str);
            // adjust path and add 1 before moving to right child
            str = str.substring(0, str.length()-1);
            str += "1";
            createTable(curr.rightTree, str);
        }

        /**
         * display given huffman tree using pre-order traversal
         * @param HuffNode -- root of tree to be displayed
         */
        // global variable used for representing 'levels' of tree
        String tacks = "";
        public void getTree(HuffNode curr) {
            // if iterator is null, return
            if (curr == null) return;
            // else if leaf, display level, weight, and value
            if (curr.leftTree == null && curr.rightTree == null) {
                if (curr.value == 32)
                    System.out.println(tacks + curr.weight + ": sp");
                if (curr.value == 10)
                    System.out.println(tacks + curr.weight + ": nl ");
                else
                    System.out.println(tacks + curr.weight + ": " + (char)curr.value);
            }
            // else display level and weight
            else
                System.out.println(tacks + curr.weight);

            // increment level marker
            tacks += "- ";
            // recursively call in pre-order
            getTree(curr.leftTree);
            getTree(curr.rightTree);
            // decrement level marker
            tacks = tacks.substring(0, tacks.length()-2);
        }

        /**
         * returns size of a given huffman tree
         * @param HuffTree - to obtain size of
         * @return int -- size of tree
         */
        public int getSize() { return this.size; }

        /**
         * returns encoded bits for a given string
         * @param String -- to be encoded
         * @return String -- encoded version of original string
         */
        public String encode(String input){
            // create empty string to hold code
            String str = "";
            // iterate through give nstring
            for (int x = 0; x < input.length(); x++) {
                // iterate through code tables
                for (int i = 0; i < valueTable.size(); i++) {
                    // if char in string matches code in table, add path to string
                    if (valueTable.get(i) == input.charAt(x)) {
                        str += pathTable.get(i);
                        //System.out.println(pathTable.get(i));
                    }
                }
            }
            return str;
        }

        /**
         * returns decoded string for a given set of bits
         * @param String -- bits to be decoded
         * @return String -- decoded version of bits
         */
        public String decode(String bits) {
            // create empty string to hold decoded message
            String decodedStr = "";

            // iterate through bits
            for (int i = 0; i < bits.length(); i++) {
                if (!getChar(bits.substring(0, i+1)).equals("")) {
                    decodedStr += getChar(bits.substring(0, i+1));
                    bits = bits.substring(i+1);
                    //System.out.println("Bits is now: " + bits);
                    i = 0;
                }
            }
            return decodedStr;
        }

        /**
         * returns character for a given set of bits
         * @param String -- bits to be checked
         * @return String -- character associated with bits if any
         */
        private String getChar(String bits) {
            //System.out.println("getChar() called on: " + bits);
            String character = "";
                for (int i = 0; i < pathTable.size(); i++) {
                    //System.out.println("Checking bits: " + bits + " against path: " + pathTable.get(i));
                    if (pathTable.get(i).equals(bits))
                        character = valueTable.get(i).toString();
                }
            return character;
        }
    }

    // driver program -- main
        public static void main(String[] args) {
            // fields
            int freq[] = {10, 15, 12, 3, 4, 13, 1};
            char code[] = {'a', 'e', 'i', 's', 't', ' ', '\n'};

            // build Huffman Tree using given codes/frequencies
            HuffTree hTree = new HuffTree(freq, code);

            // display contents of Huffman Tree in Pre-Order Traversal
            System.out.println("Display Tree:");
            HuffNode curr = hTree.root;
            hTree.getTree(curr);
            System.out.println("");

            // display code tables
            System.out.println("Code Tables:");
            System.out.println(hTree.pathTable.toString());
            System.out.println(hTree.valueTable.toString());
            System.out.println("");

            // encode 'tea'
            System.out.println("Encode 'tea': " + hTree.encode("tea"));
            System.out.println("");        

            // decode 'tea' -- 1100, 10, 111
            System.out.println("Decode '110010111': " + hTree.decode("110010111"));
            System.out.println("Decode '10': " + hTree.decode("10"));
        }
}