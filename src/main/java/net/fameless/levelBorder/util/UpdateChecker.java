package net.fameless.levelBorder.util;

import net.fameless.levelBorder.LevelBorderPlugin;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.logging.Level;

public class UpdateChecker {

    private final LevelBorderPlugin levelBorderPlugin;
    private final int resourceId;
    private final Duration checkInterval;
    private Instant lastCheckTime;

    public UpdateChecker(LevelBorderPlugin levelBorderPlugin, int resourceId, Duration checkInterval) {
        this.levelBorderPlugin = levelBorderPlugin;
        this.resourceId = resourceId;
        this.checkInterval = checkInterval;
        this.lastCheckTime = Instant.MIN;
        checkForUpdates();
    }

    public void checkForUpdates() {
        Bukkit.getScheduler().runTaskAsynchronously(levelBorderPlugin, () -> {
            Instant currentTime = Instant.now();
            if (Duration.between(lastCheckTime, currentTime).compareTo(checkInterval) < 0) {
                return;
            }

            try {
                URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                String latestVersion = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();

                String currentVersion = levelBorderPlugin.getDescription().getVersion();
                if (latestVersion != null && !latestVersion.equalsIgnoreCase(currentVersion)) {
                    levelBorderPlugin.getLogger().info("A new update is available! Version " + latestVersion + " can be downloaded from the SpigotMC website: https://www.spigotmc.org/resources/" + resourceId);
                }
                lastCheckTime = currentTime;
            } catch (IOException e) {
                levelBorderPlugin.getLogger().log(Level.WARNING, "Failed to check for updates: " + e.getMessage(), e);
            }
        });
    }
}
