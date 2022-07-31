package dev.jadss.jadapi.management.nms.objects.world.block.state;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.bukkitImpl.item.JMaterial;
import dev.jadss.jadapi.management.nms.enums.*;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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


    @StateId(id = "facing")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "The direction the Block is facing.")
    public static final EnumBlockState<EnumDirection> FACING;

    @StateId(id = "rotation")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "The rotation the Block is facing.")
    public static final IntegerBlockState ROTATION;

    @StateId(id = "axis")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "The axis the Block, only 3 values instead of 6 from facing.")
    public static final EnumBlockState<EnumAxis> AXIS;

    @StateId(id = "part")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "The part of the Bed.")
    public static final EnumBlockState<EnumBedPart> PART;

    @StateId(id = "occupied")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "If the bed is occupied...")
    public static final BooleanBlockState OCCUPIED;

    @StateId(id = "damage")
    @ImplementedAt(implementedVersion = JVersion.v1_8, removedVersion = JVersion.v1_13)
    @StateExplanation(explanation = "Represents the damage of the anvil before the Flattening update!")
    public static final IntegerBlockState ANVIL_DAMAGE;

    @StateId(id = "has_bottle_0")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents if the bottle has a potion the first position.")
    public static final BooleanBlockState BREWING_STAND_HAS_BOTTLE_0;

    @StateId(id = "has_bottle_1")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents if the bottle has a potion the second position.")
    public static final BooleanBlockState BREWING_STAND_HAS_BOTTLE_1;

    @StateId(id = "has_bottle_2")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents if the bottle has a potion the third position.")
    public static final BooleanBlockState BREWING_STAND_HAS_BOTTLE_2;

    @StateId(id = "powered")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents if the block can be powered by Redstone!")
    public static final BooleanBlockState POWERED_BY_REDSTONE;

    @StateId(id = "power")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents the power of a Redstone Wire or Redstone object!")
    public static final IntegerBlockState POWER;

    @StateId(id = "age")
    @ImplementedAt(implementedVersion = JVersion.v1_14) //bamboo
    @StateExplanation(explanation = "Represents the age of this block! (like bamboo)")
    public static final IntegerBlockState AGE_TO_1;

    @StateId(id = "age")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents the age of this block! (like cocoa)")
    public static final IntegerBlockState AGE_TO_2;

    @StateId(id = "age")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents the age of this block! (like nether wart and beetroots)")
    public static final IntegerBlockState AGE_TO_3;

    @StateId(id = "age")
    @ImplementedAt(implementedVersion = JVersion.v1_9)
    @StateExplanation(explanation = "Represents the age of this block! (like Chorus Flower)")
    public static final IntegerBlockState AGE_TO_5;

    @StateId(id = "age")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents the age of this block! (like carrots and potatoes)")
    public static final IntegerBlockState AGE_TO_7;

    @StateId(id = "age")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents the age of this block! (like cactus and sugar cane)")
    public static final IntegerBlockState AGE_TO_15;

    @StateId(id = "age")
    @ImplementedAt(implementedVersion = JVersion.v1_13)
    @StateExplanation(explanation = "Represents the age of this block! (like kelp)")
    public static final IntegerBlockState AGE_TO_25;

    @StateId(id = "bites")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents the amount of bites a cake has, starting at 0 until 6 where there's only 1 slice left.")
    public static final IntegerBlockState CAKE_BITES;

    @StateId(id = "color")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents the color the block.")
    public static final EnumBlockState<EnumColor> COLOR;

    @StateId(id = "level")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents the level of the liquid in the cauldron.")
    public static final IntegerBlockState CAULDRON_LEVEL;

    @StateId(id = "up")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Depending on the block this has different objectives.")
    public static final BooleanBlockState UP;

    @StateId(id = "down")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Depending on the block this has different objectives.")
    public static final BooleanBlockState DOWN;

    @StateId(id = "north")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Depending on the block this has different objectives.")
    public static final BooleanBlockState NORTH;

    @StateId(id = "east")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Depending on the block this has different objectives.")
    public static final BooleanBlockState EAST;

    @StateId(id = "south")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Depending on the block this has different objectives.")
    public static final BooleanBlockState SOUTH;

    @StateId(id = "west")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Depending on the block this has different objectives.")
    public static final BooleanBlockState WEST;

    @StateId(id = "triggered")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents if this block is activated!")
    public static final BooleanBlockState TRIGGERED;

    @StateId(id = "snowy")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents when a grass block has a snow layer on top!")
    public static final BooleanBlockState SNOWY;

    @StateId(id = "open")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents if a door OR trapdoor is open.")
    public static final BooleanBlockState OPEN;

    @StateId(id = "hinge")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents where the hinge of the door is located.")
    public static final EnumBlockState<EnumDoorHinge> DOOR_HINGE;

    @StateId(id = "half")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents the half of the door.")
    public static final EnumBlockState<EnumDoorHalf> HALF;

    @StateId(id = "seamless")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents if the Slab has a line in the middle of it's a double slab. (true means there's no line)")
    public static final BooleanBlockState SEAMLESS;

    @StateId(id = "eye")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents if an Ender Frame has an eye or not.")
    public static final BooleanBlockState HAS_EYE;

    @StateId(id = "in_wall")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "If a Fence is attached to a wall, it spawns a little bit lower.")
    public static final BooleanBlockState FENCE_GATE_ATTACHED_TO_WALL;

    @StateId(id = "level")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents how the fluid \"works\"")
    public static final IntegerBlockState FLUID_LEVEL;

    @StateId(id = "enabled")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents if the hopper is enabled.")
    public static final BooleanBlockState HOPPER_ENABLED;

    @StateId(id = "has_record")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents if a jukebox has a disk inside of it!")
    public static final BooleanBlockState JUKEBOX_HAS_RECORD;

    @StateId(id = "persistent")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents if a leaf is persistent")
    @StateChanged(changeExplanation = "This State was changed during the flatten update from the original \"decayable\" state name. Make sure to invert the value given on an older version!")
    public static final BooleanBlockState LEAVES_PERSISTENT;

    @StateId(id = "distance")
    @ImplementedAt(implementedVersion = JVersion.v1_13)
    @StateExplanation(explanation = "Represents the distance of this leave to the log")
    public static final IntegerBlockState LEAVES_DISTANCE;

    @StateId(id = "extended")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents when the piston is extended.")
    public static final BooleanBlockState EXTENDED;

    @StateId(id = "short")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents if the head of the piston is short (2-6 pixels less on the rod)")
    public static final BooleanBlockState SHORT_PISTON;

    @StateId(id = "shape")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents how the rail is positioned.")
    public static final EnumBlockState<EnumTrackPosition> TRACK_POSITION;

    @StateId(id = "mode")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents the mode of a comparator.")
    public static final EnumBlockState<EnumComparatorMode> COMPARATOR_MODE;

    @StateId(id = "conditional")
    @ImplementedAt(implementedVersion = JVersion.v1_9)
    @StateExplanation(explanation = "Represents if the command block has the conditional tag.")
    public static final BooleanBlockState CONDITIONAL_COMMAND_BLOCK;

    @StateId(id = "north")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents where the redstone wire will connect on the pole.")
    public static final EnumBlockState<EnumRedstoneWireConnection> REDSTONE_WIRE_CONNECTION_NORTH;

    @StateId(id = "east")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents where the redstone wire will connect on the pole.")
    public static final EnumBlockState<EnumRedstoneWireConnection> REDSTONE_WIRE_CONNECTION_EAST;

    @StateId(id = "south")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents where the redstone wire will connect on the pole.")
    public static final EnumBlockState<EnumRedstoneWireConnection> REDSTONE_WIRE_CONNECTION_SOUTH;

    @StateId(id = "west")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents where the redstone wire will connect on the pole.")
    public static final EnumBlockState<EnumRedstoneWireConnection> REDSTONE_WIRE_CONNECTION_WEST;

    @StateId(id = "locked")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents if the repeater is locked.")
    public static final BooleanBlockState REPEATER_LOCKED;

    @StateId(id = "delay")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents the delay of a repeater.")
    public static final IntegerBlockState REPEATER_DELAY;

    @StateId(id = "stage")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents the stage of a sapling")
    public static final IntegerBlockState SAPLING_STAGE;

    @StateId(id = "nodrop")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents if this skull will NOT be dropped on break.")
    public static final BooleanBlockState SKULL_NO_DROP;

    @StateId(id = "layers")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents how many snow layers there are on a snow layer block.")
    public static final IntegerBlockState SNOW_LAYERS;

    @StateId(id = "moisture")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents if the farm land has/is moisture/wet or not.")
    public static final IntegerBlockState FARM_LAND_MOISTURE;

    @StateId(id = "wet")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents if a sponge is wet.")
    public static final BooleanBlockState SPONGE_WET;

    @StateId(id = "half")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents the half of the stairs.")
    public static final EnumBlockState<EnumStairsHalf> STAIRS_HALF;

    @StateId(id = "shape")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents how the stairs are connected.")
    public static final EnumBlockState<EnumStairsShape> STAIRS_SHAPE;

    @StateId(id = "half")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents which half of a tall plant this block is.")
    public static final EnumBlockState<EnumTallPlantHalf> TALL_PLANT_HALF;

    @StateId(id = "half")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents where a trapdoor is attached to, this means, the lower or upper part.")
    public static final EnumBlockState<EnumTrapdoorHalf> TRAPDOOR_ATTACHED_HALF;

    @StateId(id = "suspended")
    @ImplementedAt(implementedVersion = JVersion.v1_8, removedVersion = JVersion.v1_9)
    @StateExplanation(explanation = "Represents if this block is suspended, this is, above a block that is not solid.")
    public static final BooleanBlockState SUSPENDED;

    @StateId(id = "attached")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents if this block is attached. (like string and tripwire hooks)")
    public static final BooleanBlockState ATTACHED;

    @StateId(id = "disarmed")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents if this tripwire was disarmed using a shears.")
    public static final BooleanBlockState TRIPWIRE_DISARMED;

    @StateId(id = "has_book")
    @ImplementedAt(implementedVersion = JVersion.v1_14)
    @StateExplanation(explanation = "Represents if this lectern has a book in it.")
    public static final BooleanBlockState LECTERN_HAS_BOOK;

    @StateId(id = "charges")
    @ImplementedAt(implementedVersion = JVersion.v1_16)
    @StateExplanation(explanation = "Represents how many charges a respawn anchor has.")
    public static final IntegerBlockState RESPAWN_ANCHOR_CHARGES;

    @StateId(id = "instrument")
    @ImplementedAt(implementedVersion = JVersion.v1_13)
    @StateExplanation(explanation = "Represents the instrument of a Note block.")
    @StateChanged(changeExplanation = "The Note Block's instrument is stored in the BlockEntity in legacy versions (1.8-1.12), which is not accessible using BlockStates.")
    public static final EnumBlockState<EnumInstrument> NOTE_BLOCK_INSTRUMENT;

    @StateId(id = "note")
    @ImplementedAt(implementedVersion = JVersion.v1_13)
    @StateExplanation(explanation = "Represents the note of the Note block.")
    @StateChanged(changeExplanation = "The Note Block's note is stored in the BlockEntity in legacy versions (1.8-1.12), which is not accessible using BlockStates.")
    public static final IntegerBlockState NOTE_BLOCK_NOTE;

    @StateId(id = "berries")
    @ImplementedAt(implementedVersion = JVersion.v1_17)
    @StateExplanation(explanation = "Represents if cave vines have berries that can be collected.")
    public static final BooleanBlockState CAVE_VINE_HAS_BERRIES;

    @StateId(id = "waterlogged")
    @ImplementedAt(implementedVersion = JVersion.v1_13)
    @StateExplanation(explanation = "Represents if this block can be water logged.")
    public static final BooleanBlockState WATERLOGGED;

    @StateId(id = "unstable")
    @ImplementedAt(implementedVersion = JVersion.v1_8)
    @StateExplanation(explanation = "Represents if the TNT should ignite when broken.")
    @StateChanged(changeExplanation = "This BlockState had it's name changed after the Flattening update from the original \"explode\"")
    public static final BooleanBlockState TNT_UNSTABLE;

    @StateId(id = "signal_fire")
    @ImplementedAt(implementedVersion = JVersion.v1_14)
    @StateExplanation(explanation = "Represents if the campfire should produce more smoke, this could also be done by adding a hay bale below the campfire.")
    public static final BooleanBlockState CAMPFIRE_SIGNAL_FIRE;

    @StateId(id = "lit")
    @ImplementedAt(implementedVersion = JVersion.v1_13)
    @StateExplanation(explanation = "Represents if a furnace, torch, etc. is lit.")
    public static final BooleanBlockState IS_LIT;

    @StateId(id = "inverted")
    @ImplementedAt(implementedVersion = JVersion.v1_13)
    @StateExplanation(explanation = "Represents if the daylight detector has it's mode inverted.")
    @StateChanged(changeExplanation = "The Daylight Detector's mode is stored in the BlockEntity in legacy versions (1.8-1.12), which is not accessible using BlockStates.")
    public static final BooleanBlockState DAYLIGHT_DETECTOR_INVERTED;

    @StateId(id = "honey_level")
    @ImplementedAt(implementedVersion = JVersion.v1_15)
    @StateExplanation(explanation = "Represents the level of honey of a beehive.")
    public static final IntegerBlockState HONEY_LEVEL;

    @StateId(id = "tilt")
    @ImplementedAt(implementedVersion = JVersion.v1_17)
    @StateExplanation(explanation = "Represents how much tilt a dripleaf has.")
    public static final EnumBlockState<EnumTilt> DRIPLEAF_TILT;

    @StateId(id = "sculk_sensor_phase")
    @ImplementedAt(implementedVersion = JVersion.v1_17)
    @StateExplanation(explanation = "Represents the current state of a sculk sensor.")
    public static final EnumBlockState<EnumSculkSensorPhase> SCULK_SENSOR_PHASE;

    @StateId(id = "level")
    @ImplementedAt(implementedVersion = JVersion.v1_14)
    @StateExplanation(explanation = "Represents the level of the composter.")
    public static final IntegerBlockState COMPOSTER_LEVEL;

    static {
        FACING = EnumBlockState.createInstance("facing", EnumDirection.class, EnumDirection.DOWN);
        ROTATION = IntegerBlockState.createInstance("rotation", 0, 15);
        AXIS = EnumBlockState.createInstance("axis", EnumAxis.class, EnumAxis.X);

        PART = EnumBlockState.createInstance("part", EnumBedPart.class, EnumBedPart.FOOT);
        OCCUPIED = BooleanBlockState.createInstance("occupied", false);
        ANVIL_DAMAGE = IntegerBlockState.createInstance("damage", 0, 2);

        BREWING_STAND_HAS_BOTTLE_0 = BooleanBlockState.createInstance("has_bottle_0", false);
        BREWING_STAND_HAS_BOTTLE_1 = BooleanBlockState.createInstance("has_bottle_1", false);
        BREWING_STAND_HAS_BOTTLE_2 = BooleanBlockState.createInstance("has_bottle_2", false);

        POWERED_BY_REDSTONE = BooleanBlockState.createInstance("powered", false);
        POWER = IntegerBlockState.createInstance("power", 0, 15);

        AGE_TO_1 = IntegerBlockState.createInstance("age", 0, 1);
        AGE_TO_2 = IntegerBlockState.createInstance("age", 0, 2);
        AGE_TO_3 = IntegerBlockState.createInstance("age", 0, 3);
        AGE_TO_5 = IntegerBlockState.createInstance("age", 0, 5);
        AGE_TO_7 = IntegerBlockState.createInstance("age", 0, 7);
        AGE_TO_15 = IntegerBlockState.createInstance("age", 0, 15);
        AGE_TO_25 = IntegerBlockState.createInstance("age", 0, 25);

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

        HOPPER_ENABLED = BooleanBlockState.createInstance("enabled", true);

        JUKEBOX_HAS_RECORD = BooleanBlockState.createInstance("has_record", false);

        LEAVES_PERSISTENT = BooleanBlockState.createInstance((JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13) ? "persistent" : "decayable"), (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_12)));
        LEAVES_DISTANCE = IntegerBlockState.createInstance("distance", 1, 7);

        EXTENDED = BooleanBlockState.createInstance("extended", false);
        SHORT_PISTON = BooleanBlockState.createInstance("short", false);

        TRACK_POSITION = EnumBlockState.createInstance("shape", EnumTrackPosition.class, EnumTrackPosition.ASCENDING_EAST);

        COMPARATOR_MODE = EnumBlockState.createInstance("mode", EnumComparatorMode.class, EnumComparatorMode.COMPARE);

        CONDITIONAL_COMMAND_BLOCK = BooleanBlockState.createInstance("conditional", false);

        REDSTONE_WIRE_CONNECTION_NORTH = EnumBlockState.createInstance("north", EnumRedstoneWireConnection.class, EnumRedstoneWireConnection.NONE);
        REDSTONE_WIRE_CONNECTION_EAST = EnumBlockState.createInstance("east", EnumRedstoneWireConnection.class, EnumRedstoneWireConnection.NONE);
        REDSTONE_WIRE_CONNECTION_SOUTH = EnumBlockState.createInstance("south", EnumRedstoneWireConnection.class, EnumRedstoneWireConnection.NONE);
        REDSTONE_WIRE_CONNECTION_WEST = EnumBlockState.createInstance("west", EnumRedstoneWireConnection.class, EnumRedstoneWireConnection.NONE);

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

        SUSPENDED = BooleanBlockState.createInstance("suspended", false);
        ATTACHED = BooleanBlockState.createInstance("attached", false);
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
        return JFieldReflector.getObjectsFromFields(StateList.class, null, true)
                .stream()
                .filter(obj -> obj instanceof StateType) //checker for the specific type
                .map(obj -> ((StateType<?>) obj)) //caster
                .filter(state -> state.getStateId().equalsIgnoreCase(typeName)) //final checker to check names
                .findFirst().orElse(null); //FINISHHHH!
    }

    private StateList() {}

    /**
     * Represents the ID of this state in NMS.
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface StateId {
        String id();
    }

    /**
     * The target block of this state.
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TargetBlock {
        JMaterial.MaterialEnum[] targets();
    }

    /**
     * The explanation of what this state does.
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface StateExplanation {
        String explanation();
    }

    /**
     * Specifies that a state has been changed!
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface StateChanged {
        String changeExplanation();
    }

    /**
     * Specifies when a specific state was implemented.
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ImplementedAt {
        JVersion implementedVersion();

        JVersion removedVersion() default JVersion.UNKNOWN;
    }

}
