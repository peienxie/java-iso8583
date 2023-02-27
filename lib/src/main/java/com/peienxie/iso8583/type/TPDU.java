package com.peienxie.iso8583.type;

/**
 * TPDU (Transaction Protocol Data Unit) is a ISO8583 header
 */
public class TPDU {
    private final byte id = 0x60;
    private final int destinationAddress;
    private final int sourceAddress;

    private TPDU(int destinationAddress, int sourceAddress) {
        this.destinationAddress = destinationAddress;
        this.sourceAddress = sourceAddress;
    }

    /**
     * creates TPDU by given destination address which should be a base 16 integer
     */
    public static TPDU of(int dst) {
        return new TPDU(dst, 0);
    }

    /**
     * creates TPDU by given destination and source addresses which should be a base 16 integer
     */
    public static TPDU of(int dst, int src) {
        return new TPDU(dst, src);
    }

    public int getDestinationAddress() {
        return destinationAddress;
    }

    public int getSourceAddress() {
        return sourceAddress;
    }

    /**
     * converts this TPDU data into a byte array with following Format. the actual length of output
     * data is depends on isBinary field.
     * <p>
     * {0x60, 0x12, 0x34, 0x8a, 0x90} if given destination address is 0x1234 and source address
     * is 0x8a90.
     */
    public byte[] encode() {
        return new byte[]{
                id,
                (byte) ((this.destinationAddress >>> 8) & 0xff),
                (byte) (this.destinationAddress & 0xff),
                (byte) ((this.sourceAddress >>> 8) & 0xff),
                (byte) (this.sourceAddress & 0xff)
        };
    }

    @Override
    public String toString() {
        return String.format("%s[0x%02X, 0x%04X, 0x%04X]",
                TPDU.class.getSimpleName(), id, destinationAddress, sourceAddress);
    }
}
