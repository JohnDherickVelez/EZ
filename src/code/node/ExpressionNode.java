package code.node;

import java.util.Stack;

public class ExpressionNode extends Node {
    private String variableName;
    private int value; // Change the type to int

    public ExpressionNode(String variableName, int value) { // Update the constructor
        this.variableName = variableName;
        this.value = value;
    }

    public String getVariableName() { return variableName; }

    public int getValue() { return value; } // Return int instead of String
}
