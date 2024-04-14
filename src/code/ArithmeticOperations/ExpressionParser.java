package code.ArithmeticOperations;

import java.util.HashMap;

public class ExpressionParser {
    private String expression;
    private HashMap<String, String> expressions = new HashMap<>();
    public ExpressionParser(String expression) {
        this.expression = expression;
    }

}
