package dfd.vo;

import java.util.List;

public class ContractTransactionReceiptVo {
    private String result_trx_id; 
    private String orig_trx_id; 
    private Long block_num; 
    private Integer block_position;
    private Integer trx_type; 
    private Boolean is_completed; 
    private List<String> reserved; 
    private String timestamp;

    public String getResult_trx_id() {
        return result_trx_id;
    }

    public void setResult_trx_id(String result_trx_id) {
        this.result_trx_id = result_trx_id;
    }

    public String getOrig_trx_id() {
        return orig_trx_id;
    }

    public void setOrig_trx_id(String orig_trx_id) {
        this.orig_trx_id = orig_trx_id;
    }

    public Long getBlock_num() {
        return block_num;
    }

    public void setBlock_num(Long block_num) {
        this.block_num = block_num;
    }

    public Integer getBlock_position() {
        return block_position;
    }

    public void setBlock_position(Integer block_position) {
        this.block_position = block_position;
    }

    public Integer getTrx_type() {
        return trx_type;
    }

    public void setTrx_type(Integer trx_type) {
        this.trx_type = trx_type;
    }

    public Boolean getIs_completed() {
        return is_completed;
    }

    public void setIs_completed(Boolean is_completed) {
        this.is_completed = is_completed;
    }

    public List<String> getReserved() {
        return reserved;
    }

    public void setReserved(List<String> reserved) {
        this.reserved = reserved;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
