package dfd;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class RawTransaction {
    private final JsonObject rawTxJson;

    public RawTransaction(JsonObject rawTxJson) {
        this.rawTxJson = rawTxJson;
    }

    public String getTrxData() {
        return rawTxJson.get("trx_data").getAsString();
    }

    public Map<String, Object> getTrxPartAsMap() {
        JsonObject rawTxTrxPart = rawTxJson.get("trx").getAsJsonObject();
        Map<String, Object> trxData = new HashMap<>();
        for (String key : rawTxTrxPart.keySet()) {
            trxData.put(key, rawTxTrxPart.get(key));
        }
        return trxData;
    }

    public JsonObject getRawTxJson() {
        return rawTxJson;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> trx = new HashMap<>();
        for (String key : rawTxJson.keySet()) {
            trx.put(key, rawTxJson.get(key));
        }
        return trx;
    }

    @Override
    public String toString() {
        return rawTxJson.toString();
    }
}
