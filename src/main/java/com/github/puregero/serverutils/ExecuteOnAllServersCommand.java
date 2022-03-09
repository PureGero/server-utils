package com.github.puregero.serverutils;

import com.github.puregero.multilib.MultiLib;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ExecuteOnAllServersCommand implements CommandExecutor {
    public ExecuteOnAllServersCommand(ServerUtilsPlugin plugin) {
        Objects.requireNonNull(plugin.getCommand("executeonallservers")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <command>");
            return false;
        }

        ((Player) sender).chat("/" + StringUtils.join(args, " "));

        MultiLib.chatOnOtherServers((Player) sender, "/" + StringUtils.join(args, " "));

        return true;
    }
}
