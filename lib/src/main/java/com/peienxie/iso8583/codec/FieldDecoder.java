package com.peienxie.iso8583.codec;

/**
 * An interface for decoding ISO8583 fields from byte arrays into objects of type T.
 *
 * @param <T> The type of object that the decoder returns.
 */
public interface FieldDecoder<T> {

    int getDecodeLength();

    /**
     * Decodes an ISO8583 field from a byte array and returns an object of type T.
     *
     * @param bytes The byte array containing the ISO8583 field.
     * @return An object of type T representing the decoded field.
     */
    T decode(byte[] bytes);
}
