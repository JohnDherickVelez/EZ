import code.Environment.Environment;
import code.lexer.Lexer;
import code.lexer.Lexer2;
import code.model.Token;
import code.node.*;
import code.parser.CustomExceptions;
import code.parser.Parser;
import code.parser.Parser2;
import code.semantics.SemanticAnalyzer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
// TODO:
// TODO: DONE!! Naming conventions | SHOULD NOT START WITH NUMBERS
// TODO: NOT YET DONE!! Negative numbers
// TODO: DONE !! Default variable declarations to zero in Semantics
// TODO: NOT YET DONE!! Handle these cases
// INT a = 10
// INT b = a + 10
// the output is a = 20; b - 20 <---- 'a' should not be added with 10. Only 'b'!
    public class Main {
        public static void main(String[] args) throws CustomExceptions {
            // Builds source code to string
            String sourceCode = String.valueOf(stringBuildSourceCode("./src/testfiles/test_semantics"));

            // Prints source
//            printSourceCode(String.valueOf(sourceCode));

            //initializes all Interpreter objects
            initializeVariables_1(new StringBuilder(sourceCode));
        }

        public static StringBuilder stringBuildSourceCode(String filepath) {
            StringBuilder sourceCode = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sourceCode.append(line).append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace(); // Handle or log any IOException that occurs
            }
            return sourceCode;
        }

        public static void printSourceCode(String sourceCode) {
            System.out.println(sourceCode);
        }
        private static void initializeVariables_1(StringBuilder sourceCode) throws CustomExceptions {
            // initializes sourcecode
            Lexer2 lexer = new Lexer2();
//            Lexer lexer = new Lexer();
            List<Token> tokenlist = lexer.tokenizeSourceCode(String.valueOf(sourceCode));

            // initializes environment
            Environment environment = new Environment();

//            Parser parser = new Parser(tokenlist, environment);
            Parser2 parser = new Parser2(tokenlist, environment);

            // initializes Abstract Syntax Tree
            Node rootNode = parser.produceAST();

            // Initializes Semantic Analyzer
            SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);
//             Can uncomment to run AST with/out errors
//            try {
//                semanticAnalyzer.analyze(tokenlist, rootNode);
//            } catch (CustomExceptions e) {
//                System.err.println("Semantic error: " + e.getMessage());
//                return; // Exit the program if semantic errors are detected
//            }

            // If semantic analysis passes without errors, execute the AST
            try {
                executeAST(rootNode, environment);
            } catch (CustomExceptions e) {
                System.err.println("Runtime error: " + e.getMessage());
            }

            // uncomment if deem necessary
            environment.displayVariables();
            lexer.printTokensFromList(tokenlist);
        }

        private static void executeAST(Node node, Environment environment) throws CustomExceptions {
            // Perform appropriate actions based on code.node type
            if (node instanceof DelimiterNode delimiterNode) {
                // Execute functionality based on delimiter type
                if (delimiterNode.getDataType().equals("BEGIN CODE")) {
                    // Logic to start the program
                    System.out.println("Program started...");
                } else if (delimiterNode.getDataType().equals("END CODE")) {
                    // Logic to end the program
                    System.out.println("Program ended...");
                }
            } else if (node instanceof VariableDeclarationNode variableNode) {
                // Logic to handle variable declarations
                String variableType = variableNode.getDataType();
                String variableName = variableNode.getVariableName();
                String variableValue = (String) variableNode.getValue();
//                // Perform actions based on variable type, name, and value
//                System.out.println("Variable declaration: " + variableType + " " + variableName + " = " + variableValue);
            } else if (node instanceof DisplayNode displayNode) {
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
                }
                System.out.println(outputBuilder);
            } else if (node instanceof AssignmentNode assignmentNode) {
                String variableName = assignmentNode.getVariableName();
                String variableValue = assignmentNode.getValue();
//                System.out.println("Assignment statement: " + variableName + " = " + variableValue);
            }
//            else if(node instanceof ScanNode scanNode) {
//                List<String> scannedVariables = scanNode.getScanVariables();
////                System.out.println("Scanned variables: " + scannedVariables);
//                Scanner scanner = new Scanner(System.in);
//                List<Integer> userInput = new ArrayList<>();
//
//                // Prompt the user for input based on scanned variables
//                for (String variableName : scannedVariables) {
//                    System.out.print("Enter value for " + variableName + ": ");
//                    int value = scanner.nextInt();
//                    userInput.add(value);
//                }
//
//                // Update variables in the environment with user input
//                for (int i = 0; i < scannedVariables.size(); i++) {
//                    String variableName = scannedVariables.get(i);
//                    environment.setVariable(variableName, userInput.get(i));
//                }
//            }
            else if(node instanceof ScanNode scanNode) {
                List<String> scannedVariables = scanNode.getScanVariables();
//                System.out.println("Scanned variables: " + scannedVariables);
                Scanner scanner = new Scanner(System.in);
                List<String> userInput = new ArrayList<>();
                String inputLine = scanner.nextLine();
                userInput = List.of(inputLine.split(","));
                // Prompt the user for input based on scanned variables
//                for (String variableName : scannedVariables) {
//                    System.out.print("Enter value for " + variableName + ": ");
//                    String value = scanner.nextLine();
//                    userInput.add(value);
//                }

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


