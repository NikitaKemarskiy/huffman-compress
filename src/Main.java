import com.huffman.HuffmanTree;
import com.huffman.Serializer;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        HuffmanTree tree = new HuffmanTree("nikita kemarskiy");
        String serialized = Serializer.serialize(tree);
    }
}
