package code.parser;

import code.ArithmeticOperations.ExpressionParser;
import code.ArithmeticOperations.ExpressionParser2;
import code.Environment.Environment;
import code.model.Token;
import code.node.*;

import java.util.*;

public class Parser2 {
    private List<Token> tokensList;
    private int currentTokenIndex = 0;

    private List<Node> ASTNode = new ArrayList<>();
    private List<String> variableList = new ArrayList<>();
    private Environment environment;

    public Parser2(List<Token> tokensList, Environment environment) {
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
                        if (Objects.equals(String.valueOf(token.getValue()), "BEGIN CODE")) {
                            // Add logic for processing "BEGIN CODE" sequence
                            rootNode.addChild(new DelimiterNode("BEGIN CODE", true));
                            i++; // Move to the next token after "CODE"
                        } else if (Objects.equals(String.valueOf(token.getValue()), "END CODE")) {
                            // Add logic for processing "END CODE" sequence
                            rootNode.addChild(new DelimiterNode("END CODE", false));
                            i++; // Move to the next token after "CODE"
                        } else if (Objects.equals(String.valueOf(token.getValue()), "IF")) {
                            // Handle case IF
                            i++;
                            while (i < tokensList.size() && token.getType() != Token.TokenType.EXPRESSION) {
                                i++;
                                if (i < tokensList.size()) {
                                    token = tokensList.get(i);
                                }
                            }
//                            if (token.getType() == Token.TokenType.EXPRESSION) {
//                                // Process the IF expression
//                                String ifExpression = token.getValue();
//                                ExpressionParser2 expressionParser = new ExpressionParser2(environment);
//                                boolean ifResult = expressionParser.evaluateLogicalExpression(ifExpression);
//                                System.out.println(ifResult);
//                                i++; // Move to the next token after the expression
//
//                                if (!ifResult) {
//                                    // Skip the IF block
//                                    if (Objects.equals(tokensList.get(i).getValue(), "BEGIN IF")) {
//                                        int beginCount = 1;
//                                        while (i < tokensList.size() && beginCount > 0) {
//                                            i++;
//                                            Token nextToken = tokensList.get(i);
//                                            if (Objects.equals(nextToken.getValue(), "BEGIN IF")) {
//                                                beginCount++;
//                                            } else if (Objects.equals(nextToken.getValue(), "END IF")) {
//                                                beginCount--;
//                                            }
//                                        }
//                                        if (i < tokensList.size() && Objects.equals(tokensList.get(i).getValue(), "END IF")) {
//                                            i++; // Skip "END IF"
//                                        }
//                                    }
//                                    // Process the ELSE block if it exists
//                                    if (i < tokensList.size() && Objects.equals(tokensList.get(i).getValue(), "ELSE")) {
//                                        i++; // Skip "ELSE"
//                                        if (Objects.equals(tokensList.get(i).getValue(), "BEGIN IF")) {
//                                            i++; // Skip "BEGIN IF"
//                                            // Let the parser continue processing the ELSE block
//                                            int beginCount = 1;
//                                            while (i < tokensList.size() && beginCount > 0) {
//                                                i++;
//                                                Token innerToken = tokensList.get(i);
//                                                if (Objects.equals(innerToken.getValue(), "BEGIN IF")) {
//                                                    beginCount++;
//                                                } else if (Objects.equals(innerToken.getValue(), "END IF")) {
//                                                    beginCount--;
//                                                }
//                                            }
//                                            if (i < tokensList.size() && Objects.equals(tokensList.get(i).getValue(), "END IF")) {
//                                                i++; // Skip "END IF"
//                                            }
//                                        }
//                                    }
//                                }
//                            } else {
//                                throw new CustomExceptions("Expected expression after IF");
//                            }
//                        }
//                        break;

//                    case DELIMITER:
//                        boolean isTrue = false;
//
//                        if (Objects.equals(String.valueOf(token.getValue()), "BEGIN CODE")) {
//                            // Add logic for processing "BEGIN CODE" sequence
//                            rootNode.addChild(new DelimiterNode("BEGIN CODE", true));
//                            i++; // Move to the next token after "CODE"
//                        }
//                        if(Objects.equals(String.valueOf(token.getValue()), "END CODE")) {
//                            // Add logic for processing "END CODE" sequence
//                            rootNode.addChild(new DelimiterNode("END CODE", false));
//                            i++; // Move to the next token after "CODE"
//                        }
//                        if (Objects.equals(String.valueOf(token.getValue()), "IF")) {
//                            // I want you to handle case IF
//                            i++;
//                            while (i < tokensList.size() && token.getType() != Token.TokenType.EXPRESSION) {
//                                i++;
//                                if (i < tokensList.size()) {
//                                    token = tokensList.get(i);
//                                }
//                            }
                            if (token.getType() == Token.TokenType.EXPRESSION) {
                                // Process the IF expression
                                String ifExpression = token.getValue();
//                                System.out.println("Processing IF expression: " + ifExpression);

                                ExpressionParser2 expressionParser = new ExpressionParser2(environment);
                                String ifResult = String.valueOf(expressionParser.evaluateLogicalExpression(ifExpression));
                                System.out.println(ifResult);
                                i+=2;
                                System.out.println(tokensList.get(i));

                                if (ifResult.equals("false")) {
                                    if (Objects.equals(tokensList.get(i).getValue(), "BEGIN IF")) {
                                        int beginCount = 1; // To track nested "BEGIN IF"
                                        while (i < tokensList.size()) {
                                            i++; // Move to the next token
                                            Token nextToken = tokensList.get(i);
                                            if (Objects.equals(nextToken.getValue(), "BEGIN IF")) {
                                                beginCount++;
                                            } else if (Objects.equals(nextToken.getValue(), "END IF")) {
                                                beginCount--;
                                                if (beginCount == 0) {
                                                    // Found the matching "END IF", exit the loop
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                throw new CustomExceptions("Expected expression after IF");
                            }

                        }
                        break;
                    case DATATYPE:
                        if (i + 1 < tokensList.size()) {
                            String datatype = token.getValue();
                            List<String> variableNames = new ArrayList<>();
                            String value = null;
                            boolean assigned = false;
                            boolean declared = false;

                            while (token.getType() != Token.TokenType.ENDLINE && i < tokensList.size()) { // Iterate through statement
                                if (token.getType() == Token.TokenType.VARIABLE) {
                                    if (!assigned) {
                                        // Only add variables to the list if not after an assignment token
                                        variableNames.add(token.getValue());
                                    }
                                    declared = false;
                                } else if (token.getType() == Token.TokenType.EXPRESSION) {
                                    String expression = token.getValue();
                                    ExpressionParser2 expressionParser = new ExpressionParser2(environment);
                                    String result;

                                    // Evaluate expression based on datatype
                                    if (datatype.equals("INT")) {
                                        result = String.valueOf((int) expressionParser.calculate(expression)); // Cast to int
                                    } else if (datatype.equals("FLOAT")) {
                                        result = String.valueOf(expressionParser.calculate(expression));
                                    } else if (datatype.equals("CHAR") || datatype.equals("BOOL")) {
                                        value = token.getValue();
                                        if (datatype.equals("BOOL")) {
                                            if (value.startsWith("\"") && value.endsWith("\"")) {
                                                value = value.substring(1, value.length() - 1).toUpperCase(); // Convert boolean value to uppercase
                                            }
                                            if (hasLogicalOperator(value)) {
                                                String logicalExpression = token.getValue();
                                                result = String.valueOf(expressionParser.evaluateLogicalExpression(logicalExpression)).toUpperCase();
                                            } else {
                                                if (variableValueValidator(datatype, value)) {
                                                    result = value;
                                                } else {
                                                    throw new IllegalArgumentException("Invalid boolean expression: " + value);
                                                }
                                            }
                                        } else if (datatype.equals("CHAR")) {
                                            if (value.length() == 1) {
                                                result = value;
                                            } else {
                                                throw new IllegalArgumentException("Invalid character value: " + value);
                                            }
                                        } else {
                                            throw new IllegalArgumentException("Unsupported datatype: " + datatype);
                                        }
                                    } else {
                                        throw new IllegalArgumentException("Unsupported datatype: " + datatype);
                                    }

                                    processVariableDeclaration(datatype, variableNames, result, rootNode); // Call the method
                                    variableNames.clear(); // Clean list
                                    declared = true;
                                } else if (token.getType() == Token.TokenType.ASSIGN) {
                                    assigned = true;
                                } else if (token.getType() == Token.TokenType.ENDLINE) {
                                    processVariableDeclaration(datatype, variableNames, String.valueOf(0), rootNode); // Call the method
                                }

                                // Move to the next token
                                i++;
                                if (i < tokensList.size()) {
                                    token = tokensList.get(i);
                                }
                            }
                            if (!declared && !assigned) { // For: int x, y, z
                                processVariableDeclaration(datatype, variableNames, value, rootNode); // Call the method
                            } else if (!declared && assigned) {
                                throw new CustomExceptions("No value assigned to variable");
                            }

                        } else {
                            // Handle unexpected token (not an identifier)
                            throw new CustomExceptions("Expected identifier for variable name.");
                        }
                        break;

                    case DISPLAY:
                        List<String> variableNames = new ArrayList<>();
                        // Iterate through tokens until the end of the DISPLAY statement
                        while (i < tokensList.size() && tokensList.get(i).getType() != Token.TokenType.ENDLINE) {
                            Token displayToken = tokensList.get(i);
                            if (displayToken.getType() == Token.TokenType.VARIABLE) {
                                // If the token is a variable, add its name to the variableNames list
                                variableNames.add(displayToken.getValue());

                            } else if (displayToken.getType() == Token.TokenType.OPERATOR && displayToken.getValue().equals("&")) {
                                // Check if there's a variable after '&'
                                if (i + 1 < tokensList.size() && tokensList.get(i + 1).getType() == Token.TokenType.VARIABLE
                                        || tokensList.get(i + 1).getType() == Token.TokenType.TEXT
                                        || tokensList.get(i + 1).getType() == Token.TokenType.IDENTIFIER
                                        || tokensList.get(i + 1).getType() == Token.TokenType.OPERATOR
                                        || tokensList.get(i + 1).getType() == Token.TokenType.ESCAPE) {
                                    variableNames.add(tokensList.get(i + 1).getValue());
                                    i++; // Move to the next token as we've already processed the variable after '&'
                                } else if (i + 1 < tokensList.size() && tokensList.get(i + 1).getType() ==
                                        Token.TokenType.OPERATOR && tokensList.get(i + 1).getValue().equals("$")) {
                                    variableNames.add("$");
                                } else {
                                    throw new CustomExceptions("Expected variable after '&' token.");
                                }
                            } else if (displayToken.getType() == Token.TokenType.ESCAPE) {
                                StringBuilder BText = new StringBuilder();
                                String text = displayToken.getValue();
//                                System.out.println("quoted string:      " + text);
                                if (text.startsWith("[") && text.endsWith("]")) {
                                    BText.append(text);
                                }

                                if (tokensList.get(++i).getType() == Token.TokenType.IDENTIFIER &&
                                        tokensList.get(i).getValue().equals("]")) {
                                    // Append the collected quoted text as a single variable name
                                    BText.append(tokensList.get(i).getValue());
                                    variableNames.add(BText.toString());
                                } else {
                                    variableNames.add(BText.toString());
                                }

                            } else if (displayToken.getType() == Token.TokenType.TEXT) {
                                String text = displayToken.getValue();
//                                System.out.println("quoted string:      " + text);
                                if (text.startsWith("\"") && text.endsWith("\"")) {
                                    variableNames.add(text);
                                }
                            }
                            i++;
                        }
                        // Create a new DisplayNode with the list of variable names
                        rootNode.addChild(new DisplayNode(variableNames));
                        break;
                    case VARIABLE:
                        System.out.println("Case Variable found: " + token.getValue());
                        String varname = token.getValue();
                        Object new_value = null;
                        String dupe_value = null;
                        String datatype = null;
                        while (token.getType() != Token.TokenType.ENDLINE && i < tokensList.size()) {   //Iterates the whole statement
                            if (token.getType() == Token.TokenType.ASSIGN) { // case: [x = y = z]
                                token = tokensList.get(i - 1); // set y as initial
                                i--;
                                varname = token.getValue().toString();
                            }
                            if (token.getType() == Token.TokenType.EXPRESSION) {
                                token = tokensList.get(++i);
                                continue;
                            }
                            if (environment.isDefined(token.getValue().toString())) { // check if variable exist [x is declared]
                                i++; // Move to the next token
                                if (i < tokensList.size()) {
                                    token = tokensList.get(i);
                                    if (token.getType() == Token.TokenType.ASSIGN) { // if encountered an assignment operator
                                        i++;
                                        token = tokensList.get(i);
                                        datatype = environment.getVariableType(varname);
                                        // Update the variable value in the environment
                                        if (token.getType() == Token.TokenType.VARIABLE) { // case1: [x = y]
                                            if (tokensList.get(++i).getType() == Token.TokenType.OPERATOR) { // case: [x = 1 + 2]
                                                while (token.getType() != Token.TokenType.EXPRESSION) {
                                                    // get next token
                                                    i++;
                                                    token = tokensList.get(i);
                                                }
                                                String expression = token.getValue();
                                                ExpressionParser2 expressionParser = new ExpressionParser2(environment);
                                                new_value = String.valueOf(expressionParser.evaluateExpression(expression));
                                                environment.setVariable(varname, new_value);
                                            } else {
                                                // check if [y] exist
                                                if (environment.isDefined(token.getValue().toString())) { // TODO: currently allows words
                                                    new_value = environment.getVariable(token.getValue()); // update value in the environment
                                                    dupe_value = new_value.toString();
                                                    if (variableValueValidator(datatype, dupe_value)) {
                                                        environment.setVariable(varname, new_value);
                                                    } else {
                                                        throw new CustomExceptions("Incorrect value" + token.getValue().toString());
                                                    }
                                                } else {
                                                    throw new CustomExceptions("Variable " + token.getValue().toString() + " not initially declared");
                                                }
                                            }
                                        } else if (token.getType() == Token.TokenType.VALUE) { // case2: [x = 3]
//                                        System.out.println("case 2: " );
                                            if (tokensList.get(++i).getType() == Token.TokenType.OPERATOR) { // case: [x = 1 + 2]
//                                            System.out.println("OPERATORRRRRRR");
                                                while (token.getType() != Token.TokenType.EXPRESSION) {
                                                    // get next token
                                                    i++;
                                                    token = tokensList.get(i);
                                                }
                                                String expression = token.getValue();
                                                ExpressionParser2 expressionParser = new ExpressionParser2(environment);
                                                new_value = String.valueOf(expressionParser.evaluateExpression(expression));
                                                environment.setVariable(varname, new_value);
                                            } else {
                                                new_value = token.getValue();
                                                dupe_value = new_value.toString();
                                                if (variableValueValidator(datatype, dupe_value)) {
                                                    environment.setVariable(varname, new_value);
                                                } else {
                                                    throw new CustomExceptions("Incorrect value" + token.getValue().toString());
                                                }
                                            }
                                        }
                                        // add AssignmentNode to root
//                                    System.out.println("TOKEN curr:   " + token.getValue());
                                        rootNode.addChild(new AssignmentNode(varname, new_value));
                                        token = tokensList.get(i);
//                                    System.out.println("TOKEN ++:   " + token.getValue());
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
//                System.out.println(intVariableNode);
            } else if (datatype.equals("FLOAT") && value.matches("\\d*\\.\\d+")) {
                VariableDeclarationNode floatVariableNode = (new VariableDeclarationNode("FLOAT", variableName, value));
                rootNode.addChild(floatVariableNode);
                environment.placeVariables(floatVariableNode);
            } else if (datatype.equals("CHAR") && value.matches("'.'")) {
                // Adjusted regular expression to allow for any single character between single quotes
                if (value.length() == 3 && value.charAt(0) == '\'' && value.charAt(2) == '\'') {
                    // Extract the character from between the single quotes
                    char charValue = value.charAt(1);
                    VariableDeclarationNode charVariableNode = new VariableDeclarationNode("CHAR", variableName, String.valueOf(charValue));
                    rootNode.addChild(charVariableNode);
                    environment.placeVariables(charVariableNode);
                } else {
                    throw new CustomExceptions("Invalid format for CHAR variable '" + variableName + "'.");
                }
            } else if (datatype.equals("BOOL") && (value.equals("TRUE") || value.equals("FALSE"))) {
                value = value.toUpperCase();
                if (!value.matches("(TRUE|FALSE)")) {
                    throw new CustomExceptions("Invalid value for BOOL variable '" + variableName + "'. It should be either \"TRUE\" or \"FALSE\".");
                }
                VariableDeclarationNode boolVariableNode = new VariableDeclarationNode("BOOL", variableName, value.toUpperCase());
                rootNode.addChild(boolVariableNode);
                environment.placeVariables(boolVariableNode);
            }
        }
    }

    private boolean variableValueValidator(String datatype, String value) {
//        System.out.println("datatype:  " + datatype + "    value:   " + value);
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

    private boolean hasLogicalOperator(String str) {
        // Define an array of logical operators
        String[] logicalOperators = {"<", ">", ">=", "<=", "==", "<>", "AND", "OR", "NOT"};

        // Check if the string contains any logical operator
        for (String operator : logicalOperators) {
            if (str.contains(operator)) {
                return true;
            }
        }

        return false;
    }

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+");
    }

    private boolean isOperator(String str) {
        return str.matches("[-+*/]");
    }
}
