package code.node;

import code.model.Token;

import java.util.ArrayList;
import java.util.List;

public class IfNode extends Node {
    private final Boolean expressionResult;
    public IfNode(Boolean expressionResult) {
        this.expressionResult = expressionResult;
    }

    public Boolean getExpressionResult() {
        return expressionResult;
    }
}

