package com.peienxie.iso8583.util;

public class StringUtils {

    /**
     * converts given hexadecimal string into byte array.
     *
     * @param hexstring must be an even-length string.
     */
    public static byte[] hexStrToBytes(String hexstring) {
        if ((hexstring.length() & 1) != 0)
            throw new IllegalArgumentException(
                    "input hexstring length not even: " + hexstring.length());

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

    public static byte[] bytesToHexBytes(byte[] bytes, int from, int to, boolean uppercase) {
        if (to - from <= 0) return new byte[0];

        int len = to - from;
        byte[] out = new byte[len * 2];
        byte[] digits = uppercase ? UPPERCASE_DIGITS : LOWERCASE_DIGITS;
        for (int i = 0; i < len; i++) {
            int v = bytes[i] & 0xff;
            out[2 * i] = digits[v >>> 4];
            out[2 * i + 1] = digits[v & 0x0f];
        }
        return out;
    }

    public static byte[] bytesToHexBytes(byte[] bytes) {
        return bytesToHexBytes(bytes, 0, bytes.length, true);
    }

    public static byte[] bytesToHexBytes(byte[] bytes, int from, int to) {
        return bytesToHexBytes(bytes, from, to, true);
    }

    public static byte[] bytesToHexBytes(byte[] bytes, boolean uppercase) {
        return bytesToHexBytes(bytes, 0, bytes.length, uppercase);
    }

    public static String bytesToHexStr(byte[] bytes, int from, int to, boolean uppercase) {
        return new String(bytesToHexBytes(bytes, from, to, uppercase));
    }

    public static String bytesToHexStr(byte[] bytes) {
        return new String(bytesToHexBytes(bytes, 0, bytes.length, true));
    }

    public static String bytesToHexStr(byte[] bytes, int from, int to) {
        return new String(bytesToHexBytes(bytes, from, to, true));
    }

    public static String bytesToHexStr(byte[] bytes, boolean uppercase) {
        return new String(bytesToHexBytes(bytes, 0, bytes.length, uppercase));
    }
}
