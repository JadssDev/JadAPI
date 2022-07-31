package dev.jadss.jadapi.management.nms.objects.attributes;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;

import java.util.*;
import java.util.stream.Collectors;

public class AttributeList {

    public static final Class<?> genericAttributesClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity.ai.attributes" : "server." + NMS.getNMSVersion()) + ".GenericAttributes");

    private static final Map<AttributeType, Attribute> attributes = new HashMap<>();

    public static Attribute getAttribute(AttributeType type) {
        return attributes.get(type);
    }

    static {
        List<Object> objects = JFieldReflector.getObjectsFromFields(genericAttributesClass, null, true).stream()
                .filter(object -> (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_15) ? Attribute.iAttributeClass.isAssignableFrom(object.getClass()) : Attribute.attributeBaseClass.isAssignableFrom(object.getClass())))
                .collect(Collectors.toList());

        List<AttributeType> attributesPossible = Arrays.stream(AttributeType.values())
                .filter(attribute -> attribute.getName() != null)
                .collect(Collectors.toCollection(ArrayList::new));

        for (Object object : objects) {
            String name = Attribute.getNameFromObject(object);
            if (name == null)
                continue;

            AttributeType type = attributesPossible.stream()
                    .filter(attribute -> attribute.getName().equals(name))
                    .findFirst().orElse(null);
            if (type == null)
                continue;

            AttributeList.attributes.put(type, new Attribute(object, type));
        }
    }
}
