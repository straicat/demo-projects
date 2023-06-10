package ketama;

public interface Cluster {
    void addNode(Node node);

    void removeNode(String name);

    Node get(String key);
}
