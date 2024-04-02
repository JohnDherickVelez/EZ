package code.parser;

import code.model.TokenType;

public class BinaryOperation extends ASTNode {
    // Represents binary operations like +, -, *, /
    ASTNode left;
    ASTNode right;
    TokenType operator;

    public ASTNode getLeft() {
        return left;
    }

    public ASTNode getRight() {
        return right;
    }

    BinaryOperation(ASTNode left, ASTNode right, TokenType operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

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
    public void visit(Statements statements) {

    }
}
