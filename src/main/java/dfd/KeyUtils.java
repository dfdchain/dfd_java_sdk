package dfd;

import com.google.common.primitives.Bytes;
import org.bitcoinj.core.Base58;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.ECGenParameterSpec;
import java.util.Arrays;

public class KeyUtils {

    public static String getPublicKeyStringFromPrivateWif(String privateKeyWif) {
        ECKey privateKey = recoverPrivateKeyFromWif(privateKeyWif);

        RIPEMD160Digest ripemd160Digest = new RIPEMD160Digest();
        byte[] publicKeyBytes = privateKey.getPubKey();
        byte[] checksumAll = new byte[160 / 8];
        ripemd160Digest.update(publicKeyBytes, 0, publicKeyBytes.length);
        ripemd160Digest.doFinal(checksumAll, 0);
        byte[] checksum = Arrays.copyOfRange(checksumAll, 0, 4);
        byte[] binaryKey = Bytes.concat(publicKeyBytes, checksum);
        return Address.ADDRESS_PREFIX + Base58.encode(binaryKey);
    }

    public static ECKey recoverPrivateKeyFromWif(String privateKeyWif) {
        byte[] base58_data = Base58.decode(privateKeyWif);
        byte[] key_data = new byte[base58_data.length - 5];
        System.arraycopy(base58_data, 1, key_data, 0, base58_data.length - 5);
        return ECKey.fromPrivate(key_data, true);
    }

    public static PrivateKeyAddressInfo generatePrivateKey() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
        ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256k1");
        keyGen.initialize(ecSpec);
        KeyPair keyPair = keyGen.generateKeyPair();
        ECPrivateKey epvt = (ECPrivateKey) keyPair.getPrivate();
        ECKey key = ECKey.fromPrivate(epvt.getS(), false);
        String privateKeyWif = key.getPrivateKeyAsWiF(NetworkParameters.fromID(NetworkParameters.ID_MAINNET));
        Address addressObj = new Address(ECKey.fromPrivate(key.getPrivKeyBytes(), true));
        String address = addressObj.toString();
        PrivateKeyAddressInfo result = new PrivateKeyAddressInfo();
        result.setPrivateKey(key);
        result.setPrivateKeyWif(privateKeyWif);
        result.setAddress(address);
        result.setPublicKey(getPublicKeyStringFromPrivateWif(privateKeyWif));
        return result;
    }
}
