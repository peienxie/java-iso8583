package com.peienxie.iso8583.codec.decoders;

import com.peienxie.iso8583.util.StringUtils;

import java.util.Arrays;

public class NumericDecoder implements ISO8583Decoder<Integer> {

    private final int formatLength;

    private NumericDecoder(int formatLength) {
        this.formatLength = formatLength;
    }

    public static NumericDecoder withLength(int formatLength) {
        return new NumericDecoder(formatLength);
    }

    @Override
    public Integer decode(byte[] bytes) {
        String str =
                StringUtils.bytesToHexStr(
                        Arrays.copyOfRange(bytes, 0, formatLength + formatLength % 2));
        return Integer.parseInt(str);
    }
}
