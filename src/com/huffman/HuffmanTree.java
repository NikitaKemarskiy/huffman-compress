package com.huffman;

import java.util.*;

public class HuffmanTree {
    // Private
    private Node root;
    private Map<Character, String> table;
    private int weight;
    private int height;

    private void buildTable() { // Build table method
        table = new HashMap<>();
        weight = 0;
        height = 0;
        if (root != null) {
            addToTable(root, "");
        }
    }

    private void addToTable(Node node, String path) { // Add character to table method
        if (node.hasLeft()) {
            addToTable(node.getLeft(), path + "0");
        }
        if (node.isLeaf()) {
            table.put(node.getKey(), path);
            weight += node.getValue() * path.length(); // Increase weight
            if (path.length() > height) { // Update tree height
                height = path.length();
            }
        }
        if (node.hasRight()) {
            addToTable(node.getRight(), path + "1");
        }
    }

    // Public
    public HuffmanTree(String str) {
        build(str);
    }

    // Getters
    public Map<Character, String> getTable() {
        return table;
    }

    public int getWeight() {
        return weight;
    }

    public int getHeight() {
        return height;
    }

    // Methods
    public void build(String str) { // Build tree method
        FrequencyTable frequencyTable = new FrequencyTable(str);
        List<Entry> frequencyList = frequencyTable.getList();
        List<Node> frequencyNodeList = new LinkedList<>();

        for (Entry item : frequencyList) { // Fill frequencyNodeList with frequencyList content converting it into Nodes
            frequencyNodeList.add(new Node(item.getKey(), item.getValue()));
        }

        while (frequencyNodeList.size() > 1) {
            // Create new Node with new frequency
            Node node = new Node(frequencyNodeList.get(0).getValue() + frequencyNodeList.get(1).getValue());
            node.setLeft(frequencyNodeList.get(0)); // Set left child of the new Node
            node.setRight(frequencyNodeList.get(1)); // Set right child of the new Node
            frequencyNodeList = frequencyNodeList.subList(2, frequencyNodeList.size()); // Remove first two items of frequencyNodeList
            frequencyNodeList.add(node); // Add new item to the frequencyNodeList
            frequencyNodeList.sort(new Comparator<Node>() {
                @Override
                public int compare(Node node1, Node node2) {
                    return node1.compareTo(node2);
                }
            });
        }

        root = frequencyNodeList.size() == 1 ? frequencyNodeList.get(0) : null;
        buildTable();
    }

    public String encode(String str) {
        String result = "";
        for (int i = 0; i < str.length(); i++) {
            result += encode(str.charAt(i));
        }
        return result;
    }

    public String encode(Character ch) {
        return table.get(ch);
    }

    public String decode(String str) {
        Node curr = root;
        String result = "";
        for (int i = 0; i < str.length(); i++) {
            curr = str.charAt(i) == '0' ? curr.getLeft() : curr.getRight();
            if (curr.isLeaf()) {
                result += curr.getKey();
                curr = root;
            }
        }
        return result;
    }

    public String infix() { // Infix method
        String str = "[";
        if (root != null) {
            str += infix(root, "");
            str = str.substring(0, str.length() - 1);
            str += " ";
        }
        str += "]";
        return str;
    }

    public String infix(Node node, String path) { // Infix method with specified Node
        String str = "";
        if (node.hasLeft()) {
            str += infix(node.getLeft(), path + "0");
        }
        if (node.isLeaf()) {
            str += String.format(" (%c; %s),", node.getKey(), path);
        }
        if (node.hasRight()) {
            str += infix(node.getRight(), path + "1");
        }
        return str;
    }

    public String serialize() {
        String[] arr = new String[table.size()];
        int index = 0;

        for (Map.Entry<Character, String> item : table.entrySet()) {
            Character ch = item.getKey();
            String path = item.getValue();
            Node curr = root;
            for (int i = 0; i < path.length(); i++) {
                if (path.charAt(i) == '0') {
                    curr = curr.getLeft();
                } else {
                    curr = curr.getRight();
                }
            }
            Integer frequency = curr.isLeaf() ? curr.getValue() : null;
            arr[index++] = String.join(",", (new String[] {ch.toString(), path, frequency.toString()}));
        }

        return String.join(";", arr);
    }

    public void deserialize(String str) {
        String[] serialized = str.split(";"); // Serialize array
        root = null;
        if (serialized.length > 0) { // Initialize root node
            int frequency = 0;
            for (String item : serialized) {
                frequency += Integer.parseInt(item.split(",")[2]);
            }
            root = new Node(frequency);
        }
        for (String item : serialized) {
            String[] arr = item.split(",");
            Character ch = arr[0].charAt(0);
            String path = arr[1];
            Integer frequency = Integer.parseInt(arr[2]);
            Node curr = root;
            for (int i = 0; i < path.length(); i++) {
                if (path.charAt(i) == '0') { // Left child
                    Node curr_ = curr;
                    curr = curr.hasLeft() ? curr.getLeft() : i == path.length() - 1 ? new Node(ch, frequency) : new Node(0);
                    curr_.setLeft(curr);
                } else { // Right child
                    Node curr_ = curr;
                    curr = curr.hasRight() ? curr.getRight() : i == path.length() - 1 ? new Node(ch, frequency) : new Node(0);
                    curr_.setRight(curr);
                }
            }
        }
        buildTable();
    }
}
