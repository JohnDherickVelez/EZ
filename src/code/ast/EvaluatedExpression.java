package code.ast;
import code.model.TokenType;// New class to represent an evaluated expression with type
public class EvaluatedExpression {
    private Object value;
    private TokenType tokenType;

    public EvaluatedExpression(Object value, TokenType tokenType) {
        this.value = value;
        this.tokenType = tokenType;
    }

    // Getter methods for value and tokenType

    public Object getValue() {
        return value;
    }

    public TokenType getTokenType() {
        return tokenType;
    }
}

// In evaluateExpression, return an EvaluatedExpression object
//return new EvaluatedExpression(result, expression.getTokenType()); // Assuming expression has getTokenType()

