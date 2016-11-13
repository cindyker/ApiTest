ApiTest
=======
Test Events from a Bukkit Minecraft Server.
-------------------------------------------

This bukkit plugin allows you to add event listeners in-game, while playing on the server.
Information from the objects returned to the event when fired are written to the Minecraft server log.

For some events this can be very very spammy. The purpose here is to gain an understanding of when an event fires or
to test that an event is firing correctly and returning the correct information. 

I have mainly used this plugin to help test updated releases of Bukkit / Spigot.

The Javadocs for Bukkit will help you get the event names needed: [JavaDocs](https://hub.spigotmc.org/javadocs/bukkit/)


The plugin has one main command:

/apitest

The subcommands are:

   add eventname

   remove eventname

   list

   notify

   quiet

Source
------
Source code is currently available on [GitHub].

Binaries
--------
Precompiled binaries are available for end users on [SpigotMc.org](https://www.spigotmc.org).

(c) 2016 cindyker.