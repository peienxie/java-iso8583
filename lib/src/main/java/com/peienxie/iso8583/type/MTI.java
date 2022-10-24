package com.peienxie.iso8583.type;

import com.peienxie.iso8583.util.StringUtils;

import java.nio.ByteBuffer;
import java.text.ParseException;

/** MTI (Message Type Identifier) is a ISO8583 message type */
public class MTI {
    private final boolean isBinary;
    private final int value;

    public MTI(int value, boolean isBinary) {
        this.value = value & 0xffff;
        this.isBinary = isBinary;
    }

    /** creates a MTI by given message type value which should be a base 16 integer */
    public static MTI of(int value) {
        return new MTI(value, true);
    }

    /**
     * creates a MTI by given message type value which should be a base 16 integer. if isBinary set
     * to true, when getBytes() is called this object will format as binary type data or format as
     * ASCII type data if set to false.
     */
    public static MTI of(int value, boolean isBinary) {
        return new MTI(value, isBinary);
    }

    /**
     * convert MTI value into a byte array, the length of output data is depends on isBinary value.
     *
     * <p>if isBinary is set to true then getBytes() will output a 2 bytes length byte array. or
     * output a 4 bytes length hexstring when set to false.
     */
    public byte[] getBytes() {
        byte[] bytes = {(byte) ((this.value >>> 8) & 0xff), (byte) (value & 0xff)};
        return isBinary ? bytes : StringUtils.bytesToHexStr(bytes).getBytes();
    }

    public MTI parse(ByteBuffer buf) throws ParseException {
        int expectLength = isBinary ? 2 : 4;

        if (buf.remaining() < expectLength) {
            throw new ParseException(
                    "input length " + buf.remaining() + " is less than expect " + expectLength,
                    buf.position());
        }

        byte[] bytes = new byte[expectLength];
        buf.get(bytes);

        int val;
        if (isBinary) {
            val = bytes[0] << 8 | bytes[1];
        } else {
            byte[] tmp = StringUtils.hexStrToBytes(new String(bytes));
            val = tmp[0] << 8 | tmp[1];
        }
        return MTI.of(val);
    }

    public MTI parse(byte[] bytes) throws ParseException {
        return parse(ByteBuffer.wrap(bytes));
    }
}
