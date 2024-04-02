package node;

import code.model.TokenType;

public class VariableDeclarationNode implements ASTNode {
    private final TokenType type;
    private final String variableName;
    private final ASTNode initialization;

    public VariableDeclarationNode(TokenType type, String variableName, ASTNode initialization) {
        this.type = type;
        this.variableName = variableName;
        this.initialization = initialization;
    }

    @Override
    public void print(int indentLevel) {
        for (int i = 0; i < indentLevel; i++) {
            System.out.print("  "); // Print indentation
        }
        System.out.println("Variable Declaration: " + type + " " + variableName);
        if (initialization != null) {
            initialization.print(indentLevel + 1); // Print initialization if present
        }
    }
}
