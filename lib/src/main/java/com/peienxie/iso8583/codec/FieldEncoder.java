package com.peienxie.iso8583.codec;

import java.nio.ByteBuffer;

/**
 * An interface for encoding objects of type T into ISO8583 field as byte arrays.
 *
 * @param <T> The type of object that the encoder accepts.
 */
public interface FieldEncoder<T> {

    int getEncodeLength();

    /**
     * Encodes an object of type T into an ISO8583 field as a byte array.
     *
     * @param data The object to be encoded as an ISO8583 field.
     */
    byte[] encode(T data);
}
