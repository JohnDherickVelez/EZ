package code.parser;

import code.model.TokenType;

public class VariableDeclarationNode extends ASTNode {
    private TokenType type;
    private String variableName;
    private ASTNode initialization;

    public VariableDeclarationNode(TokenType type, String variableName, ASTNode initialization) {
        this.type = type;
        this.variableName = variableName;
        this.initialization = initialization;
    }

    // Getters for type, variableName, initialization

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void visit(BinaryOperation node) {

    }

    @Override
    public void visit(Literal node) {

    }

    @Override
    public void visit(VariableDeclarationNode node) {

    }

    @Override
    public void visit(Statements statements) {

    }
}

