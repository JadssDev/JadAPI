package dev.jadss.jadapi.management.nms.objects.world.block.state;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.enums.*;
import dev.jadss.jadapi.utils.JReflection;

/**
 * Represents a list with all the Block states Minecraft has!
 * <p>I used this page here with some consulting in NMS: <b>https://minecraft.fandom.com/wiki/Block_states</b></p>
 */
public class StateList {

    //MISSING BLOCK STATES:
    // "vine_end" //idk
    // "short" //piston
    // "bottom" //idk
    // "drag" //bubbles?
    // "falling" //fluids
    // "hanging" //?
    // "mode" //idk
    // "orientation" //enum
    // "face" //enum
    // "attachment" //enum
    // "candles //int
    // "eggs" //int
    // "hatch" //int
    // "level" //int
    // "pickles" //int
    // "leaves" //Enum
    // "vertical_direction"
    // "thickness" //Enum

    /**
     * This BlockState can be used in many many many Blocks, so that's why the material specified is AIR.
     */
    public static final EnumBlockState<EnumDirection> FACING;

    /**
     * This BlockState can be used in many many many Blocks, so that's why the material specified is AIR.
     */
    public static final IntegerBlockState ROTATION;

    /**
     * This BlockState can be used in many many many Blocks, so that's why the material specified is AIR.
     */
    public static final EnumBlockState<EnumAxis> AXIS;

    /**
     * Represents which part of the bed this block state is.
     */
    public static final EnumBlockState<EnumBedPart> BED_PART;

    /**
     * Represents if this bed is occupied!
     */
    public static final BooleanBlockState BED_OCCUPIED;

    /**
     * Represents the damage the anvil has.
     */
    public static final IntegerBlockState ANVIL_DAMAGE;

    /**
     * Represents the bottles in a brewing stand.
     */
    public static final BooleanBlockState BREWING_STAND_HAS_BOTTLE_0, BREWING_STAND_HAS_BOTTLE_1, BREWING_STAND_HAS_BOTTLE_2;

    /**
     * Represents if the block is powered by redstone (means a block is affected by redstone, and is currently updated).
     */
    public static final BooleanBlockState POWERED_BY_REDSTONE;

    /**
     * Means the power of a redstone wire. (can also be used in daylight detectors to determine the sun's power basically)
     */
    public static final IntegerBlockState REDSTONE_POWER;

    /**
     * Represents the many ages plants can have.
     */
    public static final IntegerBlockState AGE_TYPE_1, AGE_TYPE_2, AGE_TYPE_3, AGE_TYPE_4, AGE_TYPE_5, AGE_TYPE_6, AGE_TYPE_7;

    /**
     * Represents the bites of a cake, 0 bites = full cake, 6 = 1 bite remains.
     */
    public static final IntegerBlockState CAKE_BITES;

    /**
     * Represents the color of for example, a wool, or a bed in 1.12
     */
    public static final EnumBlockState<EnumColor> COLOR;

    /**
     * The level of water in the cauldron!
     */
    public static final IntegerBlockState CAULDRON_LEVEL;

    /**
     * Used for walls and fences (please note fences do not support up and down), for their connections to other blocks or walls, etc.
     */
    public static final BooleanBlockState UP, DOWN, NORTH, EAST, SOUTH, WEST;

    /**
     * if the block is triggered by redstone, this is specific to Droppers, dispensers, and others....
     */
    public static final BooleanBlockState TRIGGERED;

    /**
     * Represents that this dirt or grass is snowy!
     */
    public static final BooleanBlockState SNOWY;

    /**
     * Represents if a door, trapdoor, or fence gate (and others) is open or closed.
     */
    public static final BooleanBlockState OPEN;

    /**
     * Where the location of the hinge is, left or right!
     */
    public static final EnumBlockState<EnumDoorHinge> DOOR_HINGE;

    /**
     * ....
     */
    public static final EnumBlockState<EnumDoorHalf> HALF;

    /**
     * Represents if the block is seamless, used in slabs. (not present in versions newer than 1.13) (only in 1.12 and down)
     */
    public static final BooleanBlockState SEAMLESS;

    /**
     * Represents if an End portal frame has an eye in its center.
     */
    public static final BooleanBlockState HAS_EYE;

    /**
     * Represents if the fence gate has a wall attached to it.
     */
    public static final BooleanBlockState FENCE_GATE_ATTACHED_TO_WALL;

    /**
     * Represents the level of a fluid in minecraft.
     */
    public static final IntegerBlockState FLUID_LEVEL;

    /**
     * You know what this is...
     */
    public static final BooleanBlockState HOPPED_ENABLED;

    /**
     * Represents if a jukebox has a record.
     */
    public static final BooleanBlockState JUKEBOX_HAS_RECORD;

    /**
     * Represents if leaves are to be decayed.
     * <b>Note, in 1.13 and above this state was actually changed to be persistent, so if you're in 1.13, make sure to invert it!</b>
     */
    public static final BooleanBlockState LEAVES_DECAYABLE;

    /**
     * Represents the distance from the respective wood type! Please note this does not exist in versions bellow 1.13. (it does in 1.13 tho)
     */
    public static final IntegerBlockState LEAVES_DISTANCE;

    /**
     * Represents if a piston is extended.
     */
    public static final BooleanBlockState EXTENDED;

    /**
     * Represents if a piston is short, the head itself, this is actually stupid, but it exists and it's mapped, it's on the piston head extended btw.
     */
    public static final BooleanBlockState SHORT_PISTON;

    /**
     * Represents how a rail is positioned.
     */
    public static final EnumBlockState<EnumTrackPosition> TRACK_POSITION;

    /**
     * Represents the power of a Weighted pressure plate!
     */
    public static final IntegerBlockState WEIGHTED_PRESSURE_PLATE_POWER;

    /**
     * Represents the mode a comparator is currently in!
     */
    public static final EnumBlockState<EnumComparatorMode> COMPARATOR_MODE;

    /**
     * Represents if the command block is conditional.
     */
    public static final BooleanBlockState CONDITIONAL_COMMAND_BLOCK;

    /**
     * Represents the connection of a Redstone Wire to the north.
     */
    public static final EnumBlockState<EnumRedstoneWireConnection> REDSTONE_WIRE_CONNECTION_NORTH;

    /**
     * Represents the connection of a Redstone Wire to the east.
     */
    public static final EnumBlockState<EnumRedstoneWireConnection> REDSTONE_WIRE_CONNECTION_EAST;

    /**
     * Represents the connection of a Redstone Wire to the south.
     */
    public static final EnumBlockState<EnumRedstoneWireConnection> REDSTONE_WIRE_CONNECTION_SOUTH;

    /**
     * Represents the connection of a Redstone Wire to the west.
     */
    public static final EnumBlockState<EnumRedstoneWireConnection> REDSTONE_WIRE_CONNECTION_WEST;

    /**
     * Represents if a repeater is currently blocked... this means the repeater's current mode (powered or not) is continuous.
     */
    public static final BooleanBlockState REPEATER_LOCKED;

    /**
     * The delay of a repeater, in ticks.
     */
    public static final IntegerBlockState REPEATER_DELAY;

    /**
     * Which stage a tree is from growing!
     */
    public static final IntegerBlockState SAPLING_STAGE;

    /**
     * Represents if this skull will be dropped when you break it.
     */
    public static final BooleanBlockState SKULL_NO_DROP;

    /**
     * How many layers a snow layer has.
     */
    public static final IntegerBlockState SNOW_LAYERS;

    /**
     * Represents the moisture level of a Farmland.
     */
    public static final IntegerBlockState FARM_LAND_MOISTURE;

    /**
     * Represents if a Sponge is wet or not!
     */
    public static final BooleanBlockState SPONGE_WET;

    /**
     * Represents the half of the slab.
     * @see EnumStairsHalf
     */
    public static final EnumBlockState<EnumStairsHalf> STAIRS_HALF;

    /**
     * Represents the shape of the Stairs!
     */
    public static final EnumBlockState<EnumStairsShape> STAIRS_SHAPE;

    /**
     * Represents what half of the tall plant this block is!
     */
    public static final EnumBlockState<EnumTallPlantHalf> TALL_PLANT_HALF;

    /**
     * Where the trapdoor is attached to!
     */
    public static final EnumBlockState<EnumTrapdoorHalf> TRAPDOOR_ATTACHED_HALF;

    /**
     * Is this tripwire suspended, please note that this does not exist in 1.9+. (yes in 1.8 tho, that's why it is here!)
     * <p>This means there is air bellow the block.</p>
     */
    public static final BooleanBlockState TRIPWIRE_SUSPENDED;

    /**
     * Is this tripwire attached to a string or is the string attached to a tripwire hook?
     */
    public static final BooleanBlockState TRIPWIRE_ATTACHED;

    /**
     * Is this tripwire disabled?
     */
    public static final BooleanBlockState TRIPWIRE_DISARMED;

    /**
     * Represents if a Lectern has a book on it.
     */
    public static final BooleanBlockState LECTERN_HAS_BOOK;

    /**
     * Represents how many charges does a Respawn Anchor have.
     */
    public static final IntegerBlockState RESPAWN_ANCHOR_CHARGES;

    /**
     * Represents the instrument of a note block!
     */
    public static final EnumBlockState<EnumInstrument> NOTE_BLOCK_INSTRUMENT; //S

    /**
     * Represents the note of Note Block!
     */
    public static final IntegerBlockState NOTE_BLOCK_NOTE; //S

    /**
     * This block state defines if a cave vine has berries to be collected.
     */
    public static final BooleanBlockState CAVE_VINE_HAS_BERRIES;

    /**
     * Does the block have water? is it waterlogged... just google it.
     */
    public static final BooleanBlockState WATERLOGGED;

    /**
     * Represents if a tnt will blow up if you even destroy it.
     */
    public static final BooleanBlockState TNT_UNSTABLE;

    /**
     * Represents if a campfire has a hay bale bellow it.
     */
    public static final BooleanBlockState CAMPFIRE_SIGNAL_FIRE;

    /**
     * Represents if a redstone torch or campfire, or furnace for example, is lit!
     */
    public static final BooleanBlockState IS_LIT;

    /**
     * Represents if a day light detector is inverted or not!
     */
    public static final BooleanBlockState DAYLIGHT_DETECTOR_INVERTED; //S <- represented in older versions in the block tile entity itself.

    /**
     * The honey level of a bee hive!
     */
    public static final IntegerBlockState HONEY_LEVEL;

    /**
     * Represents the title of a drip leaf!
     */
    public static final EnumBlockState<EnumTilt> DRIPLEAF_TILT;

    /**
     * Represents the phase a sculk sensor is currently in.
     */
    public static final EnumBlockState<EnumSculkSensorPhase> SCULK_SENSOR_PHASE;

    /**
     * Represents how much things it has, level 8 means it can be collected the bone meal!
     */
    public static final IntegerBlockState COMPOSTER_LEVEL;

    static {
        FACING = EnumBlockState.createInstance("facing", EnumDirection.class, EnumDirection.DOWN);
        ROTATION = IntegerBlockState.createInstance("rotation", 0, 15);
        AXIS = EnumBlockState.createInstance("axis", EnumAxis.class, EnumAxis.X);

        BED_PART = EnumBlockState.createInstance("part", EnumBedPart.class, EnumBedPart.FOOT);
        BED_OCCUPIED = BooleanBlockState.createInstance("occupied", false);
        ANVIL_DAMAGE = IntegerBlockState.createInstance("damage", 0, 15);

        BREWING_STAND_HAS_BOTTLE_0 = BooleanBlockState.createInstance("has_bottle_0", false);
        BREWING_STAND_HAS_BOTTLE_1 = BooleanBlockState.createInstance("has_bottle_1", false);
        BREWING_STAND_HAS_BOTTLE_2 = BooleanBlockState.createInstance("has_bottle_2", false);

        POWERED_BY_REDSTONE = BooleanBlockState.createInstance("powered", false);
        REDSTONE_POWER = IntegerBlockState.createInstance("power", 0, 15);

        AGE_TYPE_1 = IntegerBlockState.createInstance("age", 0, 1);
        AGE_TYPE_2 = IntegerBlockState.createInstance("age", 0, 2);
        AGE_TYPE_3 = IntegerBlockState.createInstance("age", 0, 3);
        AGE_TYPE_4 = IntegerBlockState.createInstance("age", 0, 5);
        AGE_TYPE_5 = IntegerBlockState.createInstance("age", 0, 7);
        AGE_TYPE_6 = IntegerBlockState.createInstance("age", 0, 15);
        AGE_TYPE_7 = IntegerBlockState.createInstance("age", 0, 25);

        CAKE_BITES = IntegerBlockState.createInstance("bites", 0, 6);

        COLOR = EnumBlockState.createInstance("color", EnumColor.class, EnumColor.WHITE.getNMSEnumClass());

        CAULDRON_LEVEL = IntegerBlockState.createInstance("level", 0, 3);

        UP = BooleanBlockState.createInstance("up", false);
        DOWN = BooleanBlockState.createInstance("down", false);
        NORTH = BooleanBlockState.createInstance("north", false);
        EAST = BooleanBlockState.createInstance("east", false);
        SOUTH = BooleanBlockState.createInstance("south", false);
        WEST = BooleanBlockState.createInstance("west", false);

        TRIGGERED = BooleanBlockState.createInstance("triggered", false);

        SNOWY = BooleanBlockState.createInstance("snowy", false);

        OPEN = BooleanBlockState.createInstance("open", false);

        DOOR_HINGE = EnumBlockState.createInstance("hinge", EnumDoorHinge.class, EnumDoorHinge.LEFT);

        HALF = EnumBlockState.createInstance("half", EnumDoorHalf.class, EnumDoorHalf.LOWER);

        SEAMLESS = BooleanBlockState.createInstance("seamless", false);

        HAS_EYE = BooleanBlockState.createInstance("eye", false);

        FENCE_GATE_ATTACHED_TO_WALL = BooleanBlockState.createInstance("in_wall", false);

        FLUID_LEVEL = IntegerBlockState.createInstance("level", 0, 15);

        HOPPED_ENABLED = BooleanBlockState.createInstance("enabled", true);

        JUKEBOX_HAS_RECORD = BooleanBlockState.createInstance("has_record", false);

        LEAVES_DECAYABLE = BooleanBlockState.createInstance((JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_12) ? "decayable" : "persistent"), true);
        LEAVES_DISTANCE = IntegerBlockState.createInstance("distance", 1, 7);

        EXTENDED = BooleanBlockState.createInstance("extended", false);
        SHORT_PISTON = BooleanBlockState.createInstance("short", false);

        TRACK_POSITION = EnumBlockState.createInstance("shape", EnumTrackPosition.class, EnumTrackPosition.ASCENDING_EAST);

        WEIGHTED_PRESSURE_PLATE_POWER = IntegerBlockState.createInstance("power", 0, 15);

        COMPARATOR_MODE = EnumBlockState.createInstance("mode", EnumComparatorMode.class, EnumComparatorMode.COMPARE);

        CONDITIONAL_COMMAND_BLOCK = BooleanBlockState.createInstance("conditional", false);

        REDSTONE_WIRE_CONNECTION_NORTH = EnumBlockState.createInstance("north", EnumRedstoneWireConnection.class, EnumRedstoneWireConnection.SIDE);
        REDSTONE_WIRE_CONNECTION_EAST = EnumBlockState.createInstance("east", EnumRedstoneWireConnection.class, EnumRedstoneWireConnection.SIDE);
        REDSTONE_WIRE_CONNECTION_SOUTH = EnumBlockState.createInstance("south", EnumRedstoneWireConnection.class, EnumRedstoneWireConnection.SIDE);
        REDSTONE_WIRE_CONNECTION_WEST = EnumBlockState.createInstance("west", EnumRedstoneWireConnection.class, EnumRedstoneWireConnection.SIDE);

        REPEATER_LOCKED = BooleanBlockState.createInstance("locked", false);
        REPEATER_DELAY = IntegerBlockState.createInstance("delay", 0, 4);

        SAPLING_STAGE = IntegerBlockState.createInstance("stage", 0, 1);

        SKULL_NO_DROP = BooleanBlockState.createInstance("nodrop", false);

        SNOW_LAYERS = IntegerBlockState.createInstance("layers", 1, 8);

        FARM_LAND_MOISTURE = IntegerBlockState.createInstance("moisture", 0, 7);

        SPONGE_WET = BooleanBlockState.createInstance("wet", false);

        STAIRS_HALF = EnumBlockState.createInstance("half", EnumStairsHalf.class, EnumStairsHalf.BOTTOM);
        STAIRS_SHAPE = EnumBlockState.createInstance("shape", EnumStairsShape.class, EnumStairsShape.STRAIGHT);

        TALL_PLANT_HALF = EnumBlockState.createInstance("half", EnumTallPlantHalf.class, EnumTallPlantHalf.LOWER);

        TRAPDOOR_ATTACHED_HALF = EnumBlockState.createInstance("half", EnumTrapdoorHalf.class, EnumTrapdoorHalf.BOTTOM);

        TRIPWIRE_SUSPENDED = BooleanBlockState.createInstance("suspended", false);
        TRIPWIRE_ATTACHED = BooleanBlockState.createInstance("attached", false);
        TRIPWIRE_DISARMED = BooleanBlockState.createInstance("disarmed", false);

        LECTERN_HAS_BOOK = BooleanBlockState.createInstance("has_book", false);

        RESPAWN_ANCHOR_CHARGES = IntegerBlockState.createInstance("charges", 0, 4);

        NOTE_BLOCK_INSTRUMENT = EnumBlockState.createInstance("instrument", EnumInstrument.class, EnumInstrument.HARP);
        NOTE_BLOCK_NOTE = IntegerBlockState.createInstance("note", 0, 24);

        CAVE_VINE_HAS_BERRIES = BooleanBlockState.createInstance("berries", false);

        WATERLOGGED = BooleanBlockState.createInstance("waterlogged", false);

        TNT_UNSTABLE = BooleanBlockState.createInstance((JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13) ? "unstable" : "explode"), false);

        CAMPFIRE_SIGNAL_FIRE = BooleanBlockState.createInstance("signal_fire", false);

        IS_LIT = BooleanBlockState.createInstance("lit", false);

        DAYLIGHT_DETECTOR_INVERTED = BooleanBlockState.createInstance("inverted", false);

        HONEY_LEVEL = IntegerBlockState.createInstance("honey_level", 0, 5);

        DRIPLEAF_TILT = EnumBlockState.createInstance("tilt", EnumTilt.class, EnumTilt.NONE);

        SCULK_SENSOR_PHASE = EnumBlockState.createInstance("sculk_sensor_phase", EnumSculkSensorPhase.class, EnumSculkSensorPhase.INACTIVE);

        COMPOSTER_LEVEL = IntegerBlockState.createInstance("level", 1, 8);
    }


    public static StateType<?> getStateTypeByName(String typeName) {
        return JReflection.getObjectFields(StateType.class, null).stream()
                .filter(obj -> obj instanceof StateType).map(obj -> ((StateType<?>) obj))
                .filter(s -> s.getStateId().equalsIgnoreCase(typeName))
                .findFirst().orElse(null);
    }

    private StateList() {}


}
