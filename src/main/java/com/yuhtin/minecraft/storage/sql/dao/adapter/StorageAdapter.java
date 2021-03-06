package com.yuhtin.minecraft.storage.sql.dao.adapter;

import com.henryfabio.sqlprovider.executor.adapter.SQLResultAdapter;
import com.henryfabio.sqlprovider.executor.result.SimpleResultSet;
import com.yuhtin.minecraft.storage.utils.Serializer;
import com.yuhtin.minecraft.storage.api.storage.PlotStorage;

public final class StorageAdapter implements SQLResultAdapter<PlotStorage> {

    @Override
    public PlotStorage adaptResult(SimpleResultSet resultSet) {
        return Serializer.deserialize(resultSet.get("storage"));
    }

}
