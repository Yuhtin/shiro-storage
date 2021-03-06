package com.yuhtin.minecraft.storage.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yuhtin.minecraft.storage.api.storage.PlotStorage;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
public class Serializer {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static String serialize(PlotStorage data) {
        return GSON.toJson(data);
    }

    public static PlotStorage deserialize(String data) {
        return GSON.fromJson(data, PlotStorage.class);
    }

}
