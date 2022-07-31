package dev.jadss.jadapi.management.nms.objects.attributes;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;

public enum AttributeType {
    MAX_HEALTH("generic.maxHealth", "generic.maxHealth", "generic.maxHealth", "generic.maxHealth", "generic.maxHealth", "generic.maxHealth", "generic.maxHealth", "generic.maxHealth", "generic.maxHealth", "attribute.name.generic.max_health", "attribute.name.generic.max_health", "attribute.name.generic.max_health", "attribute.name.generic.max_health"),
    FOLLOW_RANGE("generic.followRange", "generic.followRange", "generic.followRange", "generic.followRange", "generic.followRange", "generic.followRange", "generic.followRange", "generic.followRange", "generic.followRange", "attribute.name.generic.follow_range", "attribute.name.generic.follow_range", "attribute.name.generic.follow_range", "attribute.name.generic.follow_range"),
    KNOCKBACK_RESISTANCE("generic.knockbackResistance", "generic.knockbackResistance", "generic.knockbackResistance", "generic.knockbackResistance", "generic.knockbackResistance", "generic.knockbackResistance", "generic.knockbackResistance", "generic.knockbackResistance", "generic.knockbackResistance", "attribute.name.generic.knockback_resistance", "attribute.name.generic.knockback_resistance", "attribute.name.generic.knockback_resistance", "attribute.name.generic.knockback_resistance"),
    MOVEMENT_SPEED("generic.movementSpeed", "generic.movementSpeed", "generic.movementSpeed", "generic.movementSpeed", "generic.movementSpeed", "generic.movementSpeed", "generic.movementSpeed", "generic.movementSpeed", "generic.movementSpeed", "attribute.name.generic.movement_speed", "attribute.name.generic.movement_speed", "attribute.name.generic.movement_speed", "attribute.name.generic.movement_speed"),
    FLYING_SPEED(null, null, null, null, null, "generic.flyingSpeed", "generic.flyingSpeed", "generic.flyingSpeed", "generic.flyingSpeed", "attribute.name.generic.flying_speed", "attribute.name.generic.flying_speed", "attribute.name.generic.flying_speed", "attribute.name.generic.flying_speed"),
    ATTACK_DAMAGE("generic.attackDamage", "generic.attackDamage", "generic.attackDamage", "generic.attackDamage", "generic.attackDamage", "generic.attackDamage", "generic.attackDamage", "generic.attackDamage", "generic.attackDamage", "attribute.name.generic.attack_damage", "attribute.name.generic.attack_damage", "attribute.name.generic.attack_damage", "attribute.name.generic.attack_damage"),
    ATTACK_KNOCKBACK(null, null, null, null, null, null, null, "generic.attackKnockback", "generic.attackKnockback", "attribute.name.generic.attack_knockback", "attribute.name.generic.attack_knockback", "attribute.name.generic.attack_knockback", "attribute.name.generic.attack_knockback"),
    ATTACK_SPEED(null, null, "generic.attackSpeed", "generic.attackSpeed", "generic.attackSpeed", "generic.attackSpeed", "generic.attackSpeed", "generic.attackSpeed", "generic.attackSpeed", "attribute.name.generic.attack_speed", "attribute.name.generic.attack_speed", "attribute.name.generic.attack_speed", "attribute.name.generic.attack_speed"),
    ARMOR(null, null, "generic.armor", "generic.armor", "generic.armor", "generic.armor", "generic.armor", "generic.armor", "generic.armor", "attribute.name.generic.armor", "attribute.name.generic.armor", "attribute.name.generic.armor", "attribute.name.generic.armor"),
    ARMOR_TOUGHNESS(null, null, "generic.armorToughness", "generic.armorToughness", "generic.armorToughness", "generic.armorToughness", "generic.armorToughness", "generic.armorToughness", "generic.armorToughness", "attribute.name.generic.armor_toughness", "attribute.name.generic.armor_toughness", "attribute.name.generic.armor_toughness", "attribute.name.generic.armor_toughness"),
    LUCK(null, null, "generic.luck", "generic.luck", "generic.luck", "generic.luck", "generic.luck", "generic.luck", "generic.luck", "attribute.name.generic.luck", "attribute.name.generic.luck", "attribute.name.generic.luck", "attribute.name.generic.luck"),
    SPAWN_REINFORCEMENTS(null, null, null, null, null, null, null, null, null, "attribute.name.zombie.spawn_reinforcements", "attribute.name.zombie.spawn_reinforcements", "attribute.name.zombie.spawn_reinforcements", "attribute.name.zombie.spawn_reinforcements"),
    JUMP_STRENGTH(null, null, null, null, null, null, null, null, null, "attribute.name.horse.jump_strength", "attribute.name.horse.jump_strength", "attribute.name.zombie.spawn_reinforcements", "attribute.name.zombie.spawn_reinforcements");

    private final String v1_7, v1_8, v1_9, v1_10, v1_11, v1_12, v1_13, v1_14, v1_15, v1_16, v1_17, v1_18, v1_19;

    AttributeType(String v1_7, String v1_8, String v1_9, String v1_10, String v1_11, String v1_12,
                  String v1_13, String v1_14, String v1_15, String v1_16, String v1_17, String v1_18, String v1_19) {
        this.v1_7 = v1_7;
        this.v1_8 = v1_8;
        this.v1_9 = v1_9;
        this.v1_10 = v1_10;
        this.v1_11 = v1_11;
        this.v1_12 = v1_12;
        this.v1_13 = v1_13;
        this.v1_14 = v1_14;
        this.v1_15 = v1_15;
        this.v1_16 = v1_16;
        this.v1_17 = v1_17;
        this.v1_18 = v1_18;
        this.v1_19 = v1_19;
    }

    public String getName() {
        switch(JVersion.getServerVersion()) {
            case UNKNOWN: {
                return v1_8;
            }
            case v1_19: {
                return v1_19;
            }
            case v1_18: {
                return v1_18;
            }
            case v1_17: {
                return v1_17;
            }
            case v1_16: {
                return v1_16;
            }
            case v1_15: {
                return v1_15;
            }
            case v1_14: {
                return v1_14;
            }
            case v1_13: {
                return v1_13;
            }
            case v1_12: {
                return v1_12;
            }
            case v1_11: {
                return v1_11;
            }
            case v1_10: {
                return v1_10;
            }
            case v1_9: {
                return v1_9;
            }
            case v1_8: {
                return v1_8;
            }
            case v1_7: {
                return v1_7;
            }
            default: {
                return null;
            }
        }
    }
}
