package com.peienxie.iso8583.codec.encoders;

import com.peienxie.iso8583.ISO8583Type;

public class NumericEncoder implements ISO8583Encoder<Integer> {

    private final int formatLength;

    private NumericEncoder(int formatLength) {
        this.formatLength = formatLength;
    }

    public static NumericEncoder withLength(int formatLength) {
        return new NumericEncoder(formatLength);
    }

    @Override
    public byte[] encode(Integer data) {
        return ISO8583Type.NUMERIC
                .format(data.toString(), formatLength + formatLength % 2)
                .getBytes();
    }
}
