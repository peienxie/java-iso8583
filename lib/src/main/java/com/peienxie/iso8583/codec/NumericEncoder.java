package com.peienxie.iso8583.codec;

import java.util.StringJoiner;

public class NumericEncoder implements FieldEncoder<Integer> {

    private final int encodeLength;

    public NumericEncoder(int encodeLength) {
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
    public byte[] encode(Integer data) {
        return intToBcd(data, getEncodeLength());
    }

    private byte[] intToBcd(int value, int width) {
        byte[] bcd = new byte[(width + 1) / 2];
        for (int i = bcd.length - 1; i >= 0 && value > 0; i--) {
            int digit1 = value % 10;
            value /= 10;
            int digit2 = value % 10;
            value /= 10;
            bcd[i] = (byte) ((digit2 << 4) | digit1);
        }
        return bcd;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", NumericEncoder.class.getSimpleName() + "[", "]")
                .add("encodeLength=" + encodeLength)
                .toString();
    }
}
