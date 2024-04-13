package code.parser;

//import code.ArithmeticOperations.BinaryOperationNode;
//import code.ArithmeticOperations.ValueNode;
//import code.ArithmeticOperations.VariableNode;
import code.Environment.Environment;
import code.model.Token;
import code.node.*;

import java.util.*;

public class Parser {
    private List<Token> tokensList;
    private int currentTokenIndex = 0;

    private List<Node> ASTNode = new ArrayList<>();
    private List<String> variableList = new ArrayList<>();
    private Environment environment;

    public Parser(List<Token> tokensList, Environment environment) {
        this.tokensList = tokensList;
        this.environment = environment;
    }

    public ASTNode produceAST() throws CustomExceptions {
        ASTNode rootNode = new ASTNode(); // Create the root code.node

        for (int i = 0; i < tokensList.size(); i++) {
            Token token = tokensList.get(i);

            try {
                switch (token.getType()) {
                    case DELIMITER:
                        if (Objects.equals(String.valueOf(token.getValue()), "BEGIN")) {
                            // Add logic for processing "BEGIN" token
                            if (i + 1 < tokensList.size() &&
                                    Objects.equals(tokensList.get(i + 1).getValue(), "CODE")) {
                                // Add logic for processing "BEGIN CODE" sequence
                                rootNode.addChild(new DelimiterNode("BEGIN_CODE", true));
                                i++; // Move to the next token after "CODE"
                            }
                        } else if (Objects.equals(String.valueOf(token.getValue()), "END")) {
                            if (i + 1 < tokensList.size() &&
                                    Objects.equals(tokensList.get(i + 1).getValue(), "CODE")) {
                                // Add logic for processing "END CODE" sequence
                                rootNode.addChild(new DelimiterNode("END_CODE", false));
                                i++; // Move to the next token after "CODE"
                            }
                        }
                        break;

                    case DATATYPE:
                        if (token.getValue().equals("INT") || token.getValue().equals("FLOAT")
                                || token.getValue().equals("CHAR") || token.getValue().equals("BOOL")) {

                            if (i + 1 < tokensList.size()) {
                                String datatype = token.getValue();
                                List<String> variableNames = new ArrayList<>();
                                String value = null;
                                boolean assigned = false;
                                boolean decleared = false;

                                while (token.getType() != Token.TokenType.ENDLINE && i < tokensList.size()) { // Iterate through statement

                                    if (token.getType() == Token.TokenType.VARIABLE) { // Store variable
                                        variableNames.add(token.getValue());
                                        decleared = false;
                                    } else if (token.getType() == Token.TokenType.VALUE) { // Store value
                                        value = token.getValue();
                                        processVariableDeclaration(datatype, variableNames, value, rootNode); // Call the method
                                        variableNames.clear(); // Clean list
                                        decleared = true;
                                    } else if (token.getType() == Token.TokenType.ASSIGN) {
                                        assigned = true;
                                    }

                                    // Move to the next token
                                    i++;
                                    if (i < tokensList.size()) {
                                        token = tokensList.get(i);
                                    }
                                }

                                if (!decleared && !assigned) { // For: int x, y, z = 3
                                    processVariableDeclaration(datatype, variableNames, value, rootNode); // Call the method
                                } else if (!decleared && assigned) {
                                    throw new CustomExceptions("No value assigned to variable");
                                }

                            } else {
                                // Handle unexpected token (not an identifier)
                                throw new CustomExceptions("Expected identifier for variable name.");
                            }
                        }
                        break;

                    case DISPLAY:
                        List<String> variableNames = new ArrayList<>();
                        boolean isFirstVariable = true; // Flag to track if it's the first variable

                        // Iterate through tokens until the end of the DISPLAY statement
                        while (i < tokensList.size() && tokensList.get(i).getType() != Token.TokenType.ENDLINE) {
                            Token displayToken = tokensList.get(i);
                            if (displayToken.getType() == Token.TokenType.VARIABLE) {
                                // If the token is a variable, add its name to the variableNames list
                                variableNames.add(displayToken.getValue());
                                isFirstVariable = false; // Reset the flag after processing the first variable
                            } else if (displayToken.getType() == Token.TokenType.OPERATOR && displayToken.getValue().equals("&")) {
                                // Check if there's a variable after '&'
                                if (i + 1 < tokensList.size() && tokensList.get(i + 1).getType() == Token.TokenType.VARIABLE) {
                                    variableNames.add(tokensList.get(i + 1).getValue());
                                    i++; // Move to the next token as we've already processed the variable after '&'
                                } else if (i + 1 < tokensList.size() && tokensList.get(i + 1).getType() ==
                                        Token.TokenType.OPERATOR && tokensList.get(i + 1).getValue().equals("$")) {
                                    variableNames.add("$");
                                }
                                if (i < tokensList.size()) {
                                    Token nextToken = tokensList.get(i);

                                    if (nextToken.getType() == Token.TokenType.VARIABLE) {
                                        // Append the variable name to the list
                                        variableNames.add(nextToken.getValue());
                                    } else if (nextToken.getType() == Token.TokenType.OPERATOR && nextToken.getValue().equals("\"")) {
                                        // Handle quoted text within DISPLAY
                                        StringBuilder quotedText = new StringBuilder();
                                        i++; // Move past the opening quote

                                        while (i < tokensList.size() &&
                                                tokensList.get(i).getType() != Token.TokenType.OPERATOR &&
                                                !tokensList.get(i).getValue().equals("\"")) {
                                            // Append token value to the quoted text
                                            quotedText.append(tokensList.get(i).getValue());
                                            i++; // Move to the next token
                                        }

                                        if (i < tokensList.size() &&
                                                tokensList.get(i).getType() == Token.TokenType.OPERATOR &&
                                                tokensList.get(i).getValue().equals("\"")) {
                                            // Append the collected quoted text as a single variable name
                                            variableNames.add(quotedText.toString());
                                        }
                                    }
                                }
                            }

                            i++; // Move to the next token
                        }





                                // Create a new DisplayNode with the list of variable names
                    rootNode.addChild(new DisplayNode(variableNames));
                    break;
                case VARIABLE:
                    String varname = token.getValue();
                    Object new_value = null;
                    String datatype = null;
                    while (token.getType() != Token.TokenType.ENDLINE && i < tokensList.size()) {
                        //System.out.println("randomhskejklqwejlqkjeklqeqwe1   "+token.getValue().toString());
                        if (token.getType() == Token.TokenType.ASSIGN) { // case: [x = y = z]
                            token = tokensList.get(i-1); // set y as initial
                            i--;
                            varname = token.getValue().toString();
                        }
                        if (token.getType() == Token.TokenType.EXPRESSION)
                        {
                            i++;
                            token = tokensList.get(i);
                            continue;
                        }
                        //System.out.println("randomhskejklqwejlqkjeklqeqwe1.2   "+token.getValue().toString());
                        if (environment.isDefined(token.getValue().toString())) { // check if variable exist [int x]
                            i++; // Move to the next token
                            if (i < tokensList.size()) {
                                token = tokensList.get(i);
                                //System.out.println("randomhskejklqwejlqkjeklqeqwe2   "+token.getValue().toString());
                                if (token.getType() == Token.TokenType.ASSIGN) { // if encountered an assignment operator
                                    i++;
                                    token = tokensList.get(i);
                                    datatype = environment.getVariableType(varname);
                                    //System.out.println("randomhskejklqwejlqkjeklqeqwe3   "+token.getValue().toString());
                                    // Update the variable value in the environment
                                    if (token.getType() == Token.TokenType.VARIABLE) { // case: [x = y]
                                        // check if [y] exist
                                        if (environment.isDefined(token.getValue().toString())) { // TODO: currently allows words
                                            new_value = environment.getVariable(token.getValue().toString()); // update value in the environment
                                            if (variableValueValidator(datatype, new_value)) {
                                                environment.setVariable(varname, new_value);
                                            } else {
                                                throw new CustomExceptions("Incorrect value" + token.getValue().toString());
                                            }
                                        } else {
                                            throw new CustomExceptions("Variable " + token.getValue().toString() + " not initially declared");
                                        }
                                    } else if (token.getType() == Token.TokenType.VALUE) { // case: [x = 3]
                                        new_value = token.getValue();
                                        if (variableValueValidator(datatype, new_value)) {
                                            environment.setVariable(varname, new_value);
                                        } else {
                                            throw new CustomExceptions("Incorrect value" + token.getValue().toString());
                                        }
                                    }
                                    // add AssignmentNode to root
                                    rootNode.addChild(new AssignmentNode(varname, new_value));
                                    i++;
                                    token = tokensList.get(i);
                                } else {
                                    throw new CustomExceptions("Invalid assignment for variable '" + varname + "'.");
                                }
                            } else {
                                throw new CustomExceptions("Missing value for variable '" + varname + "'.");
                            }
                        } else {
                            throw new CustomExceptions("Variable " + token.getValue().toString() + " not initially declared");
                        }
                    }
                    break;
                case SCAN:
                    List<String> scanVariables = new ArrayList<>();
                    i++; // Move to the next token after SCAN
                    boolean commaExpected = false; // Flag to track if a comma is expected
                    while (i < tokensList.size() && tokensList.get(i).getType() != Token.TokenType.ENDLINE) {
                        Token scanToken = tokensList.get(i);
                        if (scanToken.getType() == Token.TokenType.VARIABLE) {
                            if (commaExpected) {
                                throw new CustomExceptions("Expected ',' between variables.");
                            }
                            scanVariables.add(scanToken.getValue());
                            commaExpected = true; // Set comma expected for the next iteration
                        } else if (scanToken.getType() == Token.TokenType.DELIMITER && scanToken.getValue().equals(",")) {
                            // If a comma is encountered, reset the comma expected flag
                            commaExpected = false;
                        } else {
                            throw new CustomExceptions("Unexpected token: " + scanToken.getValue());
                        }
                        i++; // Move to the next token
                    }
                    rootNode.addChild(new ScanNode(scanVariables));
                    break;
                }
            currentTokenIndex++;
            } catch (CustomExceptions e) {
                System.out.println("Custom exception caught: " + e.getMessage());
            }
        }
        return rootNode; // Return the root code.node of the AST
    }
    private void processVariableDeclaration(String datatype, List<String> variableNames, String value, ASTNode rootNode) throws CustomExceptions {
        for (String variableName : variableNames) {
            if (datatype.equals("INT") && value.matches("[0-9]+")) {
                VariableDeclarationNode intVariableNode = new VariableDeclarationNode("INT", variableName, value);
                rootNode.addChild(intVariableNode);
                environment.placeVariables(intVariableNode); // Place the variable into the environment
            } else if (datatype.equals("FLOAT") && value.matches("\\d*\\.\\d+")) {
                VariableDeclarationNode floatVariableNode = (new VariableDeclarationNode("FLOAT", variableName, value));
                rootNode.addChild(floatVariableNode);
                environment.placeVariables(floatVariableNode);
            } else if (datatype.equals("CHAR") && value.matches("'.'")) {
                VariableDeclarationNode charVariableNode = new VariableDeclarationNode("CHAR", variableName, value);
                rootNode.addChild(charVariableNode);
                environment.placeVariables(charVariableNode);
            } else if (datatype.equals("BOOL") && (value.equals("TRUE") || value.equals("FALSE"))) {
                if (!value.matches("(TRUE|FALSE)")) {
                    throw new CustomExceptions("Invalid value for BOOL variable '" + variableName + "'. It should be either \"TRUE\" or \"FALSE\".");
                }
                VariableDeclarationNode boolVariableNode = new VariableDeclarationNode("BOOL", variableName, value);
                rootNode.addChild(boolVariableNode);
                environment.placeVariables(boolVariableNode);
            }
        }
    }

    private boolean variableValueValidator(String datatype, Object values) {
        String value = values.toString();
        if (datatype.equals("INT") && value.matches("[0-9]+")) {
            return true;
        } else if (datatype.equals("FLOAT") && value.matches("\\d*\\.\\d+")) {
            return true;
        } else if (datatype.equals("CHAR") && value.matches("'.'")) {
            return true;
        } else if (datatype.equals("BOOL") && (value.equals("TRUE") || value.equals("FALSE"))) {
            return true;
        } else {
            return false;
        }
    }
//    private boolean isArithmeticOperator(Token token) {
//        return token.getValue().equals("+") || token.getValue().equals("-") ||
//                token.getValue().equals("*") || token.getValue().equals("/");
//    }
//
//    private Node parseArithmeticExpression(int currentIndex) throws CustomExceptions {
//        Token token = tokensList.get(currentIndex);
//        // Assuming token is an arithmetic operator here
//
//        BinaryOperationNode operationNode = new BinaryOperationNode(token.getValue());
//        currentIndex++; // Move to the next token after the operator
//
//        // Parse left expression
//        Node leftExpression = parsePrimary(currentIndex - 1);
//        operationNode.setLeftOperand(leftExpression);
//
//        // Parse right expression
//        Node rightExpression = parsePrimary(currentIndex + 1);
//        operationNode.setRightOperand(rightExpression);
//
//        return operationNode;
//    }
//
//    private Node parsePrimary(int currentIndex) throws CustomExceptions {
//        Token token = tokensList.get(currentIndex);
//        // Assuming token is a number or variable here
//
//        if (token.getType() == Token.TokenType.VALUE) {
//            // Create a node for the value
//            return new ValueNode(token.getValue());
//        } else if (token.getType() == Token.TokenType.VARIABLE) {
//            // Assuming you handle variable lookup
//            return new VariableNode(token.getValue());
//        } else {
//            throw new CustomExceptions("Invalid token in arithmetic expression");
//        }
//    }
}