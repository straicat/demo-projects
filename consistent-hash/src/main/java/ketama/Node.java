package ketama;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Node {
    private String name;
    private String host;
    private Integer port;
}
