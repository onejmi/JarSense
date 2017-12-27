package io.github.scarger.jarsense;

import io.github.scarger.jarsense.scan.Watcher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Created by Synch on 2017-12-26.
 */
public class Bootstrap extends JavaPlugin{

    private boolean changeFound;
    private Watcher watcher;
    private final String PREFIX =
            ChatColor.BLACK+"["+ChatColor.YELLOW+"JarSense"+ChatColor.BLACK+"] "+ChatColor.YELLOW;

    public void onEnable(){
        setupConfig();
        Bukkit.getLogger().info(PREFIX+"Preparing system listener..");
        //define watcher with root director + appended plugins folder
        watcher = new Watcher(new File(getConfig().getString("pluginsDir")).getPath(),this);
        //start the watcher (default 3 second interval)
        Bukkit.getLogger().info(PREFIX+"Starting...");
        watcher.start();
    }

    public void onDisable(){
        if(changeFound) {
            Bukkit.getLogger().info(PREFIX+"Change found, restarting..");
        }
        watcher.close();
    }

    public void changeFound(){
        changeFound=true;
    }

    private void setupConfig(){
        getConfig().options().copyDefaults(true);
        saveConfig();
    }
}
