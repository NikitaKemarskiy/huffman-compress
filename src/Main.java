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

    private static final File DATA_FILE = new File("./data", "data.dat");

    private static void parseArgs(String[] args) {
        boolean serialize = false;
        boolean deserialize = false;
        String str = null;

        for (String item : args) {
            switch (item) {
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
                default: { // Passed string
                    str = item;
                    break;
                }
            }
        }

        if (str == null) {
            return;
        } else {
            if (str.charAt(0) == '"' && str.charAt(1) == '"') {
                str = str.substring(1, str.length() - 1);
            }
        }

        if (serialize) {
            serialize(str);
        } else if (deserialize) {
            deserialize(str);
        }
    }

    private static void serialize(String str) {
        try { // Create new tree and data files if they don't exist
            TREE_FILE.createNewFile();
            DATA_FILE.createNewFile();
        } catch (IOException err) {
            System.out.println(err.getMessage());
            System.exit(1);
        }
        HuffmanTree tree = new HuffmanTree(str);
        try (FileWriter treeWriter = new FileWriter(TREE_FILE);
             FileWriter dataWriter = new FileWriter(DATA_FILE)) {
            String serialized = tree.serialize(); // Serialized string
            treeWriter.write(serialized);
            dataWriter.write(tree.encode(str));
        } catch (IOException err) {
            System.out.println(err.getMessage());
            System.exit(1);
        }
    }

    private static void deserialize(String str) {
        if (!(TREE_FILE.exists())) {
            return;
        }
        HuffmanTree tree = new HuffmanTree();
        String str_ = "";
        try (FileReader treeReader = new FileReader(TREE_FILE)) {
            int num = -1;
            while ((num = treeReader.read()) != -1) {
                str_ += (char) num;
            }
            tree.deserialize(str_);
            System.out.println(tree.decode(str));
        } catch (IOException err) {
            System.out.println(err.getMessage());
            System.exit(1);
        }
    }
}
