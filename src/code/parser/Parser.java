package code.parser;

import code.model.Token;
import code.model.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private List<Token> tokens;
    private int currentTokenIndex;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
    }

    public ASTNode parse() {
        return parseStatements();
    }

    ASTNode parseStatements() {
        List<ASTNode> statements = new ArrayList<>();
        expect(TokenType.BEGIN); // Ensure the code block starts with BEGIN CODE
        advance();

        while (currentToken().getType() != TokenType.END) {
            ASTNode statement = parseStatement();
            if (statement != null) {
                statements.add(statement);
            }
            // Advance to the next token
            advance();
        }

        expect(TokenType.END); // Ensure the code block ends with END CODE
        advance();

        return new Statements(statements);
    }
//        List<ASTNode> statements = new ArrayList<>();
//        expect(TokenType.BEGIN); // Ensure the code block starts with BEGIN CODE
//        advance();
//
//        // Create BeginCode node
//        BeginCode beginCode = new BeginCode();
//
//        while (currentToken().getType() != TokenType.END) {
//            statements.add(parseStatement());
//            // Advance to the next token
//            advance();
//        }
//
//        expect(TokenType.END); // Ensure the code block ends with END CODE
//        advance();
//
//        // Create EndCode node
//        EndCode endCode = new EndCode();
//
//        if (statements == null) {
//            throw new RuntimeException("List of statements is null.");
//        }
//
//        // Add statements to BeginCode node
//        beginCode.setStatements(statements);
//
//        // Return BeginCode node
//        return beginCode;
//    }

    ASTNode parseStatement() {
        // Implement parsing logic for different types of statements
        // For simplicity, let's assume we only support arithmetic expressions for now
        return parseExpression();
    }

    ASTNode parseExpression() {
        // Implement parsing logic for expression
        return null;
    }

    ASTNode parseTerm() {
        // Implement parsing logic for term
        return null;
    }

    ASTNode parseFactor() {
        // Implement parsing logic for factor
        return null;
    }

    private Token currentToken() {
        return tokens.get(currentTokenIndex);
    }

    private Token advance() {
        return tokens.get(currentTokenIndex++);
    }

    private void expect(TokenType expectedType) {
        Token currentToken = currentToken();
        if (currentToken.getType() != expectedType) {
            throw new RuntimeException("Expected " + expectedType + " but found " + currentToken.getType());
        }
    }
}

// Define ASTNode subclasses as before

