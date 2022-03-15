package com.github.puregero.serverutils;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SetPersistenceCommand implements CommandExecutor {
    public SetPersistenceCommand(ServerUtilsPlugin plugin) {
        Objects.requireNonNull(plugin.getCommand("setpersistence")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <true/false> <radius>");
            return false;
        }

        if (!"true".equalsIgnoreCase(args[0]) && !"1".equalsIgnoreCase(args[0]) && !"false".equalsIgnoreCase(args[0]) && !"0".equalsIgnoreCase(args[0])) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <true/false> <radius>");
            return false;
        }

        boolean persistenceRequired = "true".equalsIgnoreCase(args[0]) || "1".equalsIgnoreCase(args[0]);
        boolean removeWhenFarAway = !persistenceRequired;
        int radius;

        try {
            radius = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <true/false> <radius>");
            return false;
        }

        int entityCount = 0;

        for (Entity entity : ((Player) sender).getNearbyEntities(radius, radius, radius)) {
            if (entity instanceof Mob mob && mob.getRemoveWhenFarAway() != removeWhenFarAway) {
                entityCount ++;
                mob.setRemoveWhenFarAway(removeWhenFarAway);
            }
        }

        sender.sendMessage("Set PersistenceRequired to " + persistenceRequired + " for " + entityCount + " entities in a radius of " + radius);

        return true;
    }
}
