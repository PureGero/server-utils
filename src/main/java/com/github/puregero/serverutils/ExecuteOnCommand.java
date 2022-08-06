package com.github.puregero.serverutils;

import com.github.puregero.multilib.MultiLib;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import puregero.multipaper.MultiPaper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class ExecuteOnCommand implements CommandExecutor, TabCompleter {
    public ExecuteOnCommand(ServerUtilsPlugin plugin) {
        Objects.requireNonNull(plugin.getCommand("executeon")).setExecutor(this);
        Objects.requireNonNull(plugin.getCommand("executeon")).setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length <= 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <server> <command>");
            return false;
        }

        if (args[0].equalsIgnoreCase(MultiLib.getLocalServerName())) {
            String[] subArgs = new String[args.length - 1];
            System.arraycopy(args, 1, subArgs, 0, args.length - 1);
            ((Player) sender).chat("/" + String.join(" ", subArgs));
            return true;
        }

        if (!MultiLib.isLocalPlayer((Player) sender)) {

            return true;
        }

        AtomicBoolean specifiedServerExists = new AtomicBoolean(false);
        MultiPaper.forEachExternalServer(externalServer -> {
            if (externalServer.getName().equalsIgnoreCase(args[0])) {
                specifiedServerExists.set(true);
            }
        });

        if (!specifiedServerExists.get()) {
            sender.sendMessage(ChatColor.RED + "Could not find server with name" + args[0]);
            return false;
        }

        MultiLib.chatOnOtherServers((Player) sender, "/" + label + " " + String.join(" ", args));

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> tabComplete = new ArrayList<>();
            MultiPaper.forEachExternalServer(externalServer -> {
                if (externalServer.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                    tabComplete.add(externalServer.getName());
                }
            });
            return tabComplete;
        }

        return null;
    }
}
