package com.github.puregero.serverutils;

import org.bukkit.plugin.java.JavaPlugin;

public class ServerUtilsPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        new SetPlayerCountCommand(this);
        new SetViewDistanceCommand(this);
        new SetSimulationDistanceCommand(this);
        new PingCommand(this);
        new SubscribersCommand(this);
        new InstancesPingCommand(this);
        new ExecuteOnAllServersCommand(this);
        new ExecuteOnCommand(this);
        new SetPersistenceCommand(this);
    }

}
