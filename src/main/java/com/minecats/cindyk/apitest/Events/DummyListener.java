package com.minecats.cindyk.apitest.Events;

import com.google.common.collect.Lists;
import com.minecats.cindyk.apitest.ApiTest;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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
                        fieldInfo +=  " Player: " + p.getName();
                        break;
                    case "BlockFace":
                        BlockFace b = (BlockFace)ff.get(event);
                        fieldInfo += " BlockFace: " + b.name();
                        break;
                    case "Block":
                        Block bl = (Block)ff.get(event);
                        fieldInfo += " Block: " + bl.getType().name();
                        break;
                    case "Action":
                        Action a = (Action) ff.get(event);
                        fieldInfo += " Action: " + a.name();
                        break;
                    case "Entity":
                        Entity ent = (Entity) ff.get(event);
                        fieldInfo += " Entity: " + ent.getType().name();
                    break;
                    case "Item":
                        Item ii = (Item) ff.get(event);
                        fieldInfo += " Item: " + ii.getItemStack().getType().name() + " Qty: " + ii.getItemStack().getAmount();
                        break;
                    case "ItemStack":
                        ItemStack is = (ItemStack) ff.get(event);
                        fieldInfo +=  " ItemStack: " + ((is == null)?"null":(is.getType().name() + " Qty: " + is.getAmount()));
                        break;
                    case "Material":
                        Material mm = (Material) ff.get(event);
                        fieldInfo +=  " Material: " + ((mm == null) ? "null" : mm.name());
                        break;
                    case "List":
                        List ll = (List)ff.get(event);
                        if(ll.size()>0)
                            fieldInfo += " List size: " + ll.size() + " of type: " + ll.get(0).getClass().getSimpleName();
                        else
                           fieldInfo +=  " List size: " + ll.size();
                     break;
                    case "Statistic":
                        Statistic ss= (Statistic)ff.get(event);
                        fieldInfo += " Statistic: " + ((ss==null)?"null":ss.name());
                        break;
                    case "Location":
                        Location loc = (Location)ff.get(event);
                        fieldInfo += " Location: " +((loc==null)?"null":loc.toString());
                        break;
                    default: {
                        // ApiTest.log.info("Class not supported " + ff.getType().getName());
                        fieldInfo += "Class not supported - "+ff.getType().getSimpleName();
                    }
                }
            }
            catch(IllegalAccessException ex)
            {
                ApiTest.log.info("Field needs its accessible set correctly.");
                ex.printStackTrace();
            }

            ApiTest.log.info("Field: " + fieldInfo);

        }


        Method[] allMethods = event.getClass().getDeclaredMethods();
        for (Method m : allMethods) {
            String mname = m.getName();

            Type[] pType = m.getGenericParameterTypes();
           /* if ((pType.length != 1)
                    || Locale.class.isAssignableFrom(pType[0].getClass())) {
                continue;
            }*/
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
                            ApiTest.log.info("Method : " + mname + " - returns int: " + m.getReturnType().getSimpleName());
                            break;
                        case "Entity":
                            Entity mEnt = (Entity) m.invoke(event);
                            ApiTest.log.info("Method : " + mname + " - returns Entity: " + ( (mEnt==null)? "null" : (mEnt.getType().name())));
                            break;
                        case "ItemStack":
                            ItemStack is = (ItemStack) m.invoke(event);
                            ApiTest.log.info("Method : " + mname + " - returns ItemStack: " + ((is==null) ? "null" : (is.getType().name() + " Qty: " + is.getAmount())));
                            break;
                        case "Material":
                            Material methmat = (Material) m.invoke(event);
                            ApiTest.log.info("Method : " + mname + "- returns Material: " + ((methmat == null) ? "null" : methmat.name()));
                            break;
                        case "Action":
                            Action methact = (Action) m.invoke(event);
                            ApiTest.log.info("Method : " + mname + "- returns Action: " + methact.name());
                            break;
                        case "BlockFace":
                            BlockFace methbf = (BlockFace) m.invoke(event);
                            ApiTest.log.info("Method : " + mname + "- returns BlockFace: " + methbf.name());
                            break;
                        case "Result":
                            Event.Result methResult = (Event.Result) m.invoke(event);
                            ApiTest.log.info("Method : " + mname + "- returns Result: " + methResult.name() );
                            break;
                        case "Statistic":
                            Statistic methStat = (Statistic) m.invoke((event));
                            ApiTest.log.info("Method : " + mname + "- returns Statistic: " + ((methStat==null)?"null":methStat.name()));
                            break;
                        case "Location":
                            Location methLoc = (Location) m.invoke(event);
                            ApiTest.log.info("Method : " + mname + "- returns Location: " + ((methLoc==null)?"null":methLoc.toString()));
                            break;
                        default:
                            ApiTest.log.info("Method : " + mname + " - return type - " + m.getReturnType().getSimpleName());

                    }
                } catch (InvocationTargetException ex) {
                    ApiTest.log.info("InvocationTargetException on Event : " + event.getClass().getSimpleName());
                    ex.printStackTrace();
                } catch (IllegalAccessException ex) {
                    ApiTest.log.info("IllegalAccessException on Event : " + event.getClass().getSimpleName());
                    ex.printStackTrace();
                }
            }

//            if(m.getGenericReturnType().getClass().getSimpleName().equalsIgnoreCase("String"))
//            {
//                try {
//                    String s =(String) m.invoke(event);
//                    ApiTest.log.info("Method : " + mname + " - " + s);
//                }
//                catch(InvocationTargetException ex)
//                {
//                    ApiTest.log.info("InvocationTargetException on Event : " + event.getClass().getSimpleName());
//                    ex.printStackTrace();
//                }
//                catch(IllegalAccessException ex) {
//                    ApiTest.log.info("IllegalAccessException on Event : " + event.getClass().getSimpleName());
//                    ex.printStackTrace();
//                }
//
//            }
//            else
//                ApiTest.log.info("Method : " + mname + " - return type - " + m.getGenericReturnType().getClass().getName());
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
