package com.peienxie.iso8583;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.Arrays;
import java.util.Collection;

import com.peienxie.iso8583.codec.AlphaEncoder;
import com.peienxie.iso8583.codec.NumericEncoder;
import com.peienxie.iso8583.util.StringUtils;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Enclosed.class)
public class ISO8583FieldTest {

    @RunWith(Parameterized.class)
    public static class AlphaFieldTest {
        private final String dataToFormat;
        private final int formatLength;
        private final String expected;

        public AlphaFieldTest(String dataToFormat, int formatLength, String expected) {
            this.dataToFormat = dataToFormat;
            this.formatLength = formatLength;
            this.expected = expected;
        }

        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][]{
                    {"qwer", 2, "qw"},
                    {"qwer", 10, "qwer      "},
                    {"1234", 2, "12"},
                    {"1234", 10, "1234      "},
            });
        }

        @Test
        public void testCreateALPHAField() {
            ISO8583Field<String> requestField = ISO8583Field.request(dataToFormat, new AlphaEncoder(formatLength));
            String actual = StringUtils.bytesToHexStr(requestField.encode());
            String expect = StringUtils.bytesToHexStr(expected.getBytes());
            assertThat(actual, is(expect));
        }

    }

    @RunWith(Parameterized.class)
    public static class NumericFieldTest {
        private final int dataToFormat;
        private final int formatLength;
        private final String expectedHexString;

        public NumericFieldTest(int dataToFormat, int formatLength, String expected) {
            this.dataToFormat = dataToFormat;
            this.formatLength = formatLength;
            this.expectedHexString = expected;
        }

        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][]{
                    {123, 2, "23"},
                    {123, 10, "0000000123"},
                    {12345, 5, "012345"},
                    {12345, 6, "012345"},
                    {12345, 7, "00012345"},
                    {12345, 8, "00012345"},
            });
        }

        @Test
        public void testCreateNumericField() {
            ISO8583Field<?> requestField = ISO8583Field.request(dataToFormat, new NumericEncoder(formatLength));
            String actual = StringUtils.bytesToHexStr(requestField.encode());
            assertThat(actual, is(expectedHexString));
        }
    }
}
