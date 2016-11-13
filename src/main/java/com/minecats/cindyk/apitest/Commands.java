package com.minecats.cindyk.apitest;


import com.minecats.cindyk.apitest.Events.DummyListener;
import com.minecats.cindyk.apitest.Events.Listeners;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.HashMap;

/**
 * Created by Cindy on 10/8/2014.
 */
public class Commands implements CommandExecutor {

    ApiTest plugin;
    DummyListener dummyListener;

    static HashMap handlers;

    Commands(ApiTest plugin)
    {
       this.plugin = plugin;
       handlers = new HashMap<String,Object>();
        dummyListener = new DummyListener();
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

                        try {

                            Class cl = Class.forName("org.bukkit.event."+strings[1]);
                            plugin.getServer().getPluginManager().registerEvent(cl, dummyListener, EventPriority.HIGH,dummyListener,plugin);
                            commandSender.sendMessage("Registered "+ cl.getName() + " for listening.");
                            ApiTest.listenedTo.add(cl);
                        }
                        catch (ClassNotFoundException ex)
                        {
                            ApiTest.log.info("Class not found : " + strings[1]);
                            ApiTest.log.info("Exception : " + ex.getMessage());

                            commandSender.sendMessage(ChatColor.GOLD+"Class not found: " + strings[1]);
                        }

                    }
                }
                break;

                case "remove":
                    ApiTest.log.info("ApiTest: Removing " + strings[1] + " to be tracked");
                    try {
                        Class cl = Class.forName("org.bukkit.event." + strings[1]);
                        if(ApiTest.listenedTo.contains(cl)) {
                            ApiTest.listenedTo.remove(cl);
                            commandSender.sendMessage(ChatColor.GOLD+strings[1]+" will no longer be logged. (It's still going to fire.)");
                        }
                        else
                            commandSender.sendMessage(ChatColor.GOLD+strings[1]+" is not being logged right now.");

                    }
                    catch (ClassNotFoundException ex)
                    {
                        ApiTest.log.info("Class not found : " + strings[1]);
                        commandSender.sendMessage("Class not found: " + strings[1]);
                    }


                break;

                case "list":
                    commandSender.sendMessage(ChatColor.GOLD+"=== Events you are listening for ===");
                    for(Class event: ApiTest.listenedTo)
                    {
                        commandSender.sendMessage("==> "+event.getName());
                    }
                    break;

                case "notify":
                     //Add Player to list to be notified in game when events fire.

                    commandSender.sendMessage(ChatColor.GOLD+"=== "+commandSender.getName()+" will now receive ingame notifications of events.");
                    ApiTest.listening.addPlayer(commandSender.getName(), false);

                    break;

                case "quiet":

                    commandSender.sendMessage(ChatColor.GOLD+"=== "+commandSender.getName()+" will no longer receive ingame notifications of events.");
                    ApiTest.listening.removePlayer(commandSender.getName());

                    break;

                default:
                     helpinfo(commandSender);

            }
        }
        else{

           helpinfo(commandSender);

        }


        return false;
    }


    void dumpCommandSender(CommandSender commandSender)
    {
        ApiTest.log.info("onCommand: CommandSender: getName = "+commandSender.getName());
        ApiTest.log.info("onCommand: CommandSender: toString = " + commandSender.toString());
        ApiTest.log.info("onCommand: CommandSender: getServer = " + commandSender.getServer().toString()); //Do we want to dump Server?
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

    void helpinfo(CommandSender cs){

        cs.sendMessage(ChatColor.AQUA+"================================================");
        cs.sendMessage(ChatColor.GOLD+"Welcome to ApiTest.");
        cs.sendMessage(ChatColor.GOLD+"This is to test events. You will get informed when the events");
        cs.sendMessage(ChatColor.GOLD+"that you add are fired with some of their data.");
        cs.sendMessage(ChatColor.GOLD+"Example: apitest add player.PlayerAnimationEvent ");
        cs.sendMessage(ChatColor.AQUA+"=================================================");
        cs.sendMessage(ChatColor.GOLD+"Available sub-commands:");
        cs.sendMessage(ChatColor.GOLD+"  add [Bukkit Class]");
        cs.sendMessage(ChatColor.GOLD+"  remove [Bukkit Class]");
        cs.sendMessage(ChatColor.GOLD+"  list ");
        cs.sendMessage(ChatColor.GOLD+"  notify ");
        cs.sendMessage(ChatColor.GOLD+"  quiet ");
    }
}
