
import code.Environment.Environment;
import code.lexer.Lexer;
import code.lexer.Lexer2;
import code.model.Token;
import code.node.*;
import code.parser.CustomExceptions;
import code.parser.Parser;
import code.parser.Parser2;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

    public class Main {
        public static void main(String[] args) throws CustomExceptions {
            String filePath = "./src/testfiles/test_arith2"; // Replace this with the path to your text file
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
//            Lexer2 lexer2 = new Lexer2();
            List<Token> tokenlist = lexer.tokenizeSourceCode(String.valueOf(sourceCode));
//            List<Token> tokenlist = lexer2.tokenizeSourceCode(String.valueOf(sourceCode));

            // Environment and Variable List for Variable Hashmap storage
            Environment environment = new Environment();
            List<String> variableList = new ArrayList<>();

            // Parser Instantiation
            Parser parser = new Parser(tokenlist, environment);
//            Parser2 parser = new Parser2(tokenlist, environment);

            // Initializes the root node of the AST
            Node rootNode = parser.produceAST();
//            Node rootNode = parser.produceAST();

            environment.displayVariables();
            // Traverses AST
            executeAST(rootNode, environment);

            // Displays variables inside the environment
            environment.displayVariables();

            // Place Interpreter only functions here:
//             parser.printAllExpressions();
//            parser.printAllExpressions();
        }
        private static void executeAST(Node node, Environment environment) throws CustomExceptions {
            // Perform appropriate actions based on code.node type
            if (node instanceof DelimiterNode delimiterNode) {
                // Execute functionality based on delimiter type
                if (delimiterNode.getDataType().equals("BEGIN_CODE")) {
                    // Logic to start the program
                    System.out.println("Program started...");
                } else if (delimiterNode.getDataType().equals("END_CODE")) {
                    // Logic to end the program
                    System.out.println("Program ended...");
                }
            } else if (node instanceof VariableDeclarationNode variableNode) {
                // Logic to handle variable declarations
                String variableType = variableNode.getDataType();
                String variableName = variableNode.getVariableName();
                String variableValue = (String) variableNode.getValue();
                // Perform actions based on variable type, name, and value
                System.out.println("Variable declaration: " + variableType + " " + variableName + " = " + variableValue);
            } else if (node instanceof DisplayNode) {// COPY START
                DisplayNode displayNode = (DisplayNode) node;
                StringBuilder outputBuilder = new StringBuilder();

                for (String varName : displayNode.getVariableNames()) {
                    if (varName.equals("$")) {
                        outputBuilder.append("\n"); // Append a newline character if the variable name is "$"
                    } else if (varName.startsWith("\"") && varName.endsWith("\"")) {
                        // This is quoted text, remove the surrounding quotes and append to the output
                        String textInsideQuotes = varName.substring(1, varName.length() - 1);
                        outputBuilder.append(textInsideQuotes);

                    } else if (varName.startsWith("[") && varName.endsWith("]")) {
                        // This is quoted text, remove the surrounding quotes and append to the output
                        String textInsideBrackets = varName.substring(1, varName.length() - 1);
                        outputBuilder.append(textInsideBrackets);

                    } else {
                        // Check if varName exists in the environment
                        Object value = environment.getVariable(varName);

                        if (value != null) {
                            outputBuilder.append(value); // Append variable value if found
                        } else {
                            // Append varName as a literal string if not found in environment
                            outputBuilder.append(varName);
                        }
                    }

                    // Append a space after each item (including quoted text)
                    outputBuilder.append(" ");
                }

                System.out.println(outputBuilder.toString());// CHANGES END
//            else if (node instanceof DisplayNode) {
//                DisplayNode displayNode = (DisplayNode) node;
//                StringBuilder outputBuilder = new StringBuilder();
//
//                for (String varName : displayNode.getVariableNames()) {
//                    if (varName.equals("$")) {
//                        outputBuilder.append("\n"); // Append a newline character if the variable name is "$"
//                    } else if (varName.startsWith("\"") && varName.endsWith("\"")) {
//                        // This is quoted text, remove the surrounding quotes and append to the output
//                        String textInsideQuotes = varName.substring(1, varName.length() - 1);
//                        outputBuilder.append(textInsideQuotes);
//                    } else {
//                        // Check if varName exists in the environment
//                        Object value = environment.getVariable(varName);
//
//                        if (value != null) {
//                            outputBuilder.append(value); // Append variable value if found
//                        } else {
//                            // Append varName as a literal string if not found in environment
//                            outputBuilder.append(varName);
//                        }
//                    }
//                    // Append a space after each item (including quoted text)
//                    //outputBuilder.append(" ");
//                }
////                // Remove the trailing space added after the last item
////                if (outputBuilder.length() > 0) {
////                    outputBuilder.setLength(outputBuilder.length() - 1);
////                }
//                System.out.println(outputBuilder.toString());
            } else if (node instanceof AssignmentNode assignmentNode) {
                String variableName = assignmentNode.getVariableName();
                String variableValue = assignmentNode.getValue();
                System.out.println("Assignment statement: " + variableName + " = " + variableValue);
            } else if(node instanceof ScanNode scanNode) {
                List<String> scannedVariables = scanNode.getScanVariables();
                System.out.println("Scanned variables: " + scannedVariables);
                Scanner scanner = new Scanner(System.in);
                List<Integer> userInput = new ArrayList<>();

                // Prompt the user for input based on scanned variables
                for (String variableName : scannedVariables) {
                    System.out.print("Enter value for " + variableName + ": ");
                    int value = scanner.nextInt();
                    userInput.add(value);
                }
                // Update variables in the environment with user input
                for (int i = 0; i < scannedVariables.size(); i++) {
                    String variableName = scannedVariables.get(i);
                    environment.setVariable(variableName, userInput.get(i));
                }
            }
            // Traverse child nodes recursively
            List<Node> children = node.getChildren();
            for (Node child : children) {
                executeAST(child, environment);
            }
        }
    }


