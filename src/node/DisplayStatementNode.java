package node;

public class DisplayStatementNode implements ASTNode {
    private ASTNode contentToDisplay;

    public DisplayStatementNode(ASTNode contentToDisplay) {
        this.contentToDisplay = contentToDisplay;
    }

    public ASTNode getContentToDisplay() {
        return contentToDisplay;
    }

    @Override
    public void print(int indentLevel) {
        // Print the indentation based on the indentLevel
        for (int i = 0; i < indentLevel; i++) {
            System.out.print("  "); // Assuming 2 spaces per level of indentation
        }

        System.out.println("Display Statement:");

        // Print the content to be displayed with increased indentation
        contentToDisplay.print(indentLevel + 1);
    }
}
