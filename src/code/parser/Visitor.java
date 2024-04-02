package code.parser;

public interface Visitor {
        void visit(BinaryOperation node);
        void visit(Literal node);

        void visit(Statements statements);
//        void visit(BeginCode node);
//        void visit(EndCode node);
//        // Add visit methods for other node types
}
