package dfd;

import java.lang.reflect.Field;

/**
 * Interface implemented by all entities for which makes sense to have a
 * byte-array representation.
 */
public abstract class ByteSerializable {

    //public abstract byte[] toBytes();
    //public abstract void readBytes(InputStream input);

    public abstract Field[] getOrderedFieldName();
}
