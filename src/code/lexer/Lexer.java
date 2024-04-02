package code.lexer;

import code.model.Token;
import code.model.TokenType;
//import java.io.FileReader;
//import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//
public class Lexer {
    private String sourceCode;
    private int currentIndex = 0;
    private int lineNumber = 1;

    public Lexer(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    private boolean isDigit(char ch) {
        return Character.isDigit(ch);
    }

    private boolean isLetter(char ch) {
        return Character.isLetter(ch);
    }

    private boolean isWhitespace(char ch) {
        return Character.isWhitespace(ch);
    }

    private Token readNumber() {
        StringBuilder numberStr = new StringBuilder();
        char currentChar = sourceCode.charAt(currentIndex);

        while (isDigit(currentChar)) {
            numberStr.append(currentChar);
            currentIndex++;
            if (currentIndex < sourceCode.length()) {
                currentChar = sourceCode.charAt(currentIndex);
            } else {
                break;
            }
        }

        return new Token(TokenType.INT_LITERAL, numberStr.toString(), lineNumber);
    }

    private Token readIdentifierOrKeyword() {
        StringBuilder word = new StringBuilder();
        char currentChar = sourceCode.charAt(currentIndex);

        while (isLetter(currentChar) || isDigit(currentChar) || currentChar == '_') {
            word.append(currentChar);
            currentIndex++;
            if (currentIndex < sourceCode.length()) {
                currentChar = sourceCode.charAt(currentIndex);
            } else {
                break;
            }
        }

        String wordStr = word.toString().toUpperCase();
        switch (wordStr) {
            case "INT":
            case "FLOAT":
            case "CHAR":
            case "BOOL":
                return new Token(TokenType.DATATYPE, wordStr, lineNumber);

            case "BEGIN": return new Token(TokenType.BEGIN, wordStr, lineNumber);
            case "END": return new Token(TokenType.END, wordStr, lineNumber);
            case "CODE": return new Token(TokenType.CODE, wordStr, lineNumber);
            default: return new Token(TokenType.IDENTIFIER, wordStr, lineNumber);
        }
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (currentIndex < sourceCode.length()) {
            char currentChar = sourceCode.charAt(currentIndex); // Declare inside loop

            if (isDigit(currentChar)) {
                tokens.add(readNumber());
            } else if (isLetter(currentChar)) {
                tokens.add(readIdentifierOrKeyword());
            } else if (isWhitespace(currentChar)) {
                if (currentChar == '\n') {
                    lineNumber++;
                }
            } else {
                switch (currentChar) {
                    // ... cases for operators and special characters
                    case '+':
                        tokens.add(new Token(TokenType.PLUS, "+", lineNumber));
                        break;
                    case '-':
                        tokens.add(new Token(TokenType.MINUS, "-", lineNumber));
                        break;
                    // ... add cases for multiplication, division, etc.
                    case '*':
                        tokens.add(new Token(TokenType.MULTIPLY, "*", lineNumber));
                        break;
                    case '/':
                        tokens.add(new Token(TokenType.DIVIDE, "/", lineNumber));
                        break;
                    case '(':
                        tokens.add(new Token(TokenType.LPAREN, "(", lineNumber));
                        break;
                    case ')':
                        tokens.add(new Token(TokenType.RPAREN, ")", lineNumber));
                        break;
                    case '=':
                        tokens.add(new Token(TokenType.ASSIGN, "=", lineNumber));
                        break;
                    case '$':
                        tokens.add(new Token(TokenType.DOLLAR, "$", lineNumber));
                        break;
                    case '&':
                        tokens.add(new Token(TokenType.AMPERSAND, "&", lineNumber));
                        break;
                    case '[':
                        tokens.add(new Token(TokenType.LBRACKET, "[", lineNumber));
                        break;
                    case ']':
                        tokens.add(new Token(TokenType.RBRACKET, "]", lineNumber));
                        break;
                    case '#':
                        skipComment();
                        break;
                    default:
                        throw new RuntimeException("Invalid character: " + currentChar + " at line " + lineNumber);
                }
            }

            currentIndex++;
        }

        tokens.add(new Token(TokenType.EOF, null, lineNumber));
        return tokens;
    }

    private void skipComment() {    // Inc.1.2. Comment Recognition
        while (currentIndex < sourceCode.length() && sourceCode.charAt(currentIndex) != '\n') {   // skips #comment until \n
            currentIndex++;
        }
    }
}