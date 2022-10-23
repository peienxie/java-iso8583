package com.peienxie.iso8583;

import com.peienxie.iso8583.util.StringUtils;

public enum ISO8583Type {
    /** Numeric data. left padding zeros when formatting */
    NUMERIC(false, 0),
    /** Alphanumeric data with ASCII format. right padding spaces when formatting */
    ALPHA(false, 0),
    /** Variable length data with 2 digit BCD format length */
    LLVAR(true, 2),
    /** Variable length data with 3 digit BCD format length */
    LLLVAR(true, 3);

    private final boolean variableLength;
    private final int variableLengthDigits;

    ISO8583Type(boolean variableLength, int variableLengthDigits) {
        this.variableLength = variableLength;
        this.variableLengthDigits = variableLengthDigits;
    }

    /** formats the given data with the given length base on this type */
    public String format(String data, int formatLength) {
        if (this == NUMERIC) {
            if (formatLength <= 0) {
                return "";
            } else if (formatLength < data.length()) {
                return data.substring(data.length() - formatLength, data.length());
            }
            // use %{length}s format for left padding, after that replace spaces with zeros
            return String.format("%" + formatLength + "s", data).replace(" ", "0");
        } else if (this == ALPHA) {
            if (formatLength <= 0) {
                return "";
            } else if (data.length() > formatLength) {
                return data.substring(0, formatLength);
            }
            // use %-{length}s format for right padding
            return String.format("%-" + formatLength + "s", data);
        } else {
            // ignore other variable length data format
            return data;
        }
    }

    public byte[] getVariableLengthBytes(int length) {
        if (this == LLVAR || this == LLLVAR) {
            // FIXME: maybe should just throw a exception when given length of data is smaller
            int inputDigits = (int) (Math.log10(length) + 1);
            int digits = Math.max(inputDigits, this.getVariableLengthDigits());
            // make sure is even length
            digits = digits + digits % 2;
            return StringUtils.hexStrToBytes(String.format("%0" + digits + "d", length));
        }
        return new byte[0];
    }

    /** check if the given length is not greater than this type variable length limit */
    public boolean validateVariableLength(int length) {
        if (this == LLVAR) {
            return length >= 0 && length < 100;
        } else if (this == LLLVAR) {
            return length >= 0 && length < 1000;
        }
        return !this.isVariableLength();
    }

    public boolean isVariableLength() {
        return this.variableLength;
    }

    public int getVariableLengthDigits() {
        return this.variableLengthDigits;
    }
}
