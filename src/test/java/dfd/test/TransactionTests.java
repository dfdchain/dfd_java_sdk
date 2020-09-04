package dfd.test;

import dfd.*;
import dfd.errors.NodeException;
import dfd.errors.SignTransactionException;
import dfd.vo.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

public class TransactionTests {
    private Logger log = LoggerFactory.getLogger(TransactionTests.class);

    private String endpoint = "http://localhost:61638/rpc"; 
    private String username = "a"; 
    private String password = "b"; 
    private NodeClient nodeClient = new NodeClient(endpoint, username, password);

    @Test
    public void testTransfer() throws NodeException, SignTransactionException {
        String privateKeyWif = "5Joayzz5K4HTs8zXksJHkhCViAm6qqoAXrFVq2giFifAQQXNvFk";
        String fromPubKey = KeyUtils.getPublicKeyStringFromPrivateWif(privateKeyWif);
        String toAddress = "DFUb6r6grQhgY8gpfopPkLLwMNqwdU3gE3"; // private key is 5Joayzz5K4HTs8zXksJHkhCViAm6qqoAXrFVq2giFifAQQXNvFk
        BigDecimal amount = new BigDecimal("1.23");
        String memo = "test123";
        RawTransaction rawTx = nodeClient.createTransferTransaction(fromPubKey, toAddress, amount, memo);
        log.info("create tx response {}", rawTx);

        Map<String, Object> signedTx = DfdTransaction.signTransaction(rawTx, privateKeyWif, toAddress, amount.toPlainString(), Constant.chainId, Constant.MAIN_PRECISION);
        log.info("signed tx {}", signedTx);

        String txid = nodeClient.broadcastTransaction(signedTx);
        log.info("broadcast response {}", txid);
    }

    @Test
    public void testInvokeContract() throws NodeException, SignTransactionException {
        String privateKeyWif = "5Joayzz5K4HTs8zXksJHkhCViAm6qqoAXrFVq2giFifAQQXNvFk"; // address DJx3BrwDo8Du8peX31YKAxFuEZ473XnSdx
        String fromPubKey = KeyUtils.getPublicKeyStringFromPrivateWif(privateKeyWif);
        String contractId = "caxe4Vdfg5tRrfZWkrVDU2NGWXm5k27CGM"; 
        String apiName = "transfer"; 
        String apiArg = "DFUb6r6grQhgY8gpfopPkLLwMNqwdU3gE3,10"; 
        long gasLimit = 10000; 
        RawTransaction rawTx = nodeClient.createContractInvokeTransaction(fromPubKey, contractId, apiName, apiArg, Constant.MAIN_ASSET_SYMBOL, gasLimit);
        log.info("create tx response {}", rawTx);

        Map<String, Object> signedTx = DfdTransaction.signTransaction(rawTx, privateKeyWif, null, null, Constant.chainId, Constant.MAIN_PRECISION);
        log.info("signed tx {}", signedTx);

        String txid = nodeClient.broadcastTransaction(signedTx);
        log.info("broadcast response {}", txid);
    }

    @Test
    public void testTransferContractToken() throws NodeException, SignTransactionException {
        String privateKeyWif = "5Joayzz5K4HTs8zXksJHkhCViAm6qqoAXrFVq2giFifAQQXNvFk"; // address DJx3BrwDo8Du8peX31YKAxFuEZ473XnSdx
        String fromPubKey = KeyUtils.getPublicKeyStringFromPrivateWif(privateKeyWif);
        String contractId = "caxe4Vdfg5tRrfZWkrVDU2NGWXm5k27CGM";
        String toAddress = "DFUb6r6grQhgY8gpfopPkLLwMNqwdU3gE3";
        BigInteger amountFull = BigInteger.valueOf(10L); 
        long gasLimit = 10000; 
        RawTransaction rawTx = nodeClient.createContractTokenTransferTransaction(fromPubKey, contractId, toAddress, amountFull, gasLimit);
        log.info("create tx response {}", rawTx);

        Map<String, Object> signedTx = DfdTransaction.signTransaction(rawTx, privateKeyWif, null, null, Constant.chainId, Constant.MAIN_PRECISION);
        log.info("signed tx {}", signedTx);

        String txid = nodeClient.broadcastTransaction(signedTx);
        log.info("broadcast response {}", txid);
    }

    @Test
    public void testInvokeContractOffline() throws NodeException {
        String contractId = "caxe4Vdfg5tRrfZWkrVDU2NGWXm5k27CGM"; 
        String callerAccount = "test0"; 
        String apiName = "balanceOf"; 
        String apiArg = "DFUb6r6grQhgY8gpfopPkLLwMNqwdU3gE3"; 
        String response = nodeClient.invokeContractOffline(contractId, callerAccount, apiName, apiArg);
        log.info("{} token balance {}", apiArg, response);
    }

    @Test
    public void testInvokeContractTesting() throws NodeException {
        String contractId = "caxe4Vdfg5tRrfZWkrVDU2NGWXm5k27CGM"; 
        String callerAccount = "test0"; 
        String apiName = "transfer"; 
        String apiArg = "DFUb6r6grQhgY8gpfopPkLLwMNqwdU3gE3,1000"; 
        Object response = nodeClient.invokeContractTesting(contractId, callerAccount, apiName, apiArg);
        log.info("invoke contract testing result {}", response);
    }

    @Test
    public void testQueryContractTokenBalance() throws NodeException {
        String contractId = "caxe4Vdfg5tRrfZWkrVDU2NGWXm5k27CGM"; 
        String callerAccount = "test0"; 
        String address = "DFUb6r6grQhgY8gpfopPkLLwMNqwdU3gE3"; 
        String balanceFull = nodeClient.queryContractTokenBalance(contractId, callerAccount, address);
        
        log.info("{} token balance {}", address, balanceFull);
    }

    @Test
    public void testQueryTokenContractPrecision() throws NodeException {
        String contractId = "caxe4Vdfg5tRrfZWkrVDU2NGWXm5k27CGM"; 
        String callerAccount = "test0"; 
        String precision = nodeClient.queryTokenContractPrecision(contractId, callerAccount);
        log.info("token contract {} precision {}", contractId, precision);
    }

    @Test
    public void testQueryTokenContractSupply() throws NodeException {
        String contractId = "caxe4Vdfg5tRrfZWkrVDU2NGWXm5k27CGM"; 
        String callerAccount = "test0"; 
        String supply = nodeClient.queryTokenContractTotalSupply(contractId, callerAccount);
        log.info("token contract {} supply {}", contractId, supply);
    }

    @Test
    public void testQueryTokenContractTokenSymbol() throws NodeException {
        String contractId = "caxe4Vdfg5tRrfZWkrVDU2NGWXm5k27CGM"; 
        String callerAccount = "test0"; 
        String tokenSymbol = nodeClient.queryTokenContractTokenSymbol(contractId, callerAccount);
        log.info("token contract {} symbol {}", contractId, tokenSymbol);
    }

    @Test
    public void testGetBlock() throws NodeException {
        long blockNumber = 6587;
        BlockVo block = nodeClient.getBlock(blockNumber);
        log.info("block #{} is {}", blockNumber, block);
    }

    @Test
    public void testGetAssetInfo() throws NodeException {
        String symbol = Constant.MAIN_ASSET_SYMBOL;
        BlockchainAssetInfoVo assetInfo = nodeClient.getAssetInfo(symbol);
        log.info("asset {} info {}", symbol, assetInfo);
    }

    @Test
    public void testGetTransaction() throws NodeException {
        String txid = "57291ffc42d7756e14690c9bd66601541a6a244e";
        TransactionVo tx = nodeClient.getTransaction(txid);
        log.info("tx {} is {}", txid, tx);
    }

    @Test
    public void testGetResultTrxId() throws NodeException {
        String txid = "6183fbbad8dd2eef08ee5131fd4fb490c0fa3001";
        String resultTrxId = nodeClient.getResultTrxId(txid);
        log.info("result txid {}", resultTrxId);
    }

    @Test
    public void testGetOriginalTrxId() throws NodeException {
        String txid = "bb21faa792fc363dbd773f5c6e670e7705376672";
        String originalTrxId = nodeClient.getOriginalTrxId(txid);
        log.info("original txid {}", originalTrxId);
    }

    @Test
    public void testGetContractTransactionReceipt() throws NodeException {
        String resultTrxId = "bb21faa792fc363dbd773f5c6e670e7705376672";
        ContractTransactionReceiptVo receipt = nodeClient.getContractTransactionReceipt(resultTrxId);
        log.info("receipt {}", receipt);
    }

    @Test
    public void testGetContractInvokeTransaction() throws NodeException {
        String txid = "6183fbbad8dd2eef08ee5131fd4fb490c0fa3001";
        TransactionVo tx = nodeClient.getTransaction(txid);
        log.info("contract tx {} is {}", txid, tx);
    }

    @Test
    public void testGetAddressBalances() throws NodeException {
        String address = "DFUb6r6grQhgY8gpfopPkLLwMNqwdU3gE3";
        AddressBalancesVo balances = nodeClient.getAddressBalances(address);
        log.info("address {} balances is {}", address, balances);
    }

    @Test
    public void testGetLatestBlockNumber() throws NodeException {
        long blockNumber = nodeClient.getHeadBlockNumber();
        log.info("head block number is {}", blockNumber);
    }

    @Test
    public void testGetAddressTransactions() throws NodeException {
        String address = "DFUb6r6grQhgY8gpfopPkLLwMNqwdU3gE3";
        Object transactions = nodeClient.listAddressTransactions(address);
        log.info("address {} transactions are {}", address, transactions);
    }
}
