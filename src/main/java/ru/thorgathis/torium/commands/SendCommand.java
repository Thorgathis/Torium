package ru.thorgathis.torium.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import ru.thorgathis.torium.Main;
import ru.thorgathis.torium.config.MessagesManager;

public class SendCommand implements SimpleCommand {
    private final Main plugin;
    private final MessagesManager messagesManager;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public SendCommand(Main plugin) {
        this.plugin = plugin;
        this.messagesManager = plugin.getMessagesManager();
    }

    @Override
    public void execute(Invocation invocation) {
        if (!plugin.getConfigManager().getConfig().getBoolean("send.enable", true)) {
            invocation.source().sendMessage(miniMessage.deserialize(messagesManager.getMessage("commands.disabled")));
            return;
        }

        if (!hasPermission(invocation)) {
            invocation.source().sendMessage(miniMessage.deserialize(messagesManager.getMessage("commands.no-permission")));
            return;
        }

        String[] args = invocation.arguments();
        if (args.length < 2) {
            invocation.source().sendMessage(miniMessage.deserialize(messagesManager.getMessage("commands.send-usage")));
            return;
        }

        String playerName = args[0];
        String serverName = args[1];

        Player player = plugin.getServer().getPlayer(playerName).orElse(null);
        if (player == null) {
            invocation.source().sendMessage(miniMessage.deserialize(messagesManager.getMessage("send.player-not-found")));
            return;
        }

        RegisteredServer server = plugin.getServer().getServer(serverName).orElse(null);
        if (server == null) {
            invocation.source().sendMessage(miniMessage.deserialize(messagesManager.getMessage("send.server-not-found")));
            return;
        }

        player.createConnectionRequest(server).fireAndForget();
        invocation.source().sendMessage(miniMessage.deserialize(messagesManager.getMessage("send.player-sent")
                .replace("{player}", playerName)
                .replace("{server}", serverName)));
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("torium.command.send");
    }
}