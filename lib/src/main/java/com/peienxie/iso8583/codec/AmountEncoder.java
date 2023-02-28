package com.peienxie.iso8583.codec;

import com.peienxie.iso8583.util.StringUtils;

public class AmountEncoder implements FieldEncoder<Number> {

    @Override
    public int getEncodeLength() {
        return 6;
    }

    @Override
    public byte[] encode(Number data) {
        long first2DecimalDigits = (long) (data.doubleValue() * 100) % 100;
        String str = String.valueOf(data.longValue() * 100 + first2DecimalDigits);
        int zerosToAdd = 12 - str.length();
        if (zerosToAdd > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < zerosToAdd; i++) {
                sb.append('0');
            }
            sb.append(str);
            str = sb.toString();
        }
        return StringUtils.hexStrToBytes(str);
    }
}
