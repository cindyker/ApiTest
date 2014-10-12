package com.minecats.cindyk.apitest.Events;

import com.google.common.collect.Lists;
import com.minecats.cindyk.apitest.ApiTest;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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

        if(!ApiTest.listenedTo.contains(event.getClass()))
            return;

        ApiTest.log.info("-----------------------------------------------------");
        ApiTest.log.info("*** -> Event Called : " + event.getClass().getSimpleName());

        Iterable<Field> flist = getFieldsUpTo(event.getClass(),null);

        for(Field ff : flist)
        {
            ff.setAccessible(true);
            try {
                switch (ff.getType().getSimpleName()) {
                    case "int":
                        ApiTest.log.info(ff.getName() +" Integer: " + ff.getInt(event));
                        break;
                    case "boolean":
                        ApiTest.log.info(ff.getName() + " Boolean: " + ff.getBoolean(event));
                        break;
                    case "String":
                        String s = (String) ff.get(event);
                        ApiTest.log.info("String: " + s);
                        break;
                    case "Player":
                        Player p = (Player)ff.get(event);
                        ApiTest.log.info(ff.getName()+ " Player: " + p.getName());
                        break;
                    case "BlockFace":
                        BlockFace b = (BlockFace)ff.get(event);
                        ApiTest.log.info(ff.getName()+" BlockFace: " + b.name());
                        break;
                    case "Block":
                        Block bl = (Block)ff.get(event);
                        ApiTest.log.info(ff.getName()+" Block: " + bl.getType().name());
                        break;
                    case "Action":
                        Action a = (Action) ff.get(event);
                        ApiTest.log.info(ff.getName()+" Action: " + a.name());
                        break;
                    case "Entity":
                        Entity ent = (Entity) ff.get(event);
                        ApiTest.log.info(ff.getName()+" Entity: " + ent.getType().name());
                    break;
                    case "List":
                        List ll = (List)ff.get(event);
                        if(ll.size()>0)
                         ApiTest.log.info(ff.getName() + " List size: " + ll.size() + " of type: " + ll.get(0).getClass().getSimpleName() );
                        else
                         ApiTest.log.info(ff.getName() + " List size: " + ll.size());
                     break;
                    default: {
                        // ApiTest.log.info("Class not supported " + ff.getType().getName());
                        ApiTest.log.info("Class not supported: "+ff.getName()+" - "+ff.getType().getSimpleName());
                    }
                }
            }
            catch(IllegalAccessException ex)
            {
                ApiTest.log.info("Field needs its accessible set correctly.");
                ex.printStackTrace();
            }

        }


        Method[] allMethods = event.getClass().getDeclaredMethods();
        for (Method m : allMethods) {
            String mname = m.getName();

            Type[] pType = m.getGenericParameterTypes();
           /* if ((pType.length != 1)
                    || Locale.class.isAssignableFrom(pType[0].getClass())) {
                continue;
            }*/
            try {
                switch(m.getGenericReturnType().getClass().getSimpleName())
                {
                    case "String":
                        ApiTest.log.info("Method : " + mname + " - return type - " + m.getGenericReturnType().getClass().getName());
                        break;
                    case "boolean":
                        ApiTest.log.info("Method : " + mname + " - return type - " + m.getGenericReturnType().getClass().getName());
                        break;
                    case "int":
                        ApiTest.log.info("Method : " + mname + " - return type - " + m.getGenericReturnType().getClass().getName());
                        break;
                    case "Entity":
                            if(m.getGenericParameterTypes().length==0) {
                                Entity mEnt = (Entity) m.invoke(event);
                                ApiTest.log.info("Method : " + mname + " - return type - " + mEnt.getType().name());
                            }
                            else
                            {
                                ApiTest.log.info("Method : " + mname + " - return type - " + m.getGenericReturnType().getClass().getName() + " has params.");
                            }
                        break;
                    default:
                        ApiTest.log.info("Method : " + mname + " - return type - " + m.getGenericReturnType().getClass().getName());

                }
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

            if(m.getGenericReturnType().getClass().getSimpleName().equalsIgnoreCase("String"))
            {
                try {
                    String s =(String) m.invoke(event);
                    ApiTest.log.info("Method : " + mname + " - " + s);
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
                ApiTest.log.info("Method : " + mname + " - return type - " + m.getGenericReturnType().getClass().getName());
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
