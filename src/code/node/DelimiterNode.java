package code.node;

public class DelimiterNode extends Node{
    private String dataType;
    private String value;

    private boolean programStart = false; // this only applies to BEGIN and END (set to default false)

    public String getDataType() {
        return dataType;
    }

    public DelimiterNode(String dataType, boolean programStart) {
        this.dataType = dataType;
        this.programStart = programStart;
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}