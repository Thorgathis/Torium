package ru.thorgathis.torium.config;

import com.moandjiezana.toml.Toml;
import ru.thorgathis.torium.Main;

import java.io.File;
import java.nio.file.Path;

public class ConfigManager {

    private final Main plugin;
    private final Path dataDirectory;
    private Toml config;

    public ConfigManager(Main plugin, Path dataDirectory) {
        this.plugin = plugin;
        this.dataDirectory = dataDirectory;
        reloadConfig();
    }

    public void reloadConfig() {
        File configFile = dataDirectory.resolve("config.toml").toFile();
        if (!configFile.exists()) {
            plugin.saveResource("config.toml", false);
        }
        this.config = new Toml().read(configFile);
    }

    public Toml getConfig() {
        return config;
    }
}