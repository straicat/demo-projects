package domain;

import enums.ValueTypeEnum;

public class RedisResponse {
    private ValueTypeEnum valueType;
    private Object value;

    public ValueTypeEnum getValueType() {
        return valueType;
    }

    public void setValueType(ValueTypeEnum valueType) {
        this.valueType = valueType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
