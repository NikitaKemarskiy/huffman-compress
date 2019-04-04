import com.huffman.HuffmanTree;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        parseArgs(args);
    }

    private static final File TREE_FILE = new File("./data", "tree.dat");

    private static void parseArgs(String[] args) {
        boolean serialize = false;
        boolean deserialize = false;
        File inputFile = null;
        File treeFile = null;
        String str = null;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-s": // Serialize
                case "--serialize": {
                    serialize = true;
                    break;
                }
                case "-d": // Deserialize
                case "--deserialize": {
                    deserialize = true;
                    break;
                }
                case "-i": // Pass input file
                case "--input": {
                    if (i == args.length - 1) {
                        break;
                    }
                    inputFile = new File(args[++i]);
                    break;
                }
                case "-t": // Pass input file
                case "--tree": {
                    if (i == args.length - 1) {
                        break;
                    }
                    treeFile = new File(args[++i]);
                    break;
                }
                default: { // Passed string
                    str = args[i];
                    break;
                }
            }
        }

        if (str == null && inputFile == null) {
            return;
        } else if (str != null) {
            if (str.charAt(0) == '"' && str.charAt(1) == '"') {
                str = str.substring(1, str.length() - 1);
            }
        }

        if (serialize && inputFile == null) { // Serialize from console text
            serialize(str);
        } else if (serialize && inputFile != null) { // Serialize from input file
            serialize(inputFile);
        } else if (treeFile == null) { // Tree file wasn't passed
            return; // End the method
        } else if (deserialize && inputFile == null) { // Deserialize from console text
            deserialize(str, treeFile);
        } else if (deserialize && inputFile != null) { // Deserialize from input file
            deserialize(inputFile, treeFile);
        }
    }

    private static void serialize(String input) {
        try { // Create new tree and data files if they don't exist
            TREE_FILE.createNewFile();
        } catch (IOException err) {
            System.out.println(err.getMessage());
            System.exit(1);
        }
        HuffmanTree tree = new HuffmanTree(input);
        try (FileWriter treeWriter = new FileWriter(TREE_FILE)) {
            String serialized = tree.serialize(); // Serialized string
            treeWriter.write(serialized); // Write serialized tree to a TREE_FILE
            System.out.println(tree.encode(input)); // Print encoded input data to the output stream
        } catch (IOException err) {
            System.out.println(err.getMessage());
            System.exit(1);
        }
    }

    private static void serialize(File inputFile) {
        String input = "";
        try (FileReader reader = new FileReader(inputFile)) {
            int num = -1;
            while ((num = reader.read()) != -1) { // Read an input file
                input += (char) num;
            }
        } catch (IOException err) {
            System.out.println(err.getMessage());
            System.exit(1);
        }

        serialize(input); // Serialize input file
    }

    private static void deserialize(String input, File treeFile) {
        HuffmanTree tree = new HuffmanTree();
        String str = "";
        try (FileReader treeReader = new FileReader(treeFile)) {
            int num = -1;
            while ((num = treeReader.read()) != -1) { // Read serialized tree file
                str += (char) num;
            }
            tree.deserialize(str); // Deserialize this tree
            System.out.println(tree.decode(input)); // Decode an input data
        } catch (IOException err) {
            System.out.println(err.getMessage());
            System.exit(1);
        }
    }

    private static void deserialize(File inputFile, File treeFile) {
        String input = "";
        try (FileReader reader = new FileReader(inputFile)) {
            int num = -1;
            while ((num = reader.read()) != -1) { // Read an input file
                input += (char) num;
            }
        } catch (IOException err) {
            System.out.println(err.getMessage());
            System.exit(1);
        }

        deserialize(input, treeFile); // Deserialize an input file
    }
}
