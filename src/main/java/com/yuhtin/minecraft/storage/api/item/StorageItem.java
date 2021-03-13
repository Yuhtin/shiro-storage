package com.yuhtin.minecraft.storage.api.item;

import lombok.Builder;
import lombok.Data;
import org.bukkit.Material;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@Data
@Builder
public class StorageItem {

    private final Material material;
    private final String name;
    private final double price;

}
