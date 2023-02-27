package com.peienxie.iso8583.codec;

import com.peienxie.iso8583.util.StringUtils;

public class NumericDecoder implements FieldDecoder<Integer> {

    private final int formatLength;

    public NumericDecoder(int formatLength) {
        if (formatLength <= 0) {
            throw new IllegalArgumentException("Illegal format length");
        }
        this.formatLength = formatLength + formatLength % 2;
    }

    @Override
    public int getDecodeLength() {
        return formatLength;
    }

    @Override
    public Integer decode(byte[] bytes) {
        int length = Math.min(bytes.length, getDecodeLength());
        String str = new String(StringUtils.bytesToHexBytes(bytes, 0, length));
        return Integer.parseInt(str);
    }
}
