package com.sp5blue.shopshare.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.sp5blue.shopshare.models.user.User;
import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;
import com.sp5blue.shopshare.models.shoppinglist.ShoppingList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ShopperGroupSerializer extends StdSerializer<ShopperGroup> {

    private final Logger logger = LoggerFactory.getLogger(UserSerializer.class);

    public ShopperGroupSerializer() {
        this(null);
    }
    protected ShopperGroupSerializer(Class<ShopperGroup> t) {
        super(t);
    }

    @Override
    public void serialize(ShopperGroup shopperGroup, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", shopperGroup.getId().toString());
        jsonGenerator.writeStringField("name", shopperGroup.getName());
        jsonGenerator.writeFieldName("users");
        jsonGenerator.writeStartArray();
        for (User user : shopperGroup.getUsers()) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("id", user.getId().toString());
            jsonGenerator.writeStringField("username", user.getUsername());
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeFieldName("lists");
        jsonGenerator.writeStartArray();
        for (ShoppingList list : shopperGroup.getLists()) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("id", list.getId().toString());
            jsonGenerator.writeStringField("name", list.getName());
            jsonGenerator.writeEndObject();
//            jsonGenerator.writeString(list.getUsername());
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeStringField("createdBy", shopperGroup.getAdmin().getUsername());
        jsonGenerator.writeEndObject();
    }
}
