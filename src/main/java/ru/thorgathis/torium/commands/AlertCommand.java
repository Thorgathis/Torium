package ru.thorgathis.torium.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import ru.thorgathis.torium.Main;
import ru.thorgathis.torium.config.MessagesManager;

public class AlertCommand implements SimpleCommand {
    private final ProxyServer server;
    private final MessagesManager messagesManager;
    private final Main plugin;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public AlertCommand(Main plugin) {
        this.plugin = plugin;
        this.server = plugin.getServer();
        this.messagesManager = plugin.getMessagesManager();
    }

    @Override
    public void execute(Invocation invocation) {
        if (!plugin.getConfigManager().getConfig().getBoolean("alert.enable", true)) {
            invocation.source().sendMessage(miniMessage.deserialize(messagesManager.getMessage("commands.disabled")));
            return;
        }

        if (!hasPermission(invocation)) {
            invocation.source().sendMessage(miniMessage.deserialize(messagesManager.getMessage("commands.no-permission")));
            return;
        }

        String[] args = invocation.arguments();
        if (args.length == 0) {
            invocation.source().sendMessage(miniMessage.deserialize(messagesManager.getMessage("commands.alert-usage")));
            return;
        }

        String message = String.join(" ", args);
        String formattedMessage = messagesManager.getMessage("alerts.alert-format")
                .replace("{message}", message);

        server.getAllPlayers().forEach(player -> player.sendMessage(miniMessage.deserialize(formattedMessage)));
        plugin.getLogger().info("[ALERT] " + message);
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("torium.command.alert");
    }
}