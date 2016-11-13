package com.minecats.cindyk.apitest;

import com.minecats.cindyk.apitest.Events.Listeners;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by Cindy on 10/8/2014.
 */
public class ApiTest extends JavaPlugin {

    public static Logger log;

    public static Set<Class<? extends Event>> listenedTo = new HashSet<>();
    public static Listeners listening;

    @Override
    public void onEnable()
    {
        log = getLogger();

        log.info("Enabled");

        log.info("1. Enabled called - Passed");

        listening  = new Listeners(this);

        getCommand("apitest").setExecutor(new Commands(this));
    }

    @Override
    public void onDisable()
    {
        log.info("Disabled!");
        log.info("2. Disabled called - Passed");
    }

}
