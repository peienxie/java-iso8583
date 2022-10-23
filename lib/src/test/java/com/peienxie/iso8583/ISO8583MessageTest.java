package com.peienxie.iso8583;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.peienxie.iso8583.type.MTI;
import com.peienxie.iso8583.type.TPDU;
import com.peienxie.iso8583.util.StringUtils;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.stream.IntStream;

public class ISO8583MessageTest {

    private byte[] createTPDU(int nii) {
        ISO8583Message msg = new ISO8583Message();
        msg.setTPDU(TPDU.of(nii));
        byte[] msgBytes = msg.getBytes();
        return Arrays.copyOfRange(msgBytes, 0, 5);
    }

    @Test
    public void createTPDU() {
        byte[] tpdu;

        tpdu = createTPDU(0x1234);
        assertThat(StringUtils.bytesToHexStr(tpdu), is("6012340000"));

        tpdu = createTPDU(0x098098);
        assertThat(StringUtils.bytesToHexStr(tpdu), is("6080980000"));
    }

    private byte[] createMTI(int mti) {
        ISO8583Message msg = new ISO8583Message();
        msg.setMTI(MTI.of(mti));
        byte[] msgBytes = msg.getBytes();
        return Arrays.copyOfRange(msgBytes, 0, 2);
    }

    @Test
    public void createMTI() {
        byte[] mti;

        mti = createMTI(0x1234);
        assertThat(StringUtils.bytesToHexStr(mti), is("1234"));

        mti = createMTI(0x098098);
        assertThat(StringUtils.bytesToHexStr(mti), is("8098"));
    }
    /**
     * creates a bitmap of ISO8583Message with empty fields by given indexes for verifying bitmap
     * creation
     */
    private byte[] createBitmap(int... indexes) {
        ISO8583Message msg = new ISO8583Message();
        for (int i : indexes) {
            msg.setField(i, ISO8583Field.ofText("", 0));
        }
        byte[] msgBytes = msg.getBytes();
        return Arrays.copyOfRange(msgBytes, msgBytes.length - 8, msgBytes.length);
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

    class CustomField {
        public final int value;

        public CustomField(int value) {
            this.value = value;
        }

        public byte[] encode() {
            return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(this.value).array();
        }
    }

    @Test
    public void createFields() {
        ISO8583Message iso = new ISO8583Message();
        iso.setField(
                3,
                ISO8583Type.NUMERIC,
                0x920000,
                x -> StringUtils.hexStrToBytes(String.format("%06X", x)));
        assertThat(StringUtils.bytesToHexStr(iso.getField(3).getBytes()), is("920000"));

        iso.setField(
                3,
                ISO8583Type.NUMERIC,
                "1234",
                x -> StringUtils.hexStrToBytes(ISO8583Type.NUMERIC.format(x, 8)));
        assertThat(StringUtils.bytesToHexStr(iso.getField(3).getBytes()), is("00001234"));

        iso.setField(3, ISO8583Field.ofInteger(920000, 6));
        assertThat(StringUtils.bytesToHexStr(iso.getField(3).getBytes()), is("920000"));

        CustomField custom = new CustomField(78456);
        iso.setField(3, ISO8583Type.NUMERIC, custom, x -> x.encode());
        assertThat(StringUtils.bytesToHexStr(iso.getField(3).getBytes()), is("78320100"));
    }

    @Test
    public void createVarlenFields() {
        ISO8583Message iso = new ISO8583Message();
        iso.setField(
                3,
                ISO8583Type.LLVAR,
                0x920000,
                x -> StringUtils.hexStrToBytes(String.format("%012X", x)));
        assertThat(StringUtils.bytesToHexStr(iso.getField(3).getBytes()), is("06000000920000"));

        iso.setField(3, ISO8583Type.LLVAR, 0x920000, x -> String.format("%012X", x).getBytes());
        assertThat(
                StringUtils.bytesToHexStr(iso.getField(3).getBytes()),
                is("12303030303030393230303030"));

        CustomField custom1 = new CustomField(78456);
        iso.setField(3, ISO8583Type.LLVAR, custom1, x -> x.encode());
        assertThat(StringUtils.bytesToHexStr(iso.getField(3).getBytes()), is("0478320100"));

        CustomField custom2 = new CustomField(1234);
        iso.setField(3, ISO8583Type.LLLVAR, custom2, x -> x.encode());
        assertThat(StringUtils.bytesToHexStr(iso.getField(3).getBytes()), is("0004D2040000"));
    }

    @Test
    public void createVarLenFieldsWithCustomEncoder() {
        ISO8583Message iso = new ISO8583Message();
        CustomField custom = new CustomField(0x78);

        iso.setField(
                3,
                ISO8583Type.LLLVAR,
                custom,
                x -> {
                    byte[] bytes = new byte[187];
                    Arrays.fill(bytes, (byte) 0x78);
                    return bytes;
                });

        byte[] output = iso.getField(3).getBytes();
        assertThat(Arrays.copyOf(output, 2), is(new byte[] {0x01, (byte) 0x87}));

        long count =
                IntStream.range(0, output.length)
                        .map(i -> output[i])
                        .filter(x -> x == 0x78)
                        .count();
        assertThat(count, is(187L));
    }
}
