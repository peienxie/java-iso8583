package com.peienxie.iso8583;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.stream.IntStream;

import com.peienxie.iso8583.codec.FieldEncoder;
import com.peienxie.iso8583.type.MTI;
import com.peienxie.iso8583.type.TPDU;

public class ISO8583Message {
    /**
     * The ISO8583 message fields.
     */
    private final ISO8583Field<?>[] fields;
    /**
     * The ISO8583 message TPDU (Transaction Protocol Data Unit)
     */
    private TPDU tpdu;
    /**
     * The ISO8583 message type identifier
     */
    private MTI mti;

    public ISO8583Message() {
        // only support primary bitmap, which is only 64 field items
        this.fields = new ISO8583Field[64];
    }

    private byte[] createBitmap() {
        byte[] bitmap = new byte[8];
        IntStream.range(0, 64)
                .filter(i -> this.fields[i] != null)
                .forEachOrdered(i -> bitmap[i / 8] |= 1 << (7 - (i % 8)));
        return bitmap;
    }

    /**
     * Encodes all the fields in the message into a byte array.
     *
     * @return a byte array containing the encoded form of the message
     * @throws IllegalStateException if any field does not have an encoder
     */
    public byte[] encode() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            if (tpdu != null) {
                out.write(tpdu.encode());
            }
            if (mti != null) {
                out.write(mti.encode());
            }

            out.write(createBitmap());
            for (ISO8583Field<?> field : fields) {
                if (field != null) {
                    out.write(field.encode());
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Should never happened when writing to a ByteArrayOutputStream.", e);
        }
        return out.toByteArray();
    }

    public TPDU getTPDU() {
        return this.tpdu;
    }

    public void setTPDU(TPDU tpdu) {
        this.tpdu = tpdu;
    }

    public MTI getMTI() {
        return this.mti;
    }

    public void setMTI(MTI mti) {
        this.mti = mti;
    }

    /**
     * Returns the ISO8583Field data for the provided field index
     *
     * @param index the index number of data fields
     * @throws IllegalArgumentException when given index is not between 2 and 64
     */
    public ISO8583Field<?> getField(int index) {
        checkFieldIndex(index);
        return fields[index - 1];
    }

    public <T> void addField(int index, T data, FieldEncoder<T> encoder) {
        addField(index, ISO8583Field.request(data, encoder));
    }

    /**
     * Sets the ISO8583Field data for the provided field index
     *
     * @param index the index number of data fields
     * @param field the ISO8583Field data
     * @throws IllegalArgumentException when given index is not between 2 and 64
     */
    public void addField(int index, ISO8583Field<?> field) {
        checkFieldIndex(index);
        this.fields[index - 1] = field;
    }

    private void checkFieldIndex(int index) {
        if (index < 1 || index > 64) {
            throw new IllegalArgumentException("The index number must be between 1 and 64");
        }
    }
}
