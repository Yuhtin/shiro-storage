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

        System.out.println("Test 1");

        org.bukkit.Location location = event.getLocation();

        Location plotLocation = new Location(
                location.getWorld().getName(),
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ()
        );

        Plot plot = Plot.getPlot(plotLocation);
        if (plot == null) return;

        System.out.println("Test 2");

        event.setCancelled(true);

        PlotStorage plotStorage = StorageManager.getInstance().getByPlot(plot);

        double items = plotStorage.getStorage().get(itemStack.getType());
        plotStorage.getStorage().replace(itemStack.getType(), items + itemStack.getAmount());

        itemStack.setType(Material.AIR);
    }

}
