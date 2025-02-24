package ru.thorgathis.torium.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import ru.thorgathis.torium.Main;
import ru.thorgathis.torium.config.MessagesManager;

import java.util.stream.Collectors;

public class ListCommand implements SimpleCommand {
    private final Main plugin;
    private final MessagesManager messagesManager;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public ListCommand(Main plugin) {
        this.plugin = plugin;
        this.messagesManager = plugin.getMessagesManager();
    }

    @Override
    public void execute(Invocation invocation) {
        if (!plugin.getConfigManager().getConfig().getBoolean("list.enable", true)) {
            invocation.source().sendMessage(miniMessage.deserialize(messagesManager.getMessage("commands.disabled")));
            return;
        }

        if (!hasPermission(invocation)) {
            invocation.source().sendMessage(miniMessage.deserialize(messagesManager.getMessage("commands.no-permission")));
            return;
        }

        boolean showEmptyServers = plugin.getConfigManager().getConfig().getBoolean("list.show-empty-servers", true);
        int totalPlayers = plugin.getServer().getAllPlayers().size();

        String header = messagesManager.getMessage("list.header")
                .replace("{count}", String.valueOf(totalPlayers));
        invocation.source().sendMessage(miniMessage.deserialize(header));

        for (RegisteredServer server : plugin.getServer().getAllServers()) {
            String serverName = server.getServerInfo().getName();
            int playerCount = server.getPlayersConnected().size();

            if (playerCount == 0 && !showEmptyServers) {
                continue;
            }

            String playerNames = server.getPlayersConnected().stream()
                    .map(Player::getUsername)
                    .collect(Collectors.joining(", "));

            String serverEntry;
            if (playerCount == 0) {
                serverEntry = messagesManager.getMessage("list.empty-server-entry")
                        .replace("{server}", serverName)
                        .replace("{count}", String.valueOf(playerCount));
            } else {
                serverEntry = messagesManager.getMessage("list.server-entry")
                        .replace("{server}", serverName)
                        .replace("{count}", String.valueOf(playerCount))
                        .replace("{players}", playerNames);
            }

            invocation.source().sendMessage(miniMessage.deserialize(serverEntry));
        }
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("torium.command.list");
    }
}