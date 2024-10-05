package com.ryu.rlogin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class RLoginPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Register commands
        this.getCommand("register").setExecutor(new CommandRegister(this));
        this.getCommand("login").setExecutor(new CommandLogin(this));
        this.getCommand("resetpass").setExecutor(new CommandResetPass(this));
        this.getCommand("requestreset").setExecutor(new CommandRequestReset(this));

        // Register events for cracking player login screen
        Bukkit.getPluginManager().registerEvents(new PlayerLoginListener(this), this);

        // Load player data
        PlayerData.loadData();
    }

    @Override
    public void onDisable() {
        // Save player data on plugin disable
        PlayerData.saveData();
    }
}
