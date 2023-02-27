package com.peienxie.iso8583.codec;

import java.util.StringJoiner;

import com.peienxie.iso8583.util.StringUtils;

public class NumericDecoder implements FieldDecoder<Integer> {

    private final int decodeLength;

    public NumericDecoder(int decodeLength) {
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
    public Integer decode(byte[] bytes) {
        int length = Math.min(bytes.length, getDecodeLength());
        String str = new String(StringUtils.bytesToHexBytes(bytes, 0, length));
        return Integer.parseInt(str);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", NumericDecoder.class.getSimpleName() + "[", "]")
                .add("decodeLength=" + decodeLength)
                .toString();
    }
}
