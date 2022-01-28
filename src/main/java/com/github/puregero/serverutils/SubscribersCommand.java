package com.github.puregero.serverutils;

import com.github.puregero.multilib.MultiLib;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_18_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import puregero.multipaper.ChunkKey;
import puregero.multipaper.ExternalServer;
import puregero.multipaper.MultiPaper;
import puregero.multipaper.externalserverprotocol.PlayerActionPacket;

import java.util.Objects;

public class SubscribersCommand implements CommandExecutor {
    public SubscribersCommand(ServerUtilsPlugin plugin) {
        Objects.requireNonNull(plugin.getCommand("ping")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;

        if (MultiLib.isLocalPlayer(player)) {
            MultiPaper.broadcastPacketToExternalServers(new PlayerActionPacket(((CraftPlayer) player).getHandle(), new ServerboundChatPacket("/subscribers")));
        }

        int cx = player.getLocation().getBlockX() >> 4;
        int cz = player.getLocation().getBlockZ() >> 4;
        ServerLevel level = ((CraftWorld) player.getWorld()).getHandle();
        if (player.getWorld().isChunkLoaded(cx, cz)) {
            LevelChunk chunk = ((CraftChunk) player.getChunk()).getHandle();
            player.sendMessage(MultiPaper.getBungeeCordName() + "[" + cx + "," + cz + "] " + (chunk.externalOwner == null ? "null" : chunk.externalOwner.getName()) + "(" + chunk.hasExternalLockRequest + "," + (chunk.isUnsaved() ? "Unsaved" : "Saved") + "): " + chunk.externalSubscribers.stream().map(ExternalServer::getName).toList());
        } else if (level.chunkSource.chunkMap.getVisibleChunkIfPresent(ChunkPos.asLong(cx, cz)) != null) {
            player.sendMessage(MultiPaper.getBungeeCordName() + "[" + cx + "," + cz + "] not loaded but visible " + level.chunkSource.chunkMap.getVisibleChunkIfPresent(ChunkPos.asLong(cx, cz)).getAvailableChunkNow());
        } else if (level.chunkSource.chunkMap.getUpdatingChunkIfPresent(ChunkPos.asLong(cx, cz)) != null) {
            player.sendMessage(MultiPaper.getBungeeCordName() + "[" + cx + "," + cz + "] not loaded but updating " + level.chunkSource.chunkMap.getUpdatingChunkIfPresent(ChunkPos.asLong(cx, cz)).getAvailableChunkNow());
        } else if (level.chunkSource.chunkMap.getUnloadingChunkHolder(cx, cz) != null) {
            player.sendMessage(MultiPaper.getBungeeCordName() + "[" + cx + "," + cz + "] not loaded but unloading " + level.chunkSource.chunkMap.getUnloadingChunkHolder(cx, cz).getAvailableChunkNow());
        } else {
            player.sendMessage(MultiPaper.getBungeeCordName() + "[" + cx + "," + cz + "] not loaded");
        }

        ChunkKey key = new ChunkKey(player.getWorld().getName(), cx, cz);
        if (MultiPaper.chunkSubscribersToSet.contains(key)) {
            player.sendMessage(MultiPaper.getBungeeCordName() + "[" + cx + "," + cz + "] toSet: " + MultiPaper.chunkSubscribersToSet.get(key).stream().map(ExternalServer::getName).toList());
        }

        return true;
    }
}
