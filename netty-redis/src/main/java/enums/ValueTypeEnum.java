package enums;

public enum ValueTypeEnum {
    ONE_LINE_STRING(1, "单行字符串"),
    ONE_LINE_MESSAGE(2, "单行消息提示"),
    MULTI_LINE_STRING(3, "多行字符串");

    private final int code;

    private final String desc;

    ValueTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
