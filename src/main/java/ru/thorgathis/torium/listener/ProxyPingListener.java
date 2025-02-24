package ru.thorgathis.torium.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import ru.thorgathis.torium.config.ConfigManager;

public class ProxyPingListener {

    private final ProxyServer server;
    private final ConfigManager configManager;

    public ProxyPingListener(ProxyServer server, ConfigManager configManager) {
        this.server = server;
        this.configManager = configManager;
    }

    @Subscribe
    public void onProxyPing(ProxyPingEvent event) {
        boolean modifyPing = configManager.getConfig().getBoolean("proxy-ping.modify-ping", true);
        if (!modifyPing) {
            return;
        }

        int totalOnline = server.getAllServers().stream()
                .mapToInt(s -> s.getPlayersConnected().size())
                .sum();

        ServerPing ping = event.getPing();
        ServerPing.Builder builder = ping.asBuilder();
        builder.onlinePlayers(totalOnline);

        event.setPing(builder.build());
    }
}