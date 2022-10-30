package com.peienxie.iso8583;

import com.peienxie.iso8583.codec.decoders.AlphaDecoder;
import com.peienxie.iso8583.codec.decoders.ISO8583Decoder;
import com.peienxie.iso8583.codec.decoders.NumericDecoder;
import com.peienxie.iso8583.codec.encoders.AlphaEncoder;
import com.peienxie.iso8583.codec.encoders.ISO8583Encoder;
import com.peienxie.iso8583.codec.encoders.NumericEncoder;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class ISO8583Field<T> {
    private final ISO8583Type type;
    private final T data;
    private final ISO8583Encoder<T> encoder;
    private final ISO8583Decoder<T> decoder;

    private ISO8583Field(
            ISO8583Type type, T data, ISO8583Encoder<T> encoder, ISO8583Decoder<T> decoder) {
        this.type = type;
        this.data = data;
        this.encoder = encoder;
        this.decoder = decoder;
    }

    public static ISO8583Field<String> ofText(String data, int formatLength) {
        return new ISO8583Field<>(
                ISO8583Type.ALPHA,
                data,
                AlphaEncoder.withLength(formatLength),
                AlphaDecoder.withLength(formatLength));
    }

    public static ISO8583Field<Integer> ofInteger(Integer data, int formatLength) {
        return new ISO8583Field<Integer>(
                ISO8583Type.NUMERIC,
                data,
                NumericEncoder.withLength(formatLength),
                NumericDecoder.withLength(formatLength));
    }

    public static <T> ISO8583Field<T> of(ISO8583Type type, T data, ISO8583Encoder<T> encoder) {
        return new ISO8583Field<T>(type, data, encoder, null);
    }

    public static <T> ISO8583Field<T> of(ISO8583Type type, T data, ISO8583Decoder<T> decoder) {
        return new ISO8583Field<T>(type, data, null, decoder);
    }

    public static <T> ISO8583Field<T> of(
            ISO8583Type type, T data, ISO8583Encoder<T> encoder, ISO8583Decoder<T> decoder) {
        return new ISO8583Field<T>(type, data, encoder, decoder);
    }

    public void writeTo(OutputStream out) throws IOException {
        out.write(getBytes());
    }

    /** converts this ISO8583Field into byte array */
    public byte[] getBytes() {
        if (encoder == null) {
            throw new IllegalStateException("can't encode this field without a encoder");
        }

        byte[] bytes = encoder.encode(data);
        if (type.isVariableLength()) {
            // write the variable length of fields first
            byte[] varLen = type.getVariableLengthBytes(bytes.length);
            return ByteBuffer.allocate(varLen.length + bytes.length).put(varLen).put(bytes).array();
        }
        return bytes;
    }

    @Override
    public String toString() {
        return "ISO8583Field{type="
                + type
                + ", data="
                + data
                + ", encoder="
                + encoder
                + ", decoder="
                + decoder
                + "}";
    }
}
