package com.sp5blue.shopshare.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.sp5blue.shopshare.models.listitem.ListItem;
import com.sp5blue.shopshare.models.shoppergroup.ShopperGroup;

import java.io.IOException;

public class ListItemSerializer extends StdSerializer<ListItem> {

    public ListItemSerializer() {
        this(null);
    }
    protected ListItemSerializer(Class<ListItem> t) {
        super(t);
    }
    @Override
    public void serialize(ListItem listItem, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", listItem.getId().toString());
        jsonGenerator.writeStringField("name", listItem.getName());
        jsonGenerator.writeStringField("status", listItem.getStatus().name());
        jsonGenerator.writeStringField("createdBy", listItem.getCreatedBy().getId().toString());
        jsonGenerator.writeBooleanField("locked", listItem.isLocked());
        serializerProvider.defaultSerializeField("createdOn", listItem.getCreatedOn(), jsonGenerator);
        jsonGenerator.writeEndObject();
    }
}
