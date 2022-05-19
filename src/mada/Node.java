package mada;

import java.util.List;
import java.util.stream.Collectors;

public class Node {

    Node leftNode;
    Node rightNode;
    int value;
    String c;
    String code;

    // root or intermediary nodes
    public Node(Node left, Node right) {
        if (left != null && right != null) {
            if (left.value < right.value) {
                this.leftNode = left;
                this.rightNode = right;
            } else {
                this.leftNode = right;
                this.rightNode = left;
            }
            this.value = left.value + right.value;
        }

    }

    // leaf nodes
    public Node(int value, char c) {
        this.c = "" + c;
        this.value = value;
    }

    // traverse the tree and
    public static void traverseTree(Node n, String prefix) {
        if (n == null) {
            return;
        }
        if (n.c != null) {
            n.code = prefix;
        }
        traverseTree(n.leftNode, prefix + "0");
        traverseTree(n.rightNode, prefix + "1");
    }

    public static void constructTree(List<Node> leaves) {
        List<Node> copy = leaves.stream().collect(Collectors.toList());
        // huffman tree generation
        while (copy.size() > 1) {
            copy.sort((a, b) -> a.value - b.value);
            Node left = copy.get(0);
            Node right = copy.get(1);
            Node n = new Node(left, right);
            copy.remove(left);
            copy.remove(right);
            copy.add(n);
        }
        traverseTree(copy.get(0), "");
    }
}
