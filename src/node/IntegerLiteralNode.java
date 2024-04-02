package node;

public class IntegerLiteralNode implements ASTNode {
    private final int value; // Add a field to store the integer value

    public IntegerLiteralNode(int value) {
        this.value = value;
    }

    @Override
    public void print(int indentLevel) {
        for (int i = 0; i < indentLevel; i++) {
            System.out.print("  "); // Print indentation
        }
        System.out.println("Integer Literal: " + value); // Print the integer value
    }
}
