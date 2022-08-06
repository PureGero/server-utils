package com.github.puregero.serverutils;

import com.github.puregero.multilib.MultiLib;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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

public class SetViewDistanceCommand implements CommandExecutor {
    public SetViewDistanceCommand(ServerUtilsPlugin plugin) {
        Objects.requireNonNull(plugin.getCommand("setviewdistance")).setExecutor(this);

        MultiLib.onString(plugin, "setviewdistance", string -> {
            int viewDistance = Integer.parseInt(string);
            ((CraftServer) Bukkit.getServer()).getHandle().setViewDistance(viewDistance);
            for (World world : Bukkit.getWorlds()) {
                ((CraftWorld) world).getHandle().spigotConfig.viewDistance = viewDistance;
            }
        });

        MultiLib.onString(plugin, "getviewdistance", (string, reply) -> {
            reply.accept("setviewdistance", Integer.toString(Bukkit.getViewDistance()));
        });

        MultiLib.notify("getviewdistance", "");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <view distance>");
            return false;
        }

        try {
            int viewDistance = Integer.parseInt(args[0]);
            ((CraftServer) Bukkit.getServer()).getHandle().setViewDistance(viewDistance);
            for (World world : Bukkit.getWorlds()) {
                ((CraftWorld) world).getHandle().spigotConfig.viewDistance = viewDistance;
            }
            MultiLib.notify("setviewdistance", Integer.toString(viewDistance));

            MultiLib.getAllOnlinePlayers().stream().filter(player -> player.hasPermission("serverutils.adminmessage")).forEach(player ->
                    player.sendMessage(Component.text("[" + sender.getName() + ": Set view distance to " + viewDistance + "]").color(NamedTextColor.GRAY))
            );
            sender.sendMessage(ChatColor.GREEN + "Set view distance to " + viewDistance);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + e.getClass().getSimpleName() + ": " + e.getMessage());
        }

        return true;
    }
}
