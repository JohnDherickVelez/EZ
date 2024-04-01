package code.ast; // Create a package for your AST

public abstract class ASTNode {
    protected int lineNumber;  // For error reporting

    public ASTNode(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
