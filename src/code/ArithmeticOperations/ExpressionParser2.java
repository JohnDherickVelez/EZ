package code.ArithmeticOperations;

import code.Environment.Environment;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: (not actually to do but a README)
//  - ExpressionParser2 utilizes Juxtaposition calculator for arithmetic operations
//  - "               " uses stack for logical operations
public class ExpressionParser2 {

    private Environment environment;

    public ExpressionParser2(Environment environment) {
        this.environment = environment;
    }

    private int index = 0;
    private String expression;

    public double calculate(String expression) {
        if (expression == null || expression.isEmpty()) {
            return 0;
        }
        // Substitute variables with their values from the environment
        this.expression = substituteVariables(expression.replaceAll("\\s", ""));
        this.index = 0;

        return parseExpression();
    }

    private String substituteVariables(String expression) {
        Pattern pattern = Pattern.compile("\\b[a-zA-Z_][a-zA-Z_0-9]*\\b");
        Matcher matcher = pattern.matcher(expression);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String variableName = matcher.group();
            Object value = environment.getVariable(variableName);
            if (value != null) {
                matcher.appendReplacement(sb, value.toString());
            } else {
                throw new IllegalArgumentException("Undefined variable: " + variableName);
            }
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    private double parseExpression() {
        double result = parseComparison();

        while (index < expression.length()) {
            char operator = expression.charAt(index);
            if (operator == '+' || operator == '-') {
                index++;
                double nextTerm = parseComparison();
                if (operator == '+') {
                    result += nextTerm;
                } else {
                    result -= nextTerm;
                }
            } else {
                break;
            }
        }
        return result;
    }

    private double parseComparison() {
        double leftOperand = parseTerm();

        while (index < expression.length()) {
            char operator = expression.charAt(index);
            if (operator == '>' || operator == '<' || operator == '=' || operator == '!') {
                index++;
                char nextChar = (index < expression.length()) ? expression.charAt(index) : 0;
                if (nextChar == '=') {
                    index++;
                    switch (operator) {
                        case '>':
                            return leftOperand >= parseTerm() ? 1 : 0;
                        case '<':
                            return leftOperand <= parseTerm() ? 1 : 0;
                        case '=':
                            return leftOperand == parseTerm() ? 1 : 0;
                        case '!':
                            return leftOperand != parseTerm() ? 1 : 0;
                        default:
                            throw new IllegalArgumentException("Invalid operator: " + operator);
                    }
                } else if (nextChar == '>') {
                    index++;
                    switch (operator) {
                        case '<':
                            return leftOperand != parseTerm() ? 1 : 0;
                        default:
                            throw new IllegalArgumentException("Invalid operator: " + operator);
                    }
                } else {
                    switch (operator) {
                        case '>':
                            return leftOperand > parseTerm() ? 1 : 0;
                        case '<':
                            return leftOperand < parseTerm() ? 1 : 0;
                        case '!':
                            return leftOperand != parseTerm() ? 1 : 0;
                        default:
                            throw new IllegalArgumentException("Invalid operator: " + operator);
                    }
                }
            } else {
                break;
            }
        }
        return leftOperand;
    }

    private double parseTerm() {
        double result = parseFactor();

        while (index < expression.length()) {
            char operator = expression.charAt(index);
            if (operator == '*' || operator == 'x') {
                index++;
                result *= parseFactor();
            } else if (operator == '/') {
                index++;
                double divisor = parseFactor();
                if (divisor == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                result /= divisor;
            } else if (operator == '%') {
                index++;
                double divisor = parseFactor();
                if (divisor == 0) {
                    throw new ArithmeticException("Modulo by zero");
                }
                result %= divisor;
            } else {
                break;
            }
        }
        return result;
    }

    private double parseFactor() {
        if (index >= expression.length()) {
            throw new IllegalArgumentException("Incomplete expression");
        }

        char firstChar = expression.charAt(index);
        if (Character.isDigit(firstChar) || firstChar == '-') {
            Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
            Matcher matcher = pattern.matcher(expression.substring(index));
            if (matcher.find()) {
                String numberStr = matcher.group();
                index += numberStr.length();
                return Double.parseDouble(numberStr);
            } else {
                throw new IllegalArgumentException("Invalid number format");
            }
        } else if (firstChar == '(' || firstChar == '[') {
            char closingBracket = (firstChar == '(') ? ')' : ']';
            index++;
            double result = parseExpression();
            if (index >= expression.length() || expression.charAt(index) != closingBracket) {
                throw new IllegalArgumentException("Missing closing bracket");
            }
            index++;
            return result;
        } else {
            throw new IllegalArgumentException("Unexpected character: " + firstChar);
        }
    }

    public Number evaluateExpression(String expression) {
        return 0;
    }

    public static void main(String[] args) {
        String expression = "a + b + 1";
        Environment environment1 = new Environment();
        environment1.setVariable("a", 10); // Example variable
        environment1.setVariable("b", 20); // Example variable

        ExpressionParser2 expressionParser2 = new ExpressionParser2(environment1);
        double result = expressionParser2.calculate(expression);

        System.out.println("Result of expression: " + result);
    }

//------------------------------------------------------------------------------------------
//    For logical Expressions only:
public boolean evaluateLogicalExpression(String expression) {
    expression = expression.replaceAll("\\s", "");

    // Stack for operands (changed to Number)
    Stack<Number> values = new Stack<>();
    Stack<Character> ops = new Stack<>();

    for (int i = 0; i < expression.length(); i++) {
        char c = expression.charAt(i);
        char cnext = '\0';
        if (i < expression.length()-1) {
            cnext = expression.charAt(i + 1);
        }
//            System.out.println(cnext);
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
        } else if (Character.isAlphabetic(c)) { // if encountered C is a variable
            // Handle variable operands
            int value = 0;
            while (i < expression.length() && Character.isAlphabetic(expression.charAt(i))) {
                sb.append(expression.charAt(i++));
            }
            i--;
            if (environment.isDefined(sb.toString())) {

                value = (int) environment.getVariable(sb.toString());
            }
            values.push(value); // no expression parsing happened
        } else if (c == '(') {
            ops.push(c);
        } else if (c == ')') {
            // Evaluate expression inside parentheses
            while (ops.peek() != '(') {
                values.push(applyOp(ops.pop(),'\0', values.pop(), values.pop()));
            }
            ops.pop(); // Pop '('
        } else if (c == '+' || c == '-' || c == '*' || c == '/') {
            // Handle arithmetic operators
            while (!ops.empty() && hasPrecedence(c, ops.peek())) {
                values.push(applyOp(ops.pop(),'\0', values.pop(), values.pop()));
            }
            ops.push(c);
        }
        else if (c == '<' || c == '>' || c == '=' || c == '!') {
            // TODO : '<>' cannot be evaluated properly
            if (cnext == '=' || cnext == '>') {
//                    System.out.println(cnext);
                while (!ops.empty() && hasPrecedence(c, ops.peek())) {
                    values.push(applyOp(ops.pop(), cnext, values.pop(), values.pop()));
//                        System.out.println(cnext);
                }
                ops.push(c);
                i++;
            } else {
//                    System.out.println("asd12083123123;djgsdgf");
                while (!ops.empty() && hasPrecedence(c, ops.peek())) {
                    values.push(applyOp(ops.pop(), '\0', values.pop(), values.pop()));
                }
                ops.push(c);
            }
        }
    }
    // Evaluate remaining operators
    while (!ops.empty()) {
        values.push(applyOp(ops.pop(), '\0', values.pop(), values.pop()));
    }

    // The result will be on top of the 'values' stack
    return values.pop().intValue() != 0;
}


    private static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
//        return (op1 != '*' && op1 != '/') || (op2 != '+' && op2 != '-');
        if ((op1 == '<' || op1 == '>' || op1 == '!' || op1 == '=') || (op2 == '>' || op2 == '=' )) {
            return true;
        }
        return true;
    }

    private static Number applyOp(char op, char op2, Number b, Number a) {
//        System.out.println("op1" + op + " " + "op2" + op2);
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
//                case '<':
//                    return (op2 == '=' ? da <= db : da < db) ? 1 : 0;
                case '<':
                    return switch (op2) {
                        case '=' -> da <= db ? 1 : 0;
                        case '>' -> da != db ? 1 : 0; // Handle '!=' operation when op2 is '>'
                        default -> da < db ? 1 : 0;
                    };
                case '>':
                    return (op2 == '=' ? da >= db : da > db) ? 1 : 0;
                case '=':
                    return da == db ? 1 : 0;
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
                    return (op2 == '=' ? ia <= ib : ia < ib) ? 1 : 0;
                case '>':
                    return (op2 == '=' ? ia >= ib : ia > ib) ? 1 : 0;
                case '=':
                    return ia == ib ? 1 : 0;
                default:
                    throw new IllegalArgumentException("Unsupported operation: " + op);
            }
        }
    }
}
