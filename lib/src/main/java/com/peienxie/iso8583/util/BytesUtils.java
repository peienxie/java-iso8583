package com.peienxie.iso8583.util;

public class BytesUtils {

    private BytesUtils() {
    }

    public static byte[] intToBcd(int value) {
        int digits = (int) Math.ceil(Math.log10(value + 1.0));
        return intToBcd(value, (digits + 1) / 2);
    }

    public static byte[] intToBcd(int value, int width) {
        byte[] bcd = new byte[width];
        for (int i = bcd.length - 1; i >= 0 && value > 0; i--) {
            int digit1 = value % 10;
            value /= 10;
            int digit2 = value % 10;
            value /= 10;
            bcd[i] = (byte) ((digit2 << 4) | digit1);
        }
        return bcd;
    }

    public static int bcdToInt(byte[] bcd) {
        int value = 0;
        for (byte b : bcd) {
            value *= 100;
            value += 10 * (b >> 4);
            value += (b & 0x0F);
        }
        return value;
    }
}
