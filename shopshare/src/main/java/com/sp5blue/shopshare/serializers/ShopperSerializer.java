package com.sp5blue.shopshare.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.sp5blue.shopshare.models.shopper.Shopper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ShopperSerializer extends StdSerializer<Shopper> {

    public ShopperSerializer() {
        this(null);
    }
    protected ShopperSerializer(Class<Shopper> t) {
        super(t);
    }

    private final Logger logger = LoggerFactory.getLogger(ShopperSerializer.class);

    @Override
    public void serialize(Shopper shopper, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", shopper.getId().toString());
        jsonGenerator.writeStringField("firstName", shopper.getFirstName());
        jsonGenerator.writeStringField("lastName", shopper.getLastName());
        jsonGenerator.writeStringField("username", shopper.getUsername());
        jsonGenerator.writeStringField("profilePicture", shopper.getProfilePicture());
        jsonGenerator.writeStringField("email", shopper.getEmail());
        jsonGenerator.writeEndObject();
    }
}
