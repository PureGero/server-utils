package com.github.puregero.serverutils;

import com.github.puregero.multilib.MultiLib;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SetSimulationDistanceCommand implements CommandExecutor {
    public SetSimulationDistanceCommand(ServerUtilsPlugin plugin) {
        Objects.requireNonNull(plugin.getCommand("setsimulationdistance")).setExecutor(this);

        MultiLib.onString(plugin, "setsimulationdistance", string -> {
            int simulationDistance = Integer.parseInt(string);
            ((CraftServer) Bukkit.getServer()).getHandle().setSimulationDistance(simulationDistance);
            for (World world : Bukkit.getWorlds()) {
                ((CraftWorld) world).getHandle().spigotConfig.simulationDistance = simulationDistance;
            }
        });

        MultiLib.onString(plugin, "getsimulationdistance", (string, reply) -> {
            reply.accept("setsimulationdistance", Integer.toString(Bukkit.getSimulationDistance()));
        });

        MultiLib.notify("getsimulationdistance", "");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <simulation distance>");
            return false;
        }

        try {
            int simulationDistance = Integer.parseInt(args[0]);
            ((CraftServer) Bukkit.getServer()).getHandle().setSimulationDistance(simulationDistance);
            for (World world : Bukkit.getWorlds()) {
                ((CraftWorld) world).getHandle().spigotConfig.simulationDistance = simulationDistance;
            }
            MultiLib.notify("setsimulationdistance", Integer.toString(simulationDistance));
            sender.sendMessage(ChatColor.GREEN + "Set simulation distance to " + simulationDistance);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + e.getClass().getSimpleName() + ": " + e.getMessage());
        }

        return true;
    }
}
