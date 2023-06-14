package maglev;

import common.Cluster;
import common.Node;
import io.whitfin.siphash.SipHasher;

import java.util.ArrayList;
import java.util.List;

public class MaglevCluster implements Cluster {

    private static final int M = 65537;
    private final List<Node> nodes;
    private int[] entry;
    private final List<int[]> permutation;
    private int N = 0;
    private static final String K1 = "u8wOJlug8qyHWvV6";
    private static final String K2 = "GlTnewYkqVGSORFh";

    public MaglevCluster() {
        nodes = new ArrayList<>();
        permutation = new ArrayList<>();
    }

    private long h1(String name) {
        return Math.abs(SipHasher.hash(K1.getBytes(), name.getBytes()));
    }

    private long h2(String name) {
        return Math.abs(SipHasher.hash(K2.getBytes(), name.getBytes()));
    }

    private void populate() {
        int[] next = new int[N];
        entry = new int[M];
        for (int j = 0; j < M; j++) {
            entry[j] = -1;
        }
        int n = 0;
        while (true) {
            for (int i = 0; i < N; i++) {
                int c = permutation.get(i)[next[i]];
                while (entry[c] >= 0) {
                    next[i]++;
                    c = permutation.get(i)[next[i]];
                }
                entry[c] = i;
                next[i]++;
                n++;
                if (n == M) {
                    for (int j = 0; j < M; j++) {
                        if (entry[j] == -1) {
                            entry[j] = 0;
                        }
                    }
                    return;
                }
            }
        }
    }

    private void generatePermutation() {
        for (int i = 0; i < N; i++) {
            String name = nodes.get(i).getName();
            long offset = h1(name) % M;
            long skip = h2(name) % (M - 1) + 1;

            int[] row = new int[M];
            for (int j = 0; j < M; j++) {
                row[j] = (int) ((offset + j * skip) % M);
            }
            permutation.add(row);
        }
    }

    @Override
    public void addNode(Node node) {
        nodes.add(node);
        N = nodes.size();
        generatePermutation();
        populate();
    }

    @Override
    public void removeNode(String name) {
        nodes.removeIf(node -> node.getName().equals(name));
        N = nodes.size();
        generatePermutation();
        populate();
    }

    @Override
    public Node get(String key) {
        int k = (int) (h1(key) % M);
        return nodes.get(entry[k]);
    }
}
