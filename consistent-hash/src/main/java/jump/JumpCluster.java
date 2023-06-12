package jump;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import common.Cluster;
import common.Node;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Stack;

public class JumpCluster implements Cluster {

    private final Random random = new Random();
    private int n = 0;
    private final Stack<Node> nodes;

    public JumpCluster() {
        nodes = new Stack<>();
    }

    @Override
    public void addNode(Node node) {
        nodes.push(node);
        n++;
    }

    @Override
    public void removeNode(String name) {
        if (nodes.peek().getName().equals(name)) {
            nodes.pop();
            n--;
        } else {
            throw new RuntimeException("只允许删除尾节点");
        }
    }

    @Override
    public Node get(String key) {
        HashFunction hashFunction = Hashing.murmur3_128();
        long k = hashFunction.hashString(key, StandardCharsets.UTF_8).asLong();
        random.setSeed(k);

        int b = -1;
        int j = 0;
        while (j < n) {
            b = j;
            double r = random.nextDouble();
            j = (int) ((b + 1) / r);
        }

        return nodes.get(b);
    }
}
