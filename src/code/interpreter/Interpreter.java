package code.interpreter; // Create a package for your interpreter

import code.ast.*; // Import your AST node classes

import java.util.HashMap;
import code.model.TokenType;


public class Interpreter {
    private HashMap<String, Object> symbolTable = new HashMap<>();
    // Method to start the interpretation process
    public void interpret(ASTNode program) {
        if (program instanceof ProgramNode) {
            for (ASTNode statement : ((ProgramNode) program).getStatements()) {
                evaluate(statement); // Evaluate each top-level statement
            }
        } else {
            // Handle the error case - the root node should be a ProgramNode
        }
    }

    // Helper to evaluate individual nodes
    private void evaluate(ASTNode node) {
        if (node instanceof DeclarationNode) {
            evaluateDeclaration((DeclarationNode) node);
        } else if (node instanceof AssignmentNode) {
            evaluateAssignment((AssignmentNode) node);
        } else if (node instanceof ExpressionNode) {
            evaluateExpression((ExpressionNode) node);
        } else {
            // Handle other possible AST node types or report an error
            throw new RuntimeException("Unsupported AST node type: " + node.getClass().getName() + " at line " + node.getLineNumber());
        }
    }
    private void evaluateDeclaration(DeclarationNode node) {
        TokenType tokenType = node.getTokenType();
        String identifier = node.getIdentifier();

        // Symbol Table Interaction
        if (symbolTable.containsKey(identifier)) {
            throw new RuntimeException("Variable redeclaration: " + identifier + " at line " + node.getLineNumber());
        } else {
            // Logic to initialize the variable based on its data type
            switch(tokenType) {
                case INT: symbolTable.put(identifier, 0); break; // Initialize integer to 0
                case FLOAT: symbolTable.put(identifier, 0.0); break;
                case CHAR: symbolTable.put(identifier, '\0'); break; // Null character as default
                case BOOL: symbolTable.put(identifier, false); break;
                // ... cases for CHAR, BOOL, etc.
                default:
                    throw new RuntimeException("Unsupported data type: " + tokenType);
            }
        }
    }


    private void evaluateAssignment(AssignmentNode node) {
        String identifier = node.getIdentifier();
        ASTNode expression = node.getExpression();

        // 1. Evaluate the expression
        Object value = evaluate(expression);

        // 2. Check if the variable exists
        if (!symbolTable.containsKey(identifier)) {
            throw new RuntimeException("Undefined variable: " + identifier + " at line " + node.getLineNumber());
        }

        // 3. Type Checking (optional)
        // You could add logic to check if the expression's type aligns with the variable's type

        // 4. Update the symbol table
        symbolTable.put(identifier, value);
    }


    private void evaluateExpression(ASTNode node) {
        // ... Logic to handle expressions (arithmetic, logical, etc.)
    }

    // ... More helper methods for other AST node types
}
