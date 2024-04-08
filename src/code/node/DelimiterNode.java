package code.node;

public class DelimiterNode extends Node{
    private String dataType;
    private String value; // Begin / End/ , / . / {/} / (/)

    private boolean programStart = false; // this only applies to BEGIN and END (set to default false)
//    public DelimiterNode(String value, List<Node> children) {
//        super(value, children);
//    }

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

    public enum Delimiter { // i dunno why i created this
        BEGIN,
        END,
        CODE,
        COMMA,
        OPENP,
        CLOSEP,
        OPENB,
        CLOSEB,
    }
}