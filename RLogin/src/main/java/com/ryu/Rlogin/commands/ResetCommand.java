package com.ryu.Rlogin.commands;

import com.ryu.Rlogin.RLoginPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetCommand implements CommandExecutor {

    private final RLoginPlugin plugin;

    public ResetCommand(RLoginPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length != 1) {
                player.sendMessage("&cUsage: /passwordreset <reason>");
                return false;
            }

            String reason = args[0];
            plugin.sendResetRequestToWebhook(player, reason);
            player.sendMessage("&aPassword reset request sent!");

            return true;
        } else {
            sender.sendMessage("&cOnly players can use this command.");
            return false;
        }
    }
}
