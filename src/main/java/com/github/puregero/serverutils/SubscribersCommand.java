package com.github.puregero.serverutils;

import com.github.puregero.multilib.MultiLib;
import io.papermc.paper.chunk.system.scheduling.NewChunkHolder;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import puregero.multipaper.ExternalServer;

import java.util.Objects;

public class SubscribersCommand implements CommandExecutor {
    public SubscribersCommand(ServerUtilsPlugin plugin) {
        Objects.requireNonNull(plugin.getCommand("subscribers")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;

        if (MultiLib.isLocalPlayer(player)) {
            MultiLib.chatOnOtherServers(player, "/subscribers");
        }

        int cx = player.getLocation().getBlockX() >> 4;
        int cz = player.getLocation().getBlockZ() >> 4;
        ServerLevel level = ((CraftWorld) player.getWorld()).getHandle();
        ChunkHolder holder = level.chunkSource.chunkMap.getVisibleChunkIfPresent(ChunkPos.asLong(cx, cz));
        if (holder != null) {
            NewChunkHolder newChunkHolder = holder.newChunkHolder;
            ChunkAccess currentChunk = newChunkHolder.getCurrentChunk();
            player.sendMessage(MultiLib.getLocalServerName() + "[" + cx + "," + cz + "] " +
                    (currentChunk == null ? "null" : currentChunk.getClass().getSimpleName()) + "." + holder.getChunkHolderStatus() + " " +
                    (newChunkHolder.externalOwner == null ? "null" : newChunkHolder.externalOwner.getName()) +
                    "(" + newChunkHolder.hasExternalLockRequest + "," +
                    (currentChunk == null ? "Unloaded" : currentChunk.isUnsaved() ? "Unsaved" : "Saved") + "): " +
                    newChunkHolder.externalSubscribers.stream().map(ExternalServer::getName).toList());
        } else {
            player.sendMessage(MultiLib.getLocalServerName() + "[" + cx + "," + cz + "] not loaded");
        }

        return true;
    }
}
