package testfiles;

import java.util.HashMap;
import java.util.Stack;

public class Calculator {
    public static void main(String[] args) {
        HashMap<String, String> expressions = new HashMap<>();
        expressions.put("a", "15 + 15");
        expressions.put("b", "((3*2)+5)");
        expressions.put("c", "12");
        expressions.put("d", "((24*43)/(12+43))");
        expressions.put("e", "((24*43/2)/12+43)");
        expressions.put("f", "5");
        expressions.put("g", "((100 * 5)/10 + 10) * 1");
//        expressions.put("h", "c");

        HashMap<String, Integer> results = new HashMap<>();

        for (String variable : expressions.keySet()) {
            try {
                String expression = expressions.get(variable);
                System.out.println(expression);
                int result = evaluateExpression(expression);
                results.put(variable, result);
            } catch (ArithmeticException e) {
                System.out.println("Error evaluating expression for variable " + variable + ": Division by zero");
            } catch (NumberFormatException e) {
                System.out.println("Error evaluating expression for variable " + variable + ": Invalid number format");
            }
        }

        // Printing results
        for (String variable : results.keySet()) {
            System.out.println(variable + " = " + results.get(variable));
        }
    }

    private static int evaluateExpression(String expression) {
        // Remove whitespace from the expression
        expression = expression.replaceAll("\\s", "");

        // Stack for operands Ex: [ 10, 9 ,17 ]
        Stack<Integer> values = new Stack<>();

        // Stack for operators Ex: [ + , - , * ]
        Stack<Character> ops = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isDigit(c)) {
                StringBuilder sb = new StringBuilder();
                while (i < expression.length() && Character.isDigit(expression.charAt(i))) {
                    sb.append(expression.charAt(i++));
                }
                i--;
                values.push(Integer.parseInt(sb.toString()));
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
