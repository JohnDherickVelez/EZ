package code.ast;

public class AssignmentNode extends ASTNode {
    private String identifier;
    private ASTNode expression;

    public AssignmentNode(String identifier, ASTNode expression, int lineNumber) {
        super(lineNumber);
        this.identifier = identifier;
        this.expression = expression;
    }

    public String getIdentifier() {
        return identifier;
    }

    public ASTNode getExpression() {
        return expression;
    }
// Getters for identifier and expression
}
