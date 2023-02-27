package com.peienxie.iso8583.codec;

import java.util.StringJoiner;

import com.peienxie.iso8583.util.BytesUtils;

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
        return BytesUtils.intToBcd(data, getEncodeLength());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", NumericEncoder.class.getSimpleName() + "[", "]")
                .add("encodeLength=" + encodeLength)
                .toString();
    }
}
