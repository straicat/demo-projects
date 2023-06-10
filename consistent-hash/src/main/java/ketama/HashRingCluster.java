package ketama;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class HashRingCluster implements Cluster {

    private static final int DEFAULT_VNODE_NUM = 150;
    private static final HashEnum DEFAULT_HASH = HashEnum.FNV1_32_HASH;

    private List<Node> nodes = new ArrayList<>();
    private SortedMap<Long, Node> vNodes = new TreeMap<>();
    private int vNodeNum = DEFAULT_VNODE_NUM;
    private Hasher hasher;

    public HashRingCluster() {
        hasher = HasherFactory.getHasher(DEFAULT_HASH);
    }

    public HashRingCluster(int vNodeNum) {
        this.vNodeNum = vNodeNum;
        hasher = HasherFactory.getHasher(DEFAULT_HASH);
    }

    public HashRingCluster(int vNodeNum, HashEnum h) {
        this.vNodeNum = vNodeNum;
        hasher = HasherFactory.getHasher(h);
    }

    @Override
    public void addNode(Node node) {
        nodes.add(node);
        for (int i = 0; i < vNodeNum; i++) {
            String vNodeName = node.getName() + "_vnode" + i;
            vNodes.put(hasher.hash(vNodeName), node);
        }
    }

    @Override
    public void removeNode(String name) {
        nodes.removeIf(node -> node.getName().equals(name));
        for (int i = 0; i < vNodeNum; i++) {
            String vNodeName = name + "_vnode" + i;
            vNodes.remove(hasher.hash(vNodeName));
        }
    }

    @Override
    public Node get(String key) {
        Long hash = hasher.hash(key);
        SortedMap<Long, Node> subMap = vNodes.tailMap(hash);
        if (!subMap.isEmpty()) {
            return subMap.get(subMap.firstKey());
        }
        return vNodes.get(vNodes.firstKey());
    }
}
