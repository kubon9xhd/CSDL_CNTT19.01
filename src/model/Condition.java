package model;

public class Condition {
    private String fieldName;
    private Object value;
    private String operator; // Biểu diễn loại điều kiện: "=", ">", ">=", "<", "<=", "!="

    public Condition(String fieldName, Object value, String operator) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getValue() {
        return value;
    }

    public String getOperator() {
        return operator;
    }
}
