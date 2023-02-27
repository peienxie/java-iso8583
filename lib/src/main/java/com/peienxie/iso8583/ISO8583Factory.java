package com.peienxie.iso8583;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

import com.peienxie.iso8583.type.MTI;
import com.peienxie.iso8583.type.TPDU;

public class ISO8583Factory {

    private final TPDU tpdu;
    private final MTI mti;
    private final Map<Integer, ISO8583Field<?>> fieldMap;

    public ISO8583Factory(TPDU tpdu, MTI mti, Map<Integer, ISO8583Field<?>> fieldMap) {
        this.tpdu = tpdu;
        this.mti = mti;
        this.fieldMap = new TreeMap<>(fieldMap);
    }

    public ISO8583Message make() {
        return make(msg -> {/* do noting */});
    }

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
