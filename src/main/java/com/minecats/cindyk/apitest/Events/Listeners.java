package com.minecats.cindyk.apitest.Events;

import org.bukkit.event.Event;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by cindy on 10/26/2014.
 *
 * 11.13.2016 - I think I was adding a way to just blast everyone on the server with api-events.
 * but I never finished it... not sure I want it now.
 */
public class Listeners {

    String EventName;
    Event thisEvent;
    ConcurrentHashMap<String,PlayersChoice> peeps = new ConcurrentHashMap<String,PlayersChoice>();

    public Listeners(Event event)
    {
        thisEvent = event;
        EventName = thisEvent.getEventName();
    }

    public boolean addPlayer(String playerName, boolean cancel)
    {
        if(!peeps.contains(playerName)) {
            PlayersChoice pc = new PlayersChoice();
            pc.Name = playerName;
            pc.Cancel = cancel;
            peeps.put(playerName,pc);
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

    public boolean setCancelled(String playerName, boolean cancel)
    {
        if(peeps.contains(playerName))
        {
            PlayersChoice pc = peeps.get(playerName);
            pc.Cancel = cancel;
            peeps.put(playerName,pc);
        }
        return false;
    }

}
