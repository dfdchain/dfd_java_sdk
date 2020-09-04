package dfd;

import org.bitcoinj.core.ECKey;

public class PrivateKeyAddressInfo {
    private ECKey privateKey;
    private String privateKeyWif;
    private String address;
    private String publicKey;

    public ECKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(ECKey privateKey) {
        this.privateKey = privateKey;
    }

    public String getPrivateKeyWif() {
        return privateKeyWif;
    }

    public void setPrivateKeyWif(String privateKeyWif) {
        this.privateKeyWif = privateKeyWif;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
