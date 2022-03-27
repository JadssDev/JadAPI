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
            JReflection.setFieldObject(scoreboardTeamPacketClass, String.class, packet, scoreboardTeam.getName(), (i) -> 0);

            //Name Set ((No cast or Mr. Exception))
            Collection collection = JReflection.getFieldObject(scoreboardTeamPacketClass, Collection.class, packet);
            collection.clear();
            collection.addAll(scoreboardTeam.getPlayerNameSet());

            //Display name, prefix and suffix.
            if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_12)) {
                //Strings
                JReflection.setFieldObject(scoreboardTeamPacketClass, String.class, packet, scoreboardTeam.getDisplayName().getMessage(), (i) -> 1);
                JReflection.setFieldObject(scoreboardTeamPacketClass, String.class, packet, scoreboardTeam.getPrefix().getMessage(), (i) -> 2);
                JReflection.setFieldObject(scoreboardTeamPacketClass, String.class, packet, scoreboardTeam.getSuffix().getMessage(), (i) -> 3);
            } else {
                //IChatBaseComponents
                JReflection.setFieldObject(scoreboardTeamPacketClass, IChatBaseComponent.iChatBaseComponentClass, packet, scoreboardTeam.getDisplayName().build(), (i) -> 0);
                JReflection.setFieldObject(scoreboardTeamPacketClass, IChatBaseComponent.iChatBaseComponentClass, packet, scoreboardTeam.getPrefix().build(), (i) -> 1);
                JReflection.setFieldObject(scoreboardTeamPacketClass, IChatBaseComponent.iChatBaseComponentClass, packet, scoreboardTeam.getSuffix().build(), (i) -> 2);
            }

            //NameTagVisibility && EnumChatFormat
            if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_8)) {
                boolean isNewer = JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13);
                JReflection.setFieldObject(scoreboardTeamPacketClass, String.class, packet, scoreboardTeam.getNameTagVisibility().getNetworkId(), (i) -> isNewer ? 1 : 4);
                JReflection.setFieldObject(scoreboardTeamPacketClass, (isNewer ? EnumChatFormat.enumChatFormat : int.class), packet, (isNewer ? scoreboardTeam.getColor().getNMSObject() : scoreboardTeam.getColor().getNetworkInt()), (i) -> 0);
            }

            //CollisionRule
            if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_9)) {
                JReflection.setFieldObject(scoreboardTeamPacketClass, String.class, packet, scoreboardTeam.getTeamPush().getNetworkId(), (i) -> JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13) ? 2 : 5);
            }

            //Action
            JReflection.setFieldObject(scoreboardTeamPacketClass, int.class, packet, action.getByte(), (i) -> JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7) || JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13) ? 0 : 1);

            //Flags (Friendly fire and see friendly invisibles)
            JReflection.setFieldObject(scoreboardTeamPacketClass, int.class, packet, scoreboardTeam.getFlags(), (i) -> i);
        } else { //>1.17
            ScoreboardMetaClass scoreboardMetaClass = new ScoreboardMetaClass(scoreboardTeam);

            packet = JReflection.executeConstructor(scoreboardTeamPacketClass, new Class[]{String.class, int.class, Optional.class, Collection.class}, scoreboardTeam.getName(), action.getByte(), (action.doAttachMeta() ? Optional.of(scoreboardMetaClass.build()) : Optional.empty()), new ArrayList<>(scoreboardTeam.getPlayerNameSet()));
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
            this.scoreboardTeam.setName(JReflection.getFieldObject(scoreboardTeamPacketClass, String.class, packet));

            //Name Set ((No cast or Mr. Exception))
            Collection collection = JReflection.getFieldObject(scoreboardTeamPacketClass, Collection.class, packet);
            HashSet<String> list = new HashSet<>(collection);
            this.scoreboardTeam.setPlayerNameSet(list);

            //Display name, prefix and suffix.
            if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_12)) {
                //Strings
                this.scoreboardTeam.setDisplayName(new IChatBaseComponent(JReflection.getFieldObject(scoreboardTeamPacketClass, String.class, packet, (i) -> 1), false, ""));
                this.scoreboardTeam.setPrefix(new IChatBaseComponent(JReflection.getFieldObject(scoreboardTeamPacketClass, String.class, packet, (i) -> 2), false, ""));
                this.scoreboardTeam.setSuffix(new IChatBaseComponent(JReflection.getFieldObject(scoreboardTeamPacketClass, String.class, packet, (i) -> 3), false, ""));
            } else {
                //IChatBaseComponents
                this.scoreboardTeam.setDisplayName(new IChatBaseComponent());
                this.scoreboardTeam.getDisplayName().parse(JReflection.getFieldObject(scoreboardTeamPacketClass, IChatBaseComponent.iChatBaseComponentClass, packet));

                this.scoreboardTeam.setPrefix(new IChatBaseComponent());
                this.scoreboardTeam.getPrefix().parse(JReflection.getFieldObject(scoreboardTeamPacketClass, IChatBaseComponent.iChatBaseComponentClass, packet, (i) -> 1));

                this.scoreboardTeam.setSuffix(new IChatBaseComponent());
                this.scoreboardTeam.getSuffix().parse(JReflection.getFieldObject(scoreboardTeamPacketClass, IChatBaseComponent.iChatBaseComponentClass, packet, (i) -> 2));
            }

            //NameTagVisibility && EnumChatFormat
            if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_8)) {
                boolean isNewer = JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13);
                this.scoreboardTeam.setNameTagVisibility(EnumNameTagVisibility.getByNetworkId(JReflection.getFieldObject(scoreboardTeamPacketClass, String.class, packet, (i) -> isNewer ? 1 : 4)));

                if (isNewer)
                    this.scoreboardTeam.setColor(NMSEnum.getEnum(EnumChatFormat.class, JReflection.getFieldObject(scoreboardTeamPacketClass, EnumChatFormat.enumChatFormat, packet)));
                else
                    this.scoreboardTeam.setColor(EnumChatFormat.getByNetworkInt(JReflection.getFieldObject(scoreboardTeamPacketClass, int.class, packet, (i) -> 0)));
            }

            //CollisionRule
            if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_9)) {
                this.scoreboardTeam.setTeamPush(EnumTeamPush.getByNetworkId(JReflection.getFieldObject(scoreboardTeamPacketClass, String.class, packet, (i) -> JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13) ? 2 : 5)));
            }

            //Action
            this.action = TeamAction.getByByte(JReflection.getFieldObject(scoreboardTeamPacketClass, int.class, packet, (i) -> JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7) || JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13) ? 0 : 1));

            //Flags (Friendly fire and see friendly invisibles)
            this.scoreboardTeam.parseFlags(JReflection.getFieldObject(scoreboardTeamPacketClass, int.class, packet, (i) -> i));
        } else { //>1.17
            this.action = TeamAction.getByByte(JReflection.getFieldObject(scoreboardTeamPacketClass, int.class, packet, (i) -> i));

            Optional<?> option = JReflection.getFieldObject(scoreboardTeamPacketClass, Optional.class, packet);
            if (option != null && option.isPresent()) {
                ScoreboardMetaClass scoreboardInfoParser = new ScoreboardMetaClass();
                scoreboardInfoParser.parse(option.get());
                this.scoreboardTeam = scoreboardInfoParser.team;
            } else {
                this.scoreboardTeam = new ScoreboardTeam();
            }

            this.scoreboardTeam.setName(JReflection.getFieldObject(scoreboardTeamPacketClass, String.class, packet));

            this.scoreboardTeam.setPlayerNameSet(new HashSet<>(JReflection.getFieldObject(scoreboardTeamPacketClass, Collection.class, packet)));
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
            team.getDisplayName().parse(JReflection.getFieldObject(scoreboardTeamInfoPacketClass, IChatBaseComponent.iChatBaseComponentClass, object, (i) -> 0));

            team.setPrefix(new IChatBaseComponent());
            team.getPrefix().parse(JReflection.getFieldObject(scoreboardTeamInfoPacketClass, IChatBaseComponent.iChatBaseComponentClass, object, (i) -> 1));

            team.setSuffix(new IChatBaseComponent());
            team.getSuffix().parse(JReflection.getFieldObject(scoreboardTeamInfoPacketClass, IChatBaseComponent.iChatBaseComponentClass, object, (i) -> 2));

            team.setColor(NMSEnum.getEnum(EnumChatFormat.class, JReflection.getFieldObject(scoreboardTeamInfoPacketClass, EnumChatFormat.enumChatFormat, object)));

            team.parseFlags(JReflection.getFieldObject(scoreboardTeamInfoPacketClass, int.class, object));

            team.setNameTagVisibility(EnumNameTagVisibility.getByNetworkId(JReflection.getFieldObject(scoreboardTeamInfoPacketClass, String.class, object, (i) -> 0)));
            team.setDeathMessageVisibility(EnumNameTagVisibility.ALWAYS);

            team.setTeamPush(EnumTeamPush.getByNetworkId(JReflection.getFieldObject(scoreboardTeamInfoPacketClass, String.class, object, (i) -> 1)));
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
