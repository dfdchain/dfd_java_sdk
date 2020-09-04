package dfd.vo;

import java.math.BigDecimal;
import java.util.List;

public class AddressBalancesVo {
    public static class BalanceItemVo {
        private BigDecimal balance; 
        private Integer assetId;

        public BigDecimal getBalance() {
            return balance;
        }

        public void setBalance(BigDecimal balance) {
            this.balance = balance;
        }

        public Integer getAssetId() {
            return assetId;
        }

        public void setAssetId(Integer assetId) {
            this.assetId = assetId;
        }

        @Override
        public String toString() {
            return "BalanceItemVo{" +
                    "balance=" + balance +
                    ", assetId=" + assetId +
                    '}';
        }
    }

    private List<BalanceItemVo> balances;

    public List<BalanceItemVo> getBalances() {
        return balances;
    }

    public void setBalances(List<BalanceItemVo> balances) {
        this.balances = balances;
    }

    @Override
    public String toString() {
        return "AddressBalancesVo{" +
                "balances=" + balances +
                '}';
    }
}
