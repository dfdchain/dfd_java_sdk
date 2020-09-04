package dfd;


import com.google.common.primitives.Bytes;
import dfd.operations.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created on 12/03/20.
 */
public class Operation extends ByteSerializable {

    public static final String TYPE = "type";
    public static final String DATA = "data";

    public Byte type;
    public List<Byte> data;


    public Operation(Byte type) {
        this.type = type;
        this.data = new LinkedList();
    }

    public Operation() {
        this.type = (byte) 0;
        this.data = new LinkedList();
        this.data.add(new Byte((byte) 0));
    }

    public <T extends OperationBase> Operation(T obj) {
        this.fromData(obj);
    }


    public <T extends OperationBase> void fromData(T obj) {
        byte[] resultdata = ByteHandle.pack(obj, obj.getClass());

        this.data = Bytes.asList(resultdata);
        this.type = obj.type;
    }

    public <T extends OperationBase> T toData() throws InstantiationException, IllegalAccessException, NoSuchFieldException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException, IOException {
        InputStream stream = new ByteArrayInputStream(Bytes.toArray(this.data));
        Object output = new Object();
        if (this.type == OperationType.withdraw_op_type.getValue()) {
            output = (T) ByteHandle.unpack(stream, WithdrawOperation.class);
        } else if (this.type == OperationType.deposit_op_type.getValue()) {
            output = (T) ByteHandle.unpack(stream, DepositOperation.class);
        } else if (this.type == OperationType.register_account_op_type.getValue()) {
            output = (T) ByteHandle.unpack(stream, RegisterAccountOperation.class);
        } else if (this.type == OperationType.update_account_op_type.getValue()) {
            output = (T) ByteHandle.unpack(stream, UpdateAccountOperation.class);
        } else if (this.type == OperationType.withdraw_pay_op_type.getValue()) {
            output = (T) ByteHandle.unpack(stream, WithdrawPayOperation.class);
        } else if (this.type == OperationType.define_slate_op_type.getValue()) {
            output = (T) ByteHandle.unpack(stream, DefineSlateOperation.class);
        } else if (this.type == OperationType.imessage_memo_op_type.getValue()) {
            output = (T) ByteHandle.unpack(stream, ImessageMemoOperation.class);
        } else if (this.type == OperationType.contract_register_op_type.getValue()) {
            output = (T) ByteHandle.unpack(stream, RegisterContractOperation.class);
        } else if (this.type == OperationType.contract_upgrade_op_type.getValue()) {
            output = (T) ByteHandle.unpack(stream, UpgradeContractOperation.class);
        } else if (this.type == OperationType.contract_destroy_op_type.getValue()) {
            output = (T) ByteHandle.unpack(stream, DestroyContractOperation.class);
        } else if (this.type == OperationType.contract_call_op_type.getValue()) {
            output = (T) ByteHandle.unpack(stream, CallContractOperation.class);
        } else if (this.type == OperationType.transfer_contract_op_type.getValue()) {
            output = (T) ByteHandle.unpack(stream, TransferContractOperation.class);
        }
        return (T) output;

    }

    @Override
    public Field[] getOrderedFieldName() {
        List<Field> fields;
        fields = new LinkedList<Field>();
        try {
            fields.add(this.getClass().getDeclaredField(TYPE));
            fields.add(this.getClass().getDeclaredField(DATA));


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


}
