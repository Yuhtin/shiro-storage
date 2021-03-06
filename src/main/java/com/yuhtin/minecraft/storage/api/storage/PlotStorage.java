package com.yuhtin.minecraft.storage.api.storage;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@Data
public class PlotStorage {

    private final Map<Material, Double> storage = Maps.newHashMap();
    private final List<String> friends = Lists.newArrayList();

}
