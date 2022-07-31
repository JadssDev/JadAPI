package dev.jadss.jadapi.utils.reflection.reflectors;

import static dev.jadss.jadapi.utils.reflection.reflectors.ReflectionUtils.CONSOLE;
import static dev.jadss.jadapi.utils.reflection.reflectors.ReflectionUtils.isDebugEnabled;

public class JClassReflector {

    private JClassReflector() {
        //Utility class
    }

    public static Class<?> getClass(String classPath) {
        try {
            if (isDebugEnabled())
                CONSOLE.sendMessage("&3&lJadAPI &c&lReflection Debug &7>> &eAttempting to get &b" + classPath + " &3class&e.");
            return Class.forName(classPath);
        } catch (ClassNotFoundException ex) {
            if (isDebugEnabled()) {
                CONSOLE.sendMessage("&3&lJadAPI &c&lReflection Debug &7>> &cFailed to get class.");
                ex.printStackTrace();
            }
            return null;
        }
    }

    public static Object cast(Class<?> caster, Object castObject) {
        try {
            if (isDebugEnabled())
                CONSOLE.sendMessage("&3&lJadAPI &c&lReflection Debug &7>> &eAttempting to cast &b" + castObject.getClass().getName() + " &eto &b" + caster.getName() + "&e.");
            return caster.cast(castObject);
        } catch (ClassCastException ex) {
            if (isDebugEnabled()) {
                CONSOLE.sendMessage("&3&lJadAPI &c&lReflection Debug &7>> &cFailed to cast.");
                ex.printStackTrace();
            }
            return null;
        }
    }
}
