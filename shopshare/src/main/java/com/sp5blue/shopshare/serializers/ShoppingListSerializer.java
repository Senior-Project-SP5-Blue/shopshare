package com.sp5blue.shopshare.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.sp5blue.shopshare.models.listitem.ListItem;
import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;
import com.sp5blue.shopshare.models.shoppinglist.ShoppingList;

import java.io.IOException;

public class ShoppingListSerializer extends StdSerializer<ShoppingList> {

    public ShoppingListSerializer() {
        this(null);
    }
    protected ShoppingListSerializer(Class<ShoppingList> t) {
        super(t);
    }
    @Override
    public void serialize(ShoppingList shoppingList, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", shoppingList.getId().toString());
        jsonGenerator.writeStringField("name", shoppingList.getName());
        serializerProvider.defaultSerializeField("modifiedOn", shoppingList.getModifiedOn(), jsonGenerator);
        jsonGenerator.writeFieldName("items");
        jsonGenerator.writeStartArray();
        for (ListItem item : shoppingList.getItems()) {
            serializerProvider.defaultSerializeValue(item, jsonGenerator);

        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();

    }
}
