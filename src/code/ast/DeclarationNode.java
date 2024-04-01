package code.ast;
import code.model.TokenType;

public class DeclarationNode extends ASTNode {
    private TokenType tokenType;
    private String identifier;

    public DeclarationNode(TokenType tokenType, String identifier, int lineNumber) {
        super(lineNumber);
        this.tokenType = tokenType;
        this.identifier = identifier;
    }

    // Getters for dataType and identifier


    public TokenType getTokenType() {
        return tokenType;
    }

    public String getIdentifier() {
        return identifier;
    }
}
