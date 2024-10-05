package com.ryu.rlogin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandRequestReset implements CommandExecutor {

    private final RLoginPlugin plugin;

    public CommandRequestReset(RLoginPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /requestreset <reason>");
            return true;
        }

        String reason = String.join(" ", args);

        // Send request to Discord webhook
        String message = "Password reset request from " + player.getName() + ". Reason: " + reason;
        WebhookUtil.sendWebhook("https://discord.com/api/webhooks/1292053943039557754/9ibMOJnHdoCcpxx2V606s7sq-Mo9J1OWa6ynyTtW4K2UKO83bz1XMQ1hvzW4cDOUQxHA", message);

        player.sendMessage(ChatColor.GREEN + "Your password reset request has been sent to the admins.");
        return true;
    }
}
