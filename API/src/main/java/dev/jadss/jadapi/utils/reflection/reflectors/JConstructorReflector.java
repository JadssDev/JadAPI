package dev.jadss.jadapi.utils.reflection.reflectors;

import dev.jadss.jadapi.utils.reflection.Reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import static dev.jadss.jadapi.utils.reflection.reflectors.ReflectionUtils.CONSOLE;
import static dev.jadss.jadapi.utils.reflection.reflectors.ReflectionUtils.isDebugEnabled;

public final class JConstructorReflector implements Reflection {

    private JConstructorReflector() {
        //Utility class
    }

    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>[] parameters) {
        if (isDebugEnabled())
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eFinding &3constructor &ein &a" + clazz.getName() + " &ewith parameters &b" + Arrays.toString(parameters) + "&e.");

        Constructor<?> constructor = getConstructors(clazz).stream()
                .filter(c -> Arrays.equals(c.getParameterTypes(), parameters))
                .findFirst().orElse(null);

        if (constructor == null) {
            if (isDebugEnabled())
                CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eCouldn't find a &b&lmatch&e.");

            return null;
        }

        if (isDebugEnabled())
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eFound &3matching constructor &ewith parameters&e!!");

        return constructor;
    }

    public static Object executeConstructor(Class<?> clazz, Class<?>[] parameters, Object... inputParamters) {
        if (isDebugEnabled()) {
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eGetting &3constructor &ein &a" + clazz.getName() + " &ewith parameters &b" + Arrays.toString(parameters) + "&e.");
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eProvided &3Input Parameters &a&m->&a " + Arrays.toString(inputParamters));
        }

        Constructor<?> constructor = getConstructor(clazz, parameters);

        if (constructor == null) {
            if (isDebugEnabled())
                CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eCouldn't find a &b&lmatch&e.");

            return null;
        }

        if (isDebugEnabled()) {
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eFound &3matching constructor &ewith parameters&e!!");
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eBuilding constructor now!");
        }

        try {
            return constructor.newInstance(inputParamters);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException ex) {
            if (isDebugEnabled()) {
                CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eCould not build Constructor.");
                ex.printStackTrace();
            }

            return null;
        }
    }

    public static List<Constructor<?>> getConstructors(Class<?> clazz) {
        if (isDebugEnabled())
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eFinding every &3constructor &ein " + clazz.getName() + " ...");

        List<Constructor<?>> constructors = Arrays.asList(clazz.getDeclaredConstructors());

        constructors.forEach(constructor -> constructor.setAccessible(true)); //Big brain move.

        if (isDebugEnabled())
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eFound &3" + constructors.size() + " &lConstructors&e!");

        return constructors;
    }
}
