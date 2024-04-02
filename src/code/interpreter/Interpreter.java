package code.interpreter; // Create a package for your interpreter

import code.ast.*; // Import your AST node classes

import java.util.HashMap;
import java.util.List;

import code.model.TokenType;
import code.parser.*;
import code.parser.ASTNode;


public class Interpreter implements Visitor {
    public void interpret(ASTNode node) {
        node.accept(this);
    }

    @Override
    public void visit(BinaryOperation node) {
        // Implement interpretation logic for binary operations
        node.getLeft().accept(this);
        node.getRight().accept(this);
        // Perform the operation and handle the result
    }

    @Override
    public void visit(Literal node) {
        // Implement interpretation logic for literals
        // This could involve storing the value or printing it, etc.
    }

    @Override
    public void visit(Statements node) {
        // Interpret each statement in the list if it's not null
        List<ASTNode> statements = node.getStatements();
        if (statements != null) {
            for (ASTNode statement : statements) {
                statement.accept(this);
            }
        }
    }


//    private HashMap<String, Object> symbolTable = new HashMap<>();
//    // Method to start the interpretation process
//    public void interpret(ASTNode program) {
//        if (program instanceof ProgramNode) {
//            for (ASTNode statement : ((ProgramNode) program).getStatements()) {
//                evaluate(statement); // Evaluate each top-level statement
//            }
//        } else {
//            // Handle the error case - the root node should be a ProgramNode
//        }
//    }
//
//    // Helper to evaluate individual nodes
//    private void evaluate(ASTNode node) {
//        if (node instanceof DeclarationNode) {
//            evaluateDeclaration((DeclarationNode) node);
//        } else if (node instanceof AssignmentNode) {
//            evaluateAssignment((AssignmentNode) node);
//        } else if (node instanceof ExpressionNode) {
//            evaluateExpression((ExpressionNode) node);
//        } else {
//            // Handle other possible AST node types or report an error
//            throw new RuntimeException("Unsupported AST node type: " + node.getClass().getName() + " at line " + node.getLineNumber());
//        }
//    }
//    private void evaluateDeclaration(DeclarationNode node) {
//        TokenType tokenType = node.getTokenType();
//        String identifier = node.getIdentifier();
//
//        // Symbol Table Interaction
//        if (symbolTable.containsKey(identifier)) {
//            throw new RuntimeException("Variable redeclaration: " + identifier + " at line " + node.getLineNumber());
//        } else {
//            // Logic to initialize the variable based on its data type
//            switch(tokenType) {
//                case INT: symbolTable.put(identifier, 0); break; // Initialize integer to 0
//                case FLOAT: symbolTable.put(identifier, 0.0); break;
//                case CHAR: symbolTable.put(identifier, '\0'); break; // Null character as default
//                case BOOL: symbolTable.put(identifier, false); break;
//                // ... cases for CHAR, BOOL, etc.
//                default:
//                    throw new RuntimeException("Unsupported data type: " + tokenType);
//            }
//        }
//    }
//
//
//    private void evaluateAssignment(AssignmentNode node) {
//        String identifier = node.getIdentifier();
//        ASTNode expression = node.getExpression();
//
//        // 1. Evaluate the expression
//        //Object value = evaluate(expression);
//
//        // 2. Check if the variable exists
//        if (!symbolTable.containsKey(identifier)) {
//            throw new RuntimeException("Undefined variable: " + identifier + " at line " + node.getLineNumber());
//        }
//
//        // 3. Type Checking
//        // DataType variableType = symbolTable.get(identifier).getDataType(); // Assuming your storage includes the type
//        // DataType expressionType = value.getDataType(); // Assuming value can provide its type
//
////        if (variableType != expressionType) {
////            throw new RuntimeException("Type mismatch: Cannot assign " + expressionType + " to " + variableType + " at line " + node.getLineNumber());
////        }
////
////        // 4. Update the symbol table (Assuming types match)
////        symbolTable.put(identifier, value);
//    }
//
//
//    private void evaluateExpression(ASTNode node) {
//        // ... Logic to handle expressions (arithmetic, logical, etc.)
//    }
//
//    // ... More helper methods for other AST node types
}
