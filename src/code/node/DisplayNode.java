package code.node;

import java.util.List;

public class DisplayNode extends Node {
//    private String value;
//
//    public DisplayNode(String value) {
//        this.value = value;
//    }
//
//    public String getValue() {
//        return value;
//    }
//
    private List<String> variableNames;

    public DisplayNode(List<String> variableNames) {
        this.variableNames = variableNames;
    }

    public List<String> getVariableNames() {
        return variableNames;
    }
}
