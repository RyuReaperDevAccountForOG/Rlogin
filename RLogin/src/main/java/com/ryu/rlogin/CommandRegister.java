package com.ryu.rlogin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandRegister implements CommandExecutor {

    private final RLoginPlugin plugin;

    public CommandRegister(RLoginPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        if (MojangAPIUtil.isPremiumPlayer(player.getName())) {
            player.sendMessage(ChatColor.RED + "You are a premium player. You don't need to register.");
            return true;
        }

        if (args.length != 2) {
            player.sendMessage(ChatColor.RED + "Usage: /register <password> <confirm_password>");
            return true;
        }

        String password = args[0];
        String confirmPassword = args[1];

        if (!password.equals(confirmPassword)) {
            player.sendMessage(ChatColor.RED + "Passwords do not match.");
            return true;
        }

        if (PlayerData.isRegistered(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You are already registered.");
            return true;
        }

        PlayerData.setPassword(player.getUniqueId(), password);
        player.sendMessage(ChatColor.GREEN + "You have successfully registered.");
        return true;
    }
}
