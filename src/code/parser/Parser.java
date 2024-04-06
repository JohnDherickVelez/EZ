package code.parser;

import code.model.Token;
import code.model.TokenType;
import node.*;

import java.util.*;

import static code.lexer.ReservedWordChecker.isReservedWord;

public class Parser {
    private List<Token> tokensList;
    private int currentTokenIndex = 0;

    private List<Node> ASTNode = new ArrayList<>();


    public Parser(List<Token> tokensList) {
        this.tokensList = tokensList;
    }

    public ASTNode produceAST() {
        ASTNode rootNode = new ASTNode(); // Create the root node

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
                                    rootNode.addChild(new VariableDeclarationNode("int", varname, value));
                                } else if (token.getValue().equals("FLOAT") && value.matches("\\d*\\.\\d+")) {
                                    rootNode.addChild(new VariableDeclarationNode("float", varname, value));
                                } else if (token.getValue().equals("CHAR") && value.matches("'.'")) {
                                    rootNode.addChild(new VariableDeclarationNode("char", varname, value));
                                } else if (token.getValue().equals("BOOL") && (value.equals("true") || value.equals("false"))) {
                                    rootNode.addChild(new VariableDeclarationNode("bool", varname, value));
                                }
                            }
                        }
                    }
                    break;

                default:
                    break;
            }
        }

        return rootNode; // Return the root node of the AST
    }
//    private List<Token> tokens;
//    private int currentTokenIndex;
//    private Map<String, Integer> symbolMap; // HashMap to store variable names and their values
//
//    public Parser(List<Token> tokens) {
//        this.tokens = tokens;
//        this.currentTokenIndex = 0;
//        this.symbolMap = new HashMap<>();
//
//    }
//
//    public ASTNode parse() {
//        return parseStatements();
//    }
//
//    ASTNode parseStatements() {
//        List<ASTNode> statements = new ArrayList<>();
//
//        if(tokens != null) {
//            expect(TokenType.BEGIN); // Ensure the code block starts with BEGIN CODE
//            advance();
//            expect(TokenType.CODE);
//            advance();
//
//            while (currentToken().getType() != TokenType.END && currentToken().getType() != TokenType.EOF) {
//                ASTNode statement = parseStatement();
//                if (statement != null) {
//                    statements.add(statement);
//                }
//                advance(); // Move to the next token
//            }
//
//
//            expect(TokenType.END); // Ensure the code block ends with END CODE
//            advance();
//            expect(TokenType.CODE);
//            advance();
//        }
//        return new Statements(statements);
//    }
//
//    ASTNode parseStatement() {
//        Token currentToken = currentToken(); // Assume a method currentToken() returns the current token
//
//        switch (currentToken.getType()) {
//            case DATATYPE:
//                return parseVariableDeclaration(TokenType.DATATYPE);
//            /*case BEGIN:
//                return parseBeginBlock();
//            case END:
//                return parseEndBlock();
//            case CODE:
//                return parseCodeBlock();*/
//            case DISPLAY:
//                return parseDisplayStatement(TokenType.DISPLAY);
//            default:
//                // If it's not a keyword, assume it's an assignment or other type of statement
//                return parseAssignmentStatement();
//        }
//    }
//
//    ASTNode parseVariableDeclaration(TokenType type) {
////        Token currentToken = currentToken(); // Assume a method currentToken() returns the current token
////        if (currentToken.getType() != TokenType.DATATYPE) {
////            throw new RuntimeException("Expected identifier in variable declaration.");
////        }
////        type = TokenType.valueOf(currentToken().getValue());
////        advance();
////        String variableName = currentToken.getValue();
////        advance(); // Move to the next token
////
////        ASTNode initialization = null;
////        if (currentToken().getType() == TokenType.ASSIGN) {
////            advance(); // Move past the '=' token
////            initialization = parseExpression(); // Assume parseExpression parses the initialization value
////        }
////
////
////        // Create a VariableDeclarationNode with the parsed information
////        return new VariableDeclarationNode(type, variableName, initialization);
//        Token currentToken = currentToken(); // Assume a method currentToken() returns the current token
//        if (currentToken.getType() != TokenType.DATATYPE) {
//            throw new RuntimeException("Expected identifier in variable declaration.");
//        }
//        type = TokenType.valueOf(currentToken().getValue());
//        advance();
//        String variableName = currentToken.getValue();
//
//        if (isReservedWord(variableName)) {
//        throw new RuntimeException("Reserved word '" + variableName + "' cannot be used as a variable name.");
//        }
//
//        advance(); // Move to the next token
//
//        ASTNode initialization = null;
//        if (currentToken().getType() == TokenType.ASSIGN) {
//            advance(); // Move past the '=' token
//            initialization = parseExpression(); // Assume parseExpression parses the initialization value
//
//            // Store the variable name and its value in the symbol map
//            symbolMap.put(variableName, ((IntegerLiteralNode) initialization).getValue());
//        }
//
//        // Create a VariableDeclarationNode with the parsed information
//        return new VariableDeclarationNode(type, variableName, initialization);
//    }
//
//    ASTNode parseDisplayStatement(TokenType type) { // TODO: Display?
////        expect(TokenType.DISPLAY);
////        advance();
////        ASTNode displayContent = parseExpression();
////
////        // Check if the display content is an identifier
////        if (displayContent instanceof IdentifierNode) {
////            String variableName = ((IdentifierNode) displayContent).getName();
////            if (symbolMap.containsKey(variableName)) {
////                int value = symbolMap.get(variableName);
////                return new IntegerLiteralNode(value);
////            } else {
////                throw new RuntimeException("Variable " + variableName + " has not been declared.");
////            }
////        } else {
////            // If the display content is not an identifier, it may be an expression
////            return displayContent;
////        }
//        expect(TokenType.DISPLAY);
//        advance();
//
//        ASTNode displayContent = parseExpression();
//
//        // Ensure displayContent is an IdentifierNode
//        return displayContent;
//    }
//
//    ASTNode parseBeginBlock() {
//        // Implement logic to parse BEGIN block
//        return null;
//    }
//
//    ASTNode parseEndBlock() {
//        // Implement logic to parse END block
//        return null;
//    }
//
//    ASTNode parseCodeBlock() {
//        // Implement logic to parse CODE block
//        return null;
//    }
//
//    ASTNode parseAssignmentStatement() {
//        // Implement logic to parse assignment statements
//        return null;
//    }
//
//    ASTNode parseExpression() {
////        Token currentToken = currentToken(); // Assume a method currentToken() returns the current token
////
////        if (currentToken.getType() == TokenType.INT_LITERAL) {
////            return new IntegerLiteralNode(Integer.parseInt(currentToken.getValue()));
////        } else if (currentToken.getType() == TokenType.IDENTIFIER) {
////            // Assume it's a variable or function call
////            //return parseVariableOrFunctionCallExpression();
////        } else {
////            throw new RuntimeException("Invalid expression syntax.");
////        }
////        return null;
//        Token currentToken = currentToken(); // Assume a method currentToken() returns the current token
//
//        if (currentToken.getType() == TokenType.INT_LITERAL) {
//            return new IntegerLiteralNode(Integer.parseInt(currentToken.getValue()));
//        } else if (currentToken.getType() == TokenType.IDENTIFIER) {
//            // Look up the variable value from the symbol map
//            String variableName = currentToken.getValue();
//            if (symbolMap.containsKey(variableName)) {
//                int value = symbolMap.get(variableName);
//                return new IntegerLiteralNode(value);
//            } else {
//                throw new RuntimeException("Variable " + variableName + " has not been declared.");
//            }
//        } else {
//            throw new RuntimeException("Invalid expression syntax.");
//        }
//    }
//
//
//    /*
//    ASTNode parseTerm() {
//        // Implement parsing logic for term
//        return null;
//    }
//
//    ASTNode parseFactor() {
//        // Implement parsing logic for factor
//        return null;
//    }
//    */
//    private Token currentToken() {
//        return tokens.get(currentTokenIndex);
//    }
//
//    private Token advance() {
//        return tokens.get(currentTokenIndex++);
//    }
//
//    private void expect(TokenType expectedType) {
//        Token currentToken = currentToken();
//        if (currentToken.getType() != expectedType) {
//            throw new RuntimeException("Expected token " +expectedType + " but found " + currentToken().getType());
//        }
//    }
}

// Define ASTNode subclasses as before