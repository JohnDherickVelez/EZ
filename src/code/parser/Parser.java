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
                        if (i + 3 < tokensList.size()) {
                            String varname = tokensList.get(i + 1).getValue();
                            String operator = tokensList.get(i + 2).getValue();
                            String value = tokensList.get(i + 3).getValue();

                            if (operator.equals("=")) {
                                if (token.getValue().equals("INT") && value.matches("[0-9]+")) {
                                    VariableDeclarationNode intVariableNode = new VariableDeclarationNode("INT", varname, value);
                                    rootNode.addChild(intVariableNode);
                                    environment.placeVariables(intVariableNode); // Place the variable into the environment
                                }
                                if (token.getValue().equals("FLOAT") && value.matches("\\d*\\.\\d+")) {
                                    VariableDeclarationNode floatVariableNode = (new VariableDeclarationNode("FLOAT", varname, value));
                                    rootNode.addChild(floatVariableNode);
                                    environment.placeVariables(floatVariableNode);
                                }
                                if (token.getValue().equals("CHAR") && value.matches("'.'")) {
                                    VariableDeclarationNode charVariableNode = (new VariableDeclarationNode("CHAR", varname, value));
                                    rootNode.addChild(charVariableNode);
                                    environment.placeVariables(charVariableNode);
                                }
                                if (token.getValue().equals("BOOL") && (value.equals("true") || value.equals("false"))) {
                                    VariableDeclarationNode boolVariableNode = (new VariableDeclarationNode("BOOL", varname, value));
                                    rootNode.addChild(boolVariableNode);
                                    environment.placeVariables(boolVariableNode);
                                }
                            }
                        }
                    }
                    break;

                case DISPLAY:
                    try {
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
                    } catch (CustomExceptions e) {
                        System.out.println("Custom exception caught: " + e.getMessage());
                    }
                    break;
            }
        }
        return rootNode; // Return the root code.node of the AST
    }
}