package ru.thorgathis.torium;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import ru.thorgathis.torium.commands.*;
import ru.thorgathis.torium.config.ConfigManager;
import ru.thorgathis.torium.config.MessagesManager;
import ru.thorgathis.torium.listener.ProxyPingListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Plugin(
        id = "torium",
        name = "Torium",
        version = "0.8",
        authors = {"Thorgathis"}
)
public class Main {
    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;
    private final ConfigManager configManager;
    private final MessagesManager messagesManager;

    @Inject
    public Main(ProxyServer server, CommandManager commandManager, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        this.configManager = new ConfigManager(this, dataDirectory);
        this.messagesManager = new MessagesManager(this, dataDirectory);
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        // Регистрируем команды
        registerCommands();

        // Регистрируем слушатель событий
        server.getEventManager().register(this, new ProxyPingListener(server, configManager));

        logger.info("Torium plugin has been enabled!");
    }

    private void registerCommands() {
        CommandManager commandManager = server.getCommandManager();

        commandManager.register("alert", new AlertCommand(this));
        commandManager.register("find", new FindCommand(this));
        commandManager.register("send", new SendCommand(this));
        commandManager.register("treload", new ReloadCommand(this));
        commandManager.register("list", new ListCommand(this));

        List<String> lobbyAliases = configManager.getConfig().getList("lobby.aliases", List.of("hub"));
        commandManager.register("lobby", new LobbyCommand(this), lobbyAliases.toArray(new String[0]));
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MessagesManager getMessagesManager() {
        return messagesManager;
    }

    public ProxyServer getServer() {
        return server;
    }

    public Logger getLogger() {
        return logger;
    }

    public Path getDataDirectory() {
        return dataDirectory;
    }

    public boolean saveResource(String resourcePath, boolean replace) {
        if (resourcePath == null || resourcePath.isEmpty()) {
            return false;
        }

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            logger.warn("Resource not found: " + resourcePath);
            return false;
        }

        File targetFile = dataDirectory.resolve(resourcePath).toFile();
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }

        if (targetFile.exists() && !replace) {
            return false;
        }

        try {
            Files.copy(inputStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            logger.error("Failed to save resource: " + resourcePath, e);
            return false;
        }
    }
}