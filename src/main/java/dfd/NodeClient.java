package dfd;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dfd.errors.NodeException;
import dfd.vo.*;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestInstance;
import org.apache.http.HttpStatus;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;

public class NodeClient {
    private Gson gson = new Gson();
    private final String endpoint;
    private final String username;
    private final String password;

    public NodeClient(String endpoint, String username, String password) {
        this.endpoint = endpoint;
        this.username = username;
        this.password = password;
    }

    private Object callRpc(String method, List<Object> params) throws NodeException {
        try (UnirestInstance unirestInstance = Unirest.spawnInstance()) {
            Map<String, Object> reqMap = new HashMap<>();
            reqMap.put("id", 1);
            reqMap.put("jsonrpc", "2.0");
            reqMap.put("method", method);
            reqMap.put("params", params);
            String reqStr = gson.toJson(reqMap);
            HttpResponse<String> response = unirestInstance.post(endpoint)
                    .body(reqStr)
                    .basicAuth(username, password)
                    .header("Content-Type", "application/json")
                    .asString();
            if (!response.isSuccess() || HttpStatus.SC_OK != response.getStatus()) {
                throw new NodeException("node rpc " + method + " error " + response.getBody());
            }
            String responseStr = response.getBody();
            JsonRpcResponse responseObj = gson.fromJson(responseStr, JsonRpcResponse.class);
            if (responseObj.getError() != null) {
                throw new NodeException("node rpc " + method + " error " + responseObj.getError().toString());
            }
            return responseObj.getResult();
        }
    }

    public RawTransaction createTransferTransaction(String fromPubKey, String toAddress, BigDecimal amount, String memo) throws NodeException {
        Object response = callRpc("wallet_transfer_to_address_build",
                Arrays.asList((Object) amount.stripTrailingZeros().toPlainString(),
                        Constant.MAIN_ASSET_SYMBOL, fromPubKey, toAddress, (memo == null || memo.isEmpty()) ? "" : memo));
        JsonObject jsonObject = gson.toJsonTree(response).getAsJsonObject();
        return new RawTransaction(jsonObject);
    }

    public RawTransaction createContractInvokeTransaction(String fromPubKey, String contractId, String apiName, String apiArg, String assetSymbol, long gasLimit) throws NodeException {
        Object response = callRpc("wallet_call_contract_build",
                Arrays.asList((Object) contractId,
                        fromPubKey, apiName, (apiArg == null || apiArg.isEmpty()) ? "" : apiArg, assetSymbol, gasLimit));
        JsonObject jsonObject = gson.toJsonTree(response).getAsJsonObject();
        return new RawTransaction(jsonObject);
    }

    public RawTransaction createContractTokenTransferTransaction(String fromPubKey, String contractId, String toAddress, BigInteger amountFull, long gasLimit) throws NodeException {
        String apiName = "transfer";
        String apiArg = toAddress + "," + amountFull.toString();
        return createContractInvokeTransaction(fromPubKey, contractId, apiName, apiArg, Constant.MAIN_ASSET_SYMBOL, gasLimit);
    }

    public String broadcastTransaction(Map<String, Object> signedTx) throws NodeException {
        Object response = callRpc("broadcast_building_transaction",
                Collections.singletonList((Object) signedTx));
        return (String) response;
    }

    public BlockVo getBlock(long blockHeight) throws NodeException {
        Object response = callRpc("get_block", Collections.singletonList((Object) blockHeight));
        if (response == null) {
            return null;
        }
        JsonObject blockJson = gson.toJsonTree(response).getAsJsonObject();
        return gson.fromJson(blockJson, BlockVo.class);
    }

    public BlockchainAssetInfoVo getAssetInfo(String symbol) throws NodeException {
        Object response = callRpc("blockchain_get_asset", Collections.singletonList((Object) symbol));
        if(response == null) {
            return null;
        }
        JsonObject assetInfoJson = gson.toJsonTree(response).getAsJsonObject();
        return gson.fromJson(assetInfoJson, BlockchainAssetInfoVo.class);
    }

    public AddressBalanceItemVo getBalanceObjectByBalanceId(String balanceId) throws NodeException {
        Object response = callRpc("blockchain_get_balance", Collections.singletonList((Object) balanceId));
        if (response == null) {
            return null;
        }
        AddressBalanceItemVo addressBalanceItemVo = gson.fromJson(gson.toJsonTree(response).getAsJsonObject(), AddressBalanceItemVo.class);
        return addressBalanceItemVo;
    }

    public ContractTransactionReceiptVo getContractTransactionReceipt(String resultTrxId) throws NodeException {
        Object response = callRpc("blockchain_get_pretty_contract_transaction", Collections.singletonList((Object) resultTrxId));
        if (response == null) {
            return null;
        }
        ContractTransactionReceiptVo result = gson.fromJson(gson.toJsonTree(response).getAsJsonObject(), ContractTransactionReceiptVo.class);
        return result;
    }

    public TransactionVo getTransaction(String txid) throws NodeException {
        Object response = callRpc("blockchain_get_transaction", Collections.singletonList((Object) txid));
        if (response == null) {
            return null;
        }
        JsonArray jsonArray = gson.toJsonTree(response).getAsJsonArray();
        JsonObject txJson = jsonArray.get(1).getAsJsonObject();
        TransactionVo tx = gson.fromJson(txJson, TransactionVo.class);
        
        Set<String> relatedAddresses = new HashSet<>(); 
        String fromAddress = null;
        String toAddress = null;
        String txMemo = null;
        BigDecimal depositAmount = BigDecimal.ZERO;
        BigDecimal withdrawAmount = BigDecimal.ZERO;
        int precision = Constant.MAIN_PRECISION;
        BigDecimal precision10 = new BigDecimal(10).pow(precision);
        for (OperationVo operation : tx.getTrx().getOperations()) {
            String opTypeStr = operation.getType();
            if (opTypeStr == null) {
                throw new NodeException("invalid operation " + operation);
            }
            switch (opTypeStr) {
                case OperationVo.DEPOSIT_OP_TYPE: {
                    toAddress = operation.getData().getAsJsonObject().get("condition").getAsJsonObject().get("data").getAsJsonObject().get("owner").getAsString();
                    relatedAddresses.add(toAddress);
                    BigDecimal fullDepositAmount = new BigDecimal(operation.getData().getAsJsonObject().get("amount").getAsString());
                    depositAmount = fullDepositAmount.setScale(precision, RoundingMode.FLOOR).divide(precision10, RoundingMode.FLOOR);
                }
                break;
                case OperationVo.WITHDRAW_OP_TYPE: {
                    String balanceId = operation.getData().getAsJsonObject().get("balance_id").getAsString();
                    BigDecimal fullWithdrawAmount = new BigDecimal(operation.getData().getAsJsonObject().get("amount").getAsString());
                    withdrawAmount = fullWithdrawAmount.setScale(precision, RoundingMode.FLOOR).divide(precision10, RoundingMode.FLOOR);
                    AddressBalanceItemVo addressBalanceItemVo = getBalanceObjectByBalanceId(balanceId);
                    if (addressBalanceItemVo == null || addressBalanceItemVo.getCondition() == null || addressBalanceItemVo.getCondition().getData() == null) {
                        continue;
                    }
                    fromAddress = addressBalanceItemVo.getCondition().getData().getOwner();
                    relatedAddresses.add(fromAddress);
                }
                break;
                case OperationVo.MEMO_OP_TYPE: {
                    txMemo = operation.getData().getAsJsonObject().get("imessage").getAsString();
                }
                break;
            }
        }
        tx.setRelatedAddresses(relatedAddresses);
        tx.setTxMemo(txMemo);
        tx.setFromAddress(fromAddress);
        tx.setToAddress(toAddress);
        BigDecimal txFee = BigDecimal.ZERO;
        if (withdrawAmount.compareTo(depositAmount) >= 0) {
            txFee = withdrawAmount.subtract(depositAmount);
        }
        tx.setTxAmount(depositAmount);
        tx.setTxFee(txFee);
        return tx;
    }

    public Object invokeContractTesting(String contractId, String callerAccountName, String apiName, String apiArg) throws NodeException {
        Object response = callRpc("contract_call_testing", Arrays.asList((Object) contractId, callerAccountName, apiName, apiArg));
        return response;
    }

    public String invokeContractOffline(String contractId, String callerAccountName, String apiName, String apiArg) throws NodeException {
        Object response = callRpc("contract_call_offline", Arrays.asList((Object) contractId, callerAccountName, apiName, apiArg));
        return response.toString();
    }

    public String queryContractTokenBalance(String contractId, String callerAccountName, String address) throws NodeException {
        String apiName = "balanceOf";
        String apiArg = address;
        return invokeContractOffline(contractId, callerAccountName, apiName, apiArg);
    }

    public String queryTokenContractPrecision(String contractId, String callerAccountName) throws NodeException {
        String apiName = "precision";
        String apiArg = "-";
        return invokeContractOffline(contractId, callerAccountName, apiName, apiArg);
    }

    public String queryTokenContractTotalSupply(String contractId, String callerAccountName) throws NodeException {
        String apiName = "totalSupply";
        String apiArg = "-";
        return invokeContractOffline(contractId, callerAccountName, apiName, apiArg);
    }

    public String queryTokenContractTokenSymbol(String contractId, String callerAccountName) throws NodeException {
        String apiName = "tokenSymbol";
        String apiArg = "-";
        return invokeContractOffline(contractId, callerAccountName, apiName, apiArg);
    }

    public String getWalletAccountAddress(String walletAccountName) throws NodeException {
        Object response = callRpc("wallet_get_account_public_address", Collections.singletonList((Object) walletAccountName));
        return response.toString();
    }

    public AddressBalancesVo getAddressBalances(String address) throws NodeException {
        Object response = callRpc("blockchain_list_address_balances", Collections.singletonList((Object) address));
        JsonArray jsonArray = gson.toJsonTree(response).getAsJsonArray();
        Map<Integer, AddressBalancesVo.BalanceItemVo> balancesMap = new HashMap<>(); // assetId => balance
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonArray arrayItem = jsonArray.get(i).getAsJsonArray();
            String balanceId = arrayItem.get(0).getAsString();
            AddressBalanceItemVo addressBalanceItemVo = gson.fromJson(arrayItem.get(1).getAsJsonObject(), AddressBalanceItemVo.class);
            Integer assetId = addressBalanceItemVo.getCondition().getAsset_id();
            if (assetId == null || 0 != assetId) {
                throw new NodeException("not supported asset id in address balance");
            }
            int precision = Constant.MAIN_PRECISION; 
            BigDecimal precision10 = new BigDecimal(10).pow(precision);
            BigDecimal balance = new BigDecimal(addressBalanceItemVo.getBalance()).setScale(precision, RoundingMode.FLOOR).divide(precision10, RoundingMode.FLOOR);
            if (!balancesMap.containsKey(assetId)) {
                AddressBalancesVo.BalanceItemVo item = new AddressBalancesVo.BalanceItemVo();
                item.setAssetId(assetId);
                item.setBalance(balance);
                balancesMap.put(assetId, item);
            } else {
                AddressBalancesVo.BalanceItemVo item = balancesMap.get(assetId);
                item.setBalance(item.getBalance().add(balance));
            }
        }
        AddressBalancesVo result = new AddressBalancesVo();
        result.setBalances(new ArrayList<>(balancesMap.values()));
        return result;
    }

    public long getHeadBlockNumber() throws NodeException {
        Object response = callRpc("blockchain_get_block_count", new ArrayList<>());
        long blockNumber = gson.toJsonTree(response).getAsLong();
        return blockNumber;
    }

    public String getOriginalTrxId(String resultTrxId) throws NodeException {
        Object response = callRpc("get_request_trx_id", Collections.singletonList((Object) resultTrxId));
        return gson.toJsonTree(response).getAsString();
    }

    public String getResultTrxId(String originalTrxId) throws NodeException {
        Object response = callRpc("get_result_trx_id", Collections.singletonList((Object) originalTrxId));
        return gson.toJsonTree(response).getAsString();
    }

    @Deprecated
    public Object listAddressTransactions(String address) throws NodeException {
        Object response = callRpc("blockchain_list_address_transactions", Collections.singletonList((Object) address));
        return response;
    }

}
