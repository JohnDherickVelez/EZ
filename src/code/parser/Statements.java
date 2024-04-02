package code.parser;

import java.util.List;

public class Statements extends ASTNode {
    public List<ASTNode> getStatements() {
        return statements;
    }

    // Represents a list of statements
    List<ASTNode> statements;

    Statements(List<ASTNode> statements) {
        this.statements = statements;
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