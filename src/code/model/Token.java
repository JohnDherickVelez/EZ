package code.model;

public class Token {
    private final TokenType type;
    private final String value;
    private boolean isReservedKey = false; // to handle reserved tokens later

    public Token(TokenType type, String value, boolean isReservedKey) {
        this.type = type;
        this.value = value;
        this.isReservedKey = isReservedKey;
    }
    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public boolean getIsReservedKey() {return isReservedKey; }
    @Override
    public String toString() {
        return "Token {" +
                "type=" + type +
                ", value='" + value + '\'' +
                '}';
    }

    public enum TokenType {
        BEGIN_CODE,
        DATATYPE,
        VARIABLE,
        VALUE,
        ENDLINE,
        IDENTIFIER,
        OPERATOR,
        DELIMITER,
        ASSIGN,
        WHITESPACE,
        TEXT,
        S_QUOTE, DISPLAY, ESCAPE,
        SCAN, OPEN_P, CLOSE_P, EXPRESSION,
    }
}