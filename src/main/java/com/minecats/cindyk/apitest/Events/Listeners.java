package com.minecats.cindyk.apitest.Events;

import com.minecats.cindyk.apitest.ApiTest;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by cindy on 10/26/2014.
 *
 * Hashmap for later use... I know its not being used now.
 *
 */
public class Listeners {

    List<String> peeps = new ArrayList<>();
    ApiTest plugin;

    public Listeners(ApiTest plugin)
    {
         this.plugin = plugin;
    }

    public boolean addPlayer(String playerName, boolean cancel)
    {
        if(!peeps.contains(playerName)) {
            peeps.add(playerName);
            return true;
        }

        return false;

    }

    public boolean removePlayer(String playerName)
    {
        if(peeps.contains(playerName)) {
            peeps.remove(playerName);
            return true;
        }
        return false;
    }

 /*   public boolean setCancelled(String playerName, boolean cancel)
    {
        if(peeps.contains(playerName))
        {
            PlayersChoice pc = peeps.get(playerName);
            pc.Cancel = cancel;
            peeps.put(playerName, pc);
        }
        return false;
    }*/

    public void sendPeepsMessage(String message){

        for(String pc : peeps ){

            Player p = plugin.getServer().getPlayer(pc);
            if (p != null) {
                p.sendMessage(ChatColor.GOLD+message);
            }
        }
    }

}
