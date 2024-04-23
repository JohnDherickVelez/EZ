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

import code.model.Token;
import code.parser.CustomExceptions;
//import java.io.FileReader;
//import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static code.lexer.ReservedWordChecker.isReservedWord;

public class Lexer {
    private String sourceCode;
    private int currentTokenIndex = 0;
    private List<Token> tokensList = new ArrayList<>();
    // Constructor to initialize the tokensList

    public List<Token> tokenizeSourceCode(String sourceCode) throws CustomExceptions {
        try {
//            Pattern pattern = Pattern.compile("\\b[()\\w.]+\\b|\\n|[-+*/=<>!&|,$]|#.*|'.'"); // good regex for float
//        Pattern pattern = Pattern.compile("\\b\\w+\\b|\\n|[-+*/=<>!&|]");
//        Pattern pattern = Pattern.compile("\\b\\w+\\b|\\n|[-+*/=<>!&|]|'");
//        Pattern pattern = Pattern.compile("\\b\\w+\\b|\\n|[-+*/=<>!&|]|'.'"); // good regex for ' ' but as a whole 'c'
//        Pattern pattern = Pattern.compile("\\b[\\w.']+\\b|\\n|[-+*/=<>!&|]");
//        Pattern pattern = Pattern.compile("\\b\\w+\\b|\\n|[-+*/=<>!&|]|'|(\\d+(\\.\\d+)?)");
//            Pattern pattern = Pattern.compile("\\b[\\w.]+\\b|\\n|[-+*/=<>!&|,$()]|#.*|'.'"); // optimal for arithmetic operations DO NOT DELETE
//            Pattern pattern = Pattern.compile("\\b[\\w.]+\\b|\\n|[-+*/=<>!&|,$()\\[\\]]|#.*|'.'");
            Pattern pattern = Pattern.compile("\"[^\"]*\"|'[^']*'|\\b[\\w.]+\\b|\\n|[-+*/=<>!&|,$()\\[\\]]|#.*");
            Matcher matcher = pattern.matcher(sourceCode);
            StringBuilder expressionBuilder = new StringBuilder(); // To build expressions
            boolean skipBuildingExpression = false;
            boolean assignmentFound = false;
            while (matcher.find()) {
                String word = matcher.group(); // Get the matched word

                // Skip comments and continue until newline is encountered
                if (word.startsWith("#")) {
                    // Skip this token (comment)
                    continue;
                }

                if (word.equals("=")) {
                    assignmentFound = true;
                    tokensList.add(new Token(Token.TokenType.ASSIGN, word, true));
                    expressionBuilder.setLength(0);
                    continue;
                }

                // Check for endline token
                if (word.equals("\n")) {
                    if (expressionBuilder.length() > 0) {
                        // Add the current expression as a single token
                        tokensList.add(new Token(Token.TokenType.EXPRESSION, expressionBuilder.toString().trim(), true));
                        expressionBuilder.setLength(0); // Clear the expression builder
                    }
                    tokensList.add(new Token(Token.TokenType.ENDLINE, "end of line", false));
//                    skipBuildingExpression = false;
                    assignmentFound = false;
                    continue;
                }
                // Check if we should skip building the expression

                // If not an endline token, handle as before
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
                    case "[":
                    case "]":
                        tokensList.add(new Token(Token.TokenType.IDENTIFIER, word, true));
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
                        skipBuildingExpression = true;
                        tokensList.add(new Token(Token.TokenType.DISPLAY, word, true));
                        break;
                    case "SCAN":
                        skipBuildingExpression = true;
                        tokensList.add(new Token(Token.TokenType.SCAN, word, true));
                        break;
                    case "'":
                        tokensList.add(new Token(Token.TokenType.S_QUOTE, word, true));
                        break;
                    default:
                        if (word.matches("'.'")) {
                            tokensList.add(new Token(Token.TokenType.VALUE, word, false)); // Tokenize as a single character literal
                        } else if (word.matches("\".*\"")) {
                            tokensList.add(new Token(Token.TokenType.TEXT, word, false));
                        } else if (isNumeric(word)) {
                            tokensList.add(new Token(Token.TokenType.VALUE, word, false)); // Tokenize as a numeric literal
                        } else if (isBoolean(word)) {
                            tokensList.add(new Token(Token.TokenType.VALUE, word, false)); // Tokenize as a numeric literal
                        } else {
                            tokensList.add(new Token(Token.TokenType.VARIABLE, word, false)); // Default to variable if not a number
                        }
                        break;
                }

//                expressionBuilder.append(word).append(" "); // Append the word with a space delimiter
                if (!skipBuildingExpression && !word.equals("BEGIN") && !word.equals("CODE") && !word.equals("END") && !word.equals("DISPLAY") && !word.equals("SCAN")) {
                    expressionBuilder.append(word).append(" "); // Append the word with a space delimiter
                }


            }
            // Handle the case where the source code ends with an expression without an endline
            if (expressionBuilder.length() > 0) {
                tokensList.add(new Token(Token.TokenType.EXPRESSION, expressionBuilder.toString().trim(), true));
            }

            checkTokenGrammar(tokensList);

        } catch (CustomExceptions e) {
            // Handle the custom exception
            System.out.println("Custom exception caught: " + e.getMessage());
        }
        return tokensList;
    }

    public void printTokensFromList(List<Token> tokensList) {
        System.out.println("Tokens:");
        for (Token token : tokensList) {
            System.out.println(currentTokenIndex + ": {" + "Token Value: " + token.getValue() + ", Token type: " + token.getType() + "}");
            currentTokenIndex++;
        }
    }
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