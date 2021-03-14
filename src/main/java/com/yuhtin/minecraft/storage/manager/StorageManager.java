package com.yuhtin.minecraft.storage.manager;

import com.google.common.collect.Maps;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotId;
import com.yuhtin.minecraft.storage.api.storage.PlotStorage;
import com.yuhtin.minecraft.storage.sql.dao.StorageDAO;
import lombok.Data;
import lombok.Getter;

import java.util.Map;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@Data
public final class StorageManager {

    @Getter private static final StorageManager instance = new StorageManager();

    private StorageDAO storageDAO;
    private final Map<PlotId, PlotStorage> plots = Maps.newHashMap();

    public void init() {
        storageDAO.createTable();
    }

    public PlotStorage getByPlot(Plot plot) {

        PlotStorage storage = plots.getOrDefault(plot.getId(), null);
        if (storage == null) {

            storage = storageDAO.selectOne(plot.getId());
            if (storage == null) {

                storage = new PlotStorage();
                insertOne(plot.getId(), storage);

            }

            addAccount(plot.getId(), storage);

        }

        return storage;
    }

    public void insertOne(PlotId plotId, PlotStorage account) {
        addAccount(plotId, account);
        storageDAO.insertOne(plotId, account);
    }

    public void purge(PlotId plotId, boolean clear) {

        PlotStorage storage = plots.getOrDefault(plotId, null);
        if (storage == null) return;

        storageDAO.insertOne(plotId, storage);
        if (clear) plots.remove(plotId);
    }

    public void purgeAll() {
        plots.keySet().forEach($ -> purge($, false));
    }

    public void addAccount(PlotId plotId, PlotStorage account) {
        if (!plots.containsKey(plotId)) {
            plots.put(plotId, account);
        }
    }

}
