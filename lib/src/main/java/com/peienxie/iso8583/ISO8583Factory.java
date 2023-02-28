package com.peienxie.iso8583;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

import com.peienxie.iso8583.type.MTI;
import com.peienxie.iso8583.type.TPDU;

/**
 * Factory for creating ISO 8583 messages.
 */
public class ISO8583Factory {
    /**
     * The TPDU (Transport Protocol Data Unit) to include in the message header.
     */
    private final TPDU tpdu;

    /**
     * The MTI (Message Type Indicator) to include in the message header.
     */
    private final MTI mti;

    /**
     * The map of ISO 8583 fields to include in the message body.
     */
    private final Map<Integer, ISO8583Field<?>> fieldMap;

    /**
     * Constructs a new ISO8583Factory with the given TPDU, MTI, and field map.
     *
     * @param tpdu     the TPDU to include in the message header
     * @param mti      the MTI to include in the message header
     * @param fieldMap the map of ISO 8583 fields to include in the message body
     */
    public ISO8583Factory(TPDU tpdu, MTI mti, Map<Integer, ISO8583Field<?>> fieldMap) {
        this.tpdu = tpdu;
        this.mti = mti;
        this.fieldMap = new TreeMap<>(fieldMap);
    }

    /**
     * Creates a new ISO8583Message with the configured TPDU, MTI, and field map.
     *
     * @return the new ISO8583Message
     */
    public ISO8583Message make() {
        return make(msg -> {/* do nothing */});
    }

    /**
     * Creates a new ISO8583Message with the configured TPDU, MTI, and field map,
     * and applies the given consumer to the message before returning it.
     *
     * @param block the consumer to apply to the new ISO8583Message
     * @return the new ISO8583Message
     */
    public ISO8583Message make(Consumer<ISO8583Message> block) {
        ISO8583Message msg = new ISO8583Message();
        msg.setTPDU(tpdu);
        msg.setMTI(mti);
        for (Map.Entry<Integer, ISO8583Field<?>> entry : fieldMap.entrySet()) {
            msg.addField(entry.getKey(), entry.getValue());
        }
        block.accept(msg);
        return msg;
    }
}
