package com.peienxie.iso8583.type;

import com.peienxie.iso8583.util.StringUtils;

/** TPDU (Transaction Protocol Data Unit) is a ISO8583 header */
public class TPDU {
    private final boolean isBinary;
    private final int id = 0x60;
    private final int dst;
    private final int src;

    private TPDU(int dst, int src, boolean isBinary) {
        this.dst = dst & 0xffff;
        this.src = src & 0xffff;
        this.isBinary = isBinary;
    }

    /** creates TPDU by given destination address which shoule be a base 16 integer */
    public static TPDU of(int dst) {
        return new TPDU(dst, 0, true);
    }

    /**
     * creates a TPDU by given destination and source addresses which should be a base 16 integer.
     * if isBinary set to true, when getBytes() is called this object will format as binary type
     * data or format as ASCII type data if set to false.
     */
    public static TPDU of(int dst, int src, boolean isBinary) {
        return new TPDU(dst, src, isBinary);
    }

    /**
     * converts this TPDU data into a byte array with following Format. the actual length of output
     * data is depends on isBinary field.
     *
     * <p>{0x60, 0x12, 0x34, 0x8a, 0x90} if given destination address is 0x1234 and source address
     * is 0x8a90.
     *
     * <p>if isBinary is set to true then getBytes() will output a 5 bytes length byte array. or
     * output a 10 bytes length hexstring when set to false.
     */
    public byte[] getBytes() {
        byte[] bytes = {
            id,
            (byte) ((this.dst >>> 8) & 0xff),
            (byte) (this.dst & 0xff),
            (byte) ((this.src >>> 8) & 0xff),
            (byte) (this.src & 0xff)
        };
        return isBinary ? bytes : StringUtils.bytesToHexStr(bytes).getBytes();
    }
}
