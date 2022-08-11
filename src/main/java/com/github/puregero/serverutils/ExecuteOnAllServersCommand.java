package com.github.puregero.serverutils;

import com.github.puregero.multilib.MultiLib;
import org.bukkit.Bukkit;
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

        MultiLib.onString(plugin, "executeonallservers", string -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), string);
        });
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <command>");
            return false;
        }

        String commandStr = String.join(" ", args);

        if (!(sender instanceof Player player)) {
            MultiLib.notify("executeonallservers", commandStr);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandStr);
            return true;
        }

        MultiLib.chatOnOtherServers(player, "/" + commandStr);
        player.chat("/" + commandStr);

        return true;
    }
}
