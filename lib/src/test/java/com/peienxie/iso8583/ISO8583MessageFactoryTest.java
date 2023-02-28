package com.peienxie.iso8583;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.peienxie.iso8583.codec.AlphaEncoder;
import com.peienxie.iso8583.codec.AmountEncoder;
import com.peienxie.iso8583.codec.FieldEncoder;
import com.peienxie.iso8583.codec.NumericEncoder;
import com.peienxie.iso8583.type.MTI;
import com.peienxie.iso8583.type.TPDU;
import com.peienxie.iso8583.util.StringUtils;
import org.junit.Before;
import org.junit.Test;

public class ISO8583MessageFactoryTest {

    private ISO8583MessageFactory saleFactory;

    @Before
    public void setup() {
        TPDU tpdu = TPDU.of(0x1234, 0);
        MTI mti = MTI.of(0x0200);
        Map<Integer, ISO8583Field<?>> defaultFields = new HashMap<>();
        defaultFields.put(3, ISO8583Field.request(0, new NumericEncoder(3)));

        LocalDateTime now = LocalDateTime.of(2022, 10, 11, 21, 22, 23);
        FieldEncoder<LocalDateTime> dateEncoder = new FieldEncoder<LocalDateTime>() {
            @Override
            public int getEncodeLength() {
                return 2;
            }

            @Override
            public byte[] encode(LocalDateTime data) {
                return StringUtils.hexStrToBytes(data.format(DateTimeFormatter.ofPattern("MMdd")));
            }
        };
        FieldEncoder<LocalDateTime> timeEncoder = new FieldEncoder<LocalDateTime>() {
            @Override
            public int getEncodeLength() {
                return 3;
            }

            @Override
            public byte[] encode(LocalDateTime data) {
                return StringUtils.hexStrToBytes(data.format(DateTimeFormatter.ofPattern("HHmmss")));
            }
        };
        defaultFields.put(12, ISO8583Field.request(now, dateEncoder));
        defaultFields.put(13, ISO8583Field.request(now, timeEncoder));
        defaultFields.put(24, ISO8583Field.request("1234", new FieldEncoder<String>() {
            @Override
            public int getEncodeLength() {
                return 2;
            }

            @Override
            public byte[] encode(String data) {
                return StringUtils.hexStrToBytes(data);
            }
        }));
        defaultFields.put(41, ISO8583Field.request("87654321", new AlphaEncoder(8)));
        defaultFields.put(42, ISO8583Field.request("123455432112345", new AlphaEncoder(15)));

        saleFactory = new ISO8583MessageFactory(tpdu, mti, defaultFields);
    }

    @Test
    public void createSaleIsoMessageWithFactory() {
        // given
        int amount = 100;
        ISO8583Message sale = saleFactory.make(
                msg -> msg.addField(4, amount, new AmountEncoder())
                        .addField(11, 123456, new NumericEncoder(3))
        );

        // when
        byte[] encode = sale.encode();

        // then
        assertThat(StringUtils.bytesToHexStr(encode), is(
                "601234000002003038010000C00000000000000000010000123456101121222312343837363534333231313233343535343332313132333435"
        ));
    }

}