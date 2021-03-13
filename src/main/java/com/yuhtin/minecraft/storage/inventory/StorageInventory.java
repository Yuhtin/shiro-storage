package com.yuhtin.minecraft.storage.inventory;

import com.google.common.collect.Lists;
import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.paged.PagedInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.item.supplier.InventoryItemSupplier;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import com.henryfabio.minecraft.inventoryapi.viewer.configuration.border.Border;
import com.henryfabio.minecraft.inventoryapi.viewer.configuration.impl.ViewerConfigurationImpl;
import com.henryfabio.minecraft.inventoryapi.viewer.impl.paged.PagedViewer;
import com.yuhtin.minecraft.storage.Storage;
import com.yuhtin.minecraft.storage.api.item.StorageItem;
import com.yuhtin.minecraft.storage.api.storage.PlotStorage;
import com.yuhtin.minecraft.storage.manager.ItemManager;
import com.yuhtin.minecraft.storage.utils.ColorUtils;
import com.yuhtin.minecraft.storage.utils.ItemBuilder;
import com.yuhtin.minecraft.storage.utils.MathUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
public class StorageInventory extends PagedInventory {

    private static final String sellCommand = Storage.getInstance().getConfig().getString("sellCommand");

    private final PlotStorage plotStorage;

    public StorageInventory(PlotStorage plotStorage) {

        super(
                "storage.main",
                "Seu armazem",
                3 * 9
        );

        this.plotStorage = plotStorage;

    }

    @Override
    protected void configureViewer(PagedViewer viewer) {

        ViewerConfigurationImpl.Paged pagedViewer = viewer.getConfiguration();

        pagedViewer.itemPageLimit(5);
        pagedViewer.border(Border.of(1, 1, 2, 1));

        pagedViewer.nextPageSlot(17);
        pagedViewer.emptyPageSlot(13);
        pagedViewer.previousPageSlot(9);

    }

    @Override
    protected void configureInventory(Viewer viewer, InventoryEditor editor) {

        double items = plotStorage.getStorage().values().stream().reduce(0d, StorageInventory::add);

        editor.setItem(0, InventoryItem.of(new ItemBuilder("MFH_Beacon")
                .name("&aInformações")
                .lore("", " &fItems armazenados: " + MathUtils.format(items))
                .result())
        );

    }

    @Override
    protected List<InventoryItemSupplier> createPageItems(PagedViewer viewer) {

        List<InventoryItemSupplier> items = Lists.newArrayList();
        for (Map.Entry<Material, Double> entry : plotStorage.getStorage().entrySet()) {
            items.add(() -> makeItem(entry.getKey(), entry.getValue()));
        }

        return items;

    }

    public InventoryItem makeItem(Material material, double items) {

        StorageItem storageItem = ItemManager.getInstance().getItems().get(material);
        return InventoryItem.of(new ItemBuilder(material)
                .name("&2" + storageItem.getName())
                .lore(
                        "",
                        " &fItens armazenados: &a" + MathUtils.format(items),
                        " &fPreço unitário: &a" + MathUtils.format(storageItem.getPrice()),
                        "",
                        " &aClique esquerdo para vender &ftudo &apor &f" + MathUtils.format(storageItem.getPrice() * items) + " coins&a.",
                        " &aClique direito para coletar &f64 itens&a.",
                        ""
                )
                .result()
        ).defaultCallback(callback -> {

            Player player = callback.getPlayer();

            if (callback.getClickType().isLeftClick()) {

                double price = storageItem.getPrice() * storageItem.getPrice();

                Bukkit.dispatchCommand(
                        Bukkit.getConsoleSender(),
                        sellCommand.replace("@player", player.getName())
                                .replace("@coins", String.valueOf(price))
                );

                plotStorage.getStorage().replace(material, 0d);
                player.sendMessage(ColorUtils.colored(
                        "&aVocê vendeu &f" + MathUtils.format(items) + " &apor &f" + price + " coins&a."
                ));

            } else {

                if (items <= 64) {

                    player.sendMessage(ColorUtils.colored("&cVocê não tem 64 itens para coletar."));
                    return;

                }

                plotStorage.getStorage().replace(material, items - 64);

                player.getInventory().addItem(new ItemBuilder(material).setAmount(64).result());
                player.sendMessage(ColorUtils.colored(
                        "&aVocê coletou 64 itens com sucesso."
                ));


            }

        });

    }

    public static double add(double a, double b) {
        return a + b;
    }

}
