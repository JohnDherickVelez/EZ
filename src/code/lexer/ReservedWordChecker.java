package code.lexer;

import java.util.HashSet;
import java.util.Set;

public class ReservedWordChecker {
    private static final Set<String> reservedWords = new HashSet<>();

    // Initialize the set of reserved words
    static {
        reservedWords.add("INT");
        reservedWords.add("FLOAT");
        reservedWords.add("CHAR");
        reservedWords.add("BOOL");
        reservedWords.add("AND");
        reservedWords.add("OR");
        reservedWords.add("NOT");
        reservedWords.add("IF");
        reservedWords.add("ELSE");
        reservedWords.add("BEGIN");
        reservedWords.add("END");
    }

    public static boolean isReservedWord(String word) {
        return reservedWords.contains(word.toLowerCase());
    }
}
