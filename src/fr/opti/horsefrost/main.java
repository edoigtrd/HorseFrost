package fr.opti.horsefrost;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin{
    public void onEnable(){
        Bukkit.getServer().getPluginManager().registerEvents(new events(),this);
    }
}