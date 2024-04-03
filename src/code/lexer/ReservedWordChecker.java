package code.lexer;

import java.util.HashSet;
import java.util.Set;

public class ReservedWordChecker {
    private static final Set<String> reservedWords = new HashSet<>();

    // Initialize the set of reserved words
    static {
        reservedWords.add("int");
        reservedWords.add("float");
        reservedWords.add("char");
        reservedWords.add("bool");
        reservedWords.add("and");
        reservedWords.add("or");
        reservedWords.add("not");
        reservedWords.add("if");
        reservedWords.add("else");
        reservedWords.add("begin");
        reservedWords.add("end");
    }

    public static boolean isReservedWord(String word) {
        return reservedWords.contains(word.toLowerCase());
    }
}
