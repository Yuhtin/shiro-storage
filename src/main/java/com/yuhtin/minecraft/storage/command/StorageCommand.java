package com.yuhtin.minecraft.storage.command;

import com.intellectualcrafters.plot.object.Location;
import com.intellectualcrafters.plot.object.Plot;
import com.yuhtin.minecraft.storage.inventory.StorageInventory;
import com.yuhtin.minecraft.storage.manager.StorageManager;
import com.yuhtin.minecraft.storage.utils.ColorUtils;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.entity.Player;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
public class StorageCommand {

    @Command(
            name = "armazem",
            aliases = {"storage"},
            target = CommandTarget.PLAYER
    )
    public void onStorageCommand(Context<Player> context) {

        Player sender = context.getSender();
        org.bukkit.Location location = sender.getLocation();

        Location plotLocation = new Location(
                location.getWorld().getName(),
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ()
        );

        Plot plot = Plot.getPlot(plotLocation);
        if (plot == null) return;

        if (!plot.getOwners().contains(sender.getUniqueId()) && !plot.getTrusted().contains(sender.getUniqueId())) {

            sender.sendMessage(ColorUtils.colored(
                    "&cVocê não tem permissão neste plot."
            ));
            return;

        }

        StorageInventory storageInventory = new StorageInventory(StorageManager.getInstance().getByPlot(plot)).init();
        storageInventory.openInventory(sender);

    }

}
