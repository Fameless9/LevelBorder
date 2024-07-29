package net.fameless.levelBorder.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemBuilder {

    @Contract("_, _, _, _ -> param1")
    public static @NotNull ItemStack buildItem(@NotNull ItemStack itemStack, Component name, boolean applyFlags, Component... lore) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return itemStack;
        meta.setDisplayName(LegacyComponentSerializer.legacySection().serialize(name.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)));

        if (applyFlags) Arrays.stream(ItemFlag.values()).forEach(meta::addItemFlags);

        List<String> lores = new ArrayList<>();
        for (Component c : lore) {
            lores.add(LegacyComponentSerializer.legacySection().serialize(c.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)));
        }

        meta.setLore(lores);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
