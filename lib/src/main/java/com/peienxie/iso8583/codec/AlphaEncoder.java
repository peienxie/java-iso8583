package com.peienxie.iso8583.codec;

import static java.util.Arrays.copyOfRange;
import static java.util.Arrays.fill;

import java.nio.charset.StandardCharsets;

public class AlphaEncoder implements FieldEncoder<String> {

    private final int formatLength;

    public AlphaEncoder(int formatLength) {
        if (formatLength <= 0) {
            throw new IllegalArgumentException("Illegal format length");
        }
        this.formatLength = formatLength;
    }

    @Override
    public int getEncodeLength() {
        return formatLength;
    }

    @Override
    public byte[] encode(String data) {
        byte[] dataBytes = data.getBytes(StandardCharsets.ISO_8859_1);
        if (dataBytes.length < getEncodeLength()) {
            // right padded spaces
            byte[] bytes = new byte[getEncodeLength()];
            fill(bytes, (byte) ' ');
            System.arraycopy(dataBytes, 0, bytes, 0, dataBytes.length);
            return bytes;
        } else {
            return copyOfRange(dataBytes, 0, getEncodeLength());
        }
    }
}
