package node;

import java.util.List;

public class Statements implements ASTNode {
    private final List<ASTNode> statements;

    public Statements(List<ASTNode> statements) {
        this.statements = statements;
    }

    public List<ASTNode> getStatements() {
        return statements;
    }

    @Override
    public void print(int indentLevel) {
        for (ASTNode statement : statements) {
            for (int i = 0; i < indentLevel; i++) {
                System.out.print("  "); // Print indentation
            }
            statement.print(indentLevel + 1); // Recursively print child nodes with increased indent level
        }
    }
}

