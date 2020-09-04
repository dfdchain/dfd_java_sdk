package dfd.vo;

import com.google.gson.JsonElement;


public class OperationVo {

    public static final String WITHDRAW_OP_TYPE = "withdraw_op_type";
    public static final String DEPOSIT_OP_TYPE = "deposit_op_type";
    public static final String MEMO_OP_TYPE = "imessage_memo_op_type";

    private String type;
    private JsonElement data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "OperationVo{" +
                "type='" + type + '\'' +
                ", data=" + data +
                '}';
    }
}
