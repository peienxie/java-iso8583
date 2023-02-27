package com.peienxie.iso8583;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.TreeMap;

import com.peienxie.iso8583.type.MTI;
import com.peienxie.iso8583.type.TPDU;

public class ISO8583Parser {
    private final boolean parseTpdu;
    private final boolean parseMti;
    private final Map<Integer, ISO8583Field<?>> fieldMap;

    public ISO8583Parser(boolean parseTpdu, boolean parseMti) {
        this.parseTpdu = parseTpdu;
        this.parseMti = parseMti;
        this.fieldMap = new TreeMap<>();
    }

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

    // bit is indexing by 1.
    private boolean isBitOn(byte[] bitmap, int bit) {
        int index = (bit - 1) / 8;
        int shift = 7 - ((bit - 1) % 8);
        return ((bitmap[index] >> shift) & 0x1) == 1;
    }

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

    private MTI parseMti(ByteBuffer buffer) {
        int expectLength = 2;
        if (buffer.remaining() < expectLength) {
            throw new IllegalArgumentException("Input length " + buffer.remaining() + " is less than expect " + expectLength);
        }

        byte[] mtiBytes = new byte[expectLength];
        buffer.get(mtiBytes);
        return MTI.of(mtiBytes[0] << 8 | (mtiBytes[1] & 0xff));
    }

    private ISO8583Field<?> parseField(int index, ByteBuffer buffer) {
        ISO8583Field<?> field = fieldMap.get(index);
        if (field == null) {
            throw new IllegalStateException("Don't know how to parse field " + index + ", the decoder is missing");
        }

        field.decode(buffer);
        return field;
    }
}


