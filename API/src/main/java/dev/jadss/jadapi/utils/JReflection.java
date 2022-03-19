package dev.jadss.jadapi.utils;

import dev.jadss.jadapi.JadAPI;
import org.bukkit.Bukkit;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JReflection {

    private static String nmsVersion;

    public static void setupNMS() {
        String[] version = Bukkit.getServer().getClass().getPackage().getName().split("\\.");
        nmsVersion = version[version.length-1];
    }

    public static String getNMSVersion() { return nmsVersion; }

    public static Class<?> getReflectionClass(String classPath) {
        try {
            return Class.forName(classPath);
        } catch(ClassNotFoundException ex) {
            if(JadAPI.getInstance().getDebug().doReflectionDebug()) ex.printStackTrace();
            return null;
        }
    }

    public static Object cast(Class<?> caster, Object castObject) {
        try {
            return caster.cast(castObject);
        } catch(ClassCastException ex) {
            if(JadAPI.getInstance().getDebug().doReflectionDebug()) ex.printStackTrace();
            return null;
        }
    }

    @Deprecated
    public static Object executeMethod(Class<?> cls, String methodName, boolean idk, Object executeOn, Class<?>[] parametersType, Object... inputParameters) {
        Method m;
        try {
            m = cls.getDeclaredMethod(methodName, parametersType);
            m.setAccessible(true);
        } catch (NoSuchMethodException ex) {
            if(JadAPI.getInstance().getDebug().doReflectionDebug()) ex.printStackTrace();
            return null;
        }

        try {
            return m.invoke(executeOn, inputParameters);
        } catch(IllegalAccessException | InvocationTargetException ex) {
            if(JadAPI.getInstance().getDebug().doReflectionDebug()) ex.printStackTrace();
            return null;
        }
    }

    public static Object executeMethod(Class<?> cls, String methodName, Object executeOn, Class<?>[] parametersType, Object... inputParameters) {
        Method m;
        try {
            m = cls.getDeclaredMethod(methodName, parametersType);
            m.setAccessible(true);
        } catch (NoSuchMethodException ex) {
            if(JadAPI.getInstance().getDebug().doReflectionDebug()) ex.printStackTrace();
            return null;
        }

        try {
            return m.invoke(executeOn, inputParameters);
        } catch(IllegalAccessException | InvocationTargetException ex) {
            if(JadAPI.getInstance().getDebug().doReflectionDebug()) ex.printStackTrace();
            return null;
        }
    }

    public static Object executeMethodByName(Class<?> cls, String methodName, Object executeOn, Object... inputParameters) {
        Method m = null;
        for(Method method : cls.getDeclaredMethods())
            if(method.getName().equals(methodName))
                m = method;

        m.setAccessible(true);

        try {
            return m.invoke(executeOn, inputParameters);
        } catch(IllegalAccessException | InvocationTargetException ex) {
            if(JadAPI.getInstance().getDebug().doReflectionDebug()) ex.printStackTrace();
            return null;
        }
    }

    public static <A> A executeUnspecificMethod(Class<?> cls, Class<?>[] parameters, Object executeOn, Class<A> returnType, Object... inputParameters) {
        Method m = null;
        for(Method declaredMethod : cls.getDeclaredMethods()) {
            if((declaredMethod.getReturnType().equals(returnType) || declaredMethod.getReturnType().getSimpleName().equalsIgnoreCase("void") && returnType == null) && Arrays.equals(declaredMethod.getParameterTypes(), parameters)) {
                m = declaredMethod;
            }
        }

        m.setAccessible(true);
        try {
            return (A) m.invoke(executeOn, inputParameters);
        } catch(InvocationTargetException | IllegalAccessException ex) {
            if(JadAPI.getInstance().getDebug().doReflectionDebug()) ex.printStackTrace();
            return null;
        }
    }

    //if index = Integer.MAX_VALUE, it will return the last index possible.
    public static <A> A executeUnspecificIndexMethod(Class<?> cls, Class<?>[] parameters, Object executeOn, Class<A> returnType, int index, Object... inputParameters) {
        List<Method> methods = Arrays.stream(cls.getDeclaredMethods()).collect(Collectors.toCollection(ArrayList::new));
        methods = methods.stream()
                .filter(method -> (method.getReturnType().equals(returnType) || method.getReturnType().getSimpleName().equalsIgnoreCase("void") && returnType == null) && Arrays.equals(method.getParameterTypes(), parameters))
                .collect(Collectors.toCollection(ArrayList::new));

        Method specifiedMethod = null;

        if(index == Integer.MAX_VALUE)
            specifiedMethod = methods.get(methods.size() - 1);
        else
            if(index < methods.size())
                specifiedMethod = methods.get(index);
            else
                throw new UnsupportedOperationException("Index " + index + " is out of bounds.");

            specifiedMethod.setAccessible(true);
        try {
            return (A) specifiedMethod.invoke(executeOn, inputParameters);
        } catch(InvocationTargetException | IllegalAccessException ex) {
            if(JadAPI.getInstance().getDebug().doReflectionDebug()) ex.printStackTrace();
            return null;
        }
    }

    public static void setFieldObject(Class<?> classy, String fieldName, Object object, Object value) {
        try {
            Field f = classy.getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(object, value);

        } catch(IllegalAccessException | NoSuchFieldException ex) {
            if(JadAPI.getInstance().getDebug().doReflectionDebug()) ex.printStackTrace();
        }
    }

    public static Object getFieldObject(Class<?> classy, String fieldName, Object fieldObject) {
        try {
            Field field = classy.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(fieldObject);
        } catch(NoSuchFieldException | IllegalAccessException ex) {
            if(JadAPI.getInstance().getDebug().doReflectionDebug()) ex.printStackTrace();
            return null;
        }
    }

    public static <A> A getUnspecificFieldObject(Class<?> classy, Class<A> fieldType, Object fieldObject) {
        try {
            List<Field> fields = new ArrayList<>(Arrays.asList(classy.getDeclaredFields()));

            for(Field f : fields)
                if(f.getType().equals(fieldType)) {
                    f.setAccessible(true);
                    return (A) f.get(fieldObject);
                }

        } catch(Exception ex) {
            if(JadAPI.getInstance().getDebug().doReflectionDebug()) ex.printStackTrace();
        }
        return null;
    }

    public static <A> A getUnspecificFieldObject(Class<?> classy, Class<A> fieldType, int fieldIndex, Object fieldObject) {
        try {
            List<Field> fields = new ArrayList<>(Arrays.asList(classy.getDeclaredFields()));
            fields = fields.stream().filter(f -> f.getType().equals(fieldType)).collect(Collectors.toCollection(ArrayList::new));

            if(fields.size() < fieldIndex && fieldIndex != Integer.MAX_VALUE)
                throw new UnsupportedOperationException("Unsupported field number");


            Field field;
            if(fieldIndex == Integer.MAX_VALUE) {
                field = fields.get(fields.size()-1);
            } else {
                field = fields.get(fieldIndex);
            }

            if(field == null)
                throw new UnsupportedOperationException("Field not found?");

            field.setAccessible(true);
            return (A) field.get(fieldObject);
        } catch(Exception ex) {
            if(JadAPI.getInstance().getDebug().doReflectionDebug()) ex.printStackTrace();
        }
        return null;
    }

    public static void setUnspecificField(Class<?> classy, Class<?> fieldType, Object fieldObject, Object fieldValue) {
        try {
            List<Field> fields = new ArrayList<>(Arrays.asList(classy.getDeclaredFields()));

            for(Field f : fields)
                if(f.getType().equals(fieldType)) {
                    f.setAccessible(true);
                    f.set(fieldObject, fieldValue);
                }

        } catch(Exception ex) {
            if(JadAPI.getInstance().getDebug().doReflectionDebug()) ex.printStackTrace();
        }
    }

    public static void setUnspecificField(Class<?> classy, Class<?> fieldType, int fieldIndex, Object fieldObject, Object fieldValue) {
        try {
            List<Field> fields = new ArrayList<>(Arrays.asList(classy.getDeclaredFields()));
            fields = fields.stream().filter(f -> f.getType().equals(fieldType)).collect(Collectors.toCollection(ArrayList::new));

            if(fields.size() < fieldIndex && fieldIndex != Integer.MAX_VALUE)
                throw new UnsupportedOperationException("Unsupported field number");

            Field field;
            if(fieldIndex == Integer.MAX_VALUE) {
                field = fields.get(fields.size()-1);
            } else {
                field = fields.get(fieldIndex);
            }

            if(field == null)
                throw new UnsupportedOperationException("Field not found?");

            field.setAccessible(true);
            field.set(fieldObject, fieldValue);

        } catch(Exception ex) {
            if(JadAPI.getInstance().getDebug().doReflectionDebug()) ex.printStackTrace();
        }
    }

    public static List<Object> getFieldsOfType(Class<?> classy, Class<?> fieldType, Object fieldObject, boolean onlyStatics) {
        List<Object> objects = new ArrayList<>();

        List<Field> fields;
        if(onlyStatics) {
            fields = Arrays.stream(classy.getDeclaredFields()).filter(field -> Modifier.isStatic(field.getModifiers()) && (fieldType == null || field.getType().equals(fieldType))).collect(Collectors.toList());
            fields.forEach(field -> {
                field.setAccessible(true);
                try {
                    objects.add(field.get(null));
                } catch(Exception ex) {
                    if(JadAPI.getInstance().getDebug().doReflectionDebug()) ex.printStackTrace();
                }
            });
        } else {
            fields = Arrays.stream(classy.getDeclaredFields()).filter(field -> !Modifier.isStatic(field.getModifiers())).collect(Collectors.toList());
            fields.forEach(field -> {
                field.setAccessible(true);
                try {
                    objects.add(field.get(fieldObject));
                } catch(Exception ex) {
                    if(JadAPI.getInstance().getDebug().doReflectionDebug()) ex.printStackTrace();
                }
            });
        }

        return objects;
    }

    public static Object executeConstructor(Class<?> cls, Class<?>[] parametersTypes, Object... parameters) {
        try {
            Constructor<?> c = cls.getDeclaredConstructor(parametersTypes);
            c.setAccessible(true);
            return c.newInstance(parameters);
        } catch(NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex) {
            if(JadAPI.getInstance().getDebug().doReflectionDebug()) ex.printStackTrace();
            return null;
        }
    }

    public static Enum<?> getEnum(String name, Class<?> cls) {
        for(Enum<?> e : (Enum<?>[]) cls.getEnumConstants())
            if(e.name().equalsIgnoreCase(name))
                return e;
        return null;
    }

    public static Enum<?> getEnum(int ordinal, Class<?> cls) {
        for(Enum<?> e : (Enum<?>[]) cls.getEnumConstants())
            if(e.ordinal() == ordinal)
                return e;
        return null;
    }

    public static Enum<?>[] getEnum(Class<?> cls) {
        try {
            return (Enum<?>[]) cls.getEnumConstants();
        } catch(Exception ex) {
            if(JadAPI.getInstance().getDebug().doReflectionDebug()) ex.printStackTrace();
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
                    } catch(Exception ex) {
                        if(JadAPI.getInstance().getDebug().doReflectionDebug()) ex.printStackTrace();
                        return null;
                    }
                })
                .filter(obj -> obj != null)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
