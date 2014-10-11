package com.minecats.cindyk.apitest.EventsPlayer;

import com.minecats.cindyk.apitest.ApiTest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 * Created by Cindy on 10/9/2014.
 */
public class PlayerLoginEventHandler implements Listener {

    @EventHandler
    public void onPlayerLoginEvent(PlayerLoginEvent ple)
    {
        if(ple != null)
        {
            if(ple.getPlayer() instanceof Player)
            {
                ApiTest.log.info("PlayerLoginEvent : Player - " + ple.getPlayer().getName());
            }

            ApiTest.log.info("PlayerLoginEvent : getAddress - " + ple.getAddress().toString());
            ApiTest.log.info("PlayerLoginEvent : getHostname - " + ple.getHostname());
            ApiTest.log.info("PlayerLoginEvent : getKickMessage - " + ple.getKickMessage());
            ApiTest.log.info("PlayerLoginEvent : getResult - " + ple.getResult().toString());
        }
        else
            ApiTest.log.info("PlayerLoginEvent : PlayerLoginEvent class was null!" );
    }

}
