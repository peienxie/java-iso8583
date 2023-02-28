package com.peienxie.iso8583;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import com.peienxie.iso8583.codec.AlphaDecoder;
import com.peienxie.iso8583.codec.NumericDecoder;
import com.peienxie.iso8583.util.StringUtils;
import org.junit.Test;

public class ISO8583MessageParserTest {

    @Test(expected = IllegalStateException.class)
    public void testParse_shouldThrow_whenDecoderIsMissing() {
        // given
        byte[] bytes = StringUtils.hexStrToBytes("601234567802003000000000000000");
        ISO8583MessageParser parser = new ISO8583MessageParser(true, true);

        // when
        parser.parse(bytes);

        fail("Should throw exception when parsing");
    }

    @Test
    public void testParseOK() {
        // given
        byte[] bytes = StringUtils.hexStrToBytes("601234567802003000000000C000001200000000001234003132333435363738303132333435363738393031323334");
        ISO8583MessageParser parser = new ISO8583MessageParser(true, true);
        parser.addField(3, ISO8583Field.response(new NumericDecoder(3)));
        parser.addField(4, ISO8583Field.response(new NumericDecoder(6)));
        parser.addField(41, ISO8583Field.response(new AlphaDecoder(8)));
        parser.addField(42, ISO8583Field.response(new AlphaDecoder(15)));

        // when
        ISO8583Message msg = parser.parse(bytes);

        // then
        assertThat(msg.getTPDU().getDestinationAddress(), is(0x1234));
        assertThat(msg.getTPDU().getSourceAddress(), is(0x5678));
        assertThat(msg.getMTI().getValue(), is(0x0200));
        assertThat(msg.getField(3).getData(), is(120000));
        assertThat(msg.getField(4).getData(), is(123400));
        assertThat(msg.getField(41).getData(), is("12345678"));
        assertThat(msg.getField(42).getData(), is("012345678901234"));
    }
}
