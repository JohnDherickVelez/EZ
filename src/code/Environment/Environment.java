package code.Environment;

import code.node.VariableDeclarationNode;

import java.util.HashMap;
import java.util.Map;

public class Environment {
private Map<String, VariableInfo> variables;

        public Environment() { variables = new HashMap<>(); }

        // Store a variable value along with datatype in the environment
        public void setVariable(String name, Object value) {
            // Check if the variable already exists in the environment
            if (variables.containsKey(name)) {
            // Get the existing datatype from the environment
            VariableInfo existingVariable = variables.get(name);
            String datatype = existingVariable.getDatatype();

            // Update the existing variable with the new value and datatype
            variables.put(name, new VariableInfo(value, datatype));
            } else {
            // If the variable doesn't exist, add it with a default datatype (or without datatype)
            variables.put(name, new VariableInfo(value, ""));
            }
        }

        // Retrieve a variable value from the environment
        public Object getVariable(String name) {
            VariableInfo variableInfo = variables.get(name);
            return variableInfo != null ? variableInfo.getValue() : null;
        }

        // Retrieve datatype of a variable from the environment
        public String getVariableType(String name) {
            VariableInfo variableInfo = variables.get(name);
            return variableInfo != null ? variableInfo.getDatatype() : null;
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

        // Inner class to hold variable information (value and datatype)
        private static class VariableInfo {
            private Object value;
            private String datatype;

            public VariableInfo(Object value, String datatype) {
                this.value = value;
                this.datatype = datatype;
            }

            public Object getValue() {
                return value;
            }

            public String getDatatype() {
                return datatype;
            }
        }

        public void updateVariable(String variableName, Object value, String datatype) {
            variables.put(variableName, new VariableInfo(value, datatype));
        }

        public void placeVariables(VariableDeclarationNode variableNode) {
            String variableName = variableNode.getVariableName();
            String variableValue = variableNode.getValue();
            String datatype = variableNode.getDataType();

            Object value = switch (datatype) {
            case "INT" -> Integer.parseInt(variableValue);
            case "FLOAT" -> Float.parseFloat(variableValue);
            case "CHAR" -> variableValue.charAt(1); // Assuming single character value
            case "BOOL" -> Boolean.parseBoolean(variableValue);
            default -> null;
            };

            updateVariable(variableName, value, datatype);
        }


        public void displayVariables() {
            System.out.println("Variables in the environment:");
                for (String name : variables.keySet()) {
                VariableInfo variableInfo = variables.get(name);
                System.out.println(name + " = " + variableInfo.getValue() + " (Type: " + variableInfo.getDatatype() + ")");
                }
            }
        }