package dev.jadss.jadapi.management.nms.objects.world.entities.base;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.enums.EnumItemSlot;
import dev.jadss.jadapi.management.nms.objects.attributes.Attribute;
import dev.jadss.jadapi.management.nms.objects.attributes.AttributeInstance;
import dev.jadss.jadapi.management.nms.objects.other.ItemStack;
import dev.jadss.jadapi.utils.reflection.JMappings;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JMethodReflector;

public abstract class EntityLiving extends Entity {

    public static final Class<?> ENTITY_LIVING = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity" : "server." + NMS.getNMSVersion()) + ".EntityLiving");

    public EntityLiving(Object entity) {
        super(entity);

        if (!isEntityLiving(entity))
            throwExceptionNotClass("Entity Living");
    }

    public static boolean isEntityLiving(Object entity) {
        return ENTITY_LIVING.isAssignableFrom(entity.getClass());
    }

    //Custom methods.

    public void setSlot(EnumItemSlot slot, ItemStack item, boolean silent) {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_8)) {
            JMethodReflector.executeUnspecificMethod(ENTITY_LIVING, new Class[]{int.class, ItemStack.itemStackClass}, this.entity, new Object[]{slot.getSlot(), item.getHandle()});
        } else if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_15)) {
            JMethodReflector.executeUnspecificMethod(ENTITY_LIVING, new Class[]{EnumItemSlot.enumItemSlotClass, ItemStack.itemStackClass}, this.entity, new Object[]{slot.getNMSObject(), item.getHandle()});
        } else {
            JMethodReflector.executeUnspecificMethod(ENTITY_LIVING, new Class[]{EnumItemSlot.enumItemSlotClass, ItemStack.itemStackClass, boolean.class}, this.entity, new Object[]{slot.getNMSObject(), item.getHandle(), silent});
        }
    }

    public static final JMappings GET_SLOT_METHOD = JMappings.create(ENTITY_LIVING)
            .add(JVersion.v1_16, "getEquipment")
            .add(JVersion.v1_18, "b")
            .add(JVersion.v1_19, "c")
            .finish();

    public ItemStack getSlot(EnumItemSlot slot) {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_15)) {
            return new ItemStack(JMethodReflector.executeUnspecificMethod(ENTITY_LIVING, new Class[]{EnumItemSlot.enumItemSlotClass}, ItemStack.itemStackClass, this.entity, new Object[]{slot.getNMSObject()}));
        } else {
            return new ItemStack(JMethodReflector.executeMethod(ENTITY_LIVING, GET_SLOT_METHOD.get(), this.entity, new Object[]{slot.getNMSObject()}));
        }
    }

    private static final Class<?> attributeMapServerClass = JClassReflector.getClass("net.minecraft." + "server." + NMS.getNMSVersion() + ".AttributeMapServer");

    public AttributeInstance getAttribute(Attribute attribute) {
        if (attribute == null) throw new NMSException("Attribute is null!");

        Object attributeInstance;
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_15)) {
            attributeInstance = JMethodReflector.executeUnspecificMethod(ENTITY_LIVING, new Class[]{Attribute.iAttributeClass}, AttributeInstance.attributeInstanceClass, this.entity, new Object[]{attribute.getHandle()});
        } else {
            attributeInstance = JMethodReflector.executeUnspecificMethod(ENTITY_LIVING, new Class[]{Attribute.attributeBaseClass}, AttributeInstance.attributeModifiableClass, this.entity, new Object[]{attribute.getHandle()});
        }

        //Note: maybe dividing this into more classes will make it better.
        if (attributeInstance == null) {
            if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_15)) {
                Object attributeMap = JFieldReflector.getObjectFromUnspecificField(ENTITY_LIVING, AttributeInstance.attributeMapBaseClass, this.entity);
                JMethodReflector.executeMethod(AttributeInstance.attributeMapBaseClass, "b", new Class[]{Attribute.iAttributeClass}, attributeMap, new Object[]{attribute.getHandle()});

                attributeInstance = JMethodReflector.executeUnspecificMethod(ENTITY_LIVING, new Class[]{Attribute.iAttributeClass}, AttributeInstance.attributeInstanceClass, this.entity, new Object[]{attribute.getHandle()});
            } else throw new NMSException("WHAT?");
        }

        return new AttributeInstance(attributeInstance, attribute.getType());
    }

    public static final JMappings GET_HEALTH = JMappings.create(EntityLiving.ENTITY_LIVING)
            .add(JVersion.v1_7, "getHealth")
            .add(JVersion.v1_18, "ea")
            .add(JVersion.v1_19, "eg")
            .finish();

    public float getHealth() {
        return (float) JMethodReflector.executeMethod(ENTITY_LIVING, GET_HEALTH.get(), this.entity, null);
    }

    public static final JMappings SET_HEALTH = JMappings.create(EntityLiving.ENTITY_LIVING)
            .add(JVersion.v1_7, "setHealth")
            .add(JVersion.v1_18, "c")
            .finish();

    public void setHealth(float health) {
        JMethodReflector.executeMethod(ENTITY_LIVING, SET_HEALTH.get(), new Class[]{float.class}, this.entity, new Object[]{health});
    }

}
