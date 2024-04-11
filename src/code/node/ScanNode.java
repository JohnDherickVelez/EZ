package code.node;

import java.util.ArrayList;
import java.util.List;

public class ScanNode extends Node {
    private List<String> scanVariables = new ArrayList<>();


    public List<String> getScanVariables() {
        return scanVariables;
    }

    public void setScanVariables(List<String> scanVariables) {
        this.scanVariables = scanVariables;
    }

    public ScanNode(List<String> scanVariables) {
        this.scanVariables = scanVariables;
    }
}
