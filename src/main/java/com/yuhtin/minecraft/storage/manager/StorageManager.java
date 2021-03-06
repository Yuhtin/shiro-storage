package com.yuhtin.minecraft.storage.manager;

import com.google.common.collect.Maps;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.PlotId;
import com.yuhtin.minecraft.storage.api.storage.PlotStorage;
import com.yuhtin.minecraft.storage.sql.dao.StorageDAO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.UUID;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
@RequiredArgsConstructor
public final class StorageManager {

    private final Map<PlotId, PlotStorage> plots = Maps.newHashMap();

    private final StorageDAO storageDAO;

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

    public void purge(PlotId plotId) {

        PlotStorage storage = plots.getOrDefault(plotId, null);
        if (storage == null) return;

        storageDAO.insertOne(plotId, storage);
        plots.remove(plotId);
    }

    public void addAccount(PlotId plotId, PlotStorage account) {
        if (!plots.containsKey(plotId)) {
            plots.put(plotId, account);
        }
    }

}
