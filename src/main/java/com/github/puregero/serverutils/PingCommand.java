package com.github.puregero.serverutils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PingCommand implements CommandExecutor {
    public PingCommand(ServerUtilsPlugin plugin) {
        Objects.requireNonNull(plugin.getCommand("ping")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage("Your ping is " + ((Player) sender).spigot().getPing());

        return true;
    }
}
