package com.peienxie.iso8583;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.peienxie.iso8583.util.StringUtils;

import org.junit.Test;

import java.io.IOException;

public class ISO8583TypeTest {

    @Test
    public void formatAlphaType() throws IOException {
        assertThat(ISO8583Type.ALPHA.format("1234", 0), is(""));
        assertThat(ISO8583Type.ALPHA.format("1234", 3), is("123"));
        assertThat(ISO8583Type.ALPHA.format("1234", 6), is("1234  "));
        assertThat(ISO8583Type.ALPHA.format("", 6), is("      "));
        assertThat(ISO8583Type.ALPHA.format("1234", 20), is("1234                "));
    }

    @Test
    public void formatNumericType() {
        assertThat(ISO8583Type.NUMERIC.format("1234", 1), is("4"));
        assertThat(ISO8583Type.NUMERIC.format("1234", 3), is("234"));
        assertThat(ISO8583Type.NUMERIC.format("1234", 6), is("001234"));
        assertThat(ISO8583Type.NUMERIC.format("", 6), is("000000"));
        assertThat(ISO8583Type.NUMERIC.format("1234", 20), is("00000000000000001234"));
    }

    @Test
    public void getVariableLengthBytes() {
        assertThat(
                ISO8583Type.LLLVAR.getVariableLengthBytes(12345),
                is(StringUtils.hexStrToBytes("012345")));
        assertThat(
                ISO8583Type.LLLVAR.getVariableLengthBytes(1000),
                is(StringUtils.hexStrToBytes("1000")));
        assertThat(
                ISO8583Type.LLLVAR.getVariableLengthBytes(100),
                is(StringUtils.hexStrToBytes("0100")));
        assertThat(
                ISO8583Type.LLLVAR.getVariableLengthBytes(12),
                is(StringUtils.hexStrToBytes("0012")));
        assertThat(
                ISO8583Type.LLLVAR.getVariableLengthBytes(9),
                is(StringUtils.hexStrToBytes("0009")));
        assertThat(
                ISO8583Type.LLLVAR.getVariableLengthBytes(0),
                is(StringUtils.hexStrToBytes("0000")));

        assertThat(
                ISO8583Type.LLVAR.getVariableLengthBytes(100),
                is(StringUtils.hexStrToBytes("0100")));
        assertThat(
                ISO8583Type.LLVAR.getVariableLengthBytes(12), is(StringUtils.hexStrToBytes("12")));
        assertThat(
                ISO8583Type.LLVAR.getVariableLengthBytes(9), is(StringUtils.hexStrToBytes("09")));
        assertThat(
                ISO8583Type.LLVAR.getVariableLengthBytes(0), is(StringUtils.hexStrToBytes("00")));
    }

    @Test
    public void getVariableLengthBytesOthers() {
        assertThat(ISO8583Type.NUMERIC.getVariableLengthBytes(10), is(new byte[0]));
        assertThat(ISO8583Type.ALPHA.getVariableLengthBytes(10), is(new byte[0]));
    }

    @Test
    public void validateVariableLength() {
        assertThat(ISO8583Type.LLVAR.validateVariableLength(0), is(true));
        assertThat(ISO8583Type.LLVAR.validateVariableLength(7), is(true));
        assertThat(ISO8583Type.LLVAR.validateVariableLength(32), is(true));
        assertThat(ISO8583Type.LLVAR.validateVariableLength(99), is(true));
        assertThat(ISO8583Type.LLVAR.validateVariableLength(100), is(false));
        assertThat(ISO8583Type.LLVAR.validateVariableLength(107), is(false));
        assertThat(ISO8583Type.LLVAR.validateVariableLength(320), is(false));
        assertThat(ISO8583Type.LLVAR.validateVariableLength(999), is(false));
        assertThat(ISO8583Type.LLVAR.validateVariableLength(1000), is(false));
        assertThat(ISO8583Type.LLVAR.validateVariableLength(10000), is(false));

        assertThat(ISO8583Type.LLLVAR.validateVariableLength(0), is(true));
        assertThat(ISO8583Type.LLLVAR.validateVariableLength(7), is(true));
        assertThat(ISO8583Type.LLLVAR.validateVariableLength(32), is(true));
        assertThat(ISO8583Type.LLLVAR.validateVariableLength(99), is(true));
        assertThat(ISO8583Type.LLLVAR.validateVariableLength(100), is(true));
        assertThat(ISO8583Type.LLLVAR.validateVariableLength(107), is(true));
        assertThat(ISO8583Type.LLLVAR.validateVariableLength(320), is(true));
        assertThat(ISO8583Type.LLLVAR.validateVariableLength(999), is(true));
        assertThat(ISO8583Type.LLLVAR.validateVariableLength(1000), is(false));
        assertThat(ISO8583Type.LLLVAR.validateVariableLength(10000), is(false));
    }

    @Test
    public void validateVariableLengthOthers() {
        assertThat(ISO8583Type.NUMERIC.validateVariableLength(0), is(true));
        assertThat(ISO8583Type.NUMERIC.validateVariableLength(7), is(true));
        assertThat(ISO8583Type.NUMERIC.validateVariableLength(32), is(true));
        assertThat(ISO8583Type.NUMERIC.validateVariableLength(99), is(true));
        assertThat(ISO8583Type.NUMERIC.validateVariableLength(100), is(true));
        assertThat(ISO8583Type.NUMERIC.validateVariableLength(107), is(true));
        assertThat(ISO8583Type.NUMERIC.validateVariableLength(320), is(true));
        assertThat(ISO8583Type.NUMERIC.validateVariableLength(999), is(true));
        assertThat(ISO8583Type.NUMERIC.validateVariableLength(1000), is(true));
        assertThat(ISO8583Type.NUMERIC.validateVariableLength(10000), is(true));

        assertThat(ISO8583Type.ALPHA.validateVariableLength(0), is(true));
        assertThat(ISO8583Type.ALPHA.validateVariableLength(7), is(true));
        assertThat(ISO8583Type.ALPHA.validateVariableLength(32), is(true));
        assertThat(ISO8583Type.ALPHA.validateVariableLength(99), is(true));
        assertThat(ISO8583Type.ALPHA.validateVariableLength(100), is(true));
        assertThat(ISO8583Type.ALPHA.validateVariableLength(107), is(true));
        assertThat(ISO8583Type.ALPHA.validateVariableLength(320), is(true));
        assertThat(ISO8583Type.ALPHA.validateVariableLength(999), is(true));
        assertThat(ISO8583Type.ALPHA.validateVariableLength(1000), is(true));
        assertThat(ISO8583Type.ALPHA.validateVariableLength(10000), is(true));
    }
}
