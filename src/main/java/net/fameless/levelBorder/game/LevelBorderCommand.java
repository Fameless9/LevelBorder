package net.fameless.levelBorder.game;

import net.fameless.levelBorder.LevelBorderPlugin;
import net.fameless.levelBorder.language.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LevelBorderCommand implements CommandExecutor, TabCompleter {

    private final LevelBorderPlugin levelBorderPlugin;

    public LevelBorderCommand(LevelBorderPlugin levelBorderPlugin) {
        this.levelBorderPlugin = levelBorderPlugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 0) {
            sender.sendMessage(Lang.getCaption("lb-command-usage"));
            return false;
        }
        switch (args[0]) {
            case "start": {
                if (levelBorderPlugin.getGameManager().isRunning()) {
                    sender.sendMessage(Lang.getCaption("challenge-running"));
                    return false;
                }
                levelBorderPlugin.getGameManager().handleStart();
                break;
            }
            case "pause": {
                if (!levelBorderPlugin.getGameManager().isRunning()) {
                    sender.sendMessage(Lang.getCaption("challenge-paused"));
                    return false;
                }
                levelBorderPlugin.getGameManager().handlePause();
                break;
            }
            case "settime": {
                if (args.length < 2) {
                    sender.sendMessage(Lang.getCaption("lb-command-usage"));
                    return false;
                }

                int time;
                try {
                    time = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(Lang.getCaption("time-not-a-number"));
                    return false;
                }
                levelBorderPlugin.getGameManager().setTime(time);
                break;
            }
            case "startsize": {
                if (args.length < 2) {
                    sender.sendMessage(Lang.getCaption("lb-command-usage"));
                    return false;
                }

                int newDefaultSize;
                try {
                    newDefaultSize = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(Lang.getCaption("size-not-a-number"));
                    return false;
                }
                levelBorderPlugin.getGameManager().setStartSize(newDefaultSize);
                break;
            }
            default: {
                sender.sendMessage(Lang.getCaption("lb-command-usage"));
                return false;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], List.of("start", "pause", "settime", "startsize"), new ArrayList<>());
        }
        if (args.length == 2 && args[1].isEmpty()) {
            if (args[0].equals("settime")) {
                return List.of("<time>");
            }
            if (args[0].equals("startsize")) {
                return List.of("<size>");
            }
        }
        return List.of();
    }
}
