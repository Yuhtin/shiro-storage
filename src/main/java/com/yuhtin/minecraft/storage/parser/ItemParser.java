package com.yuhtin.minecraft.storage.parser;

import com.yuhtin.minecraft.storage.api.item.StorageItem;
import com.yuhtin.minecraft.storage.utils.ColorUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
public class ItemParser {

    public List<StorageItem> parseItemCollection(ConfigurationSection section) {

        List<StorageItem> items = new ArrayList<>();
        for (String key : section.getKeys(false)) {

            items.add(
                    parseItemSection(section.getConfigurationSection(key))
            );

        }

        return items;
    }

    public StorageItem parseItemSection(ConfigurationSection section) {

        return StorageItem.builder()
                .material(Material.getMaterial(section.getString("material")))
                .name(ColorUtils.colored(section.getString("itemName")))
                .price(section.getDouble("unitPrice"))
                .build();
    }
}
