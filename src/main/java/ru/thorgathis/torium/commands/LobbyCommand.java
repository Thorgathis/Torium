package ru.thorgathis.torium.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import ru.thorgathis.torium.Main;
import ru.thorgathis.torium.config.MessagesManager;

public class LobbyCommand implements SimpleCommand {
    private final Main plugin;
    private final MessagesManager messagesManager;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public LobbyCommand(Main plugin) {
        this.plugin = plugin;
        this.messagesManager = plugin.getMessagesManager();
    }

    @Override
    public void execute(Invocation invocation) {
        if (!plugin.getConfigManager().getConfig().getBoolean("lobby.enable", true)) {
            invocation.source().sendMessage(miniMessage.deserialize(messagesManager.getMessage("commands.disabled")));
            return;
        }

        if (!hasPermission(invocation)) {
            invocation.source().sendMessage(miniMessage.deserialize(messagesManager.getMessage("commands.no-permission")));
            return;
        }

        if (!(invocation.source() instanceof Player player)) {
            invocation.source().sendMessage(miniMessage.deserialize(messagesManager.getMessage("commands.only-players")));
            return;
        }

        String lobbyServerName = plugin.getConfigManager().getConfig().getString("lobby.server", "lobby");
        RegisteredServer lobbyServer = plugin.getServer().getServer(lobbyServerName).orElse(null);

        if (lobbyServer == null) {
            player.sendMessage(miniMessage.deserialize(messagesManager.getMessage("lobby.server-not-found")));
            return;
        }

        player.createConnectionRequest(lobbyServer).fireAndForget();
        player.sendMessage(miniMessage.deserialize(messagesManager.getMessage("lobby.teleported")
                .replace("{server}", lobbyServerName)));
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("torium.command.lobby");
    }
}