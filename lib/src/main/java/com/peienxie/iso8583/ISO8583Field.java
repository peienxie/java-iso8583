package com.peienxie.iso8583;

import com.peienxie.iso8583.encoder.ISO8583Encoder;
import com.peienxie.iso8583.util.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Optional;

public class ISO8583Field<T> {
    private final ISO8583Type type;
    private final T data;
    private final int formatLength;
    private final Optional<ISO8583Encoder<T>> encoder;

    private ISO8583Field(ISO8583Type type, T data, int formatLength) {
        if (!type.validateVariableLength(formatLength)) {
            throw new IllegalArgumentException(
                    type + " type with a invalid formatLength: " + formatLength);
        }

        this.type = type;
        this.data = data;
        this.formatLength = formatLength;
        this.encoder = Optional.empty();
    }

    private ISO8583Field(ISO8583Type type, T data, Optional<ISO8583Encoder<T>> encoder) {
        this.type = type;
        this.data = data;
        this.formatLength = 0;
        this.encoder = encoder;
    }

    public static ISO8583Field<String> ofText(String data, int formatLength) {
        return new ISO8583Field<>(ISO8583Type.ALPHA, data, formatLength);
    }

    public static ISO8583Field<Integer> ofInteger(Integer data, int formatLength) {
        return new ISO8583Field<>(ISO8583Type.NUMERIC, data, formatLength);
    }

    public static <T> ISO8583Field<T> of(ISO8583Type type, T data, ISO8583Encoder<T> encoder) {
        return new ISO8583Field<T>(type, data, Optional.of(encoder));
    }

    public void writeTo(OutputStream out) throws IOException {
        out.write(this.getBytes());
    }

    /** converts this ISO8583Field into byte array */
    public byte[] getBytes() {
        byte[] bytes;
        // use encoder encodes data into byte array
        if (this.encoder.isPresent()) {
            bytes = this.encoder.get().encode(this.data);
        } else if (this.type == ISO8583Type.NUMERIC) {
            // make sure format to even length hex string before convert to bytes
            String hexstr =
                    this.type.format(
                            this.data.toString(), this.formatLength + this.formatLength % 2);
            bytes = StringUtils.hexStrToBytes(hexstr);
        } else {
            bytes = this.type.format(this.data.toString(), this.formatLength).getBytes();
        }

        if (this.type.isVariableLength()) {
            // write the variable length of fields first
            byte[] varLen = this.type.getVariableLengthBytes(bytes.length);
            return ByteBuffer.allocate(varLen.length + bytes.length).put(varLen).put(bytes).array();
        }
        return bytes;
    }

    @Override
    public String toString() {
        String str;
        // use encoder encodes data into displayable string
        if (this.encoder.isPresent()) {
            str = StringUtils.bytesToHexStr(this.encoder.get().encode(this.data));
        } else {
            str = this.type.format(this.data.toString(), this.formatLength);
        }

        if (this.type.isVariableLength()) {
            return String.format("[%d] %s", str.length(), str);
        }
        return str;
    }
}
