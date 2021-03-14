package com.yuhtin.minecraft.storage;

import com.henryfabio.minecraft.inventoryapi.manager.InventoryManager;
import com.henryfabio.sqlprovider.connector.SQLConnector;
import com.henryfabio.sqlprovider.executor.SQLExecutor;
import com.yuhtin.minecraft.storage.command.StorageCommand;
import com.yuhtin.minecraft.storage.listener.PlotReceiveItemListener;
import com.yuhtin.minecraft.storage.manager.ItemManager;
import com.yuhtin.minecraft.storage.manager.StorageManager;
import com.yuhtin.minecraft.storage.parser.ItemParser;
import com.yuhtin.minecraft.storage.sql.SQLProvider;
import com.yuhtin.minecraft.storage.sql.dao.StorageDAO;
import me.bristermitten.pdm.PluginDependencyManager;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import me.saiintbrisson.minecraft.command.message.MessageType;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Storage extends JavaPlugin {

    private SQLConnector sqlConnector;
    private SQLExecutor sqlExecutor;

    private StorageDAO storageDAO;

    public static Storage getInstance() { return getPlugin(Storage.class); }

    @Override
    public void onLoad() {
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {

        PluginDependencyManager.of(this).loadAllDependencies().thenRun(() -> {

            PluginManager pluginManager = Bukkit.getPluginManager();
            try {

                InventoryManager.enable(this);

                sqlConnector = SQLProvider.of(this).setup();
                sqlExecutor = new SQLExecutor(sqlConnector);

                storageDAO = new StorageDAO(sqlExecutor);

                StorageManager storageManager = StorageManager.getInstance();

                storageManager.setStorageDAO(storageDAO);
                storageManager.init();

                BukkitFrame bukkitFrame = new BukkitFrame(this);
                bukkitFrame.registerCommands(new StorageCommand());

                PlotReceiveItemListener plotReceiveItemListener = new PlotReceiveItemListener();
                pluginManager.registerEvents(plotReceiveItemListener, this);

                ItemParser itemParser = new ItemParser();
                ItemManager itemManager = ItemManager.getInstance();

                itemParser.parseItemCollection(getConfig().getConfigurationSection("items")).forEach(itemManager::addItem);

            } catch (Throwable t) {
                t.printStackTrace();
                getLogger().severe("Ocorreu um erro durante a inicialização do plugin!");
                pluginManager.disablePlugin(this);
            }

        });

    }

    @Override
    public void onDisable() {
        StorageManager.getInstance().purgeAll();
    }
}
