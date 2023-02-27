package com.peienxie.iso8583.codec;

import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;

public class AlphaDecoder implements FieldDecoder<String> {

    private final int decodeLength;

    public AlphaDecoder(int decodeLength) {
        if (decodeLength <= 0) {
            throw new IllegalArgumentException("Illegal length value");
        }
        this.decodeLength = decodeLength;
    }

    @Override
    public int getDecodeLength() {
        return decodeLength;
    }

    @Override
    public String decode(byte[] bytes) {
        int length = Math.min(bytes.length, getDecodeLength());
        return new String(bytes, 0, length, StandardCharsets.ISO_8859_1);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AlphaDecoder.class.getSimpleName() + "[", "]")
                .add("decodeLength=" + decodeLength)
                .toString();
    }
}
