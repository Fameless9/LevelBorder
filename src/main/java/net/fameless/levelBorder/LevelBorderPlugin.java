package net.fameless.levelBorder;

import net.fameless.levelBorder.game.GameManager;
import net.fameless.levelBorder.game.LevelBorderCommand;
import net.fameless.levelBorder.language.Lang;
import net.fameless.levelBorder.language.LanguageCommand;
import net.fameless.levelBorder.util.UpdateChecker;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;

public final class LevelBorderPlugin extends JavaPlugin {

    private static LevelBorderPlugin instance;
    private GameManager gameManager;

    public static LevelBorderPlugin get() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        saveResource("lang_de.json", false);
        saveResource("lang_en.json", false);

        Lang.loadLanguage(this);

        int timerTime = getConfig().getInt("timer.time", 0);
        int defaultSize = getConfig().getInt("start-size", 1);
        gameManager = new GameManager(this, defaultSize, timerTime);

        LanguageCommand languageCommand = new LanguageCommand(this);

        Bukkit.getPluginManager().registerEvents(languageCommand, this);
        Bukkit.getPluginManager().registerEvents(gameManager, this);

        getCommand("levelborder").setExecutor(new LevelBorderCommand(this));

        getCommand("language").setExecutor(languageCommand);

        new Metrics(this, 22785);
        new UpdateChecker(this, 118480, Duration.ofHours(2));
    }

    @Override
    public void onDisable() {
        gameManager.handleShutdown();
    }

    public GameManager getGameManager() {
        return gameManager;
    }
}
