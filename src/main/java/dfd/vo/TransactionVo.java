package dfd.vo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class TransactionVo {

    private Set<String> relatedAddresses; 
    private BigDecimal txAmount; 
    private BigDecimal txFee; 
    private String txMemo; 
    private String fromAddress; 
    private String toAddress; 

    public static class Trx {
        private String expiration;
        private String dfd_account;
        private String result_trx_type;
        private String result_trx_id;
        private List<String> signatures;
        private List<OperationVo> operations;

        public String getExpiration() {
            return expiration;
        }

        public void setExpiration(String expiration) {
            this.expiration = expiration;
        }

        public String getDfd_account() {
            return dfd_account;
        }

        public void setDfd_account(String dfd_account) {
            this.dfd_account = dfd_account;
        }

        public String getResult_trx_type() {
            return result_trx_type;
        }

        public void setResult_trx_type(String result_trx_type) {
            this.result_trx_type = result_trx_type;
        }

        public String getResult_trx_id() {
            return result_trx_id;
        }

        public void setResult_trx_id(String result_trx_id) {
            this.result_trx_id = result_trx_id;
        }

        public List<String> getSignatures() {
            return signatures;
        }

        public void setSignatures(List<String> signatures) {
            this.signatures = signatures;
        }

        public List<OperationVo> getOperations() {
            return operations;
        }

        public void setOperations(List<OperationVo> operations) {
            this.operations = operations;
        }

        @Override
        public String toString() {
            return "Trx{" +
                    "expiration='" + expiration + '\'' +
                    ", dfd_account='" + dfd_account + '\'' +
                    ", result_trx_type='" + result_trx_type + '\'' +
                    ", result_trx_id='" + result_trx_id + '\'' +
                    ", signatures=" + signatures +
                    ", operations=" + operations +
                    '}';
        }
    }

    public static class ChainLocation {
        private Long block_num;
        private Integer trx_num;

        public Long getBlock_num() {
            return block_num;
        }

        public void setBlock_num(Long block_num) {
            this.block_num = block_num;
        }

        public Integer getTrx_num() {
            return trx_num;
        }

        public void setTrx_num(Integer trx_num) {
            this.trx_num = trx_num;
        }

        @Override
        public String toString() {
            return "ChainLocation{" +
                    "block_num=" + block_num +
                    ", trx_num=" + trx_num +
                    '}';
        }
    }

    private Trx trx;
    private AssetVo required_fees;
    private AssetVo alt_fees_paid;
    private Boolean skipexec;
    private Integer imessage_length;
    private ChainLocation chain_location;

    public Set<String> getRelatedAddresses() {
        return relatedAddresses;
    }

    public void setRelatedAddresses(Set<String> relatedAddresses) {
        this.relatedAddresses = relatedAddresses;
    }

    public BigDecimal getTxAmount() {
        return txAmount;
    }

    public void setTxAmount(BigDecimal txAmount) {
        this.txAmount = txAmount;
    }

    public BigDecimal getTxFee() {
        return txFee;
    }

    public void setTxFee(BigDecimal txFee) {
        this.txFee = txFee;
    }

    public String getTxMemo() {
        return txMemo;
    }

    public void setTxMemo(String txMemo) {
        this.txMemo = txMemo;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public Trx getTrx() {
        return trx;
    }

    public void setTrx(Trx trx) {
        this.trx = trx;
    }

    public AssetVo getRequired_fees() {
        return required_fees;
    }

    public void setRequired_fees(AssetVo required_fees) {
        this.required_fees = required_fees;
    }

    public AssetVo getAlt_fees_paid() {
        return alt_fees_paid;
    }

    public void setAlt_fees_paid(AssetVo alt_fees_paid) {
        this.alt_fees_paid = alt_fees_paid;
    }

    public Boolean getSkipexec() {
        return skipexec;
    }

    public void setSkipexec(Boolean skipexec) {
        this.skipexec = skipexec;
    }

    public Integer getImessage_length() {
        return imessage_length;
    }

    public void setImessage_length(Integer imessage_length) {
        this.imessage_length = imessage_length;
    }

    public ChainLocation getChain_location() {
        return chain_location;
    }

    public void setChain_location(ChainLocation chain_location) {
        this.chain_location = chain_location;
    }

    @Override
    public String toString() {
        return "TransactionVo{" +
                "relatedAddresses=" + relatedAddresses +
                ", txAmount=" + txAmount +
                ", txFee=" + txFee +
                ", txMemo='" + txMemo + '\'' +
                ", fromAddress='" + fromAddress + '\'' +
                ", toAddress='" + toAddress + '\'' +
                ", trx=" + trx +
                ", required_fees=" + required_fees +
                ", alt_fees_paid=" + alt_fees_paid +
                ", skipexec=" + skipexec +
                ", imessage_length=" + imessage_length +
                ", chain_location=" + chain_location +
                '}';
    }
}
