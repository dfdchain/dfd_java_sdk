package dfd.vo;

public class AddressBalanceItemVo {
    private String balance; 
    private String deposit_date;
    private String last_update;
    private ConditionVo condition;

    public static class ConditionVo {
        private Integer asset_id;
        private String type;
        private String balance_type;
        private DataVo data;

        public static class DataVo {
            private String owner;

            public String getOwner() {
                return owner;
            }

            public void setOwner(String owner) {
                this.owner = owner;
            }

            @Override
            public String toString() {
                return "DataVo{" +
                        "owner='" + owner + '\'' +
                        '}';
            }
        }

        public Integer getAsset_id() {
            return asset_id;
        }

        public void setAsset_id(Integer asset_id) {
            this.asset_id = asset_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getBalance_type() {
            return balance_type;
        }

        public void setBalance_type(String balance_type) {
            this.balance_type = balance_type;
        }

        public DataVo getData() {
            return data;
        }

        public void setData(DataVo data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "ConditionVo{" +
                    "asset_id=" + asset_id +
                    ", type='" + type + '\'' +
                    ", balance_type='" + balance_type + '\'' +
                    ", data=" + data +
                    '}';
        }
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getDeposit_date() {
        return deposit_date;
    }

    public void setDeposit_date(String deposit_date) {
        this.deposit_date = deposit_date;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public ConditionVo getCondition() {
        return condition;
    }

    public void setCondition(ConditionVo condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "AddressBalanceItemVo{" +
                "balance=" + balance +
                ", deposit_date='" + deposit_date + '\'' +
                ", last_update='" + last_update + '\'' +
                ", condition=" + condition +
                '}';
    }
}
