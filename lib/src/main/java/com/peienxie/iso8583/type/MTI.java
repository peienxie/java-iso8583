package com.peienxie.iso8583.type;

/**
 * MTI (Message Type Identifier) is a ISO8583 message type
 */
public class MTI {
    private final int value;

    public MTI(int value) {
        this.value = value & 0xffff;
    }

    /**
     * creates a MTI by given message type value which should be a base 16 integer
     */
    public static MTI of(int value) {
        return new MTI(value);
    }

    public int getValue() {
        return value;
    }

    /**
     * convert MTI value into a byte array, the length of output data is depends on isBinary value.
     */
    public byte[] encode() {
        return new byte[]{(byte) ((this.value >>> 8) & 0xff), (byte) (value & 0xff)};
    }

    @Override
    public String toString() {
        return String.format("%s[0x%04X]", TPDU.class.getSimpleName(), value);
    }
}
