package com.github.puregero.serverutils;

import com.github.puregero.multilib.MultiLib;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class InstancesPingCommand implements CommandExecutor {
    public InstancesPingCommand(ServerUtilsPlugin plugin) {
        Objects.requireNonNull(plugin.getCommand("instancesping")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        String server;

        if (args.length == 0 && MultiLib.isLocalPlayer(player)) {
            MultiLib.chatOnOtherServers(player, "/instancesping");
        }

        if (args.length == 0) {
            server = MultiLib.getLocalServerName();
            MultiLib.chatOnOtherServers(player, "/instancesping " + server);
        } else {
            player.sendMessage(args[0] + " -> " + MultiLib.getLocalServerName());
        }

        return true;
    }
}
