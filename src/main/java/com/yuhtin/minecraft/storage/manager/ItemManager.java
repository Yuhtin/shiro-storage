package com.yuhtin.minecraft.storage.manager;

import com.google.common.collect.Maps;
import com.yuhtin.minecraft.storage.api.item.StorageItem;
import lombok.Getter;
import org.bukkit.Material;

import java.util.Map;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@Getter
public class ItemManager {

    @Getter private static final ItemManager instance = new ItemManager();

    private final Map<Material, StorageItem> items = Maps.newHashMap();

    public void addItem(StorageItem item) {
        items.put(item.getMaterial(), item);
    }

}
