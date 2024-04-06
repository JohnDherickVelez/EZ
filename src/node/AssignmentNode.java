package node;

public class AssignmentNode {
//    implements ASTNode
    private final String variableName;
    private final ASTNode value;

    public AssignmentNode(String variableName, ASTNode value) {
        this.variableName = variableName;
        this.value = value;
    }

//    @Override
//    public void print(int indentLevel) {
//        for (int i = 0; i < indentLevel; i++) {
//            System.out.print("  "); // Print indentation
//        }
//        System.out.println("Assignment Statement:");
//        System.out.println("  Variable: " + variableName);
//        value.print(indentLevel + 1); // Recursively print value node
//    } imma comment lng sa for noww pra mapushh gomen!!!!
}
