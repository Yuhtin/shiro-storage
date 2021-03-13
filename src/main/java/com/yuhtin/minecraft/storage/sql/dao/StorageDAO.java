package com.yuhtin.minecraft.storage.sql.dao;

import com.henryfabio.sqlprovider.executor.SQLExecutor;
import com.intellectualcrafters.plot.object.PlotId;
import com.yuhtin.minecraft.storage.utils.Serializer;
import com.yuhtin.minecraft.storage.api.storage.PlotStorage;
import com.yuhtin.minecraft.storage.sql.dao.adapter.StorageAdapter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class StorageDAO {

    private final String TABLE = "storage_data";

    private final SQLExecutor sqlExecutor;

    public void createTable() {
        sqlExecutor.updateQuery("CREATE TABLE IF NOT EXISTS " + TABLE + "(" +
                "plotId CHAR(20) NOT NULL PRIMARY KEY," +
                "storage TEXT NOT NULL" +
                ");"
        );
    }

    public PlotStorage selectOne(PlotId plot) {
        return sqlExecutor.resultOneQuery(
                "SELECT * FROM " + TABLE + " WHERE plotId = ?",
                statement -> statement.set(1, plot.toString()),
                StorageAdapter.class
        );
    }

    public void insertOne(PlotId plotId, PlotStorage storage) {
        sqlExecutor.updateQuery(
                "REPLACE INTO " + TABLE + " VALUES(?,?);",
                statement -> {
                    statement.set(1, plotId.toString());
                    statement.set(2, Serializer.serialize(storage));
                }
        );
    }

}
