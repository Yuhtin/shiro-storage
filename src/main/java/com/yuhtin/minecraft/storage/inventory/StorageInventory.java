package com.yuhtin.minecraft.storage.inventory;

import com.google.common.collect.Lists;
import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.configuration.InventoryConfiguration;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.paged.PagedInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.item.supplier.InventoryItemSupplier;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import com.henryfabio.minecraft.inventoryapi.viewer.configuration.border.Border;
import com.henryfabio.minecraft.inventoryapi.viewer.configuration.impl.ViewerConfigurationImpl;
import com.henryfabio.minecraft.inventoryapi.viewer.impl.paged.PagedViewer;
import com.intellectualcrafters.plot.object.Plot;
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
    private static final String economyName = Storage.getInstance().getConfig().getString("economyName");

    private final PlotStorage plotStorage;
    private final Plot plot;

    public StorageInventory(Plot plot, PlotStorage plotStorage) {

        super(
                "storage.main",
                "Seu armazem",
                3 * 9
        );

        this.plot = plot;
        this.plotStorage = plotStorage;

        InventoryConfiguration configuration = getConfiguration();
        configuration.secondUpdate(25);

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

        double items = plotStorage.getStorage().values().stream().reduce(0d, Double::sum);

        editor.setItem(0, InventoryItem.of(new ItemBuilder("MFH_Beacon")
                .name("&2Informações do Armazem")
                .lore(
                        "",
                        " &aItens armazenados: &f" + MathUtils.format(items),
                        " &a" + economyName + " adquiridos: &f" + MathUtils.format(plotStorage.getCoinsTotal()) + " " + economyName.toLowerCase(),
                        " &aJogadores permitidos: &f" + (plot.getOwners().size() + plot.getTrusted().size()) + " jogador(es)",
                        ""
                )
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

        int minCollect = (int) Math.min(items, 64);
        return InventoryItem.of(new ItemBuilder(material)
                .name("&2" + storageItem.getName())
                .lore(
                        "",
                        " &fItens armazenados: &a" + MathUtils.format(items),
                        " &fPreço unitário: &a" + MathUtils.format(storageItem.getPrice()),
                        "",
                        " &aClique esquerdo para vender &ftudo &apor &f" + MathUtils.format(storageItem.getPrice() * items) + " " + economyName.toLowerCase() + "&a.",
                        " &aClique direito para coletar &f" + minCollect + " itens&a.",
                        ""
                )
                .result()
        ).defaultCallback(callback -> {

            Player player = callback.getPlayer();
            double currentItems = plotStorage.getStorage().get(material);

            if (callback.getClickType().isLeftClick()) {

                if (currentItems <= 0) {

                    player.sendMessage(ColorUtils.colored(
                            "&cVocê não tem itens para vender."
                    ));
                    return;

                }

                double price = currentItems * storageItem.getPrice();

                Bukkit.dispatchCommand(
                        Bukkit.getConsoleSender(),
                        sellCommand.replace("@player", player.getName())
                                .replace("@coins", String.valueOf(price))
                );

                plotStorage.getStorage().replace(material, 0d);
                plotStorage.setCoinsTotal(plotStorage.getCoinsTotal() + price);

                player.sendMessage(ColorUtils.colored(
                        "&aVocê vendeu &f" + MathUtils.format(currentItems) + " &apor &f" + MathUtils.format(price) + " " + economyName.toLowerCase() + "&a."
                ));

            } else {

                if (minCollect == 0) {

                    player.sendMessage(ColorUtils.colored("&cVocê não tem nenhum item para coletar."));
                    return;

                }

                if (player.getInventory().firstEmpty() == -1) {

                    player.sendMessage(ColorUtils.colored(
                            "&cVocê não tem espaço no inventário para fazer isto."
                    ));
                    return;

                }

                plotStorage.getStorage().replace(material, currentItems - minCollect);

                player.getInventory().addItem(new ItemBuilder(material).setAmount(minCollect).result());
                player.sendMessage(ColorUtils.colored(
                        "&aVocê coletou &f'" + minCollect + " " + storageItem.getName() + "' &ado armazem com sucesso."
                ));

            }

            callback.updateInventory();

        });

    }

    @Override
    protected void update(PagedViewer viewer, InventoryEditor editor) {
        configureInventory(viewer, editor);
    }

}
