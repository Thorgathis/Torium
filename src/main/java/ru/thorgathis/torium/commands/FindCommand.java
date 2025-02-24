package ru.thorgathis.torium.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.minimessage.MiniMessage;
import ru.thorgathis.torium.Main;
import ru.thorgathis.torium.config.MessagesManager;

public class FindCommand implements SimpleCommand {
    private final Main plugin;
    private final MessagesManager messagesManager;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public FindCommand(Main plugin) {
        this.plugin = plugin;
        this.messagesManager = plugin.getMessagesManager();
    }

    @Override
    public void execute(Invocation invocation) {
        if (!plugin.getConfigManager().getConfig().getBoolean("find.enable", true)) {
            invocation.source().sendMessage(miniMessage.deserialize(messagesManager.getMessage("commands.disabled")));
            return;
        }

        if (!hasPermission(invocation)) {
            invocation.source().sendMessage(miniMessage.deserialize(messagesManager.getMessage("commands.no-permission")));
            return;
        }

        String[] args = invocation.arguments();
        if (args.length == 0) {
            invocation.source().sendMessage(miniMessage.deserialize(messagesManager.getMessage("commands.find-usage")));
            return;
        }

        String playerName = args[0];
        Player player = plugin.getServer().getPlayer(playerName).orElse(null);
        if (player == null) {
            invocation.source().sendMessage(miniMessage.deserialize(messagesManager.getMessage("find.player-not-found")));
            return;
        }

        String serverName = player.getCurrentServer().map(s -> s.getServerInfo().getName()).orElse("Unknown");
        invocation.source().sendMessage(miniMessage.deserialize(messagesManager.getMessage("find.player-location")
                .replace("{player}", playerName)
                .replace("{server}", serverName)));
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("torium.command.find");
    }
}