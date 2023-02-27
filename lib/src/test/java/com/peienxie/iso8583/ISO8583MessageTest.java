package com.peienxie.iso8583;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import com.peienxie.iso8583.codec.AlphaEncoder;
import com.peienxie.iso8583.codec.FieldEncoder;
import com.peienxie.iso8583.codec.NumericEncoder;
import com.peienxie.iso8583.type.MTI;
import com.peienxie.iso8583.type.TPDU;
import com.peienxie.iso8583.util.StringUtils;
import org.junit.Test;

public class ISO8583MessageTest {

    @Test
    public void createTPDU() {
        byte[] tpdu;

        tpdu = createTPDU(0x1234);
        assertThat(StringUtils.bytesToHexStr(tpdu), is("6012340000"));

        tpdu = createTPDU(0x098098);
        assertThat(StringUtils.bytesToHexStr(tpdu), is("6080980000"));
    }

    private byte[] createTPDU(int dst) {
        ISO8583Message msg = new ISO8583Message();
        msg.setTPDU(TPDU.of(dst));
        byte[] msgBytes = msg.encode();
        return Arrays.copyOfRange(msgBytes, 0, 5);
    }

    @Test
    public void createMTI() {
        byte[] mti;

        mti = createMTI(0x1234);
        assertThat(StringUtils.bytesToHexStr(mti), is("1234"));

        mti = createMTI(0x098098);
        assertThat(StringUtils.bytesToHexStr(mti), is("8098"));
    }

    private byte[] createMTI(int mti) {
        ISO8583Message msg = new ISO8583Message();
        msg.setMTI(MTI.of(mti));
        byte[] msgBytes = msg.encode();
        return Arrays.copyOfRange(msgBytes, 0, 2);
    }


    @Test
    public void createBitmap() {
        byte[] bitmap;

        bitmap = createBitmap(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        assertThat(StringUtils.bytesToHexStr(bitmap), is("FFC0000000000000"));

        bitmap = createBitmap(56, 57, 58, 59, 60, 61, 62, 63, 64);
        assertThat(StringUtils.bytesToHexStr(bitmap), is("00000000000001FF"));

        bitmap = createBitmap(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 56, 57, 58, 59, 60, 61, 62, 63, 64);
        assertThat(StringUtils.bytesToHexStr(bitmap), is("FFC00000000001FF"));

        bitmap = createBitmap(3, 11, 12, 13, 24, 41, 61);
        assertThat(StringUtils.bytesToHexStr(bitmap), is("2038010000800008"));

        bitmap = createBitmap(2, 3, 4, 11, 12, 13, 15, 22, 24, 41, 58, 62, 63, 64);
        assertThat(StringUtils.bytesToHexStr(bitmap), is("703A050000800047"));

        bitmap = createBitmap(4, 11, 12, 13, 15, 22, 24, 41, 42, 58, 63, 64);
        assertThat(StringUtils.bytesToHexStr(bitmap), is("103A050000C00043"));
    }

    /**
     * creates a bitmap of ISO8583Message with empty fields by given indexes for verifying bitmap
     * creation
     */
    private byte[] createBitmap(int... indexes) {
        ISO8583Message msg = new ISO8583Message();
        for (int i : indexes) {
            msg.addField(i, ISO8583Field.request("", new AlphaEncoder(1)));
        }
        byte[] msgBytes = msg.encode();
        return Arrays.copyOfRange(msgBytes, 0, 8);
    }

    @SuppressWarnings("Convert2Diamond")
    @Test
    public void createFields() {
        ISO8583Message msg = new ISO8583Message();
        msg.addField(3, 920000, new NumericEncoder(6));
        msg.addField(4, 12300, new NumericEncoder(12));
        msg.addField(11, 23, new NumericEncoder(6));

        LocalDateTime now = LocalDateTime.of(2022, 10, 11, 12,34, 56);
        FieldEncoder<LocalDateTime> dateEncoder = new FieldEncoder<LocalDateTime>() {
            @Override
            public int getEncodeLength() {
                return 4;
            }

            @Override
            public byte[] encode(LocalDateTime data) {
                return StringUtils.hexStrToBytes(data.format(DateTimeFormatter.ofPattern("MMdd")));
            }
        };
        FieldEncoder<LocalDateTime> timeEncoder = new FieldEncoder<LocalDateTime>() {
            @Override
            public int getEncodeLength() {
                return 6;
            }

            @Override
            public byte[] encode(LocalDateTime data) {
                return StringUtils.hexStrToBytes(data.format(DateTimeFormatter.ofPattern("HHmmss")));
            }
        };
        msg.addField(12, now, dateEncoder);
        msg.addField(13, now, timeEncoder);

        assertThat(StringUtils.bytesToHexStr(msg.encode()), is("30380000000000009200000000000123000000231011123456"));
    }
}
