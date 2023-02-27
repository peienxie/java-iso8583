package com.peienxie.iso8583.util;

public class StringUtils {
    private static final byte[] UPPERCASE_DIGITS = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
    };
    private static final byte[] LOWERCASE_DIGITS = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f',
    };

    private StringUtils() {
    }


    public static String bytesToHexStr(byte[] bytes, int offset, int length, boolean uppercase) {
        return new String(bytesToHexBytes(bytes, offset, length, uppercase));
    }

    public static String bytesToHexStr(byte[] bytes) {
        return new String(bytesToHexBytes(bytes, 0, bytes.length, true));
    }

    public static String bytesToHexStr(byte[] bytes, int offset, int length) {
        return new String(bytesToHexBytes(bytes, offset, length, true));
    }

    public static String bytesToHexStr(byte[] bytes, boolean uppercase) {
        return new String(bytesToHexBytes(bytes, 0, bytes.length, uppercase));
    }


    public static byte[] bytesToHexBytes(byte[] bytes) {
        return bytesToHexBytes(bytes, 0, bytes.length, true);
    }

    public static byte[] bytesToHexBytes(byte[] bytes, int offset, int length) {
        return bytesToHexBytes(bytes, offset, length, true);

    }

    public static byte[] bytesToHexBytes(byte[] bytes, boolean uppercase) {
        return bytesToHexBytes(bytes, 0, bytes.length, uppercase);
    }

    public static byte[] bytesToHexBytes(byte[] bytes, int offset, int length, boolean uppercase) {
        byte[] out = new byte[length * 2];
        byte[] digits = uppercase ? UPPERCASE_DIGITS : LOWERCASE_DIGITS;
        for (int i = offset; i < offset + length; i++) {
            int v = bytes[i] & 0xff;
            out[2 * i] = digits[v >>> 4];
            out[2 * i + 1] = digits[v & 0x0f];
        }
        return out;
    }

    /**
     * Converts given hexadecimal string into byte array.
     *
     * @param hexString must be an even-length string.
     */
    public static byte[] hexStrToBytes(String hexString) {
        if ((hexString.length() & 1) != 0) {
            throw new IllegalArgumentException(
                    "Input hexString length is not even: " + hexString.length());
        }

        int len = hexString.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            int a = Character.digit(hexString.charAt(i), 16);
            int b = Character.digit(hexString.charAt(i + 1), 16);
            bytes[i / 2] = (byte) ((a << 4) | b);
        }
        return bytes;
    }
}
