package code.ArithmeticOperations;

import code.Environment.Environment;

import java.util.HashMap;
import java.util.Stack;

public class ExpressionParser {
    private String expression;
    private Environment environment;
    private StringBuilder sb = new StringBuilder();

    public ExpressionParser(Environment environment) {
        this.environment = environment;
    }

    public int evaluateExpression(String expression) {
        // Remove whitespace from the expression
//        System.out.println("THE Expression: " + expression);
        expression = expression.replaceAll("\\s", "");

        // Stack for operands
        Stack<Integer> values = new Stack<>();

        // Stack for operators
        Stack<Character> ops = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            sb.setLength(0); // Clear the StringBuilder

//            System.out.println("CHAR: " + c);
            if (Character.isDigit(c)) {
                while (i < expression.length() && Character.isDigit(expression.charAt(i))) {
                    sb.append(expression.charAt(i++));
                }
                i--;
                values.push(Integer.parseInt(sb.toString()));
            } else if (Character.isAlphabetic(c)) {
//                System.out.println("IF ALPHA CHAR: " + c);
                int value = 0;
                while (i < expression.length() && Character.isAlphabetic(expression.charAt(i))) {
                    sb.append(expression.charAt(i++));
                    //System.out.println("RUNNING: " + sb.toString());
                }
                i--;
                if (environment.isDefined(sb.toString())) {
                    value = (int) environment.getVariable(sb.toString());
//                    System.out.println("VALUE: " + value);
                } else {
                    // TODO: trow an exception
                }
//                System.out.println("kjkqlwjelkqwejlk"+value);
                    values.push(value);
            } else if (c == '(') {
                ops.push(c);
            } else if (c == ')') {
                while (ops.peek() != '(') {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.pop();
            } else if (c == '+' || c == '-' || c == '*' || c == '/') {
                while (!ops.empty() && hasPrecedence(c, ops.peek())) {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.push(c);
            }
        }

        // Remaining operations
        while (!ops.empty()) {
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));
        }

        return values.pop();
    }

    private static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        return (op1 != '*' && op1 != '/') || (op2 != '+' && op2 != '-');
    }

    private static int applyOp(char op, int b, int a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return a / b;
        }
        return 0;
    }
}
