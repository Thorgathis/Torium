package ru.thorgathis.torium.commands;

import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.minimessage.MiniMessage;
import ru.thorgathis.torium.Main;
import ru.thorgathis.torium.config.MessagesManager;

public class ReloadCommand implements SimpleCommand {
    private final Main plugin;
    private final MessagesManager messagesManager;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public ReloadCommand(Main plugin) {
        this.plugin = plugin;
        this.messagesManager = plugin.getMessagesManager();
    }

    @Override
    public void execute(Invocation invocation) {
        if (!plugin.getConfigManager().getConfig().getBoolean("reload.enable", true)) {
            invocation.source().sendMessage(miniMessage.deserialize(messagesManager.getMessage("commands.disabled")));
            return;
        }

        if (!hasPermission(invocation)) {
            invocation.source().sendMessage(miniMessage.deserialize(plugin.getMessagesManager().getMessage("commands.no-permission")));
            return;
        }

        plugin.getConfigManager().reloadConfig();
        plugin.getMessagesManager().reloadMessages();
        invocation.source().sendMessage(miniMessage.deserialize(plugin.getMessagesManager().getMessage("reload.success")));
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("torium.command.reload");
    }
}