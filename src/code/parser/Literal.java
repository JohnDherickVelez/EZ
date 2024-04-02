package code.parser;

public class Literal extends ASTNode {
    // Represents literals like integers or identifiers
    String value;

    Literal(String value) {
        this.value = value;
    }

    @Override
    public void accept(Visitor visitor) {

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
