package com.peienxie.iso8583.codec.decoders;

import java.util.Arrays;

public class AlphaDecoder implements ISO8583Decoder<String> {

    private final int formatLength;

    private AlphaDecoder(int formatLength) {
        this.formatLength = formatLength;
    }

    public static AlphaDecoder withLength(int formatLength) {
        return new AlphaDecoder(formatLength);
    }

    @Override
    public String decode(byte[] bytes) {
        return new String(Arrays.copyOfRange(bytes, 0, formatLength));
    }
}
