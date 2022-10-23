package com.peienxie.iso8583;

import com.peienxie.iso8583.encoder.ISO8583Encoder;
import com.peienxie.iso8583.type.MTI;
import com.peienxie.iso8583.type.TPDU;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ISO8583Message {
    /** The ISO8583 message TPDU (Transaction Protocol Data Unit) */
    private TPDU tpdu;
    /** The ISO8583 message type identifier */
    private MTI mti;
    /** The ISO8583 message fields, only support primary bitmap */
    private ISO8583Field<?>[] fields = new ISO8583Field[64];

    /** Creates a empty ISO8583 message object */
    public ISO8583Message() {}

    private byte[] createBitmap() {
        byte[] bitmap = new byte[8];
        for (int i = 0; i < 64; i++) {
            if (this.fields[i] != null) {
                bitmap[i / 8] |= 1 << (7 - (i % 8));
            }
        }
        return bitmap;
    }

    public byte[] getBytes() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            if (this.tpdu != null) out.write(this.tpdu.getBytes());
            if (this.mti != null) out.write(this.mti.getBytes());
            out.write(createBitmap());
            for (int i = 0; i < 64; i++) {
                if (this.fields[i] != null) {
                    this.fields[i].writeTo(out);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException(
                    "should never happand when write to a ByteArrayOutputStream.", e);
        }
        return out.toByteArray();
    }

    public TPDU getTPDU() {
        return this.tpdu;
    }

    public ISO8583Message setTPDU(TPDU tpdu) {
        this.tpdu = tpdu;
        return this;
    }

    public MTI getMTI() {
        return this.mti;
    }

    public ISO8583Message setMTI(MTI mti) {
        this.mti = mti;
        return this;
    }

    /**
     * Returns the ISO8583Field data for the provided field index
     *
     * @param index the index number of data fields
     * @throws IllegalArgumentException when given index is not between 2 and 64
     */
    public ISO8583Field<?> getField(int index) {
        if (index < 2 && index > 64) {
            throw new IllegalArgumentException("the index number must be between 2 and 64");
        }
        return fields[index - 1];
    }

    /**
     * Sets the ISO8583Field data for the provided field index
     *
     * @param index the index number of data fields
     * @param field the ISO8583Field data
     * @throws IllegalArgumentException when given index is not between 2 and 64
     */
    public ISO8583Message setField(int index, ISO8583Field<?> field) {
        if (index < 2 && index > 64) {
            throw new IllegalArgumentException("the index number must be between 2 and 64");
        }
        this.fields[index - 1] = field;
        return this;
    }

    public <T> ISO8583Message setField(
            int index, ISO8583Type type, T data, ISO8583Encoder<T> encoder) {
        if (index < 2 && index > 64) {
            throw new IllegalArgumentException("the index number must be between 2 and 64");
        }
        this.fields[index - 1] = ISO8583Field.of(type, data, encoder);
        return this;
    }
}
