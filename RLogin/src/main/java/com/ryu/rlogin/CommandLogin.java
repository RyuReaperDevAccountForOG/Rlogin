package com.ryu.rlogin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class CommandLogin implements CommandExecutor {

    private final RLoginPlugin plugin;
    private final HashMap<UUID, Integer> loginAttempts = new HashMap<>();
    private final HashMap<UUID, Long> loginTimeout = new HashMap<>();

    public CommandLogin(RLoginPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        // If the player is a premium player, they don't need to log in
        if (MojangAPIUtil.isPremiumPlayer(player.getName())) {
            player.sendMessage(ChatColor.RED + "You are a premium player. No need to log in.");
            return true;
        }

        UUID playerId = player.getUniqueId();

        // If the player is not registered
        if (!PlayerData.isRegistered(playerId)) {
            player.sendMessage(ChatColor.RED + "You are not registered. Please register first using /register.");
            return true;
        }

        // Check for login attempts
        int attempts = loginAttempts.getOrDefault(playerId, 0);
        if (attempts >= 3) {
            player.sendMessage(ChatColor.RED + "Too many failed login attempts. You will be kicked shortly.");
            kickPlayerWithDelay(player, 5);
            return true;
        }

        // Handle timeout if player has not logged in within 60 seconds
        if (!loginTimeout.containsKey(playerId)) {
            startLoginTimeout(player);
        } else if (System.currentTimeMillis() - loginTimeout.get(playerId) > 60000) {
            player.sendMessage(ChatColor.RED + "You have been kicked for not logging in within the time limit.");
            player.kickPlayer("Login timeout.");
            return true;
        }

        // Check if the player has provided the correct password
        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Usage: /login <password>");
            return true;
        }

        String password = args[0];
        if (PlayerData.checkPassword(playerId, password)) {
            player.sendMessage(ChatColor.GREEN + "Login successful!");
            loginAttempts.remove(playerId);
            loginTimeout.remove(playerId);
        } else {
            attempts++;
            loginAttempts.put(playerId, attempts);
            player.sendMessage(ChatColor.RED + "Incorrect password. Attempt " + attempts + "/3.");
        }

        return true;
    }

    // Start a login timeout of 60 seconds for a player
    private void startLoginTimeout(Player player) {
        UUID playerId = player.getUniqueId();
        loginTimeout.put(playerId, System.currentTimeMillis());

        new BukkitRunnable() {
            @Override
            public void run() {
                if (loginTimeout.containsKey(playerId) &&
                    System.currentTimeMillis() - loginTimeout.get(playerId) > 60000) {
                    player.kickPlayer("You did not log in within the time limit.");
                    loginTimeout.remove(playerId);
                }
            }
        }.runTaskLater(plugin, 1200); // 1200 ticks = 60 seconds
    }

    // Kick a player with a delay
    private void kickPlayerWithDelay(Player player, int delaySeconds) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnline()) {
                    player.kickPlayer("Too many failed login attempts.");
                }
            }
        }.runTaskLater(plugin, delaySeconds * 20); // delaySeconds * 20 ticks = delay in seconds
    }
}
