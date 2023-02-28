# java-iso8583

This is a Java library for working with ISO8583 messages.

It provides classes for encoding and decoding ISO8583 fields, and for building and parsing ISO8583 messages.

## How to use

### Encoding and Decoding Fields

To encode and decode ISO8583 fields, you can use the ISO8583Field class. Here are some example:

- create a field with data `"123456789"` in the `a` (alphabet) of ISO8583 format with fixed length 12.

```
ISO8583Field<String> field = ISO8583Field.request("123456789", new AlphaEncoder(12));
byte[] encoded = field.encode(); // output: "123456789   " in byte array (note there are trailing spaces) 
```

- create a field with data `100` in the `n` (numeric) ISO8583 format with fixed length 3.

```
ISO8583Field<Integer> field = ISO8583Field.request(100, new NumericEncoder(3));
byte[] encoded = field.encode(); // output: [0x00 0x01 0x00]
```

### Building and Parsing Messages

To build and parse ISO8583 messages, you can use the `ISO8583MessageFactory` and `ISO8583MessageParser` classes.
Here's an example:

```
TPDU tpdu = TPDU.of(0x1234);
MTI mti = MTI.of(0x0200);
// create a default field for this factory
Map<Integer, ISO8583Field<?>> defaultFields = new HashMap<>();
defaultFields.put(3, ISO8583Field.request(0, new NumericEncoder(3)));
ISO8583MessageFactory factory = new ISO8583MessageFactory(tpdu, mti, defaultFields);

// create a new iso8583 message using this factory.
// you can optionally add new fields when creating the message
byte[] messageBytes = builder.make(msg -> {
    msg.addField(4, ISO8583Field.request(100, new AmountEncoder()));
);

ISO8583MessageParser parser = new ISO8583MessageParser(true, true);
ISO8583Message message = parser.parse(messageBytes);
TPDU tpdu = message.getTPDU();
MTI mti = message.getMTI();
int field3Data = message.getFieldData(3);
int field4Data = message.getFieldData(4);
```

### Building the Library

To build the ISO8583 library from source code, you can use Gradle. Here's how:

1. Clone this repository:

2. Build the library `./gradlew build`

3. The library JAR file will be located in the build/libs directory.

4. You can include it in your project as a local dependency:

```
dependencies {
    implementation files('path/to/java-iso8583-0.1.0.jar')
}
```


