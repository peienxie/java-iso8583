package com.peienxie.iso8583;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import com.peienxie.iso8583.codec.FieldEncoder;
import com.peienxie.iso8583.type.MTI;
import com.peienxie.iso8583.type.TPDU;

/**
 * Represents an ISO8583 message.
 */
public class ISO8583Message {
    /**
     * The ISO8583 message fields.
     */
    private final Map<Integer, ISO8583Field<?>> fieldMap = new TreeMap<>();
    /**
     * The ISO8583 message TPDU (Transaction Protocol Data Unit)
     */
    private TPDU tpdu;
    /**
     * The ISO8583 message type identifier
     */
    private MTI mti;

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
     * Returns the ISO8583Field data for the provided field index.
     *
     * @param index the index number of data fields
     * @return the ISO8583Field data for the provided field index
     * @throws IllegalArgumentException when the given index is in invalid range
     */
    public ISO8583Field<?> getField(int index) {
        checkFieldIndex(index);
        return fieldMap.get(index);
    }

    /**
     * Adds the ISO8583Field data for the provided field index with the given encoder.
     *
     * @param index   the index number of data fields
     * @param data    the ISO8583Field data to be added
     * @param encoder the encoder for the data
     * @param <T>     the type of the data
     * @return the current instance of ISO8583Message
     * @throws IllegalArgumentException when the given index is in invalid range
     */
    public <T> ISO8583Message addField(int index, T data, FieldEncoder<T> encoder) {
        addField(index, ISO8583Field.request(data, encoder));
        return this;
    }

    /**
     * Adds the ISO8583Field data for the provided field index.
     *
     * @param index the index number of data fields
     * @param field the ISO8583Field data to be added
     * @throws IllegalArgumentException when the given index is in invalid range
     */
    public void addField(int index, ISO8583Field<?> field) {
        checkFieldIndex(index);
        fieldMap.put(index, field);
    }

    /**
     * Checks if the provided field index is between 2 and 64.
     *
     * @param index the index number of data fields
     * @throws IllegalArgumentException when the given index is not between 2 and 64
     */
    private void checkFieldIndex(int index) {
        if (index < 2 || index > 64) {
            throw new IllegalArgumentException("The index number must be between 2 and 64");
        }
    }
}
