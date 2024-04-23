package code.ArithmeticOperations;

import code.Environment.Environment;

import java.util.Stack;

public class ExpressionParser {
    private String expression;
    private Environment environment;
    private StringBuilder sb = new StringBuilder();

    public ExpressionParser(Environment environment) {
        this.environment = environment;
    }

    public Number evaluateExpression(String expression) {
        expression = expression.replaceAll("\\s", "");

        // Stack for operands (changed to Number)
        Stack<Number> values = new Stack<>();

        Stack<Character> ops = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            sb.setLength(0);

            if (Character.isDigit(c) || c == '.') {
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sb.append(expression.charAt(i++));
                }
                i--;
                if (sb.toString().contains(".")) {
                    values.push(Double.parseDouble(sb.toString())); // Parse as Double for decimals
                } else {
                    values.push(Integer.parseInt(sb.toString())); // Parse as Integer for integers
                }
            } else if (Character.isAlphabetic(c)) {
                int value = 0;
                while (i < expression.length() && Character.isAlphabetic(expression.charAt(i))) {
                    sb.append(expression.charAt(i++));
                }
                i--;
                if (environment.isDefined(sb.toString())) {
                    value = (int) environment.getVariable(sb.toString());
                } else {
                    // Handle undefined variable
                }
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

        while (!ops.empty()) {
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));
        }

        return values.pop();
    }

    public boolean evaluateLogicalExpression(String expression) {
        expression = expression.replaceAll("\\s", "");

        // Stack for operands (changed to Number)
        Stack<Number> values = new Stack<>();
        Stack<Character> ops = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            StringBuilder sb = new StringBuilder();

            if (Character.isDigit(c) || c == '.') {
                // Handle numeric operands
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sb.append(expression.charAt(i++));
                }
                i--;
                if (sb.toString().contains(".")) {
                    values.push(Double.parseDouble(sb.toString())); // Parse as Double for decimals
                } else {
                    values.push(Integer.parseInt(sb.toString())); // Parse as Integer for integers
                }
            } else if (Character.isAlphabetic(c)) {
                // Handle variable operands
                int value = 0;
                while (i < expression.length() && Character.isAlphabetic(expression.charAt(i))) {
                    sb.append(expression.charAt(i++));
                }
                i--;
                if (environment.isDefined(sb.toString())) {
                    value = (int) environment.getVariable(sb.toString());
                } else {
                    // Handle undefined variable
                }
                values.push(value);
            } else if (c == '(') {
                ops.push(c);
            } else if (c == ')') {
                // Evaluate expression inside parentheses
                while (ops.peek() != '(') {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.pop(); // Pop '('
            } else if (c == '+' || c == '-' || c == '*' || c == '/') {
                // Handle arithmetic operators
                while (!ops.empty() && hasPrecedence(c, ops.peek())) {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.push(c);
            } else if (c == '<' || c == '>' || c == '!' || c == '=') {
                // Handle logical comparison operators
                if (c == '=' && expression.charAt(i + 1) == '=') {
                    i++;
                    while (!ops.empty() && hasPrecedence(c, ops.peek())) {
                        values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                    }
                    ops.push(c);
                } else if (c == '!' && expression.charAt(i + 1) == '=') {
                    i++;
                    while (!ops.empty() && hasPrecedence(c, ops.peek())) {
                        values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                    }
                    ops.push(c);
                } else {
                    while (!ops.empty() && hasPrecedence(c, ops.peek())) {
                        values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                    }
                    ops.push(c);
                }
            }
        }

        // Evaluate remaining operators
        while (!ops.empty()) {
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));
        }

        // The result will be on top of the 'values' stack
        return values.pop().intValue() != 0;
    }


    private static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
//        return (op1 != '*' && op1 != '/') || (op2 != '+' && op2 != '-');
        if ((op1 == '<' || op1 == '>' || op1 == '!' || op1 == '=') && (op2 == '+' || op2 == '-' || op2 == '*' || op2 == '/')) {
            return true;
        }
        return false;
    }

    private static Number applyOp(char op, Number b, Number a) {
        if (a instanceof Double || b instanceof Double) {
            double da = a.doubleValue();
            double db = b.doubleValue();
            switch (op) {
                case '+':
                    return da + db;
                case '-':
                    return da - db;
                case '*':
                    return da * db;
                case '/':
                    if (db == 0) {
                        throw new ArithmeticException("Division by zero");
                    }
                    return da / db;
                case '<':
                    return a.doubleValue() < b.doubleValue() ? 1 : 0;
                case '>':
                    return a.doubleValue() > b.doubleValue() ? 1 : 0;
                case '!':
                    return a.doubleValue() != b.doubleValue() ? 1 : 0;
                case '=':
                    return a.doubleValue() == b.doubleValue() ? 1 : 0;
                default:
                    throw new IllegalArgumentException("Unsupported operation: " + op);
            }
        } else {
            int ia = a.intValue();
            int ib = b.intValue();
            switch (op) {
                case '+':
                    return ia + ib;
                case '-':
                    return ia - ib;
                case '*':
                    return ia * ib;
                case '/':
                    if (ib == 0) {
                        throw new ArithmeticException("Division by zero");
                    }
                    return ia / ib;
                case '<':
                    return a.doubleValue() < b.doubleValue() ? 1 : 0;
                case '>':
                    return a.doubleValue() > b.doubleValue() ? 1 : 0;
                case '!':
                    return a.doubleValue() != b.doubleValue() ? 1 : 0;
                case '=':
                    return a.doubleValue() == b.doubleValue() ? 1 : 0;
                default:
                    throw new IllegalArgumentException("Unsupported operation: " + op);

            }
        }
    }

    private static Boolean applyLogicalOp(String op, Number b, Number a) {
        int ia = a.intValue();
        System.out.println("Value of ia: " + ia);
        int ib = b.intValue();
        System.out.println("Value of ib: " + ib);
        switch (op) {
            case "<":
                return ia < ib;
            case ">":
                return ia > ib;
            case "==":
                return ia == ib;
            case "&&":
                return (ia != 0) && (ib != 0);
            case "||":
                return (ia != 0) || (ib != 0);
        }
        return false; // Default return value
    }

    public static void main(String[] args) {
        Environment environment = new Environment();
        environment.setVariable("x", 5);
        environment.setVariable("y", 10);

        ExpressionParser expressionParser = new ExpressionParser(environment);

        // Test arithmetic expressions
        String arithmeticExpression = "x + y * 2";
        Number arithmeticResult = expressionParser.evaluateExpression(arithmeticExpression);
        System.out.println(arithmeticExpression + " = " + arithmeticResult); // Expected: 25

//        boolean ehh = applyLogicalOp("<", 10, 5);
//        System.out.println(ehh);

        String logicalExpression = "8 == 8";
        boolean result = expressionParser.evaluateLogicalExpression(logicalExpression);

        System.out.println("Result of logical expression evaluation: " + result);



    }
}
