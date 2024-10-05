package com.ryu.rlogin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandResetPass implements CommandExecutor {

    private final RLoginPlugin plugin;

    public CommandResetPass(RLoginPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("rlogin.admin")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /resetpass <player> <new_password>");
            return true;
        }

        String playerName = args[0];
        String newPassword = args[1];

        Player targetPlayer = plugin.getServer().getPlayer(playerName);
        if (targetPlayer == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }

        PlayerData.setPassword(targetPlayer.getUniqueId(), newPassword);
        sender.sendMessage(ChatColor.GREEN + "Password for " + playerName + " has been reset.");
        targetPlayer.sendMessage(ChatColor.YELLOW + "Your password has been reset by an admin.");
        return true;
    }
}
