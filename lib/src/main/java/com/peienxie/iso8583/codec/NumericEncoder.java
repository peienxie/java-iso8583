package com.peienxie.iso8583.codec;

public class NumericEncoder implements FieldEncoder<Integer> {

    private final int formatLength;

    public NumericEncoder(int formatLength) {
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
}
