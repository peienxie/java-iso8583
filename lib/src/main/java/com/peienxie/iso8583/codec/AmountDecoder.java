package com.peienxie.iso8583.codec;

import com.peienxie.iso8583.util.StringUtils;

public class AmountDecoder implements FieldDecoder<Number> {
    @Override
    public int getDecodeLength() {
        return 6;
    }

    @Override
    public Number decode(byte[] bytes) {
        long value = Long.parseLong(StringUtils.bytesToHexStr(bytes, 0, getDecodeLength()));
        return ((double) value) / 100;
    }
}
