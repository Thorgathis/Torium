package ru.thorgathis.torium.config;

import com.moandjiezana.toml.Toml;
import ru.thorgathis.torium.Main;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class MessagesManager {

    private final Main plugin;
    private final Path dataDirectory;
    private final Map<String, String> messages = new HashMap<>();

    public MessagesManager(Main plugin, Path dataDirectory) {
        this.plugin = plugin;
        this.dataDirectory = dataDirectory;
        reloadMessages();
    }

    public void reloadMessages() {
        messages.clear();

        String language = plugin.getConfigManager().getConfig().getString("language.default", "en");

        File langDirectory = dataDirectory.resolve("lang").toFile();
        if (!langDirectory.exists()) {
            langDirectory.mkdirs();
        }

        copyLanguageFile("messages_en.toml");
        copyLanguageFile("messages_ru.toml");

        File langFile = dataDirectory.resolve("lang/messages_" + language + ".toml").toFile();

        if (!langFile.exists()) {
            langFile = dataDirectory.resolve("lang/messages_en.toml").toFile();
        }

        Toml toml = new Toml().read(langFile);
        toml.toMap().forEach((key, value) -> {
            if (value instanceof Map) {
                ((Map<?, ?>) value).forEach((subKey, subValue) -> {
                    messages.put(key + "." + subKey, subValue.toString());
                });
            }
        });
    }

    private void copyLanguageFile(String fileName) {
        File targetFile = dataDirectory.resolve("lang/" + fileName).toFile();
        if (!targetFile.exists()) {
            plugin.saveResource("lang/" + fileName, false);
        }
    }

    public String getMessage(String path) {
        return messages.getOrDefault(path, "<red>Message not found: " + path);
    }
}