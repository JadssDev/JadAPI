package dev.jadss.jadapi.utils.reflection.reflectors;

import dev.jadss.jadapi.utils.reflection.Reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static dev.jadss.jadapi.utils.reflection.reflectors.ReflectionUtils.CONSOLE;
import static dev.jadss.jadapi.utils.reflection.reflectors.ReflectionUtils.isDebugEnabled;

public final class JMethodReflector implements Reflection {

    private JMethodReflector() {
        //Utility class
    }

    //Unspecific

    public static Method getUnspecificMethod(Class<?> clazz, Class<?>[] parameters) {
        if (isDebugEnabled())
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eFinding &3Unspecific method &ein &a" + clazz.getName() + " &ewith parameters &b" + Arrays.toString(parameters) + "&e.");

        List<Method> methods = getMethods(clazz, true).stream()
                .filter(c -> Arrays.equals(c.getParameterTypes(), parameters))
                .collect(Collectors.toCollection(ArrayList::new));

        if (methods.size() != 1) {
            if (isDebugEnabled())
                CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eCouldn't find a &b&lmatch&e. ( " + ("Size => " + methods.size()) + " )");

            return null;
        }

        if (isDebugEnabled())
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eFound &3matching method &ewith parameters&e!!");

        return methods.get(0);
    }

    public static Object executeUnspecificMethod(Class<?> clazz, Class<?>[] parameters, Object object, Object[] inputParameters) {
        if (isDebugEnabled()) {
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eGetting &3method &ein &a" + clazz.getName() + " &ewith parameters &b" + Arrays.toString(parameters) + "&e.");
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eProvided &3Input Parameters &a&m->&a " + Arrays.toString(inputParameters));
        }

        Method method = getUnspecificMethod(clazz, parameters);

        if (method == null) {
            if (isDebugEnabled())
                CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eCouldn't find a &b&lmatch&e.");

            return null;
        }

        if (isDebugEnabled()) {
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eFound &3matching method &ewith parameters&e!!");
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eExecuting method now!");
        }

        try {
            Object result = method.invoke(object, inputParameters);
            if (isDebugEnabled())
                CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eMethod returned " + result);
            return result;
        } catch (IllegalAccessException | InvocationTargetException ex) {
            if (isDebugEnabled()) {
                CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eCould not execute Method.");
                ex.printStackTrace();
            }

            return null;
        }
    }

    public static Method getUnspecificMethod(Class<?> clazz, Class<?>[] parameters, Class<?> returnType) {
        if (isDebugEnabled())
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eFinding &3Unspecific method &ein &a" + clazz.getName() + " &ewith parameters &b" + Arrays.toString(parameters) + " &eand with return type &3" + returnType.getName() + "&e.");

        List<Method> methods = getMethods(clazz, true).stream()
                .filter(m -> Arrays.equals(m.getParameterTypes(), parameters))
                .filter(m -> returnType.equals(m.getReturnType()))
                .collect(Collectors.toCollection(ArrayList::new));

        if (methods.size() != 1) {
            if (isDebugEnabled())
                CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eCouldn't find a &b&lmatch&e. ( " + ("Size => " + methods.size()) + " )");

            return null;
        }

        if (isDebugEnabled())
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eFound &3matching method &ewith parameters&e!!");

        return methods.get(0);
    }

    public static <T> T executeUnspecificMethod(Class<?> clazz, Class<?>[] parameters, Class<T> returnType, Object object, Object[] inputParameters) {
        if (isDebugEnabled()) {
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eGetting &3method &ein &a" + clazz.getName() + " &ewith parameters &b" + Arrays.toString(parameters) + " &eand with return type &3" + returnType.getName() + "&e.");
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eProvided &3Input Parameters &a&m->&a " + Arrays.toString(inputParameters));
        }

        Method method = getUnspecificMethod(clazz, parameters, returnType);

        if (method == null) {
            if (isDebugEnabled())
                CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eCouldn't find a &b&lmatch&e.");

            return null;
        }

        if (isDebugEnabled()) {
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eFound &3matching method &ewith parameters&e!!");
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eExecuting method now!");
        }

        try {
            Object result = method.invoke(object, inputParameters);
            if (isDebugEnabled())
                CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eMethod returned " + result);
            return (T) result;
        } catch (IllegalAccessException | InvocationTargetException ex) {
            if (isDebugEnabled()) {
                CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eCould not execute Method.");
                ex.printStackTrace();
            }

            return null;
        }
    }

    //Specific

    public static Method getMethod(Class<?> clazz, String methodName) {
        if (isDebugEnabled())
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eFinding &3Unspecific method &ein &a" + clazz.getName() + " &ewith name &3" + methodName + "&e.");

        List<Method> methods = getMethods(clazz, true).stream()
                .filter(m -> m.getName().equals(methodName))
                .collect(Collectors.toCollection(ArrayList::new));

        if (methods.size() != 1) {
            if (isDebugEnabled())
                CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eCouldn't find a &b&lmatch&e. ( " + ("Size => " + methods.size()) + " )");

            return null;
        }

        if (isDebugEnabled())
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eFound &3matching method &ewith name&e!!");

        return methods.get(0);
    }

    public static Object executeMethod(Class<?> clazz, String methodName, Object object, Object[] inputParameters) {
        if (isDebugEnabled()) {
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eGetting &3method &ein &a" + clazz.getName() + "&e.");
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eProvided &3Input Parameters &a&m->&a " + Arrays.toString(inputParameters));
        }

        Method method = getMethod(clazz, methodName);

        if (method == null) {
            if (isDebugEnabled())
                CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eCouldn't find a &b&lmatch&e.");

            return null;
        }

        if (isDebugEnabled()) {
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eFound &3matching method &ewith parameters&e!!");
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eExecuting method now!");
        }

        try {
            Object result = method.invoke(object, inputParameters);
            if (isDebugEnabled())
                CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eMethod returned " + result);
            return result;
        } catch (IllegalAccessException | InvocationTargetException ex) {
            if (isDebugEnabled()) {
                CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eCould not execute Method.");
                ex.printStackTrace();
            }

            return null;
        }
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>[] parameters) {
        if (isDebugEnabled())
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eFinding &3Unspecific method &ein &a" + clazz.getName() + " &ewith name &3" + methodName + " &eand with parameters &3" + Arrays.toString(parameters) + "&e.");

        List<Method> methods = getMethods(clazz, true).stream()
                .filter(m -> m.getName().equals(methodName))
                .filter(m -> Arrays.equals(m.getParameterTypes(), parameters))
                .collect(Collectors.toCollection(ArrayList::new));

        if (methods.size() != 1) {
            if (isDebugEnabled())
                CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eCouldn't find a &b&lmatch&e. ( " + ("Size => " + methods.size()) + " )");

            return null;
        }

        if (isDebugEnabled())
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eFound &3matching method &ewith name &eand parameters!!");

        return methods.get(0);
    }

    public static Object executeMethod(Class<?> clazz, String methodName, Class<?>[] parameters, Object object, Object[] inputParameters) {
        if (isDebugEnabled()) {
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eGetting &3method &ein &a" + clazz.getName() + "&e.");
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eProvided &3Input Parameters &a&m->&a " + Arrays.toString(inputParameters));
        }

        Method method = getMethod(clazz, methodName, parameters);

        if (method == null) {
            if (isDebugEnabled())
                CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eCouldn't find a &b&lmatch&e.");

            return null;
        }

        if (isDebugEnabled()) {
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eFound &3matching method &ewith parameters&e!!");
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eExecuting method now!");
        }

        try {
            Object result = method.invoke(object, inputParameters);
            if (isDebugEnabled())
                CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eMethod returned " + result);
            return result;
        } catch (IllegalAccessException | InvocationTargetException ex) {
            if (isDebugEnabled()) {
                CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eCould not execute Method.");
                ex.printStackTrace();
            }

            return null;
        }
    }

    //Global

    public static List<Method> getMethods(Class<?> clazz, boolean includeStatics) {
        if (isDebugEnabled())
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eFinding every &3method &ein " + clazz.getName() + " ... (Contain static methods? " + includeStatics + ")");

        List<Method> methods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> !m.isBridge())
                .collect(Collectors.toCollection(ArrayList::new));;

        if (!includeStatics)
            methods = methods.stream()
                    .filter(m -> !Modifier.isStatic(m.getModifiers()))
                    .collect(Collectors.toCollection(ArrayList::new));

        methods.forEach(method -> method.setAccessible(true)); //Big brain move.

        if (isDebugEnabled()) {
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eFound &3" + methods.size() + " &lMethods&e!");
            for (Method method : methods)
                CONSOLE.sendMessage("&c&m->&a " + method.getName() + "&e(Accepts -> &3" + Arrays.toString(method.getParameterTypes()) + "&e; Returns -> &b" + method.getReturnType() + "&e)");
        }

        return methods;
    }
}
