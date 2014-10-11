package com.minecats.cindyk.apitest.EventsPlayer;

import com.minecats.cindyk.apitest.ApiTest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Locale;

/**
 * Created by Cindy on 10/9/2014.
 */
public class PlayerJoinEventHandler implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event)
    {
        ApiTest.log.info("Event Called : " + event.getClass().getSimpleName());

        Field[] allFields = event.getClass().getDeclaredFields();
        for(Field f: allFields)
        {
            String fname = f.getName();
            String fvalue = "";
            if(f.getType().getName().equalsIgnoreCase("String"))
            {
                fvalue = f.toString();
            }
            ApiTest.log.info("Field: " + fname + " - " + fvalue);

        }
        //Object t = c.newInstance();
        Method[] allMethods = event.getClass().getDeclaredMethods();
        for (Method m : allMethods) {
            String mname = m.getName();

            Type[] pType = m.getGenericParameterTypes();
           /* if ((pType.length != 1)
                    || Locale.class.isAssignableFrom(pType[0].getClass())) {
                continue;
            }*/
            if(m.getGenericReturnType().toString().equalsIgnoreCase("String"))
            {
                 try {
                     String s =(String) m.invoke(event);
                     ApiTest.log.info(this.getClass().getSimpleName() + " : " + mname + " - ");
                 }
                 catch(InvocationTargetException ex)
                 {
                     ApiTest.log.info("InvocationTargetException on Event : " + event.getClass().getSimpleName());
                     ex.printStackTrace();
                 }
                 catch(IllegalAccessException ex) {
                     ApiTest.log.info("IllegalAccessException on Event : " + event.getClass().getSimpleName());
                     ex.printStackTrace();
                 }

            }
            else
                ApiTest.log.info(this.getClass().getSimpleName() + " : " + mname + " - return type - " + m.getGenericReturnType().getClass().getName());
        }
    }
}
