package com.kingdom.system.jsonserializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.kingdom.system.util.DateUtil;

import java.io.IOException;
import java.util.Date;

public class DateForOrderSerializer extends JsonSerializer {

    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        final Date date = (Date) o;
        jsonGenerator.writeString(DateUtil.format(date, "yyyy-MM-dd HH:mm:ss"));
    }
}