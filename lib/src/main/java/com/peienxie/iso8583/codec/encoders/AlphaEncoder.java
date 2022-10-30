package com.peienxie.iso8583.codec.encoders;

import com.peienxie.iso8583.ISO8583Type;

public class AlphaEncoder implements ISO8583Encoder<String> {

    private final int formatLength;

    private AlphaEncoder(int formatLength) {
        this.formatLength = formatLength;
    }

    public static AlphaEncoder withLength(int formatLength) {
        return new AlphaEncoder(formatLength);
    }

    @Override
    public byte[] encode(String data) {
        return ISO8583Type.ALPHA.format(data, formatLength).getBytes();
    }
}
