package dev.jadss.jadapi.utils;

import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.annotations.ForRemoval;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JReflection {

    private static String nmsVersion;

    public static void setupNMS() {
        String[] version = Bukkit.getServer().getClass().getPackage().getName().split("\\.");
        nmsVersion = version[version.length - 1];
    }

    public static String getNMSVersion() {
        return nmsVersion;
    }

    public static Class<?> getReflectionClass(String classPath) {
        try {
            if (isDebugEnabled())
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eAttempting to get &b" + classPath + " &3class&e."));
            return Class.forName(classPath);
        } catch (ClassNotFoundException ex) {
            if (isDebugEnabled()) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &cFailed to get class."));
                ex.printStackTrace();
            }
            return null;
        }
    }

    public static Object cast(Class<?> caster, Object castObject) {
        try {
            if (isDebugEnabled())
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eAttempting to cast &b" + castObject.getClass().getName() + " &eto &b" + caster.getName() + "&e."));
            return caster.cast(castObject);
        } catch (ClassCastException ex) {
            if (isDebugEnabled()) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &cFailed to cast."));
                ex.printStackTrace();
            }
            return null;
        }
    }

    //Methods

    //By name

    public static Object executeMethodByName(Class<?> cls, String methodName, Object executeOn, Object... inputParameters) {
        Method m = null;
        if (isDebugEnabled()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eExecuting method " + methodName + " in " + cls.getSimpleName() + "."));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bParameters Inputted &e&m->&a " + Arrays.toString(inputParameters)));
        }
        for (Method method : cls.getDeclaredMethods())
            if (method.getName().equals(methodName))
                m = method;
        ;
        if (m == null) {
            if (isDebugEnabled())
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &cFailed to execute method. (&edid not find method&c)"));
            return null;
        }

        m.setAccessible(true);

        try {
            return m.invoke(executeOn, inputParameters);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            if (isDebugEnabled()) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &cFailed to execute method."));
                ex.printStackTrace();
            }
            return null;
        }
    }

    //Without IndexGetter && With Name

    public static Object executeMethod(Class<?> cls, String methodName, Object executeOn, Class<?>[] parametersType, Object... inputParameters) {
        Method m;

        if (isDebugEnabled()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eExecuting method " + methodName + " in " + cls.getSimpleName() + "."));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bParameters &e&m->&a " + Arrays.toString(parametersType)));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bParameters Inputted &e&m->&a " + Arrays.toString(inputParameters)));
        }

        try {
            m = cls.getDeclaredMethod(methodName, parametersType);
            m.setAccessible(true);
        } catch (NoSuchMethodException ex) {
            if (isDebugEnabled()) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &cFailed to execute method. (&edid not find method&c)"));
                ex.printStackTrace();
            }
            return null;
        }

        try {
            return m.invoke(executeOn, inputParameters);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            if (isDebugEnabled()) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &cFailed to execute method."));
                ex.printStackTrace();
            }
            return null;
        }
    }

    //With IndexGetter && without Name

    public static <A> A executeMethod(Class<?> cls, Class<?>[] parameters, Object executeOn, Class<A> returnType, IndexGetter index, Object... inputParameters) {
        List<Method> methods = new ArrayList<>(Arrays.asList(cls.getDeclaredMethods()));

        if (isDebugEnabled()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eExecuting method in " + cls.getSimpleName() + "."));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bParameters &e&m->&a " + Arrays.toString(parameters)));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bParameters Inputted &e&m->&a " + Arrays.toString(inputParameters)));
        }

        methods.removeIf(method -> !((method.getReturnType().equals(returnType) || method.getReturnType().getSimpleName().equalsIgnoreCase("void") && returnType == null)
                && Arrays.equals(method.getParameterTypes(), parameters)));

        if (isDebugEnabled()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eFound &a" + methods.size() + " &emethods that match the parameters."));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &3Method &eList: "));
            for (int i = 0; i < methods.size(); i++) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "-> " + methods.get(i).getName()));
            }
        }

        int indexNum = index.getIndex(methods.size() - 1);

        if (indexNum > methods.size() - 1) {
            throw new UnsupportedOperationException("Index " + index + " is out of bounds.");
        }

        Method specifiedMethod = methods.get(indexNum);

        if (isDebugEnabled()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eFound method " + specifiedMethod.getName() + " in " + cls.getSimpleName() + "!"));
        }

        specifiedMethod.setAccessible(true);

        try {
            return (A) specifiedMethod.invoke(executeOn, inputParameters);
        } catch (InvocationTargetException | IllegalAccessException ex) {
            if (isDebugEnabled()) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &cFailed to execute method."));
                ex.printStackTrace();
            }
            return null;
        }
    }

    //Fiels

    //Set

    //By Name

    public static void setFieldObjectByName(Class<?> cls, String fieldName, Object object, Object value) {
        try {
            Field f = cls.getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(object, value);
        } catch (IllegalAccessException | NoSuchFieldException ex) {
            if (JadAPI.getInstance().getDebug().doReflectionDebug()) ex.printStackTrace();
        }
    }

    public static void setFieldObject(Class<?> cls, Class<?> fieldType, Object executeObject, Object value) {
        setFieldObject(cls, fieldType, executeObject, value, (i) -> 0);
    }

    public static void setFieldObject(Class<?> cls, Class<?> fieldType, Object executeObject, Object value, IndexGetter index) {
        List<Field> fields = new ArrayList<>(Arrays.asList(cls.getDeclaredFields()));

        if (isDebugEnabled()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eSetting field of " + cls.getSimpleName() + "."));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bField Type &e&m->&a " + fieldType.getSimpleName()));
        }

        fields.removeIf(f -> !f.getType().equals(fieldType));

        if (isDebugEnabled()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eFound &a" + fields.size() + " &efields that match."));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &3Field &eList: "));
            for (int i = 0; i < fields.size(); i++) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "-> " + fields.get(i).getName()));
            }
        }

        int indexNum = index.getIndex(fields.size() - 1);

        if (indexNum > fields.size() - 1) {
            throw new UnsupportedOperationException("Index " + index + " is out of bounds.");
        }

        Field specifiedField = fields.get(indexNum);

        if (isDebugEnabled()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eFound field " + specifiedField.getName() + " in " + cls.getSimpleName() + "!"));
        }

        specifiedField.setAccessible(true);

        try {
            specifiedField.set(executeObject, value);
        } catch (IllegalAccessException ex) {
            if (isDebugEnabled()) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &cFailed to execute get field."));
                ex.printStackTrace();
            }
        }
    }

    public static Object getFieldObjectByName(Class<?> classy, String fieldName, Object fieldObject) {
        try {
            Field field = classy.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(fieldObject);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            if (JadAPI.getInstance().getDebug().doReflectionDebug()) ex.printStackTrace();
            return null;
        }
    }

    public static <A> A getFieldObject(Class<?> cls, Class<A> fieldType, Object executeObject) {
        return getFieldObject(cls, fieldType, executeObject, (i) -> 0);
    }

    public static <A> A getFieldObject(Class<?> cls, Class<A> fieldType, Object executeObject, IndexGetter index) {
        List<Field> fields = new ArrayList<>(Arrays.asList(cls.getDeclaredFields()));

        if (isDebugEnabled()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eGetting field of " + cls.getSimpleName() + "."));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bField Type &e&m->&a " + fieldType.getSimpleName()));
        }

        fields.removeIf(f -> !f.getType().equals(fieldType));

        if (isDebugEnabled()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eFound &a" + fields.size() + " &efields that match."));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &3Field &eList: "));
            for (int i = 0; i < fields.size(); i++) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "-> " + fields.get(i).getName()));
            }
        }

        int indexNum = index.getIndex(fields.size() - 1);

        if (indexNum > fields.size() - 1) {
            throw new UnsupportedOperationException("Index " + index + " is out of bounds.");
        }

        Field specifiedField = fields.get(indexNum);

        if (isDebugEnabled()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eFound field " + specifiedField.getName() + " in " + cls.getSimpleName() + "!"));
        }

        specifiedField.setAccessible(true);

        try {
            return (A) specifiedField.get(executeObject);
        } catch (IllegalAccessException ex) {
            if (isDebugEnabled()) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &cFailed to execute get field."));
                ex.printStackTrace();
            }
            return null;
        }
    }


    //OLD METHODS TO BE DONE!

    @Deprecated
    @ForRemoval(willBeRemoved = true, expectedVersionForRemoval = "1.23.1", reason = "This method will be removed since a better alternative is available.")
    public static <A> A getUnspecificFieldObject(Class<?> classy, Class<A> fieldType, Object fieldObject) {
        try {
            List<Field> fields = new ArrayList<>(Arrays.asList(classy.getDeclaredFields()));

            for (Field f : fields)
                if (f.getType().equals(fieldType)) {
                    f.setAccessible(true);
                    return (A) f.get(fieldObject);
                }

        } catch (Exception ex) {
            if (JadAPI.getInstance().getDebug().doReflectionDebug()) ex.printStackTrace();
        }
        return null;
    }

    @Deprecated
    @ForRemoval(willBeRemoved = true, expectedVersionForRemoval = "1.23.1", reason = "This method will be removed since a better alternative is available.")
    public static <A> A getUnspecificFieldObject(Class<?> classy, Class<A> fieldType, int fieldIndex, Object fieldObject) {
        try {
            List<Field> fields = new ArrayList<>(Arrays.asList(classy.getDeclaredFields()));
            fields = fields.stream().filter(f -> f.getType().equals(fieldType)).collect(Collectors.toCollection(ArrayList::new));

            if (fields.size() < fieldIndex && fieldIndex != Integer.MAX_VALUE)
                throw new UnsupportedOperationException("Unsupported field number");


            Field field;
            if (fieldIndex == Integer.MAX_VALUE) {
                field = fields.get(fields.size() - 1);
            } else {
                field = fields.get(fieldIndex);
            }

            if (field == null)
                throw new UnsupportedOperationException("Field not found?");

            field.setAccessible(true);
            return (A) field.get(fieldObject);
        } catch (Exception ex) {
            if (JadAPI.getInstance().getDebug().doReflectionDebug()) ex.printStackTrace();
        }
        return null;
    }

    @Deprecated
    @ForRemoval(willBeRemoved = true, expectedVersionForRemoval = "1.23.1", reason = "This method will be removed since a better alternative is available.")
    public static void setUnspecificField(Class<?> classy, Class<?> fieldType, Object fieldObject, Object fieldValue) {
        try {
            List<Field> fields = new ArrayList<>(Arrays.asList(classy.getDeclaredFields()));

            for (Field f : fields)
                if (f.getType().equals(fieldType)) {
                    f.setAccessible(true);
                    f.set(fieldObject, fieldValue);
                }

        } catch (Exception ex) {
            if (JadAPI.getInstance().getDebug().doReflectionDebug()) ex.printStackTrace();
        }
    }

    @Deprecated
    @ForRemoval(willBeRemoved = true, expectedVersionForRemoval = "1.23.1", reason = "This method will be removed since a better alternative is available.")
    public static void setUnspecificField(Class<?> classy, Class<?> fieldType, int fieldIndex, Object fieldObject, Object fieldValue) {
        try {
            List<Field> fields = new ArrayList<>(Arrays.asList(classy.getDeclaredFields()));
            fields = fields.stream().filter(f -> f.getType().equals(fieldType)).collect(Collectors.toCollection(ArrayList::new));

            if (fields.size() < fieldIndex && fieldIndex != Integer.MAX_VALUE)
                throw new UnsupportedOperationException("Unsupported field number");

            Field field;
            if (fieldIndex == Integer.MAX_VALUE) {
                field = fields.get(fields.size() - 1);
            } else {
                field = fields.get(fieldIndex);
            }

            if (field == null)
                throw new UnsupportedOperationException("Field not found?");

            field.setAccessible(true);
            field.set(fieldObject, fieldValue);

        } catch (Exception ex) {
            if (JadAPI.getInstance().getDebug().doReflectionDebug()) ex.printStackTrace();
        }
    }

    public static List<Object> getFieldsOfType(Class<?> classy, Class<?> fieldType, Object fieldObject, boolean onlyStatics) {
        List<Object> objects = new ArrayList<>();

        List<Field> fields;
        if (onlyStatics) {
            fields = Arrays.stream(classy.getDeclaredFields()).filter(field -> Modifier.isStatic(field.getModifiers()) && (fieldType == null || field.getType().equals(fieldType))).collect(Collectors.toList());
            fields.forEach(field -> {
                field.setAccessible(true);
                try {
                    objects.add(field.get(null));
                } catch (Exception ex) {
                    if (JadAPI.getInstance().getDebug().doReflectionDebug()) ex.printStackTrace();
                }
            });
        } else {
            fields = Arrays.stream(classy.getDeclaredFields()).filter(field -> !Modifier.isStatic(field.getModifiers())).collect(Collectors.toList());
            fields.forEach(field -> {
                field.setAccessible(true);
                try {
                    objects.add(field.get(fieldObject));
                } catch (Exception ex) {
                    if (JadAPI.getInstance().getDebug().doReflectionDebug()) ex.printStackTrace();
                }
            });
        }

        return objects;
    }

    public static Object executeConstructor(Class<?> cls, Class<?>[] parameters, Object... inputParameters) {
        try {
            if (isDebugEnabled()) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &aExecuting constructor for class &e" + cls.getSimpleName()));
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &aParameters &e&m->&a " + Arrays.toString(parameters)));
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &aParameters inputted &e&m->&a " + Arrays.toString(inputParameters)));
            }
            Constructor<?> c = cls.getDeclaredConstructor(parameters);
            c.setAccessible(true);
            return c.newInstance(inputParameters);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex) {
            if (JadAPI.getInstance().getDebug().doReflectionDebug()) ex.printStackTrace();
            return null;
        }
    }

    public static Enum<?> getEnum(String name, Class<?> cls) {
        for (Enum<?> e : (Enum<?>[]) cls.getEnumConstants())
            if (e.name().equalsIgnoreCase(name))
                return e;
        return null;
    }

    public static Enum<?> getEnum(int ordinal, Class<?> cls) {
        for (Enum<?> e : (Enum<?>[]) cls.getEnumConstants())
            if (e.ordinal() == ordinal)
                return e;
        return null;
    }

    public static Enum<?>[] getEnum(Class<?> cls) {
        try {
            return (Enum<?>[]) cls.getEnumConstants();
        } catch (Exception ex) {
            if (JadAPI.getInstance().getDebug().doReflectionDebug()) ex.printStackTrace();
            return null;
        }
    }

    public static List<Field> getFields(Class<?> cls) {
        return Arrays.stream(cls.getDeclaredFields()).collect(Collectors.toCollection(ArrayList::new));
    }

    public static List<Object> getObjectFields(Class<?> cls, Object object) {
        return Arrays.stream(cls.getDeclaredFields())
                .map(field -> {
                    try {
                        field.setAccessible(true);
                        return field.get(object);
                    } catch (Exception ex) {
                        if (JadAPI.getInstance().getDebug().doReflectionDebug()) ex.printStackTrace();
                        return null;
                    }
                })
                .filter(obj -> obj != null)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static boolean isDebugEnabled() {
        return JadAPI.getInstance().getDebug().doReflectionDebug();
    }

    @FunctionalInterface
    public interface IndexGetter {

        int getIndex(int lastIndex);

    }
}
