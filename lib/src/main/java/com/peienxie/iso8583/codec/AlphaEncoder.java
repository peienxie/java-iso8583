package com.peienxie.iso8583.codec;

import static java.util.Arrays.copyOfRange;
import static java.util.Arrays.fill;

import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;

public class AlphaEncoder implements FieldEncoder<String> {

    private final int encodeLength;

    public AlphaEncoder(int encodeLength) {
        if (encodeLength <= 0) {
            throw new IllegalArgumentException("Illegal length value");
        }
        this.encodeLength = encodeLength;
    }

    @Override
    public int getEncodeLength() {
        return encodeLength;
    }

    @Override
    public byte[] encode(String data) {
        byte[] dataBytes = data.getBytes(StandardCharsets.ISO_8859_1);
        if (dataBytes.length < getEncodeLength()) {
            // right padded spaces
            byte[] bytes = new byte[getEncodeLength()];
            fill(bytes, (byte) ' ');
            System.arraycopy(dataBytes, 0, bytes, 0, dataBytes.length);
            return bytes;
        } else {
            return copyOfRange(dataBytes, 0, getEncodeLength());
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AlphaEncoder.class.getSimpleName() + "[", "]")
                .add("encodeLength=" + encodeLength)
                .toString();
    }
}
