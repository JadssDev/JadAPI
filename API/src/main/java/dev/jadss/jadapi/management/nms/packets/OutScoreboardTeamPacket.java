package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
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
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JConstructorReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

public class OutScoreboardTeamPacket extends DefinedPacket {

    private ScoreboardTeam scoreboardTeam;
    private TeamAction action;

    public static final Class<?> scoreboardTeamPacketClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.protocol.game" : "server." + NMS.getNMSVersion()) + ".PacketPlayOutScoreboardTeam");

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
            packet = JConstructorReflector.executeConstructor(scoreboardTeamPacketClass, new Class[]{});

            //Name
            JFieldReflector.setObjectToUnspecificField(scoreboardTeamPacketClass, String.class, packet, scoreboardTeam.getName());

            //Name Set ((No cast or Mr. Exception))
            Collection collection = JFieldReflector.getObjectFromUnspecificField(scoreboardTeamPacketClass, Collection.class, packet);
            collection.clear();
            collection.addAll(scoreboardTeam.getPlayerNameSet());

            //Display name, prefix and suffix.
            if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_12)) {
                //Strings
                JFieldReflector.setObjectToUnspecificField(scoreboardTeamPacketClass, String.class, (i) -> 1, packet, scoreboardTeam.getDisplayName().getMessage());
                JFieldReflector.setObjectToUnspecificField(scoreboardTeamPacketClass, String.class, (i) -> 2, packet, scoreboardTeam.getPrefix().getMessage());
                JFieldReflector.setObjectToUnspecificField(scoreboardTeamPacketClass, String.class, (i) -> 3, packet, scoreboardTeam.getSuffix().getMessage());
            } else {
                //IChatBaseComponents
                JFieldReflector.setObjectToUnspecificField(scoreboardTeamPacketClass, IChatBaseComponent.iChatBaseComponentClass, (i) -> 0, packet, scoreboardTeam.getDisplayName().build());
                JFieldReflector.setObjectToUnspecificField(scoreboardTeamPacketClass, IChatBaseComponent.iChatBaseComponentClass, (i) -> 1, packet, scoreboardTeam.getPrefix().build());
                JFieldReflector.setObjectToUnspecificField(scoreboardTeamPacketClass, IChatBaseComponent.iChatBaseComponentClass, (i) -> 2, packet, scoreboardTeam.getSuffix().build());
            }

            //NameTagVisibility && EnumChatFormat
            if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_8)) {
                boolean isNewer = JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13);
                JFieldReflector.setObjectToUnspecificField(scoreboardTeamPacketClass, String.class, (i) -> isNewer ? 1 : 4, packet, scoreboardTeam.getNameTagVisibility().getNetworkId());
                JFieldReflector.setObjectToUnspecificField(scoreboardTeamPacketClass, (isNewer ? EnumChatFormat.enumChatFormat : int.class), (i) -> 0, packet, (isNewer ? scoreboardTeam.getColor().getNMSObject() : scoreboardTeam.getColor().getNetworkInt()));
            }

            //CollisionRule
            if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_9)) {
                JFieldReflector.setObjectToUnspecificField(scoreboardTeamPacketClass, String.class, (i) -> i, packet, scoreboardTeam.getTeamPush().getNetworkId());
            }

            //Action
            JFieldReflector.setObjectToUnspecificField(scoreboardTeamPacketClass, int.class, (i) -> i - 1, packet, action.getByte());

            //Flags (Friendly fire and see friendly invisibles)
            JFieldReflector.setObjectToUnspecificField(scoreboardTeamPacketClass, int.class, (i) -> i, packet, scoreboardTeam.getFlags());
        } else { //>1.17
            ScoreboardMetaClass scoreboardMetaClass = new ScoreboardMetaClass(scoreboardTeam);

            packet = JConstructorReflector.executeConstructor(scoreboardTeamPacketClass, new Class[]{String.class, int.class, Optional.class, Collection.class}, scoreboardTeam.getName(), action.getByte(), (action.doAttachMeta() ? Optional.of(scoreboardMetaClass.build()) : Optional.empty()), new ArrayList<>(scoreboardTeam.getPlayerNameSet()));
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
            this.scoreboardTeam = new ScoreboardTeam();

            //Name
            this.scoreboardTeam.setName(JFieldReflector.getObjectFromUnspecificField(scoreboardTeamPacketClass, String.class, packet));

            //Name Set ((No cast or Mr. Exception))
            Collection collection = JFieldReflector.getObjectFromUnspecificField(scoreboardTeamPacketClass, Collection.class, packet);
            HashSet<String> list = new HashSet<>(collection);
            this.scoreboardTeam.setPlayerNameSet(list);

            //Display name, prefix and suffix.
            if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_12)) {
                //Strings
                this.scoreboardTeam.setDisplayName(new IChatBaseComponent(JFieldReflector.getObjectFromUnspecificField(scoreboardTeamPacketClass, String.class, (i) -> 1, packet), false, ""));
                this.scoreboardTeam.setPrefix(new IChatBaseComponent(JFieldReflector.getObjectFromUnspecificField(scoreboardTeamPacketClass, String.class, (i) -> 2, packet), false, ""));
                this.scoreboardTeam.setSuffix(new IChatBaseComponent(JFieldReflector.getObjectFromUnspecificField(scoreboardTeamPacketClass, String.class, (i) -> 3, packet), false, ""));
            } else {
                //IChatBaseComponents
                this.scoreboardTeam.setDisplayName(new IChatBaseComponent());
                this.scoreboardTeam.getDisplayName().parse(JFieldReflector.getObjectFromUnspecificField(scoreboardTeamPacketClass, IChatBaseComponent.iChatBaseComponentClass, (i) -> 0, packet));

                this.scoreboardTeam.setPrefix(new IChatBaseComponent());
                this.scoreboardTeam.getPrefix().parse(JFieldReflector.getObjectFromUnspecificField(scoreboardTeamPacketClass, IChatBaseComponent.iChatBaseComponentClass, (i) -> 1, packet));

                this.scoreboardTeam.setSuffix(new IChatBaseComponent());
                this.scoreboardTeam.getSuffix().parse(JFieldReflector.getObjectFromUnspecificField(scoreboardTeamPacketClass, IChatBaseComponent.iChatBaseComponentClass, (i) -> 2, packet));
            }

            //NameTagVisibility && EnumChatFormat
            if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_8)) {
                boolean isNewer = JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13);
                this.scoreboardTeam.setNameTagVisibility(EnumNameTagVisibility.getByNetworkId(JFieldReflector.getObjectFromUnspecificField(scoreboardTeamPacketClass, String.class, (i) -> isNewer ? 1 : 4, packet)));

                if (isNewer)
                    this.scoreboardTeam.setColor(NMSEnum.getEnum(EnumChatFormat.class, JFieldReflector.getObjectFromUnspecificField(scoreboardTeamPacketClass, EnumChatFormat.enumChatFormat, packet)));
                else
                    this.scoreboardTeam.setColor(EnumChatFormat.getByNetworkInt(JFieldReflector.getObjectFromUnspecificField(scoreboardTeamPacketClass, int.class, packet)));
            }

            //CollisionRule
            if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_9)) {
                this.scoreboardTeam.setTeamPush(EnumTeamPush.getByNetworkId(JFieldReflector.getObjectFromUnspecificField(scoreboardTeamPacketClass, String.class, (i) -> i, packet)));
            }

            //Action
            this.action = TeamAction.getByByte(JFieldReflector.getObjectFromUnspecificField(scoreboardTeamPacketClass, int.class, (i) -> i - 1, packet));

            //Flags (Friendly fire and see friendly invisibles)
            this.scoreboardTeam.parseFlags(JFieldReflector.getObjectFromUnspecificField(scoreboardTeamPacketClass, int.class, (i) -> i, packet));
        } else { //>1.17
            this.action = TeamAction.getByByte(JFieldReflector.getObjectFromUnspecificField(scoreboardTeamPacketClass, int.class, (i) -> i, packet));

            Optional<?> option = JFieldReflector.getObjectFromUnspecificField(scoreboardTeamPacketClass, Optional.class, packet);
            if (option != null && option.isPresent()) {
                ScoreboardMetaClass scoreboardInfoParser = new ScoreboardMetaClass();
                scoreboardInfoParser.parse(option.get());
                this.scoreboardTeam = scoreboardInfoParser.team;
            } else {
                this.scoreboardTeam = new ScoreboardTeam();
            }

            this.scoreboardTeam.setName(JFieldReflector.getObjectFromUnspecificField(scoreboardTeamPacketClass, String.class, packet));

            this.scoreboardTeam.setPlayerNameSet(new HashSet<>(JFieldReflector.getObjectFromUnspecificField(scoreboardTeamPacketClass, Collection.class, packet)));
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

    @Override
    public String toString() {
        return "OutScoreboardTeamPacket{" +
                "scoreboardTeam=" + scoreboardTeam +
                ", action=" + action +
                '}';
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

        public static final Class<?> scoreboardTeamInfoPacketClass = JClassReflector.getClass("net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam$b");

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
            team.getDisplayName().parse(JFieldReflector.getObjectFromUnspecificField(scoreboardTeamInfoPacketClass, IChatBaseComponent.iChatBaseComponentClass, (i) -> i - 2, object));

            team.setPrefix(new IChatBaseComponent());
            team.getPrefix().parse(JFieldReflector.getObjectFromUnspecificField(scoreboardTeamInfoPacketClass, IChatBaseComponent.iChatBaseComponentClass, (i) -> i - 1, object));

            team.setSuffix(new IChatBaseComponent());
            team.getSuffix().parse(JFieldReflector.getObjectFromUnspecificField(scoreboardTeamInfoPacketClass, IChatBaseComponent.iChatBaseComponentClass, (i) -> i, object));

            team.setColor(NMSEnum.getEnum(EnumChatFormat.class, JFieldReflector.getObjectFromUnspecificField(scoreboardTeamInfoPacketClass, EnumChatFormat.enumChatFormat, object)));

            team.parseFlags(JFieldReflector.getObjectFromUnspecificField(scoreboardTeamInfoPacketClass, int.class, object));

            team.setNameTagVisibility(EnumNameTagVisibility.getByNetworkId(JFieldReflector.getObjectFromUnspecificField(scoreboardTeamInfoPacketClass, String.class, (i) -> i - 1, object)));
            team.setDeathMessageVisibility(EnumNameTagVisibility.ALWAYS); //useless.

            team.setTeamPush(EnumTeamPush.getByNetworkId(JFieldReflector.getObjectFromUnspecificField(scoreboardTeamInfoPacketClass, String.class, (i) -> i, object)));
        }

        @Override
        public Object build() {
            return JConstructorReflector.executeConstructor(scoreboardTeamInfoPacketClass, new Class[]{ScoreboardTeam.scoreboardTeamClass}, team.build());
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
