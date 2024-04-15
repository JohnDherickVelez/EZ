package code.node;

public class AssignmentNode extends Node {
//    implements ASTNode
    private final String variableName;
    private final Object value;


    public AssignmentNode(String variableName, Object value) {
        this.variableName = variableName;
        this.value = value;
    }

    public String getVariableName() { return  variableName; }

    public String getValue() { return value.toString();}

}
