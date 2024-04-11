package code.parser;

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

//    public Parser(List<Token> tokensList) {
//        this.tokensList = tokensList;
//    }
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

                            if(!decleared && !assigned) { // For: int x, y, z = 3
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
                        StringBuilder stringBuilder = new StringBuilder();
                        boolean isFirstVariable = true; // Flag to track if it's the first variable
                        while (i < tokensList.size() && tokensList.get(i).getType() != Token.TokenType.ENDLINE) {
                            Token displayToken = tokensList.get(i);
                            if (displayToken.getType() == Token.TokenType.VARIABLE) {
                                // If the token is a variable, and it's not the first variable or if it's the first variable itself, add its name to the variableList
                                if (!isFirstVariable) {
                                    throw new CustomExceptions("Expected '&' token between variables.");
                                } else {
                                    variableList.add(displayToken.getValue());
                                    isFirstVariable = false; // Reset the flag after processing the first variable
                                }
                            } else if (displayToken.getType() == Token.TokenType.OPERATOR && displayToken.getValue().equals("&")) {
                                // Check if there's a variable after '&'
                                if (i + 1 < tokensList.size() && tokensList.get(i + 1).getType() == Token.TokenType.VARIABLE) {
                                    variableList.add(tokensList.get(i + 1).getValue());
                                    i++; // Move to the next token as we've already processed the variable after '&'
                                } else {
                                    throw new CustomExceptions("Expected variable after '&' token.");
                                }
                            }
                            i++; // Move to the next token
                        }
                        for (String varName : variableList) {
                            Object value = environment.getVariable(varName);
                            if (value != null) {
                                stringBuilder.append(value);
                            } else {
                                throw new CustomExceptions("Value not inside environment! " + varName);
                            }
                        }
                        rootNode.addChild(new DisplayNode(stringBuilder.toString()));
                    break;

                case VARIABLE:
                    if (environment.isDefined(token.getValue().toString())) { // check if variable exist [int x]
                        String varname = token.getValue();
                        Object new_value = null;
                        i++; // Move to the next token
                        if (i < tokensList.size()) {
                            token = tokensList.get(i);
                            if (token.getType() == Token.TokenType.ASSIGN) { // if encountered an assign operator
                                i++;
                                token = tokensList.get(i);
                                // Update the variable value in the environment
                                if (token.getType() == Token.TokenType.VARIABLE) { // for: [ x = y ]
                                    // check if [y] exist
                                    if (environment.isDefined(token.getValue().toString())) {
                                        new_value = environment.getVariable(token.getValue().toString()); // update value in the environment
                                        environment.setVariable(varname, new_value);
                                    } else {
                                        throw new CustomExceptions("Variable "+ token.getValue().toString() +" not initially declared");
                                    }
                                } else if (token.getType() == Token.TokenType.VALUE) { // for: [ x = 3 ]
                                    new_value = token.getValue();
                                    environment.setVariable(varname, new_value);
                                }
                                // add AssignmentNode to root
                                rootNode.addChild(new AssignmentNode(varname,new_value));
                            } else {
                                throw new CustomExceptions("Invalid assignment for variable '" + varname + "'.");
                            }
                        } else {
                            throw new CustomExceptions("Missing value for variable '" + varname + "'.");
                        }
                    } else {
                        throw new CustomExceptions("Variable "+ token.getValue().toString() +" not initially declared");
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
            } else if (datatype.equals("BOOL") && (value.equals("true") || value.equals("false"))) {
                VariableDeclarationNode boolVariableNode = new VariableDeclarationNode("BOOL", variableName, value);
                rootNode.addChild(boolVariableNode);
                environment.placeVariables(boolVariableNode);
            }
        }
    }
}