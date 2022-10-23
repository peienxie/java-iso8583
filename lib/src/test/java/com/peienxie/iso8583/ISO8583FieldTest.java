package com.peienxie.iso8583;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.peienxie.iso8583.util.StringUtils;

import org.junit.Test;

public class ISO8583FieldTest {

    @Test
    public void createALPHAField() {
        assertThat(ISO8583Field.ofText("qwer", 2).getBytes(), is("qw".getBytes()));
        assertThat(ISO8583Field.ofText("qwer", 10).getBytes(), is("qwer      ".getBytes()));
        assertThat(ISO8583Field.ofText("1234", 2).getBytes(), is("12".getBytes()));
        assertThat(ISO8583Field.ofText("1234", 10).getBytes(), is("1234      ".getBytes()));
    }

    @Test
    public void createNumericField() {
        assertThat(ISO8583Field.ofInteger(123, 2).getBytes(), is(StringUtils.hexStrToBytes("23")));
        assertThat(
                ISO8583Field.ofInteger(123, 10).getBytes(),
                is(StringUtils.hexStrToBytes("0000000123")));
        assertThat(
                ISO8583Field.ofInteger(12345, 5).getBytes(),
                is(StringUtils.hexStrToBytes("012345")));
        assertThat(
                ISO8583Field.ofInteger(12345, 6).getBytes(),
                is(StringUtils.hexStrToBytes("012345")));
        assertThat(
                ISO8583Field.ofInteger(12345, 7).getBytes(),
                is(StringUtils.hexStrToBytes("00012345")));
        assertThat(
                ISO8583Field.ofInteger(12345, 8).getBytes(),
                is(StringUtils.hexStrToBytes("00012345")));
    }

    @Test
    public void createVariableLengthField() {
        assertThat(ISO8583Field.ofInteger(123, 2).getBytes(), is(StringUtils.hexStrToBytes("23")));
        assertThat(
                ISO8583Field.ofInteger(123, 10).getBytes(),
                is(StringUtils.hexStrToBytes("0000000123")));
        assertThat(
                ISO8583Field.ofInteger(12345, 5).getBytes(),
                is(StringUtils.hexStrToBytes("012345")));
        assertThat(
                ISO8583Field.ofInteger(12345, 6).getBytes(),
                is(StringUtils.hexStrToBytes("012345")));
        assertThat(
                ISO8583Field.ofInteger(12345, 7).getBytes(),
                is(StringUtils.hexStrToBytes("00012345")));
        assertThat(
                ISO8583Field.ofInteger(12345, 8).getBytes(),
                is(StringUtils.hexStrToBytes("00012345")));
    }
}
