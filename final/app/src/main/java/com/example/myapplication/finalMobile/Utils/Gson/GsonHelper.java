package com.example.myapplication.finalMobile.Utils.Gson;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class GsonHelper {

    static Gson gson;

    public static Gson getGson() {
        if (gson == null)
            gson = new Gson();
        return gson;
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return getGson().fromJson(json, typeOfT);
    }

    public static String toJson(Object src) {
        return getGson().toJson(src);
    }
}
