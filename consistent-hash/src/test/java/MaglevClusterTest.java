import common.Cluster;
import common.Node;
import maglev.MaglevCluster;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

public class MaglevClusterTest {

    @Test
    public void test() {
        Cluster cluster = new MaglevCluster();
        cluster.addNode(new Node("node1", "192.168.10.1", 8080));
        cluster.addNode(new Node("node2", "192.168.10.2", 8080));
        cluster.addNode(new Node("node3", "192.168.10.3", 8080));
        cluster.addNode(new Node("node4", "192.168.10.4", 8080));

        show(cluster);

        System.out.println("----------------------------");
        cluster.removeNode("node4");
        show(cluster);
    }

    private void show(Cluster cluster) {
        int dataCnt = 1000000;
        String keyPrefix = "dataNode_";
        Map<String, Integer> nodeCntMap = new TreeMap<>();
        for (int i = 0; i < dataCnt; i++) {
            Node node = cluster.get(keyPrefix + i);
            nodeCntMap.put(node.getName(), nodeCntMap.getOrDefault(node.getName(), 0) + 1);
        }
        for (Map.Entry<String, Integer> entry : nodeCntMap.entrySet()) {
            System.out.println("node: " + entry.getKey() + ", ratio: " + (double) entry.getValue() / dataCnt);
        }
    }
}
