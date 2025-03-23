package com.powerManager.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.powerManager.dto.Test;

import java.io.IOException;

public class TestSerializer extends JsonSerializer<Test> {

    @Override
    public void serialize(Test test, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", test.getId());
        jsonGenerator.writeStringField("lastName", test.getField());
        jsonGenerator.writeEndObject();
    }
}
