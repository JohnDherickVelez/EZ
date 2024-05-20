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
                            if (token.getType() == Token.TokenType.EXPRESSION) {
                                // Process the IF expression
                                String ifExpression = token.getValue();
//                                System.out.println("Processing IF expression: " + ifExpression);

                                ExpressionParser2 expressionParser = new ExpressionParser2(environment);
                                boolean ifResult = expressionParser.evaluateLogicalExpression(ifExpression);
////                                System.out.println(ifResult);
////                                System.out.println(tokensList.get(i).getValue());
//
//                                IfNode ifNode = new IfNode(ifResult);
//                                i += 2; // Skip to the token after "BEGIN IF"
//                                if (Objects.equals(tokensList.get(i).getValue(), "BEGIN IF")) {
//                                    int ifBlockStart = i;
//                                    int beginCount = 1; // Track nested "BEGIN IF"
//                                    while (i < tokensList.size() && beginCount > 0) {
//                                        i++;
//                                        if (Objects.equals(tokensList.get(i).getValue(), "BEGIN IF")) {
//                                            beginCount++;
//                                        } else if (Objects.equals(tokensList.get(i).getValue(), "END IF")) {
//                                            beginCount--;
//                                        }
//                                    }
//                                    if (beginCount == 0 && Objects.equals(tokensList.get(i).getValue(), "END IF")) {
//                                        i++;
//                                    }
//                                    if(ifNode.getExpressionResult()) {
//                                        List<Token> ifBlockTokens = tokensList.subList(ifBlockStart + 1, i - 1);
//                                        Parser2 ifParser = new Parser2(ifBlockTokens, environment);
//                                        ASTNode ifBlockAST = ifParser.produceAST();
//                                        ifNode.addChild(ifBlockAST);
//                                    }
//                                }
//                                rootNode.addChild(ifNode);
//                            } else {
//                                throw new CustomExceptions("Expected expression after IF");
//                            }
//                        } else if(Objects.equals(String.valueOf(token.getValue()), "ELSE IF")){
//                            System.out.println("biatch");
//                        }
//                        break;

                                // DONT TOUCH!!
                                // IF STATEMENT IS WORKING
                                if (!ifResult) {
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
                            boolean decleared = false;


//                            while (token.getType() != Token.TokenType.ENDLINE && i < tokensList.size()) { // Iterate through statement
//                                if (token.getType() == Token.TokenType.VARIABLE) {
//                                    if (!assigned) {
//                                        // Only add variables to the list if not after an assignment token
//                                        variableNames.add(token.getValue());
//                                    }
//                                    declared = false;
//                                } else if (token.getType() == Token.TokenType.EXPRESSION) {
//                                    String expression = token.getValue();
//                                    ExpressionParser2 expressionParser = new ExpressionParser2(environment);
//                                    String result;
//
//                                    // Evaluate expression based on datatype
//                                    if (datatype.equals("INT")) {
//                                        result = String.valueOf((int) expressionParser.calculate(expression)); // Cast to int
//                                    } else if (datatype.equals("FLOAT")) {
//                                        result = String.valueOf(expressionParser.calculate(expression));
//                                    } else if (datatype.equals("CHAR") || datatype.equals("BOOL")) {
//                                        value = token.getValue();
//                                        if (datatype.equals("BOOL")) {
//                                            if (value.startsWith("\"") && value.endsWith("\"")) {
//                                                value = value.substring(1, value.length() - 1).toUpperCase(); // Convert boolean value to uppercase
//                                            }
//                                            if (hasLogicalOperator(value)) {
//                                                String logicalExpression = token.getValue();
//                                                result = String.valueOf(expressionParser.evaluateLogicalExpression(logicalExpression)).toUpperCase();
//                                            } else {
//                                                if (variableValueValidator(datatype, value)) {
//                                                    result = value;
//                                                } else {
//                                                    throw new IllegalArgumentException("Invalid boolean expression: " + value);
//                                                }
//                                            }
//                                        } else if (datatype.equals("CHAR")) {
//                                            if (value.length() == 1) {
//                                                result = value;
//                                            } else {
//                                                throw new IllegalArgumentException("Invalid character value: " + value);
//                                            }
//                                        } else {
//                                            throw new IllegalArgumentException("Unsupported datatype: " + datatype);
//                                        }
//                                    } else {
//                                        throw new IllegalArgumentException("Unsupported datatype: " + datatype);
//                                    }
//
//                                    processVariableDeclaration(datatype, variableNames, result, rootNode); // Call the method
//                                    variableNames.clear(); // Clean list
//                                    declared = true;
//                                } else if (token.getType() == Token.TokenType.ASSIGN) {
//                                    assigned = true;
//                                } else if (token.getType() == Token.TokenType.ENDLINE) {
//                                    processVariableDeclaration(datatype, variableNames, String.valueOf(0), rootNode); // Call the method
//                                }
//
//                                // Move to the next token
//                                i++;
//                                if (i < tokensList.size()) {
//                                    token = tokensList.get(i);
//                                }
//                            }
//                            if (!declared && !assigned) { // For: int x, y, z
//                                processVariableDeclaration(datatype, variableNames, value, rootNode); // Call the method
//                            } else if (!declared && assigned) {
//                                throw new CustomExceptions("No value assigned to variable");
//                            }
//
//                        } else {
//                            // Handle unexpected token (not an identifier)
//                            throw new CustomExceptions("Expected identifier for variable name.");
//                        }
//                        break;
                            if (token.getValue().equals("INT") || token.getValue().equals("FLOAT")) {
                                while (token.getType() != Token.TokenType.ENDLINE && i < tokensList.size()) { // Iterate through statement
                                    if (token.getType() == Token.TokenType.VARIABLE) { // Store variable
                                        variableNames.add(token.getValue());
                                        decleared = false;
                                    } else if (token.getType() == Token.TokenType.EXPRESSION) { // Store value
//                                          value = token.getValue();
//                                        System.out.println("Token:   " + token.getValue());
                                        String expression = token.getValue();
//                                        System.out.println(expression);
                                        ExpressionParser2 expressionParser = new ExpressionParser2(environment);
                                        if(Objects.equals(datatype, "INT")) {
                                            String result = String.valueOf((int) expressionParser.calculate(expression));
                                            processVariableDeclaration(datatype, variableNames, result, rootNode); // Call the method
                                            variableNames.clear(); // Clean list
                                        }
                                        if(Objects.equals(datatype, "FLOAT")) {
                                            String result = String.valueOf(expressionParser.calculate(expression));
                                            processVariableDeclaration(datatype, variableNames, result, rootNode); // Call the method
                                            variableNames.clear(); // Clean list
                                            decleared = true;
                                        }


//                                        processVariableDeclaration(datatype, variableNames, result, rootNode); // Call the method
//                                        variableNames.clear(); // Clean list
//                                        decleared = true;
                                    } else if (token.getType() == Token.TokenType.ASSIGN) {
                                        assigned = true;
                                    }

                                    // Move to the next token
                                    i++;
                                    if (i < tokensList.size()) {
                                        token = tokensList.get(i);
                                    }
                                }
                            } else if (token.getValue().equals("CHAR") || token.getValue().equals("BOOL")) {
                                while (token.getType() != Token.TokenType.ENDLINE && i < tokensList.size()) {
                                    if (token.getType() == Token.TokenType.VARIABLE) { // Store variable
                                        variableNames.add(token.getValue());
                                        decleared = false;
                                    } else if (token.getType() == Token.TokenType.EXPRESSION) {
                                        value = token.getValue();
                                        if ((datatype.equals("BOOL") && value.startsWith("\"") && value.endsWith("\""))) {
                                            value = value.substring(1, value.length() - 1);
                                            value = value.toUpperCase(); // Convert boolean value to uppercase
                                        }

                                        if (variableValueValidator(datatype, value)){
                                            processVariableDeclaration(datatype, variableNames, value, rootNode); // Call the method
                                            variableNames.clear(); // Clean list
                                            decleared = true;
                                        } else {
                                            // TODO: throw an exception
                                            System.out.println("NOOOOOO WAYYYYYY");
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
//
//                            if (!decleared && !assigned) { // For: int x, y, z = 3 // TODO: something wrong with this lines of code
//                                processVariableDeclaration(datatype, variableNames, value, rootNode); // Call the method
//                            } else if (!decleared && assigned) {
//                                throw new CustomExceptions("No value assigned to variable");
//                            }

                        } else { /// hissss
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
//                        System.out.println("Case Variable found: " + token.getValue());
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
    private static void executeAST(Node node, Environment environment) throws CustomExceptions {
        // Perform appropriate actions based on code.node type
        if (node instanceof DelimiterNode delimiterNode) {
            // Execute functionality based on delimiter type
            if (delimiterNode.getDataType().equals("BEGIN CODE")) {

                // Logic to start the program
                System.out.println("Start Delimiter Node: Program started...");
            } else if (delimiterNode.getDataType().equals("END CODE")) {
                // Logic to end the program
                System.out.println("End Delimiter Node: Program ended...");
            }
        } else if (node instanceof VariableDeclarationNode variableNode) {
            System.out.println("oidngosdfngpdfg");
            // Logic to handle variable declarations
            String variableType = variableNode.getDataType();
            String variableName = variableNode.getVariableName();
            String variableValue = (String) variableNode.getValue();
//                // Perform actions based on variable type, name, and value
            System.out.println("Variable declaration: " + variableType + " " + variableName + " = " + variableValue);
        } else if (node instanceof DisplayNode displayNode) {
            System.out.print("Display Node: ");
            StringBuilder outputBuilder = new StringBuilder();
            for (String varName : displayNode.getVariableNames()) {
                if (varName.equals("$")) {
                    outputBuilder.append("\n"); // Append a newline character if the variable name is "$"
                } else if (varName.startsWith("\"") && varName.endsWith("\"")) {
                    // This is quoted text, remove the surrounding quotes and append to the output
                    String textInsideQuotes = varName.substring(1, varName.length() - 1);
                    outputBuilder.append(textInsideQuotes);

                } else if (varName.startsWith("[") && varName.endsWith("]")) {
                    // This is quoted text, remove the surrounding quotes and append to the output
                    String textInsideBrackets = varName.substring(1, varName.length() - 1);
                    outputBuilder.append(textInsideBrackets);

                } else {
                    // Check if varName exists in the environment
                    Object value = environment.getVariable(varName);

                    if (value != null) {
                        outputBuilder.append(value); // Append variable value if found
                    } else {
                        // Append varName as a literal string if not found in environment
                        outputBuilder.append(varName);
                    }
                }
            }
            System.out.println(outputBuilder);
        } else if (node instanceof AssignmentNode assignmentNode) {

            String variableName = assignmentNode.getVariableName();
            String variableValue = assignmentNode.getValue();
//            System.out.println("Assignment statement: " + variableName + " = " + variableValue);
        }
//            else if(node instanceof ScanNode scanNode) {
//                List<String> scannedVariables = scanNode.getScanVariables();
////                System.out.println("Scanned variables: " + scannedVariables);
//                Scanner scanner = new Scanner(System.in);
//                List<Integer> userInput = new ArrayList<>();
//
//                // Prompt the user for input based on scanned variables
//                for (String variableName : scannedVariables) {
//                    System.out.print("Enter value for " + variableName + ": ");
//                    int value = scanner.nextInt();
//                    userInput.add(value);
//                }
//
//                // Update variables in the environment with user input
//                for (int i = 0; i < scannedVariables.size(); i++) {
//                    String variableName = scannedVariables.get(i);
//                    environment.setVariable(variableName, userInput.get(i));
//                }
//            }
        else if(node instanceof ScanNode scanNode) {
            List<String> scannedVariables = scanNode.getScanVariables();
            System.out.println("Scanned variables: " + scannedVariables);
            Scanner scanner = new Scanner(System.in);
            List<String> userInput = new ArrayList<>();
            String inputLine = scanner.nextLine();
            userInput = List.of(inputLine.split(","));
            // Prompt the user for input based on scanned variables
//                for (String variableName : scannedVariables) {
//                    System.out.print("Enter value for " + variableName + ": ");
//                    String value = scanner.nextLine();
//                    userInput.add(value);
//                }

            // Update variables in the environment with user input
            for (int i = 0; i < scannedVariables.size(); i++) {
                String variableName = scannedVariables.get(i);
                environment.setVariable(variableName, userInput.get(i));
            }
        }
        else if(node instanceof IfNode ifNode) {
            boolean condition = ifNode.getExpressionResult();
            System.out.println(condition);
            if(condition) {
                for(Node child : ifNode.getChildren()) {
                    System.out.println("inside if block");
                    executeAST(child, environment);
                }
            } else {
                System.out.println("uwu");
            }
        }
        // Traverse child nodes recursively
        List<Node> children = node.getChildren();
        for (Node child : children) {
            executeAST(child, environment);
        }
    }
}