package net.fameless.levelBorder.language;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fameless.levelBorder.LevelBorderPlugin;
import net.fameless.levelBorder.util.Format;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Lang {

    private static JsonObject langObject = null;
    private static Language language;

    public static void loadLanguage(@NotNull LevelBorderPlugin levelBorderPlugin) {
        levelBorderPlugin.reloadConfig();
        String lang = levelBorderPlugin.getConfig().getString("lang", "en");
        File jsonFile;
        switch (lang) {
            case "de": {
                try {
                    jsonFile = new File(levelBorderPlugin.getDataFolder(), "lang_de.json");
                    langObject = JsonParser.parseReader(new FileReader(jsonFile)).getAsJsonObject();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                language = Language.GERMAN;
                break;
            }
            case "en": {
                try {
                    jsonFile = new File(levelBorderPlugin.getDataFolder(), "lang_en.json");
                    langObject = JsonParser.parseReader(new FileReader(jsonFile)).getAsJsonObject();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                language = Language.ENGLISH;
                break;
            }
        }
    }

    public static @NotNull String getCaption(String path) {
        String prefix = langObject.get("prefix").getAsString();
        String message = langObject.get(path).getAsString();
        message = message.replace("{timer.time}", Format.formatTime(LevelBorderPlugin.get().getGameManager().getTime()))
                .replace("{prefix}", prefix)
                .replace("{border.size}", String.valueOf(LevelBorderPlugin.get().getGameManager().getBorderSize()));
        return LegacyComponentSerializer.legacySection().serialize(MiniMessage.miniMessage().deserialize(message));
    }

    public static Language getLanguage() {
        return language;
    }
}
