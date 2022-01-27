package com.github.puregero.serverutils;

import com.github.puregero.multilib.MultiLib;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SetPlayerCountCommand implements CommandExecutor {
    public SetPlayerCountCommand(ServerUtilsPlugin plugin) {
        Objects.requireNonNull(plugin.getCommand("setplayercount")).setExecutor(this);

        MultiLib.onString(plugin, "setplayercount", string -> {
            int playerCount = Integer.parseInt(string);
            Bukkit.setMaxPlayers(playerCount);
        });
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <player count>");
            return false;
        }

        try {
            int playerCount = Integer.parseInt(args[0]);
            Bukkit.setMaxPlayers(playerCount);
            MultiLib.notify("setplayercount", Integer.toString(playerCount));
            sender.sendMessage(ChatColor.GREEN + "Set player count to " + playerCount);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + e.getClass().getSimpleName() + ": " + e.getMessage());
        }

        return true;
    }
}
