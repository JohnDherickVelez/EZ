package code.parser;

public abstract class ASTNode implements Visitor {
    abstract void accept(Visitor visitor); // Visitor pattern for traversal
}
