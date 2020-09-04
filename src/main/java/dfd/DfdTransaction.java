package dfd;


import com.google.common.primitives.Bytes;
import com.google.common.primitives.UnsignedInteger;
import dfd.errors.SignTransactionException;
import dfd.operations.DepositOperation;
import dfd.operations.WithdrawOperation;
import dfd.operations.WithdrawWithSignature;
import org.bitcoinj.core.*;
import org.bitcoinj.params.MainNetParams;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DfdTransaction extends ByteSerializable {

    public static final String EXPIRATION = "expiration";
    public static final String RESERVED = "reserved";
    public static final String FROM_ACCOUNT = "from_account";
    public static final String INPORT_ASSET = "inport_asset";
    public static final String OPERATIONS = "operations";
    public static final String RESULT_TRX_TYPE = "result_trx_type";
    public static final String RESULT_TRX_ID = "result_trx_id";


    UnsignedInteger expiration;
    Optional<Map<String, String>> reserved;
    String from_account;
    Asset inport_asset;
    List<Operation> operations;
    Byte result_trx_type;

    Ripemd1160 result_trx_id;

    public DfdTransaction() {
        this.operations = new LinkedList<Operation>();
        this.operations.add(new Operation());
        this.reserved = new Optional<Map<String, String>>(new HashMap<String, String>());
        this.reserved.getOptionalField().put("a", "b");

        this.from_account = "";
        this.inport_asset = new Asset();
        this.result_trx_type = 0;
        this.result_trx_id = new Ripemd1160();

    }


    @Override
    public Field[] getOrderedFieldName() {
        List<Field> fields;
        fields = new LinkedList<Field>();
        try {
            fields.add(this.getClass().getDeclaredField(EXPIRATION));
            fields.add(this.getClass().getDeclaredField(RESERVED));
            fields.add(this.getClass().getDeclaredField(FROM_ACCOUNT));
            fields.add(this.getClass().getDeclaredField(INPORT_ASSET));
            fields.add(this.getClass().getDeclaredField(OPERATIONS));
            fields.add(this.getClass().getDeclaredField(RESULT_TRX_TYPE));
            fields.add(this.getClass().getDeclaredField(RESULT_TRX_ID));


        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Field[] ret_fields = new Field[fields.size()];
        fields.toArray(ret_fields);
        return ret_fields;
    }

    public byte[] getSignature(ECKey privateKey, String chainid) {
        boolean isGrapheneCanonical = false;
        byte[] sigData = null;

        while (!isGrapheneCanonical) {
            byte[] serializedTransaction = ByteHandle.pack(this);
            byte[] chainId = Util.hexToBytes(chainid);
            serializedTransaction = Bytes.concat(serializedTransaction, chainId);


            Sha256Hash hash = Sha256Hash.wrap(Sha256Hash.hash(serializedTransaction));
            int recId = -1;
            ECKey.ECDSASignature sig = privateKey.sign(hash);

            // Now we have to work backwards to figure out the recId needed to recover the signature.
            for (int i = 0; i < 4; i++) {
                ECKey k = ECKey.recoverFromSignature(i, sig, hash, privateKey.isCompressed());
                if (k != null && k.getPubKeyPoint().equals(privateKey.getPubKeyPoint())) {
                    recId = i;
                    break;
                }
            }

            sigData = new byte[65];  // 1 header + 32 bytes for R + 32 bytes for S
            int headerByte = recId + 27 + (privateKey.isCompressed() ? 4 : 0);
            sigData[0] = (byte) headerByte;
            System.arraycopy(Utils.bigIntegerToBytes(sig.r, 32), 0, sigData, 1, 32);
            System.arraycopy(Utils.bigIntegerToBytes(sig.s, 32), 0, sigData, 33, 32);

            // Further "canonicality" tests
            if (((sigData[0] & 0x80) != 0) || (sigData[0] == 0) ||
                    ((sigData[1] & 0x80) != 0) || ((sigData[32] & 0x80) != 0) ||
                    (sigData[32] == 0) || ((sigData[33] & 0x80) != 0)) {
                this.expiration = this.expiration.plus(UnsignedInteger.fromIntBits(1));
            } else {
                isGrapheneCanonical = true;
            }
        }
        return sigData;
    }

    public static ECKey fromBase58(String base58) {
        ECKey key2 = DumpedPrivateKey.fromBase58(MainNetParams.get(), base58).getKey();
        return key2;
    }

    /**
     * 
     *
     * @param rawTx
     * @param priavteKeyWif
     * @param toAddress     
     * @param amount        
     * @return
     */
    public static Map<String, Object> signTransaction(RawTransaction rawTx, String priavteKeyWif,
                                                      String toAddress,
                                                      String amount, String chainId, int precision) throws SignTransactionException {
        Map<String, Object> result = new HashMap<String, Object>(rawTx.toMap());
        try {
            //trx_data
            String hexdata = rawTx.getTrxData();
            byte[] datas = Util.hexToBytes(hexdata);
            InputStream input = new ByteArrayInputStream(datas);
            DfdTransaction temp;
            temp = ByteHandle.unpack(input, DfdTransaction.class);

            for (int i = 0; i < temp.operations.size(); ++i) {
                if (temp.operations.get(i).type == OperationType.withdraw_op_type.getValue()) {
                    WithdrawOperation aa = temp.operations.get(i).toData();
                }
                if (temp.operations.get(i).type == OperationType.deposit_op_type.getValue()) {
                    DepositOperation aa = temp.operations.get(i).toData();
                    WithdrawWithSignature ss = ((DepositOperation) aa).condition.toData();
                    if ((amount != null && new BigDecimal(((DepositOperation) aa).amount).compareTo(
                            new BigDecimal(amount).multiply(new BigDecimal(precision > 0 ? String.valueOf(Math.pow(10, precision)) : "100000"))) != 0)
                            || (toAddress != null && !ss.owner.toString().equals(toAddress))) {
                        throw new SignTransactionException("invalid tx amount");
                    }
                }
            }

            ECKey eckey;
            byte[] base58_data = Base58.decode(priavteKeyWif);
            byte[] key_data = new byte[base58_data.length - 5];
            System.arraycopy(base58_data, 1, key_data, 0, base58_data.length - 5);
            eckey = ECKey.fromPrivate(key_data, false);
            byte[] signature = temp.getSignature(eckey, chainId);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));  
            Date dt = new Date(temp.expiration.longValue() * 1000);
            Map<String, Object> trxPart = new HashMap<>(rawTx.getTrxPartAsMap());
            trxPart.put("expiration", sdf.format(dt));
            trxPart.put("signatures", Collections.singletonList(Util.bytesToHex(signature)));
            result.put("trx", trxPart);
        } catch (Exception e) {
            e.getStackTrace();
            Logger.getLogger(DfdTransaction.class.getName()).log(Level.SEVERE, null, e);
            throw new SignTransactionException(e);
        }

        return result;
    }

    public static void main(String[] args) {
    	
    }
}
