package com.vaqueras.util;

import java.lang.reflect.Type; 
import java.time.LocalDate;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
// mi clase para adaptar LocalDate a Gson y viceversa 
public class LocalDateAdapter implements JsonDeserializer<LocalDate>, JsonSerializer<LocalDate> {
    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {
    return LocalDate.parse(json.getAsString()); // convierte "2000-01-01" en LocalDate
    }

    @Override
    public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString()); // convierte LocalDate a "2000-01-01"
    }
}
