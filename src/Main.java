import com.huffman.HuffmanTree;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        HuffmanTree tree = new HuffmanTree("nikita kemarskiy");
        String serialized = tree.serialize(); // Serialized string

        HuffmanTree tree_ = new HuffmanTree("");
        tree_.deserialize(serialized); // Deserialize string
    }
}
