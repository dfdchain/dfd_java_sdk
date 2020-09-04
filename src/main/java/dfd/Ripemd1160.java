package dfd;

import com.google.common.primitives.UnsignedInteger;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class Ripemd1160 extends ByteSerializable {

    public static final String HASH = "hash";
    UnsignedInteger[] hash;
    public static final Integer hashSize = 5;


    public Ripemd1160() {
        hash = new UnsignedInteger[hashSize];
        for (int i = 0; i < hashSize; ++i) {
            hash[i] = UnsignedInteger.fromIntBits(0);
        }
        sethashArray(hash);
    }

//	public Ripemd1160(Ripemd1160 ripe){
//		hash = new UnsignedInteger[hashSize];
//		System.arraycopy(ripe.hash, 0, this.hash, 0, hashSize);
//	}


    public void sethashArray(Object[] array) {
        for (int i = 0; i < hashSize; ++i) {
            this.hash[i] = (UnsignedInteger) array[i];
        }
    }

    @Override
    public Field[] getOrderedFieldName() {
        List<Field> fields;
        fields = new LinkedList<Field>();
        try {
            fields.add(this.getClass().getDeclaredField(HASH));

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
