package code.node;

import java.util.ArrayList;
import java.util.List;

public abstract class Node {
    protected List<Node> children = new ArrayList<>();
    public List<Node> getChildren() {
        return children;
    }
    public void addChild(Node child) {
        children.add(child);
    }
}
