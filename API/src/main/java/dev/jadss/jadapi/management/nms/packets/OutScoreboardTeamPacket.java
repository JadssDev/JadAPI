package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.enums.EnumChatFormat;
import dev.jadss.jadapi.management.nms.enums.EnumNameTagVisibility;
import dev.jadss.jadapi.management.nms.enums.EnumTeamPush;
import dev.jadss.jadapi.management.nms.enums.NMSEnum;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.interfaces.NMSBuildable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.management.nms.interfaces.NMSParsable;
import dev.jadss.jadapi.management.nms.objects.chat.IChatBaseComponent;
import dev.jadss.jadapi.management.nms.objects.other.ScoreboardTeam;
import dev.jadss.jadapi.utils.JReflection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

public class OutScoreboardTeamPacket extends DefinedPacket {

    private ScoreboardTeam scoreboardTeam;
    private TeamAction action;

    public static final Class<?> scoreboardTeamPacketClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.protocol.game" : "server." + JReflection.getNMSVersion()) + ".PacketPlayOutScoreboardTeam");

    public OutScoreboardTeamPacket() {
    }

    public OutScoreboardTeamPacket(ScoreboardTeam scoreboardTeam, TeamAction action) {
        this.scoreboardTeam = (ScoreboardTeam) scoreboardTeam.copy();
        this.action = action;
    }

    @Override
    public Object build() {
        Object packet;

        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_16)) {
            packet = JReflection.executeConstructor(scoreboardTeamPacketClass, new Class[]{});

            //Name
            JReflection.setUnspecificField(scoreboardTeamPacketClass, String.class, 0, packet, scoreboardTeam.getName());

            //Name Set ((No cast or Mr. Exception))
            Collection collection = JReflection.getUnspecificFieldObject(scoreboardTeamPacketClass, Collection.class, 0, packet);
            collection.clear();
            collection.addAll(scoreboardTeam.getPlayerNameSet());

            //Display name, prefix and suffix.
            if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_12)) {
                //Strings
                JReflection.setUnspecificField(scoreboardTeamPacketClass, String.class, 1, packet, scoreboardTeam.getDisplayName().getMessage());
                JReflection.setUnspecificField(scoreboardTeamPacketClass, String.class, 2, packet, scoreboardTeam.getPrefix().getMessage());
                JReflection.setUnspecificField(scoreboardTeamPacketClass, String.class, 3, packet, scoreboardTeam.getSuffix().getMessage());
            } else {
                //IChatBaseComponents
                JReflection.setUnspecificField(scoreboardTeamPacketClass, IChatBaseComponent.iChatBaseComponentClass, 0, packet, scoreboardTeam.getDisplayName().build());
                JReflection.setUnspecificField(scoreboardTeamPacketClass, IChatBaseComponent.iChatBaseComponentClass, 1, packet, scoreboardTeam.getPrefix().build());
                JReflection.setUnspecificField(scoreboardTeamPacketClass, IChatBaseComponent.iChatBaseComponentClass, 2, packet, scoreboardTeam.getSuffix().build());
            }

            //NameTagVisibility && EnumChatFormat
            if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_8)) {
                boolean isNewer = JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13);
                JReflection.setUnspecificField(scoreboardTeamPacketClass, String.class, (isNewer ? 1 : 4), packet, scoreboardTeam.getNameTagVisibility().getNetworkId());
                JReflection.setUnspecificField(scoreboardTeamPacketClass, (isNewer ? EnumChatFormat.enumChatFormat : int.class), 0, packet, (isNewer ? scoreboardTeam.getColor().getNMSObject() : scoreboardTeam.getColor().getNetworkInt()));
            }

            //CollisionRule
            if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_9)) {
                JReflection.setUnspecificField(scoreboardTeamPacketClass, String.class, (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13) ? 2 : 5), packet, scoreboardTeam.getTeamPush().getNetworkId());
            }

            //Action
            JReflection.setUnspecificField(scoreboardTeamPacketClass, int.class, (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7) || JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13) ? 0 : 1), packet, action.getByte());

            //Flags (Friendly fire and see friendly invisibles)
            JReflection.setUnspecificField(scoreboardTeamPacketClass, int.class, Integer.MAX_VALUE, packet, scoreboardTeam.getFlags());
        } else { //>1.17
            ScoreboardMetaClass scoreboardMetaClass = new ScoreboardMetaClass(scoreboardTeam);

            packet = JReflection.executeConstructor(scoreboardTeamPacketClass, new Class[]{String.class, int.class, Optional.class, Collection.class}, scoreboardTeam.getName(), action.getByte(), (action.doAttachMeta() ? scoreboardMetaClass.build() : false), new ArrayList<>(scoreboardTeam.getPlayerNameSet()));
        }

        return packet;
    }

    @Override
    public void parse(Object packet) {
        if (packet == null)
            return;
        if (!canParse(packet))
            throw new NMSException("The packet specified is not parsable by this class.");

        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_16)) {
            //Name
            this.scoreboardTeam.setName(JReflection.getUnspecificFieldObject(scoreboardTeamPacketClass, String.class, 0, packet));

            //Name Set ((No cast or Mr. Exception))
            Collection collection = JReflection.getUnspecificFieldObject(scoreboardTeamPacketClass, Collection.class, 0, packet);
            HashSet<String> list = new HashSet<>(collection);
            this.scoreboardTeam.setPlayerNameSet(list);

            //Display name, prefix and suffix.
            if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_12)) {
                //Strings
                this.scoreboardTeam.setDisplayName(new IChatBaseComponent(JReflection.getUnspecificFieldObject(scoreboardTeamPacketClass, String.class, 1, packet), false, ""));
                this.scoreboardTeam.setPrefix(new IChatBaseComponent(JReflection.getUnspecificFieldObject(scoreboardTeamPacketClass, String.class, 2, packet), false, ""));
                this.scoreboardTeam.setSuffix(new IChatBaseComponent(JReflection.getUnspecificFieldObject(scoreboardTeamPacketClass, String.class, 3, packet), false, ""));
            } else {
                //IChatBaseComponents
                this.scoreboardTeam.setDisplayName(new IChatBaseComponent());
                this.scoreboardTeam.getDisplayName().parse(JReflection.getUnspecificFieldObject(scoreboardTeamPacketClass, IChatBaseComponent.iChatBaseComponentClass, 0, packet));

                this.scoreboardTeam.setPrefix(new IChatBaseComponent());
                this.scoreboardTeam.getPrefix().parse(JReflection.getUnspecificFieldObject(scoreboardTeamPacketClass, IChatBaseComponent.iChatBaseComponentClass, 1, packet));

                this.scoreboardTeam.setSuffix(new IChatBaseComponent());
                this.scoreboardTeam.getSuffix().parse(JReflection.getUnspecificFieldObject(scoreboardTeamPacketClass, IChatBaseComponent.iChatBaseComponentClass, 2, packet));
            }

            //NameTagVisibility && EnumChatFormat
            if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_8)) {
                boolean isNewer = JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13);
                this.scoreboardTeam.setNameTagVisibility(EnumNameTagVisibility.getByNetworkId(JReflection.getUnspecificFieldObject(scoreboardTeamPacketClass, String.class, (isNewer ? 1 : 4), packet)));

                if (isNewer)
                    this.scoreboardTeam.setColor(NMSEnum.getEnum(EnumChatFormat.class, (Enum<?>) JReflection.getUnspecificFieldObject(scoreboardTeamPacketClass, EnumChatFormat.enumChatFormat, 0, packet)));
                else
                    this.scoreboardTeam.setColor(EnumChatFormat.getByNetworkInt(JReflection.getUnspecificFieldObject(scoreboardTeamPacketClass, int.class, 0, packet)));
            }

            //CollisionRule
            if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_9)) {
                this.scoreboardTeam.setTeamPush(EnumTeamPush.getByNetworkId(JReflection.getUnspecificFieldObject(scoreboardTeamPacketClass, String.class, (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13) ? 2 : 5), packet)));
            }

            //Action
            this.action = TeamAction.getByByte(JReflection.getUnspecificFieldObject(scoreboardTeamPacketClass, int.class, (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7) || JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13) ? 0 : 1), packet));

            //Flags (Friendly fire and see friendly invisibles)
            this.scoreboardTeam.parseFlags(JReflection.getUnspecificFieldObject(scoreboardTeamPacketClass, int.class, Integer.MAX_VALUE, packet));
        } else { //>1.17
            this.action = TeamAction.getByByte(JReflection.getUnspecificFieldObject(scoreboardTeamPacketClass, int.class, Integer.MAX_VALUE, packet));

            Optional<?> option = JReflection.getUnspecificFieldObject(scoreboardTeamPacketClass, Optional.class, packet);
            if (option != null && option.isPresent()) {
                ScoreboardMetaClass scoreboardInfoParser = new ScoreboardMetaClass();
                scoreboardInfoParser.parse(option.get());
                this.scoreboardTeam = scoreboardInfoParser.team;
            } else {
                this.scoreboardTeam = new ScoreboardTeam();
            }

            this.scoreboardTeam.setName(JReflection.getUnspecificFieldObject(scoreboardTeamPacketClass, String.class, packet));

            this.scoreboardTeam.setPlayerNameSet(new HashSet<>(JReflection.getUnspecificFieldObject(scoreboardTeamPacketClass, Collection.class, packet)));
        }
    }

    @Override
    public Class<?> getParsingClass() {
        return scoreboardTeamPacketClass;
    }

    @Override
    public boolean canParse(Object object) {
        return scoreboardTeamPacketClass.equals(object.getClass());
    }

    @Override
    public DefinedPacket copy() {
        return new OutScoreboardTeamPacket(this.scoreboardTeam, this.action);
    }


    public enum TeamAction {
        TEAM_ADD((byte) 0, true),
        TEAM_REMOVE((byte) 1, false),
        TEAM_CHANGED((byte) 2, true),
        PLAYER_ADDED_TO_TEAM((byte) 3, false),
        PLAYER_REMOVED_FROM_TEAM((byte) 4, false);

        private final byte b;
        private final boolean attachMeta;

        TeamAction(byte b, boolean attachMeta) {
            this.b = b;
            this.attachMeta = attachMeta;
        }

        public byte getByte() {
            return b;
        }

        public boolean doAttachMeta() {
            return attachMeta;
        }

        public static TeamAction getByByte(int b) {
            for (TeamAction action : TeamAction.values())
                if (action.getByte() == b) return action;
            return null;
        }
    }

    /**
     * The class under the PacketPlayOutScoreboardTeam in newer versions.
     */
    public static class ScoreboardMetaClass implements NMSObject, NMSParsable, NMSBuildable {

        private ScoreboardTeam team;

        public static final Class<?> scoreboardTeamInfoPacketClass = JReflection.getReflectionClass("net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam$b");

        public ScoreboardMetaClass() {
        }

        public ScoreboardMetaClass(ScoreboardTeam team) {
            this.team = team;
        }

        public ScoreboardTeam getTeam() {
            return team;
        }

        public void setTeam(ScoreboardTeam team) {
            this.team = team;
        }

        @Override
        public void parse(Object object) {
            if (object == null)
                return;
            if (!canParse(object))
                throw new NMSException("The packet specified is not parsable by this class.");

            team = new ScoreboardTeam();

            team.setDisplayName(new IChatBaseComponent());
            team.getDisplayName().parse(JReflection.getUnspecificFieldObject(scoreboardTeamInfoPacketClass, IChatBaseComponent.iChatBaseComponentClass, 0, object));

            team.setPrefix(new IChatBaseComponent());
            team.getPrefix().parse(JReflection.getUnspecificFieldObject(scoreboardTeamInfoPacketClass, IChatBaseComponent.iChatBaseComponentClass, 1, object));

            team.setSuffix(new IChatBaseComponent());
            team.getSuffix().parse(JReflection.getUnspecificFieldObject(scoreboardTeamInfoPacketClass, IChatBaseComponent.iChatBaseComponentClass, 2, object));

            team.setColor(NMSEnum.getEnum(EnumChatFormat.class, (Enum<?>) JReflection.getUnspecificFieldObject(scoreboardTeamInfoPacketClass, EnumChatFormat.enumChatFormat, object)));

            team.parseFlags(JReflection.getUnspecificFieldObject(scoreboardTeamInfoPacketClass, int.class, object));

            team.setNameTagVisibility(EnumNameTagVisibility.getByNetworkId(JReflection.getUnspecificFieldObject(scoreboardTeamInfoPacketClass, String.class, 0, object)));
            team.setDeathMessageVisibility(EnumNameTagVisibility.ALWAYS);

            team.setTeamPush(EnumTeamPush.getByNetworkId(JReflection.getUnspecificFieldObject(scoreboardTeamInfoPacketClass, String.class, 1, object)));
        }

        @Override
        public Object build() {
            return JReflection.executeConstructor(scoreboardTeamInfoPacketClass, new Class[]{ScoreboardTeam.scoreboardTeamClass}, team.build());
        }

        @Override
        public Class<?> getParsingClass() {
            return scoreboardTeamInfoPacketClass;
        }

        @Override
        public boolean canParse(Object object) {
            return scoreboardTeamInfoPacketClass.equals(object.getClass());
        }
    }
}
