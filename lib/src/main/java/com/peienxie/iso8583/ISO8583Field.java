package com.peienxie.iso8583;

import java.nio.ByteBuffer;
import java.util.StringJoiner;

import com.peienxie.iso8583.codec.FieldDecoder;
import com.peienxie.iso8583.codec.FieldEncoder;

/**
 * This class represents an ISO8583 field with data, encoder and decoder.
 *
 * @param <T> the type of data to be encoded and decoded
 */
public class ISO8583Field<T> {

    private final FieldEncoder<T> encoder;
    private final FieldDecoder<T> decoder;

    private T data;

    /**
     * Constructs a new ISO8583Field object with the specified data, encoder, and decoder.
     *
     * @param data    the data to be encoded and decoded
     * @param encoder the encoder to be used for encoding the data
     * @param decoder the decoder to be used for decoding the data
     */
    private ISO8583Field(T data, FieldEncoder<T> encoder, FieldDecoder<T> decoder) {
        this.data = data;
        this.encoder = encoder;
        this.decoder = decoder;
    }

    /**
     * Creates a new ISO8583Field object for a request message with the specified data and encoder.
     *
     * @param data    the data to be encoded
     * @param encoder the encoder to be used for encoding the data
     * @return a new ISO8583Field object for the request message
     */
    public static <T> ISO8583Field<T> request(T data, FieldEncoder<T> encoder) {
        return new ISO8583Field<>(data, encoder, null);
    }

    /**
     * Creates a new ISO8583Field object for a response message with the specified decoder.
     *
     * @param decoder the decoder to be used for decoding the response data
     * @return a new ISO8583Field object for the response message
     */
    public static <T> ISO8583Field<T> response(FieldDecoder<T> decoder) {
        return new ISO8583Field<>(null, null, decoder);
    }

    public boolean hasEncoder() {
        return encoder != null;
    }

    public boolean hasDecoder() {
        return decoder != null;
    }

    public FieldDecoder<T> getDecoder() {
        return decoder;
    }

    public T getData() {
        return data;
    }

    /**
     * Writes the encoded form of this ISO8583 field to the specified byte buffer.
     *
     * @throws IllegalStateException if the encoder is null
     */
    public byte[] encode() {
        if (!hasEncoder()) {
            throw new IllegalStateException("Encoder is null");
        }

        return encoder.encode(data);
    }

    /**
     * Reads the encoded form of this object from the specified byte buffer and
     * updates the state of this object.
     * <p>
     * The position of the byte buffer is updated to reflect the number of bytes read.
     *
     * @throws IllegalStateException if the decoder is null
     */
    public void decode(ByteBuffer buffer) {
        if (!hasDecoder()) {
            throw new IllegalStateException("Decoder is null");
        }

        byte[] bytes = new byte[decoder.getDecodeLength()];
        buffer.get(bytes);
        data = decoder.decode(bytes);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ISO8583Field.class.getSimpleName() + "[", "]")
                .add("encoder=" + encoder)
                .add("decoder=" + decoder)
                .add("data=" + data)
                .toString();
    }
}
