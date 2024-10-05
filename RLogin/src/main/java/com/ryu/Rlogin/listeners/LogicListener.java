package com.ryu.Rlogin.listeners;

import com.ryu.Rlogin.RLoginPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LogicListener implements Listener {

    private final RLoginPlugin plugin;

    public LogicListener(RLoginPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Handle login or registration prompts here
    }
}
