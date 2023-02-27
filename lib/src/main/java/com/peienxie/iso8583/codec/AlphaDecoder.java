package com.peienxie.iso8583.codec;

import java.nio.charset.StandardCharsets;

public class AlphaDecoder implements FieldDecoder<String> {

    private final int formatLength;

    public AlphaDecoder(int formatLength) {
        if (formatLength <= 0) {
            throw new IllegalArgumentException("format length must be greater than 0");
        }
        this.formatLength = formatLength;
    }

    @Override
    public int getDecodeLength() {
        return formatLength;
    }

    @Override
    public String decode(byte[] bytes) {
        int length = Math.min(bytes.length, getDecodeLength());
        return new String(bytes, 0, length, StandardCharsets.ISO_8859_1);
    }
}
