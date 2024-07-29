package net.fameless.levelBorder.language;

import net.fameless.levelBorder.LevelBorderPlugin;
import net.fameless.levelBorder.util.ItemBuilder;
import net.fameless.levelBorder.util.Skull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class LanguageCommand implements CommandExecutor, Listener, InventoryHolder {

    private final LevelBorderPlugin levelBorderPlugin;

    public LanguageCommand(LevelBorderPlugin levelBorderPlugin) {
        this.levelBorderPlugin = levelBorderPlugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Lang.getCaption("not-a-player"));
            return false;
        }
        player.openInventory(getLanguageInventory());
        return false;
    }

    private @NotNull Inventory getLanguageInventory() {
        Inventory gui = Bukkit.createInventory(this, 9, Lang.getCaption("adjust-language"));
        gui.setItem(0, ItemBuilder.buildItem(Skull.FLAG_UK.asItemStack(), Component.text("English", NamedTextColor.GOLD), false,
                Component.text("Click to set the language to english", NamedTextColor.GRAY)));
        gui.setItem(1, ItemBuilder.buildItem(Skull.FLAG_GERMANY.asItemStack(), Component.text("Deutsch", NamedTextColor.GOLD), false,
                Component.text("Klicke, um die Sprache auf deutsch zu stellen", NamedTextColor.GRAY)));
        return gui;
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(@NotNull InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof LanguageCommand)) return;
        event.setCancelled(true);

        switch (event.getSlot()) {
            case 0: {
                if (Lang.getLanguage() == Language.ENGLISH) {
                    event.getWhoClicked().sendMessage(Lang.getCaption("prefix") + ChatColor.RED + "The language is already set to english!");
                    return;
                }
                levelBorderPlugin.getConfig().set("lang", "en");
                levelBorderPlugin.saveConfig();
                Lang.loadLanguage(levelBorderPlugin);
                Bukkit.broadcastMessage(Lang.getCaption("prefix") + ChatColor.GREEN + "Language has been updated to english.");
                break;
            }
            case 1: {
                if (Lang.getLanguage() == Language.GERMAN) {
                    event.getWhoClicked().sendMessage(Lang.getCaption("prefix") + ChatColor.RED + "Die Sprache ist bereits auf Deutsch eingestellt.");
                    return;
                }
                levelBorderPlugin.getConfig().set("lang", "de");
                levelBorderPlugin.saveConfig();
                Lang.loadLanguage(levelBorderPlugin);
                Bukkit.broadcastMessage(Lang.getCaption("prefix") + ChatColor.GREEN + "Die Sprache wurde auf deutsch gesetzt.");
                break;
            }
        }
        levelBorderPlugin.getGameManager().sendActionbar();
        event.getWhoClicked().openInventory(getLanguageInventory());
    }

    @Override
    public @NotNull Inventory getInventory() {
        return getLanguageInventory();
    }
}
