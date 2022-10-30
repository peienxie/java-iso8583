package com.peienxie.iso8583.codec.decoders;

public interface ISO8583Decoder<T> {
    /** decodes bytes into data, this is used for crate a type T from given a byte array */
    public T decode(byte[] bytes);
}
