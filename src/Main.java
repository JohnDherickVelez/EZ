import code.Environment.Environment;
import code.lexer.Lexer;
import code.model.Token;
import code.node.DisplayNode;
import code.parser.CustomExceptions;
import code.parser.Parser;
import code.node.DelimiterNode;
import code.node.Node;
import code.node.VariableDeclarationNode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws CustomExceptions {
        String filePath = "./src/testfiles/test_1"; // Replace this with the path to your text file
        StringBuilder sourceCode = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sourceCode.append(line).append("\n"); // Append each line to the sourceCode string
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle or log any IOException that occurs
        }
        // Place all Testing functions here:

        // Test if source code is indeed converted to String (Can Comment tests when running full program)
        System.out.println("Source code:");
        System.out.println(sourceCode);

        // Test if Lexer successfully tokenizes the source code
        Lexer lexer = new Lexer();
        List<Token> tokenlist = lexer.tokenizeSourceCode(String.valueOf(sourceCode));

        // Environment and Variable List for Variable Hashmap storage
        Environment environment = new Environment();
        List<String> variableList = new ArrayList<>();

        // Parser Instantiation
        Parser parser = new Parser(tokenlist, environment);

        // Initializes the root node of the AST
        Node rootNode = parser.produceAST();

        // Traverses AST
        executeAST(rootNode);

        // Displays variables inside the environment
        environment.displayVariables();

        // Place Interpreter only functions here:

    }

    private static void executeAST(Node node) {
        // Perform appropriate actions based on code.node type
        if (node instanceof DelimiterNode) {
            DelimiterNode delimiterNode = (DelimiterNode) node;
            // Execute functionality based on delimiter type
            if (delimiterNode.getDataType().equals("BEGIN_CODE")) {
                // Logic to start the program
                System.out.println("Program started...");
            } else if (delimiterNode.getDataType().equals("END_CODE")) {
                // Logic to end the program
                System.out.println("Program ended...");
            }
        } else if (node instanceof VariableDeclarationNode) {
            VariableDeclarationNode variableNode = (VariableDeclarationNode) node;
            // Logic to handle variable declarations
            String variableType = variableNode.getDataType();
            String variableName = variableNode.getVariableName();
            String variableValue = variableNode.getValue();
            // Perform actions based on variable type, name, and value
            System.out.println("Variable declaration: " + variableType + " " + variableName + " = " + variableValue);
        } else if (node instanceof DisplayNode) {
            DisplayNode displayNode = (DisplayNode) node;
            // Print out the value stored in the DisplayNode
//            System.out.println("Display: " + displayNode.getValue());
            System.out.println(displayNode.getValue());
        }
        // Traverse child nodes recursively
        List<Node> children = node.getChildren();
        for (Node child : children) {
            executeAST(child);
        }
    }
}
