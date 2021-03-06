package com.yuhtin.minecraft.storage.listener;

import com.intellectualcrafters.plot.object.Location;
import com.intellectualcrafters.plot.object.Plot;
import com.yuhtin.minecraft.storage.api.storage.PlotStorage;
import com.yuhtin.minecraft.storage.manager.ItemManager;
import com.yuhtin.minecraft.storage.manager.StorageManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
public class PlotReceiveItemListener implements Listener {

    @EventHandler
    public void itemSpawn(ItemSpawnEvent event) {
        ItemStack itemStack = event.getEntity().getItemStack();

        boolean validItem = ItemManager.getInstance().getItems().containsKey(itemStack.getType());
        if (!validItem) return;

        org.bukkit.Location location = event.getLocation();

        Location plotLocation = new Location(
                location.getWorld().getName(),
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ()
        );

        Plot plot = Plot.getPlot(plotLocation);
        if (plot == null) return;

        event.setCancelled(true);

        PlotStorage plotStorage = StorageManager.getInstance().getByPlot(plot);
        plotStorage.getStorage().merge(itemStack.getType(), (double) itemStack.getAmount(), Double::sum);

        itemStack.setType(Material.AIR);
    }

}
