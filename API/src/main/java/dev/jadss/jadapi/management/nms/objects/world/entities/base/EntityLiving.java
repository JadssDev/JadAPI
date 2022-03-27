package dev.jadss.jadapi.management.nms.objects.world.entities.base;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.enums.EnumItemSlot;
import dev.jadss.jadapi.management.nms.objects.attributes.Attribute;
import dev.jadss.jadapi.management.nms.objects.attributes.AttributeInstance;
import dev.jadss.jadapi.management.nms.objects.other.ItemStack;
import dev.jadss.jadapi.utils.JReflection;

public abstract class EntityLiving extends Entity {

    public static final Class<?> entityLivingClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity" : "server." + JReflection.getNMSVersion()) + ".EntityLiving");

    public EntityLiving(Object nmsEntityLiving) {
        super(nmsEntityLiving);
        if(!isEntityLivingObject(nmsEntityLiving)) throwExceptionNotClass("Entity living");
    }

    public boolean isEntityLivingObject(Object entity) { return entityLivingClass.isAssignableFrom(entity.getClass()); }

    //Custom methods.

    public void setSlot(EnumItemSlot slot, ItemStack item, boolean silent) {
        if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_8)) {
            JReflection.executeMethod(entityLivingClass, new Class[] { int.class, ItemStack.itemStackClass}, this.entity, null, (i) -> 0, slot.getSlot(), item.getHandle());
        } else if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_15) ){
            JReflection.executeMethod(entityLivingClass, new Class[] { EnumItemSlot.enumItemSlotClass, ItemStack.itemStackClass }, this.entity, null, (i) -> 0, slot, item.getHandle());
        } else {
            JReflection.executeMethod(entityLivingClass, new Class[] { EnumItemSlot.enumItemSlotClass, ItemStack.itemStackClass, boolean.class }, this.entity, null, (i) -> 0, slot, item.getHandle(), silent);
        }
    }

    public void getSlot(EnumItemSlot slot) {
        JReflection.executeMethod(entityLivingClass, new Class[] { EnumItemSlot.enumItemSlotClass }, this.entity, ItemStack.itemStackClass, (i) -> 0, slot.getNMSObject());
    }

    private static final Class<?> attributeMapServerClass = JReflection.getReflectionClass("net.minecraft." + "server." + JReflection.getNMSVersion() + ".AttributeMapServer");

    public AttributeInstance getAttribute(Attribute attribute) {
        if(attribute == null) throw new NMSException("Attribute is null!");

        Object attributeInstance;
        if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_15)) {
            attributeInstance = JReflection.executeMethod(entityLivingClass, new Class[] { Attribute.iAttributeClass }, this.entity, AttributeInstance.attributeInstanceClass, (i) -> 0, attribute.getHandle());
        } else {
            attributeInstance = JReflection.executeMethod(entityLivingClass, new Class[] { Attribute.attributeBaseClass }, this.entity, AttributeInstance.attributeModifiableClass, (i) -> 0, attribute.getHandle());
        }

        //Note: maybe dividing this into more classes will make it better.
        if(attributeInstance == null) {
            if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_15)) {
                Object attributeMap = JReflection.getFieldObject(entityLivingClass, AttributeInstance.attributeMapBaseClass, this.entity);
                JReflection.executeMethod(AttributeInstance.attributeMapBaseClass, new Class[] { Attribute.iAttributeClass }, attributeMap, AttributeInstance.attributeInstanceClass, (i) -> 0, attribute.getHandle());

                attributeInstance = JReflection.executeMethod(entityLivingClass, new Class[] { Attribute.iAttributeClass }, this.entity, AttributeInstance.attributeInstanceClass, (i) -> 0, attribute.getHandle());
            } else throw new NMSException("WHAT?");
        }

        return new AttributeInstance(attributeInstance, attribute.getType());
    }

    public float getHealth() {
        return (float) JReflection.executeMethod(entityLivingClass, (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_18) ? "dZ" : "getHealth"), this.entity, new Class[0]);
    }

    public void setHealth(float health) {
        JReflection.executeMethod(entityLivingClass, (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_18) ? "c" : "setHealth"), this.entity, new Class[] { float.class }, health);
    }

}
