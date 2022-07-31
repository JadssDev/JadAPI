package dev.jadss.jadapi.bukkitImpl.enums;

import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JEnumReflector;
import org.bukkit.Effect;

public enum JParticle {
    EXPLOSION_NORMAL("EXPLOSION"),
    EXPLOSION_LARGE("EXPLOSION_LARGE"),
    EXPLOSION_HUGE("EXPLOSION_HUGE"),
    FIREWORKS_SPARK,
    WATER_BUBBLE,
    WATER_SPLASH("SPLASH"),
    WATER_WAKE,
    SUSPENDED("VOID_FOG"),
    SUSPENDED_DEPTH("VOID_FOG"),
    CRIT,
    CRIT_MAGIC("MAGIC_CRIT"),
    SMOKE_NORMAL("PARTICLE_SMOKE"),
    SMOKE_LARGE("LARGE_SMOKE"),
    SPELL,
    SPELL_INSTANT("INSTANT_SPELL"),
    SPELL_MOB("POTION_SWIRL"),
    SPELL_MOB_AMBIENT("POTION_SWIRL_TRANSPARENT"),
    SPELL_WITCH("WITCH_MAGIC"),
    DRIP_WATER("WATERDRIP"),
    DRIP_LAVA("LAVADRIP"),
    VILLAGER_ANGRY("VILLAGER_THUNDERCLOUD"),
    VILLAGER_HAPPY("HAPPY_VILLAGER"),
    TOWN_AURA("SMALL_SMOKE"),
    NOTE,
    PORTAL,
    ENCHANTMENT_TABLE("FLYING_GLYPH"),
    FLAME,
    LAVA,
    FOOTSTEP,
    CLOUD,
    REDSTONE("COLOURED_DUST"),
    SNOWBALL("SNOWBALL_BREAK"),
    SNOW_SHOVEL,
    SLIME,
    HEART,
    BARRIER,
    ITEM_CRACK("ITEM_BREAK"),
    BLOCK_CRACK("TILE_BREAK"),
    BLOCK_DUST("TILE_DUST"),
    WATER_DROP,
    ITEM_TAKE,
    MOB_APPEARANCE,
    DRAGON_BREATH,
    END_ROD,
    DAMAGE_INDICATOR,
    SWEEP_ATTACK,
    FALLING_DUST,
    TOTEM,
    SPIT,
    SQUID_INK,
    BUBBLE_POP,
    CURRENT_DOWN,
    BUBBLE_COLUMN_UP,
    NAUTILUS,
    DOLPHIN,
    SNEEZE,
    CAMPFIRE_COSY_SMOKE,
    CAMPFIRE_SIGNAL_SMOKE,
    COMPOSTER,
    FLASH,
    FALLING_LAVA,
    LANDING_LAVA,
    FALLING_WATER,
    DRIPPING_HONEY,
    FALLING_HONEY,
    LANDING_HONEY,
    FALLING_NECTAR,
    SOUL_FIRE_FLAME,
    ASH,
    CRIMSON_SPORE,
    WARPED_SPORE,
    SOUL,
    DRIPPING_OBSIDIAN_TEAR,
    FALLING_OBSIDIAN_TEAR,
    LANDING_OBSIDIAN_TEAR,
    REVERSE_PORTAL,
    WHITE_ASH,
    DUST_COLOR_TRANSITION,
    VIBRATION,
    FALLING_SPORE_BLOSSOM,
    SPORE_BLOSSOM_AIR,
    SMALL_FLAME,
    SNOWFLAKE,
    DRIPPING_DRIPSTONE_LAVA,
    FALLING_DRIPSTONE_LAVA,
    DRIPPING_DRIPSTONE_WATER,
    FALLING_DRIPSTONE_WATER,
    GLOW_SQUID_INK,
    GLOW,
    WAX_ON,
    WAX_OFF,
    ELECTRIC_SPARK,
    SCRAPE,
    SONIC_BOOM,
    SCULK_SOUL,
    SCULK_CHARGE,
    SCULK_CHARGE_POP,
    SHRIEK,
    BLOCK_MARKER,

    LIGHT; //only added 1.17? wtf.

    public static final Class<?> PARTICLE_CLASS = JClassReflector.getClass("org.bukkit.Particle");
    public static final Class<?> EFFECT_CLASS = Effect.class;

    private final String[] alternativeNames;

    JParticle(String... alternativeNames) {
        this.alternativeNames = alternativeNames;
    }

    public Enum<?> parseParticle() {
        Enum<?> object;

        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_8)) {
            //Effect.class
            object = JEnumReflector.getEnum(this.name(), EFFECT_CLASS);

            if (object == null) {
                for (String alternate : this.alternativeNames) {
                    object = JEnumReflector.getEnum(alternate, EFFECT_CLASS);
                    if (object != null)
                        break;
                }
            }
        } else {
            //Particle.class
            object = JEnumReflector.getEnum(this.name(), PARTICLE_CLASS);

            if (object == null) {
                for (String alternate : this.alternativeNames) {
                    object = JEnumReflector.getEnum(alternate, PARTICLE_CLASS);
                    if (object != null)
                        break;
                }
            }
        }

        return object;
    }
}
