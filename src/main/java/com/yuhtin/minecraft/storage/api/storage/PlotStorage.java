package com.yuhtin.minecraft.storage.api.storage;

import com.google.common.collect.Maps;
import lombok.Data;
import org.bukkit.Material;

import java.util.Map;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@Data
public class PlotStorage {

    private final Map<Material, Double> storage = Maps.newHashMap();
    private double coinsTotal;

}
