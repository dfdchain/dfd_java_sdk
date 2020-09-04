package dfd.vo;

import java.math.BigInteger;

public class BlockchainAssetInfoVo {
    private Long id;
    private String symbol;
    private String name;
    private String description;
    private String precision;
    private String collected_fees; 

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrecision() {
        return precision;
    }

    public void setPrecision(String precision) {
        this.precision = precision;
    }

    public String getCollected_fees() {
        return collected_fees;
    }

    public void setCollected_fees(String collected_fees) {
        this.collected_fees = collected_fees;
    }
}
