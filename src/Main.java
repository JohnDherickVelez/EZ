import code.lexer.Lexer;
import code.model.Token;
import code.parser.CustomExceptions;
import code.parser.Parser;
import node.DelimiterNode;
import node.Node;
import node.VariableDeclarationNode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

        // Now, the contents of the text file are stored in the sourceCode string
        System.out.println("Source code:");
        System.out.println(sourceCode);

        Lexer lexer = new Lexer();
        List<Token> tokenlist = lexer.tokenizeSourceCode(String.valueOf(sourceCode));


//        lexer.printAllTokens();
        Parser parser = new Parser(tokenlist);

        Node rootNode = parser.produceAST();

        executeAST(rootNode);
//        parser.produceAST();
    }

    private static void executeAST(Node node) {
        // Perform appropriate actions based on node type
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
        }

        // Traverse child nodes recursively
        List<Node> children = node.getChildren();
        for (Node child : children) {
            executeAST(child);
        }
    }
}
