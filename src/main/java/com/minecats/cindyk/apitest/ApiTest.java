package com.minecats.cindyk.apitest;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * Created by Cindy on 10/8/2014.
 */
public class ApiTest extends JavaPlugin {

    public static Logger log;


    @Override
    public void onEnable()
    {
        log = getLogger();

        log.info("Enabled");

        log.info("1. Enabled called - Passed");

        //Going to Add lots of Events, so lets call a function to do that
        //May move it to its own class.
        getCommand("apitest").setExecutor(new Commands(this));
    }

    @Override
    public void onDisable()
    {
        log.info("Disabled!");
        log.info("2. Disabled called - Passed");
    }

}
