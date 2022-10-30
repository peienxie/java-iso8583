package com.peienxie.iso8583.codec.encoders;

public interface ISO8583Encoder<T> {
    /** encode data into bytes, this is used for create a serializable bytes for given type T */
    public byte[] encode(T data);
}
