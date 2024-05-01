package code.parser;

import code.ArithmeticOperations.ExpressionParser;
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
    public static boolean parseIfExpression(String expression) {

        return true;
    }
    public ASTNode produceAST() throws CustomExceptions {
        ASTNode rootNode = new ASTNode(); // Create the root code.node

        for (int i = 0; i < tokensList.size(); i++) {
            Token token = tokensList.get(i);

            try {
                switch (token.getType()) {
                    case DELIMITER:
                        Token nextToken = tokensList.get(i+1);
                        // TODO: REVAMP BEGIN CODE TOKEN PARSING
                        if (Objects.equals(String.valueOf(token.getValue()), "BEGIN CODE")) {
                            // Add logic for processing "BEGIN CODE" sequence
                            rootNode.addChild(new DelimiterNode("BEGIN CODE", true));
                            i++; // Move to the next token after "CODE"
                        }
                        if(Objects.equals(String.valueOf(token.getValue()), "END CODE")) {
                                // Add logic for processing "END CODE" sequence
                                rootNode.addChild(new DelimiterNode("END CODE", false));
                                i++; // Move to the next token after "CODE"
                        }
                        if (Objects.equals(String.valueOf(token.getValue()), "IF")) {
                            // Parse the condition expression for the if statement
                            ExpressionParser expressionParser = new ExpressionParser(environment);
                            i++; // Move to the next token after "IF"
                            // Skip tokens until reaching the opening parenthesis "("
                            while (i < tokensList.size() && !Objects.equals(tokensList.get(i).getType(), Token.TokenType.EXPRESSION)) {
                                i++; // Move to the next token
                            }
//                            System.out.println(tokensList.get(i)); // should print out EXPRESSION token value
                            String expression = token.getValue();
                            boolean conditionResult = expressionParser.evaluateLogicalExpression(expression);
//                                // Create the IfNode with the evaluated condition
                                IfNode ifNode = new IfNode(conditionResult);
//                                // Add the IfNode to the AST
                                List<Token> ifBlockTokens = new ArrayList<>(); // store all tokens inside if BLOCk
                            i = i + 4; // i = i + 2 should be of value BEGIN IF immed. after token IF
                            // Iterate through tokens until 'END IF' is encountered
                            // should handle exception if i = i + 4 is not of token BEGIN IF
                            // LAZY ALGO GAMING I bet daghan kaynig problema unya
                            while (i < tokensList.size() && !Objects.equals(tokensList.get(i).getValue(), "end of line")) {
                                // Add tokens to the if block list
                                ifBlockTokens.add(tokensList.get(i));
                                i++;
                            }
                            // Print all tokens inside the if block
                            for (Token tokenP : ifBlockTokens) {
                                System.out.println(tokenP);
                            }

                            Parser ifBlockParser = new Parser(ifBlockTokens, environment);
                            ASTNode ifBlockAST = ifBlockParser.produceAST();
                            ifNode.setIfBlock(ifBlockAST);
                            rootNode.addChild(ifNode);

//                            System.out.println(ifBlockTokens);
                            // TODO: FIND OUT HOW TO ADD IF BLOCK TO NODE
                            // Currently, you're inside BEGIN IF
//                                rootNode.addChild(ifNode);

                        }
                        break;
                    case DATATYPE:
                        if (i + 1 < tokensList.size()) {
                            String datatype = token.getValue();
                            List<String> variableNames = new ArrayList<>();
                            String value = null;
                            boolean assigned = false;
                            boolean decleared = false;

                        if (token.getValue().equals("INT") || token.getValue().equals("FLOAT")) {
                            while (token.getType() != Token.TokenType.ENDLINE && i < tokensList.size()) { // Iterate through statement
                                if (token.getType() == Token.TokenType.VARIABLE) { // Store variable
                                    variableNames.add(token.getValue());
                                    decleared = false;
                                } else if (token.getType() == Token.TokenType.EXPRESSION) { // Store value
//                                         value = token.getValue();
//                                       System.out.println("Token:   " + token.getValue());
                                    String expression = token.getValue();
                                    ExpressionParser expressionParser = new ExpressionParser(environment);
                                    String result = String.valueOf(expressionParser.evaluateExpression(expression));
//                                       System.out.println("RESULT:   " + result);
                                    processVariableDeclaration(datatype, variableNames, result, rootNode); // Call the method
                                    variableNames.clear(); // Clean list
                                    decleared = true;
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
                        }

                        else if (token.getValue().equals("CHAR") || token.getValue().equals("BOOL")) {
                            while (token.getType() != Token.TokenType.ENDLINE && i < tokensList.size()) {
                                if (token.getType() == Token.TokenType.VARIABLE) { // Store variable
                                    variableNames.add(token.getValue());
                                    decleared = false;
                                } else if (token.getType() == Token.TokenType.EXPRESSION) {
                                    value = token.getValue();
                                    if ((datatype.equals("BOOL") && value.startsWith("\"") && value.endsWith("\""))) {
                                        value = value.substring(1, value.length() - 1);
                                        value = value.toUpperCase(); // Convert boolean value to uppercase
                                    } // this 'if' statement checks if it has "TRUE" or "FALSE"

                                    if (datatype.equals("BOOL") && hasLogicalOperator(value)) {
                                        // Handle expressions with logical operators separately
                                        // You can add your logic here
                                        System.out.println("TOKEEENN VALUEEE:    " + token.getValue());
                                        String logicalExpression = token.getValue();
                                        ExpressionParser expressionParser = new ExpressionParser(environment);
                                        String resultL = String.valueOf(expressionParser.evaluateLogicalExpression(logicalExpression)).toUpperCase();
                                        processVariableDeclaration(datatype, variableNames, resultL, rootNode); // Call the method
                                        variableNames.clear(); // Clean list
                                        decleared = true;
                                    } else {
                                        // Handle expressions without logical operators
                                        if (variableValueValidator(datatype, value)){
                                            processVariableDeclaration(datatype, variableNames, value, rootNode); // Call the method
                                            variableNames.clear(); // Clean list
                                            decleared = true;
                                        } else {
                                            // TODO: throw an exception
//                                            System.out.println("NOOOOOO WAYYYYYY");
                                        }
                                    }
                                } else if (token.getType() == Token.TokenType.ASSIGN) {
                                    assigned = true;
                                }

                                // Move to the next token
                                i++;
                                if (i < tokensList.size()) {
                                    token = tokensList.get(i);
                                }
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
                                } else {variableNames.add(BText.toString());}

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
                    String varname = token.getValue();
                    Object new_value = null;
                    String dupe_value = null;
                    String datatype = null;
                    while (token.getType() != Token.TokenType.ENDLINE && i < tokensList.size()) {   //Iterates the whole statement
                        //System.out.println("randomhskejklqwejlqkjeklqeqwe1   "+token.getValue().toString());
                        if (token.getType() == Token.TokenType.ASSIGN) { // case: [x = y = z]
                            token = tokensList.get(i-1); // set y as initial
                            i--;
                            varname = token.getValue().toString();
                        }
                        if (token.getType() == Token.TokenType.EXPRESSION) {
                            token = tokensList.get(++i);
                            continue;
                        }
//                        System.out.println("BEGIN AGAIN:     "+token.getValue());
                        if (environment.isDefined(token.getValue().toString())) { // check if variable exist [x is declared]
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
                                    if (token.getType() == Token.TokenType.VARIABLE) { // case1: [x = y]
//                                        System.out.println("case 1: ");
                                        if (tokensList.get(++i).getType() == Token.TokenType.OPERATOR) { // case: [x = 1 + 2]
                                            while(token.getType() != Token.TokenType.EXPRESSION) {
                                                // get next token
                                                i++;
                                                token = tokensList.get(i);
                                            }
                                            String expression = token.getValue();
                                            ExpressionParser expressionParser = new ExpressionParser(environment);
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
                                            while(token.getType() != Token.TokenType.EXPRESSION) {
                                                // get next token
                                                i++;
                                                token = tokensList.get(i);
                                            }
                                            String expression = token.getValue();
                                            ExpressionParser expressionParser = new ExpressionParser(environment);
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
                    i++; // DEBUG GAMING
                    boolean commaExpected = false; // Flag to track if a comma is expected
                    while (i < tokensList.size() && tokensList.get(i).getType() != Token.TokenType.ENDLINE) {
                        Token scanToken = tokensList.get(i);
//                        System.out.println(scanToken);

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

//    private boolean compareValues(Object value1, Object value2, String operator) {
//        // Handle different comparison operators
//        switch (operator) {
//            case "==":
//                return value1.equals(value2);
//            case "!=":
//                return !value1.equals(value2);
//            case ">":
//                if (value1 instanceof Comparable && value2 instanceof Comparable) {
//                    return ((Comparable) value1).compareTo(value2) > 0;
//                }
//                break;
//            case ">=":
//                if (value1 instanceof Comparable && value2 instanceof Comparable) {
//                    return ((Comparable) value1).compareTo(value2) >= 0;
//                }
//                break;
//            case "<":
//                if (value1 instanceof Comparable && value2 instanceof Comparable) {
//                    return ((Comparable) value1).compareTo(value2) < 0;
//                }
//                break;
//            case "<=":
//                if (value1 instanceof Comparable && value2 instanceof Comparable) {
//                    return ((Comparable) value1).compareTo(value2) <= 0;
//                }
//                break;
//            default:
//                System.out.println("Unsupported comparison operator: " + operator);
//        }
//        return false; // Default return value if comparison fails
//    }
}