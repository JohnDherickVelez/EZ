package code.ast;

import java.util.List;

public class ProgramNode extends ASTNode {
    private List<ASTNode> statements;

    public ProgramNode(List<ASTNode> statements, int lineNumber) {
        super(lineNumber);
        this.statements = statements;
    }

    // Getter for statements

    public List<ASTNode> getStatements() {
        return statements;
    }
}
