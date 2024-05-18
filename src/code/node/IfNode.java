package code.node;

import java.util.ArrayList;
import java.util.List;

public class IfNode extends Node {
    private ConditionNode condition;
    private List<Node> ifBlock;

    public IfNode(ConditionNode condition) {
        this.condition = condition;
        this.ifBlock = new ArrayList<>();
    }

    public ConditionNode getCondition() {
        return condition;
    }

    public List<Node> getIfBlock() {
        return ifBlock;
    }

    public void addIfBlockNode(Node node) {
        ifBlock.add(node);
    }
}

