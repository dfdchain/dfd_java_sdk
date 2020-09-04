package dfd.vo;

import java.math.BigDecimal;

public class AssetVo {
    private BigDecimal amount;
    private Integer asset_id;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getAsset_id() {
        return asset_id;
    }

    public void setAsset_id(Integer asset_id) {
        this.asset_id = asset_id;
    }

    @Override
    public String toString() {
        return "AssetVo{" +
                "amount=" + amount +
                ", asset_id='" + asset_id + '\'' +
                '}';
    }
}
