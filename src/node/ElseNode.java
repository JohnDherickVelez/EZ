package node;

public class ElseNode{
//     implements ASTNode
    private final ASTNode elseBody;

    public ElseNode(ASTNode elseBody) {
        this.elseBody = elseBody;
    }

//    @Override
//    public void print(int indentLevel) {
//        for (int i = 0; i < indentLevel; i++) {
//            System.out.print("  "); // Print indentation
//        }
//        System.out.println("Else Statement:");
//        elseBody.print(indentLevel + 1); // Recursively print else body node
//    }
}
