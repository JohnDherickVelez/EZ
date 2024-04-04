package code.parser;

import code.model.Token;
import code.model.TokenType;
import node.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {
    private List<Token> tokens;
    private int currentTokenIndex;
    private Map<String, Integer> symbolMap; // HashMap to store variable names and their values

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
        this.symbolMap = new HashMap<>();

    }

    public ASTNode parse() {
        return parseStatements();
    }

    ASTNode parseStatements() {
        List<ASTNode> statements = new ArrayList<>();

        if(tokens != null) {
            expect(TokenType.BEGIN); // Ensure the code block starts with BEGIN CODE
            advance();
            expect(TokenType.CODE);
            advance();

            while (currentToken().getType() != TokenType.END && currentToken().getType() != TokenType.EOF) {
                ASTNode statement = parseStatement();
                if (statement != null) {
                    statements.add(statement);
                }
                advance(); // Move to the next token
            }


            expect(TokenType.END); // Ensure the code block ends with END CODE
            advance();
            expect(TokenType.CODE);
            advance();
        }
        return new Statements(statements);
    }

    ASTNode parseStatement() {
        Token currentToken = currentToken(); // Assume a method currentToken() returns the current token

        switch (currentToken.getType()) {
            case DATATYPE:
                return parseVariableDeclaration(TokenType.DATATYPE);
            /*case BEGIN:
                return parseBeginBlock();
            case END:
                return parseEndBlock();
            case CODE:
                return parseCodeBlock();*/
            case DISPLAY:
                return parseDisplayStatement(TokenType.DISPLAY);
            default:
                // If it's not a keyword, assume it's an assignment or other type of statement
                return parseAssignmentStatement();
        }
    }

    ASTNode parseVariableDeclaration(TokenType type) {
//        Token currentToken = currentToken(); // Assume a method currentToken() returns the current token
//        if (currentToken.getType() != TokenType.DATATYPE) {
//            throw new RuntimeException("Expected identifier in variable declaration.");
//        }
//        type = TokenType.valueOf(currentToken().getValue());
//        advance();
//        String variableName = currentToken.getValue();
//        advance(); // Move to the next token
//
//        ASTNode initialization = null;
//        if (currentToken().getType() == TokenType.ASSIGN) {
//            advance(); // Move past the '=' token
//            initialization = parseExpression(); // Assume parseExpression parses the initialization value
//        }
//
//
//        // Create a VariableDeclarationNode with the parsed information
//        return new VariableDeclarationNode(type, variableName, initialization);
        Token currentToken = currentToken(); // Assume a method currentToken() returns the current token
        if (currentToken.getType() != TokenType.DATATYPE) {
            throw new RuntimeException("Expected identifier in variable declaration.");
        }
        type = TokenType.valueOf(currentToken().getValue());
        advance();
        String variableName = currentToken.getValue();
        advance(); // Move to the next token

        ASTNode initialization = null;
        if (currentToken().getType() == TokenType.ASSIGN) {
            advance(); // Move past the '=' token
            initialization = parseExpression(); // Assume parseExpression parses the initialization value

            // Store the variable name and its value in the symbol map
            symbolMap.put(variableName, ((IntegerLiteralNode) initialization).getValue());
        }

        // Create a VariableDeclarationNode with the parsed information
        return new VariableDeclarationNode(type, variableName, initialization);
    }

    ASTNode parseDisplayStatement(TokenType type) { // TODO: Display?
//        expect(TokenType.DISPLAY);
//        advance();
//        ASTNode displayContent = parseExpression();
//
//        // Check if the display content is an identifier
//        if (displayContent instanceof IdentifierNode) {
//            String variableName = ((IdentifierNode) displayContent).getName();
//            if (symbolMap.containsKey(variableName)) {
//                int value = symbolMap.get(variableName);
//                return new IntegerLiteralNode(value);
//            } else {
//                throw new RuntimeException("Variable " + variableName + " has not been declared.");
//            }
//        } else {
//            // If the display content is not an identifier, it may be an expression
//            return displayContent;
//        }
        expect(TokenType.DISPLAY);
        advance();

        ASTNode displayContent = parseExpression();

        // Ensure displayContent is an IdentifierNode
        if (displayContent instanceof IdentifierNode) {
            // Return the DisplayStatementNode with the identifier
            return new DisplayStatementNode(displayContent);
        } else {
            throw new RuntimeException("Invalid display content. Expected an identifier.");
        }
    }

    ASTNode parseBeginBlock() {
        // Implement logic to parse BEGIN block
        return null;
    }

    ASTNode parseEndBlock() {
        // Implement logic to parse END block
        return null;
    }

    ASTNode parseCodeBlock() {
        // Implement logic to parse CODE block
        return null;
    }

    ASTNode parseAssignmentStatement() {
        // Implement logic to parse assignment statements
        return null;
    }

    ASTNode parseExpression() {
//        Token currentToken = currentToken(); // Assume a method currentToken() returns the current token
//
//        if (currentToken.getType() == TokenType.INT_LITERAL) {
//            return new IntegerLiteralNode(Integer.parseInt(currentToken.getValue()));
//        } else if (currentToken.getType() == TokenType.IDENTIFIER) {
//            // Assume it's a variable or function call
//            //return parseVariableOrFunctionCallExpression();
//        } else {
//            throw new RuntimeException("Invalid expression syntax.");
//        }
//        return null;
        Token currentToken = currentToken(); // Assume a method currentToken() returns the current token

        if (currentToken.getType() == TokenType.INT_LITERAL) {
            return new IntegerLiteralNode(Integer.parseInt(currentToken.getValue()));
        } else if (currentToken.getType() == TokenType.IDENTIFIER) {
            // Look up the variable value from the symbol map
            String variableName = currentToken.getValue();
            if (symbolMap.containsKey(variableName)) {
                int value = symbolMap.get(variableName);
                return new IntegerLiteralNode(value);
            } else {
                throw new RuntimeException("Variable " + variableName + " has not been declared.");
            }
        } else {
            throw new RuntimeException("Invalid expression syntax.");
        }
    }


    /*
    ASTNode parseTerm() {
        // Implement parsing logic for term
        return null;
    }

    ASTNode parseFactor() {
        // Implement parsing logic for factor
        return null;
    }
    */
    private Token currentToken() {
        return tokens.get(currentTokenIndex);
    }

    private Token advance() {
        return tokens.get(currentTokenIndex++);
    }

    private void expect(TokenType expectedType) {
        Token currentToken = currentToken();
        if (currentToken.getType() != expectedType) {
            throw new RuntimeException("Expected token " +expectedType + " but found " + currentToken().getType());
        }
    }
}

// Define ASTNode subclasses as before