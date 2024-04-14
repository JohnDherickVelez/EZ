package code.node;

public class VariableDeclarationNode extends Node {
    private String variableName;
    private String dataType;
    private Object value; // Change the type of 'value' to Object to support both integers and arithmetic expressions

    public VariableDeclarationNode(String dataType, String variableName, Object value) {
        this.dataType = dataType;
        this.variableName = variableName;
        this.value = value;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Object getValue() {
        return value;
    }
}
