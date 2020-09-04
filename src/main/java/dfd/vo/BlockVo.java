package dfd.vo;

import java.math.BigDecimal;
import java.util.List;

public class BlockVo {
    private String id;
    private String previous;
    private Long block_num;
    private String timestamp;
    private String transaction_digest;
    private String next_secret_hash;
    private String previous_secret;
    private String delegate_signature;
    private List<String> user_transaction_ids;
    private BigDecimal signee_shares_issued;
    private BigDecimal signee_fees_collected;
    private BigDecimal signee_fees_destroyed;
    private String random_seed;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public Long getBlock_num() {
        return block_num;
    }

    public void setBlock_num(Long block_num) {
        this.block_num = block_num;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTransaction_digest() {
        return transaction_digest;
    }

    public void setTransaction_digest(String transaction_digest) {
        this.transaction_digest = transaction_digest;
    }

    public String getNext_secret_hash() {
        return next_secret_hash;
    }

    public void setNext_secret_hash(String next_secret_hash) {
        this.next_secret_hash = next_secret_hash;
    }

    public String getPrevious_secret() {
        return previous_secret;
    }

    public void setPrevious_secret(String previous_secret) {
        this.previous_secret = previous_secret;
    }

    public String getDelegate_signature() {
        return delegate_signature;
    }

    public void setDelegate_signature(String delegate_signature) {
        this.delegate_signature = delegate_signature;
    }

    public List<String> getUser_transaction_ids() {
        return user_transaction_ids;
    }

    public void setUser_transaction_ids(List<String> user_transaction_ids) {
        this.user_transaction_ids = user_transaction_ids;
    }

    public BigDecimal getSignee_shares_issued() {
        return signee_shares_issued;
    }

    public void setSignee_shares_issued(BigDecimal signee_shares_issued) {
        this.signee_shares_issued = signee_shares_issued;
    }

    public BigDecimal getSignee_fees_collected() {
        return signee_fees_collected;
    }

    public void setSignee_fees_collected(BigDecimal signee_fees_collected) {
        this.signee_fees_collected = signee_fees_collected;
    }

    public BigDecimal getSignee_fees_destroyed() {
        return signee_fees_destroyed;
    }

    public void setSignee_fees_destroyed(BigDecimal signee_fees_destroyed) {
        this.signee_fees_destroyed = signee_fees_destroyed;
    }

    public String getRandom_seed() {
        return random_seed;
    }

    public void setRandom_seed(String random_seed) {
        this.random_seed = random_seed;
    }

    @Override
    public String toString() {
        return "BlockVo{" +
                "id='" + id + '\'' +
                ", previous='" + previous + '\'' +
                ", block_num=" + block_num +
                ", timestamp='" + timestamp + '\'' +
                ", transaction_digest='" + transaction_digest + '\'' +
                ", next_secret_hash='" + next_secret_hash + '\'' +
                ", previous_secret='" + previous_secret + '\'' +
                ", delegate_signature='" + delegate_signature + '\'' +
                ", user_transaction_ids=" + user_transaction_ids +
                ", signee_shares_issued=" + signee_shares_issued +
                ", signee_fees_collected=" + signee_fees_collected +
                ", signee_fees_destroyed=" + signee_fees_destroyed +
                ", random_seed='" + random_seed + '\'' +
                '}';
    }
}
