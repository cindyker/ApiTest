package com.minecats.cindyk.apitest.Events;

import com.google.common.collect.Lists;
import com.minecats.cindyk.apitest.ApiTest;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.EventExecutor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by cindy on 10/11/2014.
 */
public class DummyListener implements Listener, EventExecutor {

    @Override
    public void execute(Listener listener, Event event) throws EventException {

        String fieldInfo = "";
        if(!ApiTest.listenedTo.contains(event.getClass()))
            return;

        ApiTest.log.info("-----------------------------------------------------");
        ApiTest.log.info("*** -> Event Called : " + event.getClass().getSimpleName());

        Iterable<Field> flist = getFieldsUpTo(event.getClass(),null);

        for(Field ff : flist)
        {
            fieldInfo = ff.getName();
            ff.setAccessible(true);
            try {
                switch (ff.getType().getSimpleName()) {
                    case "int":
                        fieldInfo += " Integer: " + ff.getInt(event);
                        break;
                    case "boolean":
                        fieldInfo += " Boolean: " + ff.getBoolean(event);
                        break;
                    case "String":
                        String s = (String) ff.get(event);
                        fieldInfo += " String: " + s;
                        break;
                    case "Player":
                        Player p = (Player)ff.get(event);
                        fieldInfo +=  " Player: " + ((p==null)?"null":p.getName());
                        break;
                    case "Item":
                        Item ii = (Item) ff.get(event);
                        fieldInfo += " Item: " + ((ii==null)?"null":(ii.getItemStack().getType().name() + " Qty: " + ii.getItemStack().getAmount()));
                        break;
                    case "ItemStack":
                        ItemStack is = (ItemStack) ff.get(event);
                        fieldInfo +=  " ItemStack: " + ((is == null)?"null":(is.getType().name() + " Qty: " + is.getAmount()));
                        break;
                    case "List":
                        List ll = (List)ff.get(event);
                        fieldInfo += " List size: " + ll.size() + " of type: " +((ll.size()>0)? ll.get(0).getClass().getSimpleName():" Empty List");
                        break;
                    case "Location":
                        Location loc = (Location)ff.get(event);
                        fieldInfo += " Location: " +((loc==null)?"null":loc.toString());
                        break;
                    case "Block":
                        Block bloc = (Block)ff.get(event);
                        fieldInfo += " Block: " +((bloc==null)?"null":bloc.getType().name());
                        break;
                    case "World":
                        World w = (World)ff.get(event);
                        fieldInfo += " World: " +((w==null)?"null":w.getName());
                        break;
                    default: {
                        try { //name is common.. lets do that if we can.
                            Object o1 = ff.get(event);
                            Method methodName;
                            methodName = o1.getClass().getMethod("name", null);
                            fieldInfo += " " + ff.getType().getSimpleName()+".name : " + methodName.invoke(o1);
                        }
                        catch(NoSuchMethodException ex) {
                            fieldInfo += "Class not supported - " + ff.getType().getSimpleName();
                        }
                        catch(InvocationTargetException ex)
                        {
                            fieldInfo += "Class not supported - " + ff.getType().getSimpleName() + " (ite)";
                        }
                    }
                }
            }
            catch(IllegalAccessException ex)
            {
                ApiTest.log.info("Field needs its accessible set correctly.");
                ex.printStackTrace();
            }

            ApiTest.log.info("Field  : " + fieldInfo);

        }


        Method[] allMethods = event.getClass().getDeclaredMethods();
        for (Method m : allMethods) {
            String mname = m.getName();

            Type[] pType = m.getGenericParameterTypes();

            if(m.getParameterTypes().length>0)
                ApiTest.log.info("Method : " + mname + " - return type - " + m.getReturnType().getSimpleName() + " has params.");
            else
            {
                try {
                    switch (m.getReturnType().getSimpleName()) {
                        case "String":
                            String ms = (String)m.invoke(event);
                            ApiTest.log.info("Method : " + mname + " - return String - " + ms);
                            break;
                        case "boolean":
                            boolean mb = (boolean)m.invoke(event);
                            ApiTest.log.info("Method : " + mname + " - return boolean - " + mb);
                            break;
                        case "int":
                            int i = (int) m.invoke(event);
                            ApiTest.log.info("Method : " + mname + " - returns int: " + i);
                            break;
                        case "ItemStack":
                            ItemStack is = (ItemStack) m.invoke(event);
                            ApiTest.log.info("Method : " + mname + " - returns ItemStack: " + ((is==null) ? "null" : (is.getType().name() + " Qty: " + is.getAmount())));
                            break;
                        case "Location":
                            Location methLoc = (Location) m.invoke(event);
                            ApiTest.log.info("Method : " + mname + " - returns Location: " + ((methLoc==null)?"null":methLoc.toString()));
                            break;
                        case "Block":
                            Block methBlock = (Block)m.invoke(event);
                            ApiTest.log.info("Method : " + mname + " - returns Block: " + ((methBlock==null)?"null":methBlock.getType().name()));
                            break;
                        case "World":
                            World methWorld = (World)m.invoke(event);
                            ApiTest.log.info("Method : " + mname + " - returns World: " + ((methWorld==null)?"null":methWorld.getName()));
                            break;
                        default:
                           try { //name is common.. lets do that if we can.
                               Object o = m.invoke(event);
                               if(o!=null) {
                                   Method methodName;
                                   methodName = o.getClass().getMethod("name");
                                   ApiTest.log.info("Method : " + mname + " : " + m.getReturnType().getSimpleName() + ".name : " + methodName.invoke(o));
                               }
                               else{ ApiTest.log.info("Method : " + mname + " - return type - " + m.getReturnType().getSimpleName());  }
                           }
                           catch(NoSuchMethodException ex)
                           {  ApiTest.log.info("Method : " + mname + " - return type - " + m.getReturnType().getSimpleName());  }

                    }
                } catch (InvocationTargetException ex) {
                    ApiTest.log.info("InvocationTargetException on Event : " + event.getClass().getSimpleName());
                    ex.printStackTrace();
                } catch (IllegalAccessException ex) {
                    ApiTest.log.info("IllegalAccessException on Event : " + event.getClass().getSimpleName());
                    ex.printStackTrace();
                }
            }

        }
    }

    public static Iterable<Field> getFieldsUpTo(@Nonnull Class<?> startClass,
                                                @Nullable Class<?> exclusiveParent) {

        List<Field> currentClassFields = Lists.newArrayList(startClass.getDeclaredFields());
        Class<?> parentClass = startClass.getSuperclass();

        if (parentClass != null &&
                (exclusiveParent == null || !(parentClass.equals(exclusiveParent)))) {
            List<Field> parentClassFields =
                    (List<Field>) getFieldsUpTo(parentClass, exclusiveParent);
            currentClassFields.addAll(parentClassFields);
        }

        return currentClassFields;
    }
}
