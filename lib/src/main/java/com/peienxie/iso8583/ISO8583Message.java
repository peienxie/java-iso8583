package com.peienxie.iso8583;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import com.peienxie.iso8583.codec.FieldEncoder;
import com.peienxie.iso8583.type.MTI;
import com.peienxie.iso8583.type.TPDU;

public class ISO8583Message {
    /**
     * The ISO8583 message TPDU (Transaction Protocol Data Unit)
     */
    private TPDU tpdu;
    /**
     * The ISO8583 message type identifier
     */
    private MTI mti;

    /**
     * The ISO8583 message fields.
     */
    private Map<Integer, ISO8583Field<?>> fieldMap = new TreeMap<>();

    /**
     * Encodes tpdu, mti, and all the fields in this message into a byte array.
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

            byte[] bitmap = new byte[8];
            fieldMap.keySet().stream()
                    .map(i -> i - 1)
                    .forEach(i -> bitmap[i / 8] |= 1 << (7 - (i % 8)));
            out.write(bitmap);

            for (Map.Entry<Integer, ISO8583Field<?>> entry : fieldMap.entrySet()) {
                out.write(entry.getValue().encode());
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
        return fieldMap.get(index);
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
        fieldMap.put(index, field);
    }

    private void checkFieldIndex(int index) {
        if (index < 1 || index > 64) {
            throw new IllegalArgumentException("The index number must be between 1 and 64");
        }
    }
}
