package dfd.test;

import dfd.Address;
import dfd.KeyUtils;
import dfd.PrivateKeyAddressInfo;
import org.bitcoinj.core.ECKey;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;

public class AddressTests {
    private Logger log = LoggerFactory.getLogger(AddressTests.class);


    @Test
    public void testRecoverAddressFromPrivateKey() {
        String privateKeyWif = "5Joayzz5K4HTs8zXksJHkhCViAm6qqoAXrFVq2giFifAQQXNvFk";
		// String privateKeyWif = "5Joayzz5K4HTs8zXksJHkhCViAm6qqoAXrFVq2giFifAQQXNvFk"; // DFUb6r6grQhgY8gpfopPkLLwMNqwdU3gE3
        ECKey eckey = KeyUtils.recoverPrivateKeyFromWif(privateKeyWif);
        Address addressObj = new Address(eckey);
        String address = addressObj.toString();
        log.info("recovered address {}", address);
    }

    @Test
    public void testRecoverPublicKeyFromPrivateKey() {
        String privateKeyWif = "5Joayzz5K4HTs8zXksJHkhCViAm6qqoAXrFVq2giFifAQQXNvFk";
        String publicKeyStr = KeyUtils.getPublicKeyStringFromPrivateWif(privateKeyWif);
        log.info("recovered public key {}", publicKeyStr);
    }

    @Test
    public void testGenerateAddress() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        PrivateKeyAddressInfo privateKeyAddressInfo = KeyUtils.generatePrivateKey();
        log.info("generated address {} public key {} private key {}",
                privateKeyAddressInfo.getAddress(),
                privateKeyAddressInfo.getPublicKey(),
                privateKeyAddressInfo.getPrivateKeyWif());
    }
}
