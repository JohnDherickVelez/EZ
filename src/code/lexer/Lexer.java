package code.lexer;

import code.model.Token;
import code.parser.CustomExceptions;
//import java.io.FileReader;
//import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static code.lexer.ReservedWordChecker.isReservedWord;

//
public class Lexer {
    private String sourceCode;
    private int currentTokenIndex = 0;
    private List<Token> tokensList = new ArrayList<>();
    // Constructor to initialize the tokensList

    public List<Token> tokenizeSourceCode(String sourceCode) throws CustomExceptions {
        try {
            Pattern pattern = Pattern.compile("\\b[\\w.]+\\b|\\n|[-+*/=<>!&|,$]|#.*|'.'"); // good regex for float
//        Pattern pattern = Pattern.compile("\\b\\w+\\b|\\n|[-+*/=<>!&|]");
//        Pattern pattern = Pattern.compile("\\b\\w+\\b|\\n|[-+*/=<>!&|]|'");
//        Pattern pattern = Pattern.compile("\\b\\w+\\b|\\n|[-+*/=<>!&|]|'.'"); // good regex for ' ' but as a whole 'c'
//        Pattern pattern = Pattern.compile("\\b[\\w.']+\\b|\\n|[-+*/=<>!&|]");
//        Pattern pattern = Pattern.compile("\\b\\w+\\b|\\n|[-+*/=<>!&|]|'|(\\d+(\\.\\d+)?)");

        Matcher matcher = pattern.matcher(sourceCode);
        while (matcher.find()) {
            String word = matcher.group(); // Get the matched word

            // Skip comments and continue until newline is encountered
            if (word.startsWith("#")) {
                // Skip this token (comment)
                continue;
            }

            // Check for various cases and add tokens accordingly
            switch (word) {
                case "BEGIN":
                case "END":
                case "CODE":
                case ":":
                case ",":
                    tokensList.add(new Token(Token.TokenType.DELIMITER, word, true));
                    break;
                case "INT":
                case "FLOAT":
                case "CHAR":
                case "BOOL":
                    tokensList.add(new Token(Token.TokenType.DATATYPE, word, true));
                    break;
                case "\n":
                    tokensList.add(new Token(Token.TokenType.ENDLINE, "end of line", false));
                    break;
                case "+":
                case "-":
                case "*":
                case "/":
                case "<":
                case ">":
                case "!":
                case "&":
                case "|":
                case "$":
                    tokensList.add(new Token(Token.TokenType.OPERATOR, word, true));
                    break;
                case "(":
                    tokensList.add(new Token(Token.TokenType.OPEN_P, word, true));
                    break;
                case ")":
                    tokensList.add(new Token(Token.TokenType.CLOSE_P, word, true));
                    break;
                case "=":
                    tokensList.add(new Token(Token.TokenType.ASSIGN, word, true));
                    break;
                case "DISPLAY":
                    tokensList.add(new Token(Token.TokenType.DISPLAY, word, true));
                    break;
                case "SCAN":
                    tokensList.add(new Token(Token.TokenType.SCAN, word, true));
                    break;
                case "'":
                    tokensList.add(new Token(Token.TokenType.S_QUOTE, word, true));
                    break;
                default:
                    if (word.matches("'.'")) {
                    tokensList.add(new Token(Token.TokenType.VALUE, word, false)); // Tokenize as a single character literal
                    } else if (isNumeric(word)) {
                        tokensList.add(new Token(Token.TokenType.VALUE, word, false)); // Tokenize as a numeric literal
                    } else if (isBoolean(word)) {
                        tokensList.add(new Token(Token.TokenType.VALUE, word, false)); // Tokenize as a numeric literal

                    }

                    else {
                        tokensList.add(new Token(Token.TokenType.VARIABLE, word, false)); // Default to variable if not a number
                    }
                    break;
            }
            System.out.println("Word: " + word);

            // Print each token from the tokensList
        }
        // Can comment this out when running full deployment of interpreter ( This only tests the tokenizer if it works)
        System.out.println("Tokens:");
        for (Token token : tokensList) {
            System.out.println(currentTokenIndex + ": {" +"Token Value: " + token.getValue() + ", Token type: " + token.getType() + "}");
            currentTokenIndex++;
        }
        checkTokenGrammar(tokensList);
        } catch (CustomExceptions e) {
            // Handle the custom exception
            System.out.println("Custom exception caught: " + e.getMessage());
        }
        return tokensList;
    }

//    private List<Token> tokenizeExpressions(List<Token> tokensList) {
//        List<Token> updatedTokensList = new ArrayList<>();
//        boolean inExpression = false;
//        StringBuilder expressionBuilder = new StringBuilder();
//
//        for (Token token : tokensList) {
//            if (token.getType() == Token.TokenType.ASSIGN || token.getType() == Token.TokenType.OPEN_P || token.getType() == Token.TokenType.OPERATOR) {
//                inExpression = true;
//                expressionBuilder.append(token.getValue());
//            } else if (token.getType() == Token.TokenType.CLOSE_P) {
//                inExpression = false;
//                expressionBuilder.append(token.getValue());
//                updatedTokensList.add(new Token(Token.TokenType.EXPRESSION, expressionBuilder.toString(), true));
//                expressionBuilder.setLength(0); // Clear the string builder
//            } else {
//                if (inExpression) {
//                    expressionBuilder.append(token.getValue());
//                } else {
//                    updatedTokensList.add(token);
//                }
//            }
//        }
//
//        return updatedTokensList;
//    }
    public void checkTokenGrammar(List<Token> tokensList) throws CustomExceptions {
        boolean foundBegin = false;
        boolean foundCodeAfterBegin = false;
        boolean foundEnd = false;
        boolean foundCodeAfterEnd = false;

        for (int i = 0; i < tokensList.size() - 1; i++) {
            Token currentToken = tokensList.get(i);
            Token nextToken = tokensList.get(i + 1);

            if (currentToken.getValue().equals("BEGIN")) {
                foundBegin = true;
                if (nextToken.getValue().equals("CODE")) {
                    foundCodeAfterBegin = true;
                }
            } else if (currentToken.getValue().equals("END")) {
                foundEnd = true;
                if (nextToken.getValue().equals("CODE")) {
                    foundCodeAfterEnd = true;
                }
            } else if (!currentToken.getIsReservedKey() && isReservedWord(currentToken.getValue())) {
                throw new CustomExceptions("Variable name is a reserved word: " + currentToken.getValue().toString());
            }
        }

        if (!foundBegin) {
            throw new CustomExceptions("Missing starting statement 'BEGIN'");
        }

        if (!foundCodeAfterBegin) {
            throw new CustomExceptions("Missing 'CODE' after 'BEGIN'");
        }

        if (!foundEnd) {
            throw new CustomExceptions("Missing ending statement 'END'");
        }

        if (!foundCodeAfterEnd) {
            throw new CustomExceptions("Missing 'CODE' after 'END'");
        }

    }

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?"); // Matches integers and floats (positive or negative)
    }
    private boolean isBoolean(String str) {
        return str.equals("TRUE") || str.equals("FALSE");
    }

}