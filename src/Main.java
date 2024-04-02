import code.interpreter.Interpreter;
import code.lexer.Lexer;
import code.model.Token;
import code.parser.Parser;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Test string
        String sampleProgram = "BEGIN CODE\n" +
                "INT a = 5;\n" +
                "# comments\n" +
                "FLOAT b = 3.14;\n" +
                "END CODE";

        Lexer lexer = new Lexer(sampleProgram);
        List<Token> tokens = lexer.tokenize();

        System.out.println("Tokenization result:");
        for (Token token : tokens) {
            System.out.println(token.getType() + ": " + token.getValue());
        }

        Parser parser = new Parser(tokens);
        ASTNode rootNode = parser.parse();

        // Now you can traverse the AST and perform any actions based on the structure
        // For testing purposes, you can print out the parsed AST
        printAST(rootNode);

        // Interpret the AST
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(rootNode);
    }
    private static void printAST(ASTNode node) {
        // Perform depth-first traversal of the AST and print out node information

        // You need to implement this method according to your AST structure
    }
}

//public class Main {
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//
//        System.out.println("Welcome to the interpreter.");
//        System.out.println("Enter your commands and functions (type 'END CODE' to finish):");
//
//        StringBuilder codeBuilder = new StringBuilder();
//
//        while (true) {
//            System.out.print("> ");
//            String input = scanner.nextLine();
//
//            if (input.equals("END CODE")) {
//                System.out.println("Parsing and interpreting code...");
//                break;
//            }
//
//            codeBuilder.append(input).append("\n");
//        }
//
//        // Tokenize the entire code block
//        Lexer lexer = new Lexer(codeBuilder.toString());
//        List<Token> tokens = lexer.tokenize();
//
//        // Parse the tokens
//        Parser parser = new Parser(tokens);
//        ASTNode rootNode = parser.parse();
//
//        // Interpret the AST
//        Interpreter interpreter = new Interpreter();
//        interpreter.interpret(rootNode);
//
//        scanner.close();
//    }
//}