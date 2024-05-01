package code.node;

import java.util.ArrayList;
import java.util.List;

public class IfNode extends ASTNode {
    private boolean condition;
    private ASTNode ifBlock;
    private List<Node> children;

    public IfNode(boolean condition) {
        this.condition = condition;
        this.ifBlock = null; // Initialize ifBlock to null
        this.children = new ArrayList<>();
    }

    public boolean getCondition() {
        return condition;
    }

    public void setIfBlock(ASTNode ifBlock) {
        this.ifBlock = ifBlock;
    }

    @Override
    public void addChild(Node node) {
        // IfNode doesn't support adding child nodes directly
        throw new UnsupportedOperationException("IfNode doesn't support adding child nodes directly.");
    }

    @Override
    public List<Node> getChildren() {
        // IfNode doesn't have children
        return children;
    }

    @Override
    public String toString() {
        return "IfNode{" +
                "condition=" + condition +
                ", ifBlock=" + ifBlock +
                '}';
    }
}
