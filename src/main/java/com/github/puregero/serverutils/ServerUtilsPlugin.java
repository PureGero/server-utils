package com.github.puregero.serverutils;

import org.bukkit.plugin.java.JavaPlugin;

public class ServerUtilsPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        new SetPlayerCountCommand(this);
        new SetViewDistanceCommand(this);
        new SetSimulationDistanceCommand(this);
    }

}
