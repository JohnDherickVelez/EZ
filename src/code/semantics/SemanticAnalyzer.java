package code.semantics;

import code.Environment.Environment;
import code.model.Token;
import code.node.AssignmentNode;
import code.node.ExpressionNode;
import code.node.Node;
import code.parser.CustomExceptions;

import java.util.ArrayList;
import java.util.List;

public class SemanticAnalyzer {
    private Environment environment;

    public SemanticAnalyzer(Environment environment) {
        this.environment = environment;
    }

    public void analyze(List<Token> tokens, Node rootNode) throws CustomExceptions {
        // Perform semantic analysis here
        // You can traverse the AST and perform checks on variables, expressions, etc.
        // Update the environment or throw exceptions for semantic errors

        // Example: Check variable usage against declaration
        checkVariableUsage(tokens);

        if (rootNode != null) {
            checkTypeCompatibility(rootNode);
        }
    }

    private void checkVariableUsage(List<Token> tokensList) throws CustomExceptions {
        for (int i = 0; i < tokensList.size() - 1; i++) {
            Token currentToken = tokensList.get(i);
            Token nextToken = tokensList.get(i + 1);
            if (currentToken.getValue().equals("INT")) {
                int j = i + 1;
                while (j < tokensList.size() && !tokensList.get(j).getValue().equals("=")) {
                    // Skip all variable names until finding '='
                    j++;
                }
                if (j == tokensList.size() - 1) {
                    throw new CustomExceptions("Missing '=' in variable declaration.");
                }
                // Now j points to '='
                // Check the expression on the right side of '='
                Token expressionToken = tokensList.get(j + 1);
                if (!expressionToken.getValue().matches("-?\\d+")) {
                    throw new CustomExceptions("Variable '" + nextToken.getValue() + "' is not assigned an integer value.");
                }
            }
            if (currentToken.getValue().equals("FLOAT")) {
                int j = i + 1;
                while (j < tokensList.size() && !tokensList.get(j).getValue().equals("=")) {
                    // Skip all variable names until finding '='
                    j++;
                }
                if (j == tokensList.size() - 1) {
                    throw new CustomExceptions("Missing '=' in variable declaration.");
                }
                // Now j points to '='
                // Check the expression on the right side of '='
                Token expressionToken = tokensList.get(j + 1);
                if (!isFloat(expressionToken.getValue())) {
                    throw new CustomExceptions("Variable '" + nextToken.getValue() + "' is not assigned a float value.");
                }
            }
            if (currentToken.getValue().equals("CHAR")) {
                int j = i + 1;
                while (j < tokensList.size() && !tokensList.get(j).getValue().equals("=")) {
                    // Skip all variable names until finding '='
                    j++;
                }
                if (j == tokensList.size() - 1) {
                    throw new CustomExceptions("Missing '=' in variable declaration.");
                }
                // Now j points to '='
                // Check the expression on the right side of '='
                Token expressionToken = tokensList.get(j + 1);
                if (!isValidCharValue(expressionToken.getValue())) {
                    throw new CustomExceptions("Variable '" + nextToken.getValue() + "' is not assigned a char value.");
                }
            }
            if (currentToken.getValue().equals("BOOL")) {
                int j = i + 1;
                while (j < tokensList.size() && !tokensList.get(j).getValue().equals("=")) {
                    // Skip all variable names until finding '='
                    j++;
                }
                if (j == tokensList.size() - 1) {
                    throw new CustomExceptions("Missing '=' in variable declaration.");
                }
                // Now j points to '='
                // Check the expression on the right side of '='
                Token expressionToken = tokensList.get(j + 1);
                if (!isValidBool(expressionToken.getValue())) {
                    throw new CustomExceptions("Variable '" + nextToken.getValue() + "' is not assigned a boolean value.");
                }
            }
        }
    }
    private boolean isValidBool(String value) {
        // Check if the value represents a valid boolean value ("true" or "false")
        return value != null && (value.equals("\"TRUE\"") || value.equals("\"FALSE\""));
    }
    private boolean isValidCharValue(String value) {
        // Check if the value is enclosed within single quotes and has only one character
        return value.matches("^'.{1}'$");
    }
    private boolean isFloat(String value) {
        try {
            Float.parseFloat(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private void checkTypeCompatibility(Node rootNode) throws CustomExceptions {
        // Implement logic to check type compatibility, such as ensuring arithmetic operations involve compatible types
        traverseAST(rootNode);
    }
    private void traverseAST(Node node) throws CustomExceptions {
        // Check type compatibility based on the node type
        if (node instanceof AssignmentNode) {
            checkAssignmentTypeCompatibility((AssignmentNode) node);
        } else if (node instanceof ExpressionNode) {
            checkExpressionTypeCompatibility((ExpressionNode) node);
        }

        // Recursively traverse child nodes
        for (Node child : node.getChildren()) {
            traverseAST(child);
        }
    }

    private void checkAssignmentTypeCompatibility(AssignmentNode assignmentNode) throws CustomExceptions {
        String variableName = assignmentNode.getVariableName();
        String dataType = environment.getVariableType(variableName);

        // Check if the variable's data type is compatible with the assigned value's data type
        // Handle data type conversions or throw an exception if the types are incompatible
    }

    private void checkExpressionTypeCompatibility(ExpressionNode expressionNode) throws CustomExceptions {
        // Check the types of operands in the expression
        // Handle data type conversions or throw an exception if the types are incompatible
    }

    public static void main(String[] args) throws CustomExceptions {
        List<Token> tokenlist = new ArrayList<>();
        List<Node> ASTNode = new ArrayList<>();
        Environment environment1 = new Environment();
        tokenlist.add(new Token(Token.TokenType.DATATYPE, "BOOL", true));
        tokenlist.add(new Token(Token.TokenType.VARIABLE, "a", false));
        tokenlist.add(new Token(Token.TokenType.VARIABLE, "b", false));
        tokenlist.add(new Token(Token.TokenType.VARIABLE, "c", false));
        tokenlist.add(new Token(Token.TokenType.ASSIGN, "=", false));
        tokenlist.add(new Token(Token.TokenType.VARIABLE, "\"TRUE\"", false));

        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment1);
        // Pass null for the rootNode since we're only testing checkVariableUsage
        semanticAnalyzer.analyze(tokenlist, null);
    }
}
