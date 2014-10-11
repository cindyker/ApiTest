package com.minecats.cindyk.apitest;


import com.minecats.cindyk.apitest.EventsPlayer.PlayerLoginEventHandler;
import com.minecats.cindyk.apitest.EventsPlayer.PlayerJoinEventHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Cindy on 10/8/2014.
 */
public class Commands implements CommandExecutor {

    ApiTest plugin;
    PlayerLoginEventHandler pleh;

    static HashMap handlers;

    Commands(ApiTest plugin)
    {
       this.plugin = plugin;
       handlers = new HashMap<String,Object>();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        ApiTest.log.info("Received a Command: " + s) ;

        dumpCommandSender(commandSender);
        dumpCommand(command);
        dumpStrings(s,strings);


        if(strings.length > 0)
        {
            switch(strings[0])
            {
                case "add":
                {
                    if(strings.length> 1)
                    {
                        ApiTest.log.info("ApiTest: Adding " + strings[1] + " to be tracked");
                        // getServer().getPluginManager().registerEvents( this, this );
                        String strClass = "com.minecats.cindyk.apitest.EventsPlayer."+strings[1]+"Handler";
                        try {
                            Class c = Class.forName(strClass);
                            Object o = c.newInstance();
                            ApiTest.log.info("Object Type: " + o.getClass().getName());
                            plugin.getServer().getPluginManager().registerEvents((Listener)o , plugin);
                            handlers.put(strClass,o);
                        }
                        catch (ClassNotFoundException ex)
                        {
                            ApiTest.log.info("Class not found : " + strClass);
                        }
                        catch(InstantiationException ex)
                        {
                            ApiTest.log.info("Couldn't instantiate : " + strClass);
                            ex.printStackTrace();
                        }
                        catch(IllegalAccessException ex)
                        {
                            ApiTest.log.info("Illegal Access Exception : " + strClass);
                            ex.printStackTrace();
                        }


                    }
                }
                break;

                case "remove":
                    ApiTest.log.info("ApiTest: Removing " + strings[1] + " to be tracked");
                    // getServer().getPluginManager().registerEvents( this, this );
                    String strClass = strings[1]+"Handler";
                    Object o = handlers.get(strClass);
                    HandlerList.unregisterAll((Listener)o);
                    handlers.remove(strClass);


                break;

            }
        }


        return false;
    }


    void dumpCommandSender(CommandSender commandSender)
    {
        ApiTest.log.info("onCommand: CommandSender: getName = "+commandSender.getName());
        ApiTest.log.info("onCommand: CommandSender: toString = "+commandSender.toString());
        ApiTest.log.info("onCommand: CommandSender: getServer = "+commandSender.getServer().toString()); //Do we want to dump Server?
        ApiTest.log.info("onCommand: CommandSender: isOp = " + commandSender.isOp());
        if(commandSender instanceof Player)
            ApiTest.log.info("onCommand: CommandSender: Player Object");
        if(commandSender instanceof ConsoleCommandSender)
            ApiTest.log.info("onCommand: CommandSender: Console");
    }

    void dumpCommand(Command command)
    {

    }

    void dumpStrings(String s, String[] strings)
    {
        ApiTest.log.info("onCommand: String s : s = " + s);
        ApiTest.log.info("onCommand: String[] strings : size = "+strings.length);
        for(String x:strings)
            ApiTest.log.info("onCommand: String[] strings : = " + x);

    }
}
