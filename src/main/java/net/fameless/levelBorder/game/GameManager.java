package net.fameless.levelBorder.game;

import net.fameless.levelBorder.LevelBorderPlugin;
import net.fameless.levelBorder.language.Lang;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class GameManager implements Listener {

    private final LevelBorderPlugin levelBorderPlugin;
    private int borderSize = 0;
    private int startSize;
    private int time;
    private boolean running;

    public GameManager(LevelBorderPlugin levelBorderPlugin, int startSize, int time) {
        this.levelBorderPlugin = levelBorderPlugin;
        this.startSize = startSize;
        this.time = time;
        runTimerTask();
        runGameTask();
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        if (!event.getPlayer().hasPermission(levelBorderPlugin.getCommand("levelborder").getPermission())) return;
        if (!levelBorderPlugin.getConfig().getBoolean("has-played", false)) {
            event.getPlayer().sendMessage(Lang.getCaption("join-message"));
            levelBorderPlugin.getConfig().set("has-played", true);
            levelBorderPlugin.saveConfig();
        }
    }

    private void runTimerTask() {
        Bukkit.getScheduler().runTaskTimer(levelBorderPlugin, () -> {
            sendActionbar();
            if (running) {
                time++;
            }
        }, 20, 20);
    }

    private void runGameTask() {
        Bukkit.getScheduler().runTaskTimer(levelBorderPlugin, () -> {
            if (Bukkit.getOnlinePlayers().isEmpty()) return;
            if (!running) return;

            int newSize = startSize;
            for (Player player : Bukkit.getOnlinePlayers()) {
                newSize += player.getLevel();
            }

            if (borderSize != newSize) {
                borderSize = newSize;

                for (World world : Bukkit.getServer().getWorlds()) {
                    world.getWorldBorder().setSize(borderSize);
                }
                sendActionbar();
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (isOutsideOfBorder(player)) {
                    player.teleport(player.getWorld().getSpawnLocation());
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 20);
                }
            }
        }, 1, 1);
    }

    public void sendActionbar() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (running) {
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Lang.getCaption("timer-running")));
            } else {
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Lang.getCaption("timer-paused")));
            }
        }
    }

    private boolean isOutsideOfBorder(@NotNull Player player) {
        Location loc = player.getLocation();
        WorldBorder border = player.getWorld().getWorldBorder();
        double size = border.getSize() / 2 + 0.5;
        Location center = border.getCenter();
        double x = loc.getX() - center.getX(), z = loc.getZ() - center.getZ();
        return ((x > size || (-x) > size) || (z > size || (-z) > size));
    }

    private void resetBorders() {
        for (World world : Bukkit.getServer().getWorlds()) {
            world.getWorldBorder().reset();
            world.getWorldBorder().setCenter(world.getSpawnLocation());
        }
    }

    public void handleStart() {
        running = true;
    }

    public void handlePause() {
        running = false;
        resetBorders();
    }

    public void handleShutdown() {
        running = false;
        resetBorders();

        levelBorderPlugin.getConfig().set("timer.time", time);
        levelBorderPlugin.getConfig().set("start-size", startSize);
        levelBorderPlugin.saveConfig();
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isRunning() {
        return running;
    }

    public int getBorderSize() {
        return borderSize;
    }

    public void setStartSize(int startSize) {
        this.startSize = startSize;
        levelBorderPlugin.getConfig().set("start-size", startSize);
        levelBorderPlugin.saveConfig();
    }
}
