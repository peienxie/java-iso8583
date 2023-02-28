package com.peienxie.iso8583;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.TreeMap;

import com.peienxie.iso8583.type.MTI;
import com.peienxie.iso8583.type.TPDU;

/**
 * The ISO8583 parser that can parse the byte array to ISO8583 message.
 */
public class ISO8583MessageParser {
    /**
     * Determines whether to parse the TPDU.
     */
    private final boolean parseTpdu;

    /**
     * Determines whether to parse the MTI.
     */
    private final boolean parseMti;

    /**
     * A map of field index numbers to their respective {@link ISO8583Field} objects.
     */
    private final Map<Integer, ISO8583Field<?>> fieldMap;

    /**
     * Creates a new instance of the ISO8583MessageParser.
     *
     * @param parseTpdu if true, the TPDU will be parsed from the input byte array
     * @param parseMti  if true, the MTI will be parsed from the input byte array
     */
    public ISO8583MessageParser(boolean parseTpdu, boolean parseMti) {
        this.parseTpdu = parseTpdu;
        this.parseMti = parseMti;
        this.fieldMap = new TreeMap<>();
    }

    /**
     * Adds a new field with the given index and decoder to the field map.
     *
     * @param index the index of the field
     * @param field the decoder of the field
     * @throws IllegalArgumentException if the index is not between 1 and 64, or the field is null or doesn't have a decoder
     */
    public void addField(int index, ISO8583Field<?> field) {
        if (index < 1 || index > 64) {
            throw new IllegalArgumentException("The index number must be between 1 and 64");
        } else if (field == null) {
            throw new IllegalArgumentException("The given field is null");
        } else if (!field.hasDecoder()) {
            throw new IllegalArgumentException("The given field don't have a decoder");
        }

        this.fieldMap.put(index, field);
    }

    /**
     * Parses the given byte array to an ISO8583Message.
     *
     * @param bytes the byte array to be parsed
     * @return the parsed ISO8583Message
     * @throws IllegalArgumentException if the input length is less than the expected length for TPDU or MTI
     * @throws IllegalStateException    if the field decoder is missing for the given field index
     */
    public ISO8583Message parse(byte[] bytes) {
        ISO8583Message msg = new ISO8583Message();

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        if (parseTpdu) {
            TPDU tpdu = parseTpdu(buffer);
            msg.setTPDU(tpdu);
        }

        if (parseMti) {
            MTI mti = parseMti(buffer);
            msg.setMTI(mti);
        }

        // parse bitmap
        byte[] bitmap = new byte[8];
        buffer.get(bitmap);

        // parse each field by bitmap and the parseFieldMap
        for (int i = 1; i <= 64; i++) {
            if (isBitOn(bitmap, i)) {
                ISO8583Field<?> field = parseField(i, buffer);
                msg.addField(i, field);
            }
        }
        return msg;
    }

    /**
     * Checks if the given bit is on in the bitmap.
     *
     * @param bitmap the bitmap byte array
     * @param bit    the bit number (indexing from 1)
     * @return true if the bit is on, false otherwise
     */
    private boolean isBitOn(byte[] bitmap, int bit) {
        int index = (bit - 1) / 8;
        int shift = 7 - ((bit - 1) % 8);
        return ((bitmap[index] >> shift) & 0x1) == 1;
    }

    /**
     * Parses the TPDU from the given {@link ByteBuffer}.
     *
     * @param buffer the buffer to parse
     * @return the parsed {@link TPDU}
     * @throws IllegalArgumentException if the buffer has fewer bytes than expected for a TPDU
     */
    private TPDU parseTpdu(ByteBuffer buffer) {
        int expectLength = 5;
        if (buffer.remaining() < expectLength) {
            throw new IllegalArgumentException("Input length " + buffer.remaining() + " is less than expect " + expectLength);
        }

        byte[] tpduBytes = new byte[expectLength];
        buffer.get(tpduBytes);
        int dst = tpduBytes[1] << 8 | (tpduBytes[2] & 0xff);
        int src = tpduBytes[3] << 8 | (tpduBytes[4] & 0xff);
        return TPDU.of(dst, src);
    }

    /**
     * Parses the MTI from the given {@link ByteBuffer}.
     *
     * @param buffer the buffer to parse
     * @return the parsed {@link MTI}
     * @throws IllegalArgumentException if the buffer has fewer bytes than expected for an MTI
     */
    private MTI parseMti(ByteBuffer buffer) {
        int expectLength = 2;
        if (buffer.remaining() < expectLength) {
            throw new IllegalArgumentException("Input length " + buffer.remaining() + " is less than expect " + expectLength);
        }

        byte[] mtiBytes = new byte[expectLength];
        buffer.get(mtiBytes);
        return MTI.of(mtiBytes[0] << 8 | (mtiBytes[1] & 0xff));
    }

    /**
     * Parses the field with the given index from the given {@link ByteBuffer}.
     *
     * @param index  the index of the field to parse
     * @param buffer the buffer to parse
     * @return the parsed {@link ISO8583Field}
     * @throws IllegalStateException if the decoder for the field is missing
     */
    private ISO8583Field<?> parseField(int index, ByteBuffer buffer) {
        ISO8583Field<?> field = fieldMap.get(index);
        if (field == null) {
            throw new IllegalStateException("Don't know how to parse field " + index + ", the decoder is missing");
        }

        field.decode(buffer);
        return field;
    }
}


