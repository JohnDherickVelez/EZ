package code.Environment;

import code.node.VariableDeclarationNode;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private Map<String, Object> variables;

    public Environment() {
        variables = new HashMap<>();
    }

    // Store a variable value in the environment
    public void setVariable(String name, Object value) {
        variables.put(name, value);
    }

    // Retrieve a variable value from the environment
    public Object getVariable(String name) {
        return variables.get(name);
    }

    // Check if a variable is defined in the environment
    public boolean isDefined(String name) {
        return variables.containsKey(name);
    }

    // Remove a variable from the environment
    public void removeVariable(String name) {
        variables.remove(name);
    }

    // Clear all variables from the environment
    public void clear() {
        variables.clear();
    }
    public void updateVariable(String variableName, int value) {
        variables.put(variableName, value);
    }

    public void placeVariables(VariableDeclarationNode variableNode) {
        String variableName = variableNode.getVariableName();
        String variableValue = variableNode.getValue();
        // Assuming you only support INT, FLOAT, CHAR, and BOOL types
        // You might need to convert variableValue to the appropriate type based on the variableNode's datatype
        Object value = switch (variableNode.getDataType()) {
            case "INT" -> Integer.parseInt(variableValue);
            case "FLOAT" -> Float.parseFloat(variableValue);
            case "CHAR" -> variableValue.charAt(1); // Assuming single character value
            case "BOOL" -> Boolean.parseBoolean(variableValue);
            default -> null;
        };
        variables.put(variableName, value);
    }

    public void displayVariables() {
        System.out.println("Variables in the environment:");
        for (String name : variables.keySet()) {
            Object value = variables.get(name);
            System.out.println(name + " = " + value);
        }
    }
}
