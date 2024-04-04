import code.lexer.Lexer;
import code.model.Token;
import code.parser.Parser;
import node.ASTNode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Test string
//        String sampleProgram =
//                "BEGIN CODE\n" +
//                "INT a = 5;\n" +
//                "# comments\n" +
//                "FLOAT b = 3.14;\n" +
//                "DISPLAY: a\n" +
//                "END CODE" +
//                "# comment\n";
//
//        Lexer lexer = new Lexer(sampleProgram);
//        List<Token> tokens = lexer.tokenize();
//
//        for (Token token : tokens) {
//            System.out.println(token.getType() + ": " + token.getValue());
//        }
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
        System.out.println("Tokenization result:");
        List<Token> tokenlist = lexer.tokenizeSourceCode(String.valueOf(sourceCode));


        Parser parser = new Parser(tokenlist);
        ASTNode rootNode = parser.parse();
        rootNode.print(0);
    }
}
