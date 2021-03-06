package com.yuhtin.minecraft.storage.manager;

import com.google.common.collect.Maps;
import com.plotsquared.core.PlotSquared;
import com.plotsquared.core.plot.Plot;
import lombok.Getter;

import java.util.Map;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
public class StorageManager {

    @Getter private static final StorageManager instance = new StorageManager();
    private final Map<Plot, PlotStorage> plots = Maps.newHashMap();

}
