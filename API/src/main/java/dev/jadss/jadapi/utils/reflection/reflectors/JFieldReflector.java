package dev.jadss.jadapi.utils.reflection.reflectors;

import dev.jadss.jadapi.utils.reflection.IndexGetter;
import dev.jadss.jadapi.utils.reflection.Reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static dev.jadss.jadapi.utils.reflection.reflectors.ReflectionUtils.CONSOLE;
import static dev.jadss.jadapi.utils.reflection.reflectors.ReflectionUtils.isDebugEnabled;

public final class JFieldReflector implements Reflection {

    private JFieldReflector() {
        //Utility class
    }

    //Unspecific

    public static Field getUnspecificField(Class<?> clazz, Class<?> fieldType, IndexGetter index) {
        if (isDebugEnabled())
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eFinding &a&lUnspecific &3field &ein &a" + clazz.getName() + " &ewith Type " + fieldType.getName() + "&e.");

        List<Field> fields = getFields(clazz, true).stream()
                .filter(f -> f.getType().equals(fieldType))
                .collect(Collectors.toCollection(ArrayList::new));

        int finalIndex = index.getIndex(fields.size() - 1);

        if (finalIndex > fields.size() - 1) {
            throw new UnsupportedOperationException("Index " + finalIndex + " is out of bounds.");
        }

        Field field = fields.get(finalIndex);

        if (isDebugEnabled())
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eFound &3matching field &ewith type &b" + fieldType.getName() + "&e!!");

        return field;
    }

    public static Field getUnspecificField(Class<?> clazz, Class<?> fieldType) {
        return getUnspecificField(clazz, fieldType, (i) -> 0);
    }

    public static <T> T getObjectFromUnspecificField(Class<?> clazz, Class<T> fieldType, IndexGetter index, Object object) {
        if (isDebugEnabled())
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eGetting &3field &ein &a" + clazz.getName() + " &ewith Type " + fieldType.getName() + "&e.");

        Field field = getUnspecificField(clazz, fieldType, index);

        if (field == null) {
            if (isDebugEnabled())
                CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eCouldn't find a &b&lmatch&e.");

            return null;
        }

        if (isDebugEnabled()) {
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eFound &3matching field &ewith type &b" + fieldType.getName() + "&e!!");
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eGetting field now!");
        }

        try {
            Object result = field.get(object);
            if (isDebugEnabled())
                CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eGot field, result -> " + result);
            return (T) result;
        } catch (IllegalAccessException | ClassCastException ex) {
            if (isDebugEnabled()) {
                CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eCould not get Field.");
                ex.printStackTrace();
            }

            return null;
        }
    }

    public static <T> T getObjectFromUnspecificField(Class<?> clazz, Class<T> fieldType, Object object) {
        return getObjectFromUnspecificField(clazz, fieldType, (i) -> 0, object);
    }

    public static void setObjectToUnspecificField(Class<?> clazz, Class<?> fieldType, IndexGetter index, Object object, Object newFieldValue) {
        if (isDebugEnabled())
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eSetting &3field &ein &a" + clazz.getName() + " &ewith Type " + fieldType.getName() + "&e.");

        Field field = getUnspecificField(clazz, fieldType, index);

        if (field == null) {
            if (isDebugEnabled())
                CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eCouldn't find a &b&lmatch&e.");
        }

        if (isDebugEnabled()) {
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eFound &3matching field &ewith type &b" + fieldType.getName() + "&e!!");
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eSetting field now!");
        }

        try {
            field.set(object, newFieldValue);
        } catch (IllegalAccessException | ClassCastException ex) {
            if (isDebugEnabled()) {
                CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eCould not get Field.");
                ex.printStackTrace();
            }
        }
    }

    public static void setObjectToUnspecificField(Class<?> clazz, Class<?> fieldType, Object object, Object newFieldValue) {
        setObjectToUnspecificField(clazz, fieldType, (i) -> 0, object, newFieldValue);
    }

    //Specific

    public static Field getField(Class<?> clazz, String fieldName) {
        if (isDebugEnabled())
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eFinding &3field &ein &a" + clazz.getName() + " &ewith name &b" + fieldName + "&e.");

        Field field = getFields(clazz, true).stream()
                .filter(f -> f.getName().equals(fieldName))
                .findFirst().orElse(null);

        if (field == null) {
            if (isDebugEnabled())
                CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eCouldn't find a &b&lmatch&e.");

            return null;
        }

        if (isDebugEnabled())
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eFound &3matching field &ewith name &b" + fieldName + "&e!!");

        return field;
    }

    public static Object getObjectFromField(Class<?> clazz, String fieldName, Object object) {
        if (isDebugEnabled())
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eGetting &3field &ein &a" + clazz.getName() + " &ewith name &b" + fieldName + "&e.");

        Field field = getField(clazz, fieldName);

        if (field == null) {
            if (isDebugEnabled())
                CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eCouldn't find a &b&lmatch&e.");

            return null;
        }

        if (isDebugEnabled()) {
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eFound &3matching field &ewith name &b" + fieldName + "&e!!");
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eGetting field now!");
        }

        try {
            Object result = field.get(object);
            if (isDebugEnabled())
                CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eGot field, result -> " + result);
            return result;
        } catch (IllegalAccessException ex) {
            if (isDebugEnabled()) {
                CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eCould not get Field.");
                ex.printStackTrace();
            }

            return null;
        }
    }

    public static void setObjectToField(Class<?> clazz, String fieldName, Object object, Object newFieldValue) {
        if (isDebugEnabled())
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eSetting &3field &ein &a" + clazz.getName() + " &ewith name &b" + fieldName + "&e.");

        Field field = getField(clazz, fieldName);

        if (field == null) {
            if (isDebugEnabled())
                CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eCouldn't find a &b&lmatch&e.");

            return;
        }

        if (isDebugEnabled()) {
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eFound &3matching field &ewith name &b" + fieldName + "&e!!");
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eSetting field now!");
        }

        try {
            field.set(object, newFieldValue);
        } catch (IllegalAccessException ex) {
            if (isDebugEnabled()) {
                CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eCould not set Field.");
                ex.printStackTrace();
            }
        }
    }


    //Globals

    public static List<Field> getFields(Class<?> clazz, boolean includeStatics) {
        if (isDebugEnabled())
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eFinding every &3field &ein " + clazz.getName() + " ... (Contain static fields? " + includeStatics + ")");

        List<Field> fields = Arrays.asList(clazz.getDeclaredFields());
        if (!includeStatics)
            fields = fields.stream()
                    .filter(field -> !Modifier.isStatic(field.getModifiers()))
                    .collect(Collectors.toCollection(ArrayList::new));

        fields.forEach(field -> field.setAccessible(true)); //Big brain move.

        if (isDebugEnabled()) {
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eFound &3" + fields.size() + " &lFields&e!");
            for (Field field : fields)
                CONSOLE.sendMessage("&c&m->&a " + field.getName() + "&e(Type -> &b" + field.getType() + "&e)");
        }

        return fields;
    }

    public static List<Object> getObjectsFromFields(Class<?> clazz, Object object, boolean includeStatics) {
        if (isDebugEnabled()) {
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eGetting every &3field &ein " + clazz.getName() + " ... (Contain static fields? " + includeStatics + ")");
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eCalling &bGet Fields&e...");
        }

        List<Object> list = getFields(clazz, includeStatics).stream().map(field -> {
            if (isDebugEnabled())
                CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eGetting Field &a" + field.getName() + "&e...");

            try {
                return field.get(object);
            } catch (IllegalAccessException ex) {
                if (isDebugEnabled()) {
                    CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eCould not get &3Field &a" + field.getName() + "&e.");
                    ex.printStackTrace();
                }

                return null;
            }
        }).collect(Collectors.toCollection(ArrayList::new));

        if (isDebugEnabled())
            CONSOLE.sendMessage("&3JadAPI &cReflection Debug &7>> &eGot a total of &3" + list.size() + " &lFields&e.");

        return list;
    }
}
