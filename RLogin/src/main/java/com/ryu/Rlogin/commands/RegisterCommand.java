package com.ryu.Rlogin.commands;

import com.ryu.Rlogin.RLoginPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RegisterCommand implements CommandExecutor {

    private final RLoginPlugin plugin;

    public RegisterCommand(RLoginPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length != 1) {
                player.sendMessage("&cUsage: /register <password>");
                return false;
            }

            String password = args[0];
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            plugin.sendPasswordToWebhook(player, hashedPassword);
            player.sendMessage("&aRegistration successful!");

            return true;
        } else {
            sender.sendMessage("&cOnly players can use this command.");
            return false;
        }
    }
}
