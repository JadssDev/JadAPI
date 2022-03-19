package dev.jadss.jadapi.bukkitImpl.enums;

import dev.jadss.jadapi.exceptions.JException;
import dev.jadss.jadapi.utils.JReflection;
import org.bukkit.Effect;

@SuppressWarnings("all")
public enum JParticle {
    EXPLOSION_NORMAL("EXPLOSION", "EXPLOSION", "EXPLOSION_NORMAL", "EXPLOSION_NORMAL", "EXPLOSION_NORMAL", "EXPLOSION_NORMAL", "EXPLOSION_NORMAL", "EXPLOSION_NORMAL", "EXPLOSION_NORMAL", "EXPLOSION_NORMAL", "EXPLOSION_NORMAL", "EXPLOSION_NORMAL"),
    EXPLOSION_LARGE("EXPLOSION_LARGE", "EXPLOSION_LARGE", "EXPLOSION_LARGE", "EXPLOSION_LARGE", "EXPLOSION_LARGE", "EXPLOSION_LARGE", "EXPLOSION_LARGE", "EXPLOSION_LARGE", "EXPLOSION_LARGE", "EXPLOSION_LARGE", "EXPLOSION_LARGE", "EXPLOSION_LARGE"),
    EXPLOSION_HUGE("EXPLOSION_HUGE", "EXPLOSION_HUGE", "EXPLOSION_HUGE", "EXPLOSION_HUGE", "EXPLOSION_HUGE", "EXPLOSION_HUGE", "EXPLOSION_HUGE", "EXPLOSION_HUGE", "EXPLOSION_HUGE", "EXPLOSION_HUGE", "EXPLOSION_HUGE", "EXPLOSION_HUGE"),

    FIREWORKS_SPARK("FIREWORKS_SPARK", "FIREWORKS_SPARK", "FIREWORKS_SPARK", "FIREWORKS_SPARK", "FIREWORKS_SPARK", "FIREWORKS_SPARK", "FIREWORKS_SPARK", "FIREWORKS_SPARK", "FIREWORKS_SPARK", "FIREWORKS_SPARK", "FIREWORKS_SPARK", "FIREWORKS_SPARK"),

    WATER_BUBBLE(null, null, "WATER_BUBBLE", "WATER_BUBBLE", "WATER_BUBBLE", "WATER_BUBBLE", "WATER_BUBBLE", "WATER_BUBBLE", "WATER_BUBBLE", "WATER_BUBBLE", "WATER_BUBBLE", "WATER_BUBBLE"),
    WATER_DROP(null, null, "WATER_DROP", "WATER_DROP", "WATER_DROP", "WATER_DROP", "WATER_DROP", "WATER_DROP", "WATER_DROP", "WATER_DROP", "WATER_DROP", "WATER_DROP"),
    WATER_SPLASH("SPLASH", "SPLASH", "WATER_SPLASH", "WATER_SPLASH", "WATER_SPLASH", "WATER_SPLASH", "WATER_SPLASH", "WATER_SPLASH", "WATER_SPLASH", "WATER_SPLASH", "WATER_SPLASH", "WATER_SPLASH"),
    WATER_WAKE(null, null, "WATER_WAKE", "WATER_WAKE", "WATER_WAKE", "WATER_WAKE", "WATER_WAKE", "WATER_WAKE", "WATER_WAKE", "WATER_WAKE", "WATER_WAKE", "WATER_WAKE"),

    SUSPENDED(null, null, "SUSPENDED", "SUSPENDED", "SUSPENDED", "SUSPENDED", "SUSPENDED", "SUSPENDED", "SUSPENDED", "SUSPENDED", "SUSPENDED", "SUSPENDED"),
    SUSPENDED_DEPTH(null, null, "SUSPENDED_DEPTH", "SUSPENDED_DEPTH", "SUSPENDED_DEPTH", "SUSPENDED_DEPTH", "SUSPENDED_DEPTH", "SUSPENDED_DEPTH", "SUSPENDED_DEPTH", "SUSPENDED_DEPTH", "SUSPENDED_DEPTH", "SUSPDENED_DEPTH"),

    CRIT("CRIT", "CRIT", "CRIT", "CRIT", "CRIT", "CRIT", "CRIT", "CRIT", "CRIT", "CRIT", "CRIT", "CRIT"),
    CRIT_MAGIC("MAGIC_CRIT", "MAGIC_CRIT", "CRIT_MAGIC", "CRIT_MAGIC", "CRIT_MAGIC", "CRIT_MAGIC", "CRIT_MAGIC", "CRIT_MAGIC", "CRIT_MAGIC", "CRIT_MAGIC", "CRIT_MAGIC", "CRIT_MAGIC"),

    SMOKE_SMALL("SMALL_SMOKE", "SMALL_SMOKE", null, null, null, null, null, null, null, null, null, null),
    SMOKE_NORMAL("PARTICLE_SMOKE", "PARTICLE_SMOKE", "SMOKE_NORMAL", "SMOKE_NORMAL", "SMOKE_NORMAL", "SMOKE_NORMAL", "SMOKE_NORMAL", "SMOKE_NORMAL", "SMOKE_NORMAL", "SMOKE_NORMAL", "SMOKE_NORMAL", "SMOKE_NORMAL"),
    SMOKE_LARGE("LARGE_SMOKE", "LARGE_SMOKE", "SMOKE_LARGE", "SMOKE_LARGE", "SMOKE_LARGE", "SMOKE_LARGE", "SMOKE_LARGE", "SMOKE_LARGE", "SMOKE_LARGE", "SMOKE_LARGE", "SMOKE_LARGE", "SMOKE_LARGE"),

    SPELL("SPELL", "SPELL", "SPELL", "SPELL", "SPELL", "SPELL", "SPELL", "SPELL", "SPELL", "SPELL", "SPELL", "SPELL"),
    SPELL_INSTANT("INSTANT_SPELL", "INSTANT_SPELL", "SPELL_INSTANT", "SPELL_INSTANT", "SPELL_INSTANT", "SPELL_INSTANT", "SPELL_INSTANT", "SPELL_INSTANT", "SPELL_INSTANT", "SPELL_INSTANT", "SPELL_INSTANT", "SPELL_INSTANT"),
    SPELL_MOB("MOBSPAWNER_FLAMES", "MOBSPAWNER_FLAMES", "SPELL_MOB", "SPELL_MOB", "SPELL_MOB", "SPELL_MOB", "SPELL_MOB", "SPELL_MOB", "SPELL_MOB", "SPELL_MOB", "SPELL_MOB", "SPELL_MOB"),
    SPELL_MOB_AMBIENT("MOBSPAWNER_FLAMES", "MOBSPAWNER_FLAMES", "SPELL_MOB_AMBIENT", "SPELL_MOB_AMBIENT", "SPELL_MOB_AMBIENT", "SPELL_MOB_AMBIENT", "SPELL_MOB_AMBIENT", "SPELL_MOB_AMBIENT", "SPELL_MOB_AMBIENT", "SPELL_MOB_AMBIENT", "SPELL_MOB_AMBIENT", "SPELL_MOB_AMBIENT"),
    SPELL_WHICH("WITCH_MAGIC", "WITCH_MAGIC", "SPELL_WHICH", "SPELL_WHICH", "SPELL_WHICH", "SPELL_WHICH", "SPELL_WHICH", "SPELL_WHICH", "SPELL_WHICH", "SPELL_WHICH", "SPELL_WHICH", "SPELL_WHICH"),

    DRIP_WATER("WATERDRIP", "WATERDRIP", "DRIP_WATER", "DRIP_WATER", "DRIP_WATER", "DRIP_WATER", "DRIP_WATER", "DRIP_WATER", "DRIP_WATER", "DRIP_WATER", "DRIP_WATER", "DRIP_WATER"),
    DRIP_LAVA("LAVADRIP", "LAVADRIP", "DRIP_LAVA", "DRIP_LAVA", "DRIP_LAVA", "DRIP_LAVA", "DRIP_LAVA", "DRIP_LAVA", "DRIP_LAVA", "DRIP_LAVA", "DRIP_LAVA", "DRIP_LAVA"),

    VILLAGER_ANGRY("VILLAGER_THUNDERCLOUD", "VILLAGER_THUNDERCLOUD", "VILLAGER_ANGRY", "VILLAGER_ANGRY", "VILLAGER_ANGRY", "VILLAGER_ANGRY", "VILLAGER_ANGRY", "VILLAGER_ANGRY", "VILLAGER_ANGRY", "VILLAGER_ANGRY", "VILLAGER_ANGRY", "VILLAGER_ANGRY"),
    VILLAGER_HAPPY("HAPPY_VILLAGER", "HAPPY_VILLAGER", "VILLAGER_HAPPY", "VILLAGER_HAPPY", "VILLAGER_HAPPY", "VILLAGER_HAPPY", "VILLAGER_HAPPY", "VILLAGER_HAPPY", "VILLAGER_HAPPY", "VILLAGER_HAPPY", "VILLAGER_HAPPY", "VILLAGER_HAPPY"),

    TOWN_AURA(null, null, "TOWN_AURA", "TOWN_AURA", "TOWN_AURA", "TOWN_AURA", "TOWN_AURA", "TOWN_AURA", "TOWN_AURA", "TOWN_AURA", "TOWN_AURA", "TOWN_AURA"),

    NOTE("NOTE", "NOTE", "NOTE", "NOTE", "NOTE", "NOTE", "NOTE", "NOTE", "NOTE", "NOTE", "NOTE", "NOTE"),

    PORTAL("PORTAL", "PORTAL", "PORTAL", "PORTAL", "PORTAL", "PORTAL", "PORTAL", "PORTAL", "PORTAL", "PORTAL", "PORTAL", "PORTAL"),

    ENCHANTMENT_TABLE("FLYING_GLYPH", "FLYING_GLYPH", "ENCHANTMENT_TABLE", "ENCHANTMENT_TABLE", "ENCHANTMENT_TABLE", "ENCHANTMENT_TABLE", "ENCHANTMENT_TABLE", "ENCHANTMENT_TABLE", "ENCHANTMENT_TABLE", "ENCHANTMENT_TABLE", "ENCHANTMENT_TABLE", "ENCHANTMENT_TABLE"),

    FLAME("FLAME", "FLAME", "FLAME", "FLAME", "FLAME", "FLAME", "FLAME", "FLAME", "FLAME", "FLAME", "FLAME", "FLAME"),

    LAVA("LAVA_POP", "LAVA_POP", "LAVA", "LAVA", "LAVA", "LAVA", "LAVA", "LAVA", "LAVA", "LAVA", "LAVA", "LAVA"),

    CLOUD("CLOUD", "CLOUD", "CLOUD", "CLOUD", "CLOUD", "CLOUD", "CLOUD", "CLOUD", "CLOUD", "CLOUD", "CLOUD", "CLOUD"),

    REDSTONE("COLOURED_DUST", "COLOURED_DUST", "REDSTONE", "REDSTONE", "REDSTONE", "REDSTONE", "REDSTONE", "REDSTONE", "REDSTONE", "REDSTONE", "REDSTONE", "REDSTONE"),

    SNOWBALL("SNOWBALL_BREAK", "SNOWBALL_BREAK", "SNOWBALL", "SNOWBALL", "SNOWBALL", "SNOWBALL", "SNOWBALL", "SNOWBALL", "SNOWBALL", "SNOWBALL", "SNOWBALL", "SNOWBALL"),

    SNOW_SHOVEL("SNOW_SHOVEL", "SNOW_SHOVEL", "SNOW_SHOVEL", "SNOW_SHOVEL", "SNOW_SHOVEL", "SNOW_SHOVEL", "SNOW_SHOVEL", "SNOW_SHOVEL", "SNOW_SHOVEL", "SNOW_SHOVEL", "SNOW_SHOVEL", "SNOW_SHOVEL"),

    SLIME("SLIME", "SLIME", "SLIME", "SLIME", "SLIME", "SLIME", "SLIME", "SLIME", "SLIME", "SLIME", "SLIME", "SLIME"),

    HEART("HEART", "HEART", "HEART", "HEART", "HEART", "HEART", "HEART", "HEART", "HEART", "HEART", "HEART", "HEART"),

    BARRIER(null, null, "BARRIER", "BARRIER", "BARRIER", "BARRIER", "BARRIER", "BARRIER", "BARRIER", "BARRIER", "BARRIER", null),

    ITEM_CRACK("ITEM_BREAK", "ITEM_BREAK", "ITEM_CRACK", "ITEM_CRACK", "ITEM_CRACK", "ITEM_CRACK", "ITEM_CRACK", "ITEM_CRACK", "ITEM_CRACK", "ITEM_CRACK", "ITEM_CRACK", "ITEM_CRACK"),
    BLOCK_CRACK("TILE_BREAK", "TILE_BREAK", "BLOCK_CRACK", "BLOCK_CRACK", "BLOCK_CRACK", "BLOCK_CRACK", "BLOCK_CRACK", "BLOCK_CRACK", "BLOCK_CRACK", "BLOCK_CRACK", "BLOCK_CRACK", "BLOCK_CRACK"),
    BLOCK_DUST("TILE_DUST", "TILE_DUST", "BLOCK_DUST", "BLOCK_DUST", "BLOCK_DUST", "BLOCK_DUST", "BLOCK_DUST", "BLOCK_DUST", "BLOCK_DUST", "BLOCK_DUST", "BLOCK_DUST", "BLOCK_DUST"),

    MOB_APPEARANCE("MOBSPAWNER_FLAMES", "MOBSPAWNER_FLAMES", "MOB_APPEARANCE", "MOB_APPEARANCE", "MOB_APPEARANCE", "MOB_APPEARANCE", "MOB_APPEARANCE", "MOB_APPEARANCE", "MOB_APPEARANCE", "MOB_APPEARANCE", "MOB_APPEARANCE", "MOB_APPEARANCE"),

    DRAGON_BREATH(null, null, "DRAGON_BREATH", "DRAGON_BREATH", "DRAGON_BREATH", "DRAGON_BREATH", "DRAGON_BREATH", "DRAGON_BREATH", "DRAGON_BREATH", "DRAGON_BREATH", "DRAGON_BREATH", "DRAGON_BREATH"),

    END_ROD("ENDER_SIGNAL", "ENDER_SIGNAL", "END_ROD", "END_ROD", "END_ROD", "END_ROD", "END_ROD", "END_ROD", "END_ROD", "END_ROD", "END_ROD", "END_ROD"),

    DAMAGE_INDICATOR(null, null, "DAMAGE_INDICATOR", "DAMAGE_INDICATOR", "DAMAGE_INDICATOR", "DAMAGE_INDICATOR", "DAMAGE_INDICATOR", "DAMAGE_INDICATOR", "DAMAGE_INDICATOR", "DAMAGE_INDICATOR", "DAMAGE_INDICATOR", "DAMAGE_INDICATOR"),

    SWEEP_ATTACK(null, null, "SWEEP_ATTACK", "SWEEP_ATTACK", "SWEEP_ATTACK", "SWEEP_ATTACK", "SWEEP_ATTACK", "SWEEP_ATTACK", "SWEEP_ATTACK", "SWEEP_ATTACK", "SWEEP_ATTACK", "SWEEP_ATTACK"),

    FALLING_DUST(null, null, "FALLING_DUST", "FALLING_DUST", "FALLING_DUST", "FALLING_DUST", "FALLING_DUST", "FALLING_DUST", "FALLING_DUST", "FALLING_DUST", "FALLING_DUST", "FALLING_DUST"),

    TOTEM(null, null, null, null, "TOTEM", "TOTEM", "TOTEM", "TOTEM", "TOTEM", "TOTEM", "TOTEM", "TOTEM"),

    SPIT(null, null, null, null, "SPIT", "SPIT", "SPIT", "SPIT", "SPIT", "SPIT", "SPIT", "SPIT"),

    SQUID_INK(null, null, null, null, null, null, "SQUID_INK", "SQUID_INK", "SQUID_INK", "SQUID_INK", "SQUID_INK", "SQUID_INK"),

    BUBBLE_POP(null, null, null, null, null, null, "BUBBLE_POP", "BUBBLE_POP", "BUBBLE_POP", "BUBBLE_POP", "BUBBLE_POP", "BUBBLE_POP"),

    CURRENT_DOWN(null, null, null, null, null, null, "CURRENT_DOWN", "CURRENT_DOWN", "CURRENT_DOWN", "CURRENT_DOWN", "CURRENT_DOWN", "CURRENT_DOWN"),

    BUBBLE_COLUMN_UP(null, null, null, null, null, null, "BUBBLE_COLUMN_UP", "BUBBLE_COLUMN_UP", "BUBBLE_COLUMN_UP", "BUBBLE_COLUMN_UP", "BUBBLE_COLUMN_UP", "BUBBLE_COLUMN_UP"),

    NAUTILUS(null, null, null, null, null, null, "NAUTILUS", "NAUTILUS", "NAUTILUS", "NAUTILUS", "NAUTILUS", "NAUTILUS"),

    DOLPHIN(null, null, null, null, null, null, "DOLPHIN", "DOLPHIN", "DOLPHIN", "DOLPHIN", "DOLPHIN", "DOLPHIN"),

    SNEEZE(null, null, null, null, null, null, null, "SNEEZE", "SNEEZE", "SNEEZE", "SNEEZE", "SNEEZE"),

    CAMPFIRE_COSY_SMOKE(null, null, null, null, null, null, null, "CAMPFIRE_COSY_SMOKE", "CAMPFIRE_COSY_SMOKE", "CAMPFIRE_COSY_SMOKE", "CAMPFIRE_COSY_SMOKE", "CAMPFIRE_COSY_SMOKE"),
    CAMPFIRE_SIGNAL_SMOKE(null, null, null, null, null, null, null, "CAMPFIRE_SIGNAL_SMOKE", "CAMPFIRE_SIGNAL_SMOKE", "CAMPFIRE_SIGNAL_SMOKE", "CAMPFIRE_SIGNAL_SMOKE", "CAMPFIRE_SIGNAL_SMOKE"),

    COMPOSTER(null, null, null, null, null, null, null, "COMPOSTER", "COMPOSTER", "COMPOSTER", "COMPOSTER", "COMPOSTER"),

    FLASH(null, null, null, null, null, null, null, "FLASH", "FLASH", "FLASH", "FLASH", "FLASH"),

    FALLING_LAVA(null, null, null, null, null, null, null, "FALLING_LAVA", "FALLING_LAVA", "FALLING_LAVA", "FALLING_LAVA", "FALLING_LAVA"),
    LANDING_LAVA(null, null, null, null, null, null, null, "LANDING_LAVA", "LANDING_LAVA", "LANDING_LAVA", "LANDING_LAVA", "LANDING_LAVA"),

    FALLING_WATER(null, null, null, null, null, null, null, "FALLING_WATER", "FALLING_WATER", "FALLING_WATER", "FALLING_WATER", "FALLING_WATER"),

    FALLING_HONEY(null, null, null, null, null, null, null, "DRIPPING_HONEY", "DRIPPING_HONEY", "DRIPPING_HONEY", "DRIPPING_HONEY", "DRIPPING_HONEY"),
    LANDING_HONEY(null, null, null, null, null, null, null, "LANDING_HONEY", "LANDING_HONEY", "LANDING_HONEY", "LANDING_HONEY", "LANDING_HONEY"),

    FALLING_NECTAR(null, null, null, null, null, null, null, null, "FALLING_NECTAR", "FALLING_NECTAR", "FALLING_NECTAR", "FALLING_NECTAR"),

    SOUL_FIRE_FLAME(null, null, null, null, null, null, null, null, null, null, null, "SOUL_FIRE_FLAME"),

    WHITE_ASH(null, null, null, null, null, null, null, null, null, null, null, "WHITE_ASH"),
    ASH(null, null, null, null, null, null, null, null, null, null, null, "ASH"),

    CRIMSON_SPORE(null, null, null, null, null, null, null, null, null, null, null, "CRIMSON_SPORE"),
    WARPED_SPORE(null, null, null, null, null, null, null, null, null, null, null, "WARPED_SPORE"),

    SOUL(null, null, null, null, null, null, null, null, null, null, null, "SOUL"),

    DRIPPING_OBSIDIAN_TEAR(null, null, null, null, null, null, null, null, null, null, null, "DRIPPING_OBSIDIAN_TEAR"),
    FALLING_OBSIDIAN_TEAR(null, null, null, null, null, null, null, null, null, null, null, "FALLING_OBSIDIAN_TEAR"),
    LANDING_OBSIDIAN_TEAR(null, null, null, null, null, null, null, null, null, null, null, "LANDING_OBISIDNA_TEAR"),

    REVERSE_PORTAL(null, null, null, null, null, null, null, null, null, null, null, "REVERSE_PORTAL"),

    DUST_COLOR_TRANSITION(null, null, null, null, null, null, null, null, null, null, null, "DUST_COLOR_TRANSITION"),

    VIBRATION(null, null, null, null, null, null, null, null, null, null, null, "VIBRATION"),

    FALLING_SPORE_BLOSSOM(null, null, null, null, null, null, null, null, null, null, null, "FALLING_SPORE_BLOSSOM"),
    SPORE_BLOSSOM_AIR(null, null, null, null, null, null, null, null, null, null, null, "SPORE_BLOSSOM_AIR"),

    SMALL_FLAME(null, null, null, null, null, null, null, null, null, null, null, "SMALL_FLAME"),

    SNOWFLAKE(null, null, null, null, null, null, null, null, null, null, null, "SNOWFLAKE"),

    DRIPPING_DRIPSTONE_LAVA(null, null, null, null, null, null, null, null, null, null, null, "DRIPPING_DRIPSTONE_LAVA"),
    FALLING_DRIPSTONE_LAVA(null, null, null, null, null, null, null, null, null, null, null, "FALLING_DRIPSTONE_LAVA"),

    DRIPPING_DRIPSTONE_WATER(null, null, null, null, null, null, null, null, null, null, null, "DRIPPING_DRIPSTONE_WATER"),
    FALLING_DRIPSTONE_WATER(null, null, null, null, null, null, null, null, null, null, null, "FALLING_DRIPSTONE_WATER"),

    GLOW_SQUID_INK(null, null, null, null, null, null, null, null, null, null, null, "GLOW_SQUID_INK"),
    GLOW(null, null, null, null, null, null, null, null, null, null, null, "GLOW"),

    WAX_ON(null, null, null, null, null, null, null, null, null, null, null, "WAX_ON"),
    WAX_OFF(null, null, null, null, null, null, null, null, null, null, null, "WAX_OFF"),

    ELECTRIC_SPARK(null, null, null, null, null, null, null, null, null, null, null, "ELECTRIC_SPARK"),
    SCRAPE(null, null, null, null, null, null, null, null, null, null, null, "SCRAPE"),

    BLOCK_MARKER(null, null, null, null, null, null, null, null, null, null, null, "BLOCK_MARKER"),

    //LEGACY
    LEGACY_COLOURED_DUST("COLOURED_DUST", "COLOURED_DUST", null, null, null, null, null, null, null, null, null, null),
    LEGACY_WITCH_MAGIC("WITCH_MAGIC", "WITCH_MAGIC", null, null, null, null, null, null, null, null, null, null);

    public String v1_7;
    public String v1_8;
    public String v1_9;
    public String v1_10;
    public String v1_11;
    public String v1_12;
    public String v1_13;
    public String v1_14;
    public String v1_15;
    public String v1_16;
    public String v1_17;
    public String v1_18;

    JParticle(String v1_7, String v1_8, String v1_9, String v1_10, String v1_11, String v1_12, String v1_13, String v1_14, String v1_15, String v1_16, String v1_17, String v1_18) {
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
    }

    public boolean isSupported() {
        JVersion serverVersion = JVersion.getServerVersion();
        if(serverVersion == JVersion.v1_7) return this.v1_7 != null;
        else if(serverVersion == JVersion.v1_8) return this.v1_8 != null;
        else if(serverVersion == JVersion.v1_9) return this.v1_9 != null;
        else if(serverVersion == JVersion.v1_10) return this.v1_10 != null;
        else if(serverVersion == JVersion.v1_11) return this.v1_11 != null;
        else if(serverVersion == JVersion.v1_12) return this.v1_12 != null;
        else if(serverVersion == JVersion.v1_13) return this.v1_13 != null;
        else if(serverVersion == JVersion.v1_14) return this.v1_14 != null;
        else if(serverVersion == JVersion.v1_15) return this.v1_15 != null;
        else if(serverVersion == JVersion.v1_16) return this.v1_16 != null;
        else if(serverVersion == JVersion.v1_17) return this.v1_17 != null;
        else if(serverVersion == JVersion.v1_18) return this.v1_18 != null;
        throw new JException(JException.Reason.UNKNOWN);
    }

    private String getVal() {
        JVersion version = JVersion.getServerVersion();

        if(version == JVersion.v1_7) return this.v1_7;
        else if(version == JVersion.v1_8) return this.v1_8;
        else if(version == JVersion.v1_9) return this.v1_9;
        else if(version == JVersion.v1_10) return this.v1_10;
        else if(version == JVersion.v1_11) return this.v1_11;
        else if(version == JVersion.v1_12) return this.v1_12;
        else if(version == JVersion.v1_13) return this.v1_13;
        else if(version == JVersion.v1_14) return this.v1_14;
        else if(version == JVersion.v1_15) return this.v1_15;
        else if(version == JVersion.v1_16) return this.v1_16;
        else if(version == JVersion.v1_17) return this.v1_17;
        else if(version == JVersion.v1_18) return this.v1_18;
        else throw new JException(JException.Reason.UNKNOWN);
    }

    /**
     * This method parses the particle.
     * @return An {@link Effect} or {@link org.bukkit.Particle} Object, can be null
     */
    public Object parseParticle() {
        JVersion version = JVersion.getServerVersion();
        Object particle = null;

        if(version == JVersion.v1_7 || version == JVersion.v1_8) {
            return Effect.valueOf(this.getVal());
        } else {
            Enum correct = null;
            for(Enum e : (Enum[]) JReflection.getReflectionClass("org.bukkit.Particle").getEnumConstants()) if (e.name().equalsIgnoreCase(this.getVal())) { correct = e; break; }
            return correct;
        }
    }
}
