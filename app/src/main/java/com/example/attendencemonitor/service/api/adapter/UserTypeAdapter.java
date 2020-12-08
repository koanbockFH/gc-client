package com.example.attendencemonitor.service.api.adapter;

import com.example.attendencemonitor.service.model.UserType;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Adapter for de- and serialization of User Type enum to integer / string
 */
public class UserTypeAdapter implements JsonSerializer<UserType>, JsonDeserializer<UserType>
{
    @Override
    public UserType deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException
    {
        int key = element.getAsInt();
        return UserType.fromKey(key);
    }

    @Override
    public JsonElement serialize(UserType src, Type typeOfSrc, JsonSerializationContext context)
    {
        if(src != null)
        {
            return new JsonPrimitive(src.getKey());
        }
        return new JsonPrimitive(-1);
    }
}
