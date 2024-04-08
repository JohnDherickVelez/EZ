package code.node;

public class IfNode  {
//    implements ASTNode
    private final ASTNode condition;
    private final ASTNode ifBody;

    public IfNode(ASTNode condition, ASTNode ifBody) {
        this.condition = condition;
        this.ifBody = ifBody;
    }

//    @Override
//    public void print(int indentLevel) {
//        for (int i = 0; i < indentLevel; i++) {
//            System.out.print("  "); // Print indentation
//        }
//        System.out.println("If Statement:");
//        System.out.println("  Condition:");
//        condition.print(indentLevel + 1); // Recursively print condition code.node
//        System.out.println("  Body:");
//        ifBody.print(indentLevel + 1); // Recursively print if body code.node
//    }
}
