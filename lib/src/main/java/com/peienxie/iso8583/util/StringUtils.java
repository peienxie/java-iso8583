package com.peienxie.iso8583.util;

public class StringUtils {

    /* @param hexstring must be an even-length string. */
    public static byte[] hexStrToBytes(String hexstring) {
        int len = hexstring.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            int a = Character.digit(hexstring.charAt(i), 16);
            int b = Character.digit(hexstring.charAt(i + 1), 16);
            bytes[i / 2] = (byte) ((a << 4) | b);
        }
        return bytes;
    }

    private static final byte[] UPPERCASE_DIGITS = {
        '0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
    };

    private static final byte[] LOWERCASE_DIGITS = {
        '0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', 'a', 'b', 'c', 'd', 'e', 'f',
    };

    public static String bytesToHexStr(byte[] bytes, int from, int to, boolean uppercase) {
        if (to - from <= 0) return "";

        int len = to - from;
        byte[] tmp = new byte[len * 2];
        byte[] digits = uppercase ? UPPERCASE_DIGITS : LOWERCASE_DIGITS;
        for (int i = 0; i < len; i++) {
            int v = bytes[i] & 0xff;
            tmp[2 * i] = digits[v >>> 4];
            tmp[2 * i + 1] = digits[v & 0x0f];
        }
        return new String(tmp);
    }

    public static String bytesToHexStr(byte[] bytes) {
        return bytesToHexStr(bytes, 0, bytes.length, true);
    }

    public static String bytesToHexStr(byte[] bytes, int from, int to) {
        return bytesToHexStr(bytes, from, to, true);
    }

    public static String bytesToHexStr(byte[] bytes, boolean uppercase) {
        return bytesToHexStr(bytes, 0, bytes.length, uppercase);
    }
}
