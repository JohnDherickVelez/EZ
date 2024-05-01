package code.semantics;

import code.Environment.Environment;
import code.model.Token;
import code.node.AssignmentNode;
import code.node.ExpressionNode;
import code.node.Node;
import code.parser.CustomExceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static code.lexer.ReservedWordChecker.isReservedWord;

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
//        checkTokenGrammar(tokens);
        checkDisplayUsage(tokens);
        checkVariableUsage(tokens);

        if (rootNode != null) {
            checkTypeCompatibility(rootNode);
        }
    }
//    public void checkTokenGrammar(List<Token> tokensList) throws CustomExceptions {
//        boolean foundBegin = false;
//        boolean foundCodeAfterBegin = false;
//        boolean foundEnd = false;
//        boolean foundCodeAfterEnd = false;
//
//        for (int i = 0; i < tokensList.size() - 1; i++) {
//            Token currentToken = tokensList.get(i);
//            Token nextToken = tokensList.get(i + 1);
//
//            if (currentToken.getValue().equals("BEGIN")) {
//                foundBegin = true;
//                if (nextToken.getValue().equals("CODE")) {
//                    foundCodeAfterBegin = true;
//                }
//            } else if (currentToken.getValue().equals("END")) {
//                foundEnd = true;
//                if (nextToken.getValue().equals("CODE")) {
//                    foundCodeAfterEnd = true;
//                }
//            } else if (!currentToken.getIsReservedKey() && isReservedWord(currentToken.getValue())) {
//                throw new CustomExceptions("Variable name is a reserved word: " + currentToken.getValue().toString());
//            }
//        }
//
//        if (!foundBegin) {
//            throw new CustomExceptions("Missing starting statement 'BEGIN'");
//
//        }
//
//        if (!foundCodeAfterBegin) {
//            throw new CustomExceptions("Missing 'CODE' after 'BEGIN'");
//        }
//
//        if (!foundEnd) {
//            throw new CustomExceptions("Missing ending statement 'END'");
//        }
//
//        if (!foundCodeAfterEnd) {
//            throw new CustomExceptions("Missing 'CODE' after 'END'");
//        }
//    }

    private void checkDisplayUsage(List<Token> tokensList) throws CustomExceptions {
        int i = 0;
        while (i < tokensList.size()) {
            Token currentToken = tokensList.get(i);
//            Token nextToken = tokensList.get(i+1);

            if (currentToken.getValue().equals("DISPLAY")) {

                i++; // Move to the next token after "DISPLAY"
                // Check if there are enough tokens remaining
                if (i < tokensList.size()) {
                    Token nextToken = tokensList.get(i);
                    // Check if the next token is ':'
                    if (!nextToken.getValue().equals(":")) {
                        throw new CustomExceptions("Expected ':' immediately after 'DISPLAY' token.");
                    }
                } else {
                    throw new CustomExceptions("Expected ':' immediately after 'DISPLAY' token, but reached end of tokens.");
                }
                while (i < tokensList.size() && tokensList.get(i).getType() != Token.TokenType.ENDLINE) {

                    Token token = tokensList.get(i);
//                    System.out.println(token);
                    if (token.getType() == Token.TokenType.VARIABLE) {
                        String variableName = token.getValue();


                        if (!environment.isDefined(variableName)) {
                            throw new CustomExceptions("Variable '" + variableName + "' is not defined.");
                        }
                    } else if (token.getType() == Token.TokenType.OPERATOR && token.getValue().equals("&")) {
                        // Check if there's a variable after '&'
                        if (i + 1 < tokensList.size() && tokensList.get(i + 1).getType() == Token.TokenType.VARIABLE
                                || tokensList.get(i + 1).getType() == Token.TokenType.TEXT
                                || tokensList.get(i + 1).getType() == Token.TokenType.IDENTIFIER
                                || tokensList.get(i + 1).getType() == Token.TokenType.OPERATOR
                                || tokensList.get(i + 1).getType() == Token.TokenType.ESCAPE) {
                            i++; // Move to the next token as we've already processed the variable after '&'
                        } else {
                            throw new CustomExceptions("Expected variable after '&' token.");
                        }
                    } else if (token.getType() == Token.TokenType.VARIABLE && token.getValue().startsWith("\"")) {
                        // Check if the string has 2 double quotes
                        String value = token.getValue();

                        if (value.length() < 2 || value.charAt(0) != '"' || value.charAt(value.length() - 1) != '"') {
                            throw new CustomExceptions("Invalid string format: " + value);
                        }

                    } else if (token.getType() == Token.TokenType.VARIABLE && token.getValue().startsWith("'")) {
                        // Check if the string has 2 single quotes
                        String value = token.getValue();

                        if (value.length() < 2 || value.charAt(0) != '\'' || value.charAt(value.length() - 1) != '\'') {
                            throw new CustomExceptions("Invalid character format: " + value);
                        }
                    }
                    i++; // Move to the next token
                }
                // Check if the next token after the loop is an ENDLINE token

            } else {
                i++; // Move to the next token if "DISPLAY" is not found
            }
        }
    }

private void checkVariableUsage(List<Token> tokensList) throws CustomExceptions {
    for (int i = 0; i < tokensList.size() - 1; i++) {
        Token currentToken = tokensList.get(i);
        Token nextToken = tokensList.get(i + 1);

        if (currentToken.getValue().equals("INT") ||
                currentToken.getValue().equals("FLOAT") ||
                currentToken.getValue().equals("CHAR") ||
                currentToken.getValue().equals("BOOL")) {
            if(nextToken.getType() != Token.TokenType.VARIABLE) {
                throw new CustomExceptions("Missing assigned variable/s after DATATYPE '" + currentToken.getValue() + "'.");
            }
//            System.out.println(nextToken);
            if(startsWithIntegers(nextToken.getValue())) {
                throw new CustomExceptions("Variable declaration error!");
            }

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
            // TODO: CANT HANDLE CASES INT a,b <--- default initializers
            Token expressionToken = tokensList.get(j+1);
            System.out.println(expressionToken);
            if (currentToken.getValue().equals("INT")) {
                if (!expressionToken.getValue().matches("-?\\d+")) {
                    throw new CustomExceptions("Variable '" + nextToken.getValue() + "' is not assigned an integer value.");
                }
            } else if (currentToken.getValue().equals("FLOAT")) {
                if (!isFloat(expressionToken.getValue())) {
                    throw new CustomExceptions("Variable '" + nextToken.getValue() + "' is not assigned a float value.");
                }
            } else if (currentToken.getValue().equals("CHAR")) {
                if (!isValidCharValue(expressionToken.getValue())) {
                    throw new CustomExceptions("Variable '" + nextToken.getValue() + "' is not assigned a char value.");
                }
            } else if (currentToken.getValue().equals("BOOL")) {
                if (!isValidBool(expressionToken.getValue()) && !expressionToken.getValue().matches("(\\(|\\)|=|\\*|/|%|-|\\+|<|>|<>|>=|<=|AND|OR)")) {
                    throw new CustomExceptions("Variable '" + nextToken.getValue() + "' is not assigned a boolean value.");
                }
            }
        }
    }
}
    public  boolean startsWithIntegers(String input) {
        // Regular expression pattern to match integers at the beginning of the string
        String pattern = "^\\d+.*";

        // Create a Pattern object
        Pattern p = Pattern.compile(pattern);

        // Create a Matcher object
        Matcher m = p.matcher(input);

        // Check if the pattern matches the input string
        return m.matches();
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
}
