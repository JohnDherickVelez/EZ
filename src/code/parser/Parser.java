package code.parser;

import code.model.Token;
import code.model.TokenType;
import node.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static code.lexer.ReservedWordChecker.isReservedWord;

public class Parser {
    private List<Token> tokens;
    private int currentTokenIndex;
    private Map<String, Integer> symbolMap;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
        // Initialize symbolMap if needed
    }

    public ASTNode parse() {
        return parseStatements();
    }

    ASTNode parseStatements() {
        List<ASTNode> statements = new ArrayList<>();

        if (tokens != null) {
            while (currentTokenIndex < tokens.size()) {
                ASTNode statement = parseStatement();
                if (statement != null) {
                    statements.add(statement);
                }
                advance(); // Move to the next token
            }
        }
        return new Statements(statements);
    }

    ASTNode parseStatement() {
        Token currentToken = currentToken();

        switch (currentToken.getType()) {
            case VARIABLE:
                return parseAssignmentStatement();
            default:
                throw new RuntimeException("Invalid statement.");
        }
    }

    ASTNode parseAssignmentStatement() {
        Token currentToken = currentToken();
        String variableName = currentToken.getValue();
        advance(); // Move past the identifier
        expect(TokenType.OPERATOR, "=");
        advance(); // Move past the '=' token
        ASTNode value = parseExpression(); // Parse the expression on the right side of the assignment

        return new AssignmentNode(variableName, value);
    }

    ASTNode parseExpression() {
        Token currentToken = currentToken();

        if (currentToken.getType() == TokenType.VARIABLE) {
            return new IdentifierNode(currentToken.getValue());
        } else {
            throw new RuntimeException("Invalid expression syntax.");
        }
    }

    private Token currentToken() {
        return tokens.get(currentTokenIndex);
    }

    private void advance() {
        currentTokenIndex++;
    }

    private void expect(TokenType expectedType, String expectedValue) {
        Token currentToken = currentToken();
        if (currentToken.getType() != expectedType || !currentToken.getValue().equals(expectedValue)) {
            throw new RuntimeException("Expected token " + expectedType + " with value " + expectedValue +
                    " but found " + currentToken.getType() + " with value " + currentToken.getValue());
        }
    }
}
