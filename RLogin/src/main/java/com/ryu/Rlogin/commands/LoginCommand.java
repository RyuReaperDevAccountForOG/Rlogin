package com.ryu.Rlogin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoginCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length != 1) {
                player.sendMessage("&cUsage: /login <password>");
                return false;
            }

            // Compare hashed passwords (stub code for now)
            String password = args[0];
            boolean correctPassword = comparePassword(player, password);

            if (correctPassword) {
                player.sendMessage("&aLogin successful!");
            } else {
                player.sendMessage("&cIncorrect password!");
            }

            return true;
        } else {
            sender.sendMessage("&cOnly players can use this command.");
            return false;
        }
    }

    private boolean comparePassword(Player player, String password) {
        // Implement password comparison logic (BCrypt or other hashing)
        return true;
    }
}
