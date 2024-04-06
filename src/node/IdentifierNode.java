package node;

public class IdentifierNode implements ASTNode {
    private final String name;

    public IdentifierNode(String name) {
        this.name = name;
    }

    @Override
    public void print(int indentLevel) {
        for (int i = 0; i < indentLevel; i++) {
            System.out.print("  "); // Print indentation
        }
        System.out.println("Identifier: " + name);
    }
}
