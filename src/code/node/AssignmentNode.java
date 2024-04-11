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

    public String getValue() { return  value.toString();}

//    @Override
//    public void print(int indentLevel) {
//        for (int i = 0; i < indentLevel; i++) {
//            System.out.print("  "); // Print indentation
//        }
//        System.out.println("Assignment Statement:");
//        System.out.println("  Variable: " + variableName);
//        value.print(indentLevel + 1); // Recursively print value code.node
//    } imma comment lng sa for noww pra mapushh gomen!!!!
}
