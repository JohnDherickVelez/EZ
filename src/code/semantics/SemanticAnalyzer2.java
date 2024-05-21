package code.semantics;

import code.Environment.Environment;
import code.model.Token;
import code.node.AssignmentNode;
import code.node.ExpressionNode;
import code.node.Node;
import code.parser.CustomExceptions;

import java.util.ArrayList;
import java.util.List;

public class SemanticAnalyzer2 {
    private Environment environment;

    public SemanticAnalyzer2(Environment environment) {
        this.environment = environment;
    }

    public void analyze(List<Token> tokens, Node rootNode) throws CustomExceptions {
        checkVariableDeclarations(tokens);
        checkDisplayUsage(tokens);
        checkVariableUsage(tokens);

        if (rootNode != null) {
            checkTypeCompatibility(rootNode);
        }
    }

    private void checkVariableDeclarations(List<Token> tokensList) throws CustomExceptions {
        boolean withinExecutableCode = false;

        for (Token token : tokensList) {
            if (token.getValue().equals("BEGIN CODE")) {
                withinExecutableCode = false;
            } else if (token.getValue().equals("END CODE")) {
                break;
            } else if (withinExecutableCode && isDeclarationToken(token)) {
                throw new CustomExceptions("Variable declaration should be after 'BEGIN CODE' and before any executable code.");
            } else if (isExecutableToken(token)) {
                withinExecutableCode = true;
            }
        }
    }

    private boolean isDeclarationToken(Token token) {
        return token.getType() == Token.TokenType.DATATYPE;
    }

    private boolean isExecutableToken(Token token) {
        return token.getType() != Token.TokenType.DATATYPE && token.getType() != Token.TokenType.ENDLINE;
    }

    private void checkDisplayUsage(List<Token> tokensList) throws CustomExceptions {
        int i = 0;
        while (i < tokensList.size()) {
            Token currentToken = tokensList.get(i);
            if (currentToken.getValue().equals("DISPLAY")) {
                i++; // Move to the next token after "DISPLAY"
                while (i < tokensList.size() && tokensList.get(i).getType() != Token.TokenType.ENDLINE) {
                    Token token = tokensList.get(i);
                    if (token.getType() == Token.TokenType.VARIABLE) {
                        String variableName = token.getValue();
                        if (!environment.isDefined(variableName)) {
                            throw new CustomExceptions("Variable '" + variableName + "' is not defined.");
                        }
                    } else if (token.getType() == Token.TokenType.OPERATOR && token.getValue().equals("&")) {
                        if (i + 1 < tokensList.size() && tokensList.get(i + 1).getType() == Token.TokenType.VARIABLE) {
                            i++; // Move to the next token as we've already processed the variable after '&'
                        } else {
                            throw new CustomExceptions("Expected variable after '&' token.");
                        }
                    } else if (token.getType() == Token.TokenType.VARIABLE && token.getValue().startsWith("\"")) {
                        String value = token.getValue();
                        if (value.length() < 2 || value.charAt(0) != '"' || value.charAt(value.length() - 1) != '"') {
                            throw new CustomExceptions("Invalid string format: " + value);
                        }
                    } else if (token.getType() == Token.TokenType.VARIABLE && token.getValue().startsWith("'")) {
                        String value = token.getValue();
                        if (value.length() < 2 || value.charAt(0) != '\'' || value.charAt(value.length() - 1) != '\'') {
                            throw new CustomExceptions("Invalid character format: " + value);
                        }
                    }
                    i++; // Move to the next token
                }
            } else {
                i++; // Move to the next token if "DISPLAY" is not found
            }
        }
    }

    private void checkVariableUsage(List<Token> tokensList) throws CustomExceptions {
        for (int i = 0; i < tokensList.size() - 1; i++) {
            Token currentToken = tokensList.get(i);
            Token nextToken = tokensList.get(i + 1);

            if (currentToken.getValue().equals("INT")) {
                checkVariableAssignment(tokensList, i, "\\d+");
            }
            if (currentToken.getValue().equals("FLOAT")) {
                checkVariableAssignment(tokensList, i, "-?\\d+(\\.\\d+)?");
            }
            if (currentToken.getValue().equals("CHAR")) {
                checkVariableAssignment(tokensList, i, "^'.{1}'$");
            }
            if (currentToken.getValue().equals("BOOL")) {
                checkVariableAssignment(tokensList, i, "\"(TRUE|FALSE)\"");
            }
        }
    }

    private void checkVariableAssignment(List<Token> tokensList, int i, String regex) throws CustomExceptions {
        int j = i + 1;
        while (j < tokensList.size() && !tokensList.get(j).getValue().equals("=")) {
            j++;
        }
        if (j == tokensList.size() - 1) {
            throw new CustomExceptions("Missing '=' in variable declaration.");
        }
        Token expressionToken = tokensList.get(j + 1);
        if (!expressionToken.getValue().matches(regex)) {
            throw new CustomExceptions("Variable is not assigned a correct value.");
        }
    }

    private void checkTypeCompatibility(Node rootNode) throws CustomExceptions {
        traverseAST(rootNode);
    }

    private void traverseAST(Node node) throws CustomExceptions {
        if (node instanceof AssignmentNode) {
            checkAssignmentTypeCompatibility((AssignmentNode) node);
        } else if (node instanceof ExpressionNode) {
            checkExpressionTypeCompatibility((ExpressionNode) node);
        }

        for (Node child : node.getChildren()) {
            traverseAST(child);
        }
    }

    private void checkAssignmentTypeCompatibility(AssignmentNode assignmentNode) throws CustomExceptions {
        String variableName = assignmentNode.getVariableName();
        String dataType = environment.getVariableType(variableName);

        // Implement type compatibility checks based on dataType and the value being assigned
    }

    private void checkExpressionTypeCompatibility(ExpressionNode expressionNode) throws CustomExceptions {
        // Implement checks for type compatibility in expressions
    }

    public static void main(String[] args) throws CustomExceptions {
        List<Token> tokenlist = new ArrayList<>();
        List<Node> ASTNode = new ArrayList<>();
        Environment environment1 = new Environment();
        environment1.setVariable("a", 1);
        environment1.setVariable("b", 2);
        environment1.setVariable("d", 3);
        tokenlist.add(new Token(Token.TokenType.DISPLAY, "DISPLAY", true));
        tokenlist.add(new Token(Token.TokenType.VARIABLE, "a", false));
        tokenlist.add(new Token(Token.TokenType.ENDLINE, "end of line", true));

        SemanticAnalyzer2 semanticAnalyzer = new SemanticAnalyzer2(environment1);
        semanticAnalyzer.analyze(tokenlist, null);
    }
}

