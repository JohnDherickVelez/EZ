package code.parser;

public interface Visitor {
        void visit(BinaryOperation node);
        void visit(Literal node);

        void visit(Statements statements);
        // Add visit methods for other node types
}
