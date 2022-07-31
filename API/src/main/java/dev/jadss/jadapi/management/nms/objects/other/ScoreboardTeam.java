package dev.jadss.jadapi.management.nms.objects.other;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.enums.EnumChatFormat;
import dev.jadss.jadapi.management.nms.enums.EnumNameTagVisibility;
import dev.jadss.jadapi.management.nms.enums.EnumTeamPush;
import dev.jadss.jadapi.management.nms.enums.NMSEnum;
import dev.jadss.jadapi.management.nms.interfaces.NMSBuildable;
import dev.jadss.jadapi.management.nms.interfaces.NMSCopyable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.management.nms.interfaces.NMSParsable;
import dev.jadss.jadapi.management.nms.objects.chat.IChatBaseComponent;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JConstructorReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;
import org.bukkit.ChatColor;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class ScoreboardTeam implements NMSObject, NMSBuildable, NMSParsable, NMSCopyable {

    public static final Class<?> scoreboardTeamClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.scores" : "server." + NMS.getNMSVersion()) + ".ScoreboardTeam");
    public static final Class<?> scoreboardClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.scores" : "server." + NMS.getNMSVersion()) + ".Scoreboard");
    /*Todo: Maybe create a Scoreboard Parser?*/

    //Scoreboard
    private ObjectPackage scoreboardPack = new ObjectPackage();

    //Name
    private String name = "JadAPI";

    //List
    private Set<String> playerNameSet = new HashSet<>();

    //Meta
    private IChatBaseComponent displayName = new IChatBaseComponent("", false, "");
    private IChatBaseComponent prefix = new IChatBaseComponent("", false, "");
    private IChatBaseComponent suffix = new IChatBaseComponent("", false, "");

    private EnumChatFormat color = EnumChatFormat.getEnumChatFormat(ChatColor.WHITE);

    //Flags
    private boolean friendlyFire = true;
    private boolean seeFriendlyInvisibles = true;

    //Options
    private EnumNameTagVisibility nameTagVisibility = EnumNameTagVisibility.ALWAYS;
    private EnumNameTagVisibility deathMessageVisibility = EnumNameTagVisibility.ALWAYS;

    private EnumTeamPush teamPush = EnumTeamPush.ALWAYS;

    public ScoreboardTeam() {
    }

    public ScoreboardTeam(@Nullable ObjectPackage scoreboardPack, String name, Set<String> playerNameSet, IChatBaseComponent displayName, IChatBaseComponent prefix, IChatBaseComponent suffix, EnumChatFormat color, boolean friendlyFire, boolean seeFriendlyInvisibles, EnumNameTagVisibility nameTagVisibility, EnumNameTagVisibility deathMessageVisibility, EnumTeamPush teamPush) {
        this.scoreboardPack = scoreboardPack;
        this.name = name;
        this.playerNameSet = playerNameSet;
        this.displayName = displayName;
        this.prefix = prefix;
        this.suffix = suffix;
        this.color = color;
        this.friendlyFire = friendlyFire;
        this.seeFriendlyInvisibles = seeFriendlyInvisibles;
        this.nameTagVisibility = nameTagVisibility;
        this.deathMessageVisibility = deathMessageVisibility;
        this.teamPush = teamPush;
    }

    public ObjectPackage getScoreboardPack() {
        return scoreboardPack;
    }

    public void setScoreboardPack(ObjectPackage scoreboardPack) {
        this.scoreboardPack = scoreboardPack;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getPlayerNameSet() {
        return playerNameSet;
    }

    public void setPlayerNameSet(Set<String> playerNameSet) {
        this.playerNameSet = playerNameSet;
    }

    public IChatBaseComponent getDisplayName() {
        return displayName;
    }

    public void setDisplayName(IChatBaseComponent displayName) {
        this.displayName = displayName;
    }

    public IChatBaseComponent getPrefix() {
        return prefix;
    }

    public void setPrefix(IChatBaseComponent prefix) {
        this.prefix = prefix;
    }

    public IChatBaseComponent getSuffix() {
        return suffix;
    }

    public void setSuffix(IChatBaseComponent suffix) {
        this.suffix = suffix;
    }

    public EnumChatFormat getColor() {
        return color;
    }

    public void setColor(EnumChatFormat color) {
        this.color = color;
    }

    public boolean isFriendlyFire() {
        return friendlyFire;
    }

    public void setFriendlyFire(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
    }

    public boolean isSeeFriendlyInvisibles() {
        return seeFriendlyInvisibles;
    }

    public void setSeeFriendlyInvisibles(boolean seeFriendlyInvisibles) {
        this.seeFriendlyInvisibles = seeFriendlyInvisibles;
    }

    public EnumNameTagVisibility getNameTagVisibility() {
        return nameTagVisibility;
    }

    public void setNameTagVisibility(EnumNameTagVisibility nameTagVisibility) {
        this.nameTagVisibility = nameTagVisibility;
    }

    public EnumNameTagVisibility getDeathMessageVisibility() {
        return deathMessageVisibility;
    }

    public void setDeathMessageVisibility(EnumNameTagVisibility deathMessageVisibility) {
        this.deathMessageVisibility = deathMessageVisibility;
    }

    public EnumTeamPush getTeamPush() {
        return teamPush;
    }

    public void setTeamPush(EnumTeamPush teamPush) {
        this.teamPush = teamPush;
    }

    public void parseFlags(int flags) {
        if (flags == 3) {
            this.friendlyFire = true;
            this.seeFriendlyInvisibles = true;
        } else if (flags == 2) {
            this.friendlyFire = false;
            this.seeFriendlyInvisibles = true;
        } else if (flags == 1) {
            this.friendlyFire = true;
            this.seeFriendlyInvisibles = false;
        } else if (flags == 0) {
            this.friendlyFire = false;
            this.seeFriendlyInvisibles = false;
        }
    }

    @Override
    public Object build() {
        Object object = JConstructorReflector.executeConstructor(scoreboardTeamClass, new Class[]{scoreboardClass, String.class}, (scoreboardPack == null ? null : this.scoreboardPack.getObject()), this.name);

        //Global (same places for each version)
        JFieldReflector.setObjectToUnspecificField(scoreboardTeamClass, boolean.class, (i) -> 0, object, friendlyFire);
        JFieldReflector.setObjectToUnspecificField(scoreboardTeamClass, boolean.class, (i) -> 1, object, seeFriendlyInvisibles);

        //No casting, or Mr. Exception will pop here.
        Set set = JFieldReflector.getObjectFromUnspecificField(scoreboardTeamClass, Set.class, object);
        set.clear();
        set.addAll(playerNameSet);

        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13)) {
            JFieldReflector.setObjectToUnspecificField(scoreboardTeamClass, IChatBaseComponent.iChatBaseComponentClass, (i) -> 0, object, displayName.build());
            JFieldReflector.setObjectToUnspecificField(scoreboardTeamClass, IChatBaseComponent.iChatBaseComponentClass, (i) -> 1, object, prefix.build());
            JFieldReflector.setObjectToUnspecificField(scoreboardTeamClass, IChatBaseComponent.iChatBaseComponentClass, (i) -> 2, object, suffix.build());
        } else {
            JFieldReflector.setObjectToUnspecificField(scoreboardTeamClass, String.class, (i) -> 0, object, displayName.getMessage());
            JFieldReflector.setObjectToUnspecificField(scoreboardTeamClass, String.class, (i) -> 1, object, prefix.getMessage());
            JFieldReflector.setObjectToUnspecificField(scoreboardTeamClass, String.class, (i) -> 2, object, suffix.getMessage());
        }

        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_8)) {
            JFieldReflector.setObjectToUnspecificField(scoreboardTeamClass, EnumNameTagVisibility.enumNameTagVisibilityClass, (i) -> 0, object, nameTagVisibility.getNMSObject());
            JFieldReflector.setObjectToUnspecificField(scoreboardTeamClass, EnumNameTagVisibility.enumNameTagVisibilityClass, (i) -> 1, object, deathMessageVisibility.getNMSObject());
            JFieldReflector.setObjectToUnspecificField(scoreboardTeamClass, EnumChatFormat.enumChatFormat, (i) -> 0, object, color.getNMSObject());
        }

        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_9)) {
            JFieldReflector.setObjectToUnspecificField(scoreboardTeamClass, EnumTeamPush.enumTeamPushClass, (i) -> 0, object, teamPush.getNMSObject());
        }

        return object;
    }

    @Override
    public void parse(Object object) {
        if (object == null) return;
        if (!canParse(object)) throw new NMSException("Cannot parse this object.");

        //Global (same places for each version)
        this.name = JFieldReflector.getObjectFromUnspecificField(scoreboardTeamClass, String.class, object);

        this.friendlyFire = JFieldReflector.getObjectFromUnspecificField(scoreboardTeamClass, boolean.class, (i) -> 0, object);
        this.seeFriendlyInvisibles = JFieldReflector.getObjectFromUnspecificField(scoreboardTeamClass, boolean.class, (i) -> 1, object);

        //No casting, or Mr. Exception will pop here.
        Set set = JFieldReflector.getObjectFromUnspecificField(scoreboardTeamClass, Set.class, object);
        this.playerNameSet.addAll(set);

        this.displayName = new IChatBaseComponent();
        this.prefix = new IChatBaseComponent();
        this.suffix = new IChatBaseComponent();

        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13)) {
            this.displayName.parse(JFieldReflector.getObjectFromUnspecificField(scoreboardTeamClass, IChatBaseComponent.iChatBaseComponentClass, (i) -> 0, object));
            this.prefix.parse(JFieldReflector.getObjectFromUnspecificField(scoreboardTeamClass, IChatBaseComponent.iChatBaseComponentClass, (i) -> 1, object));
            this.suffix.parse(JFieldReflector.getObjectFromUnspecificField(scoreboardTeamClass, IChatBaseComponent.iChatBaseComponentClass, (i) -> 2, object));
        } else {
            String displayName = JFieldReflector.getObjectFromUnspecificField(scoreboardTeamClass, String.class, (i) -> 0, object);
            String prefix = JFieldReflector.getObjectFromUnspecificField(scoreboardTeamClass, String.class, (i) -> 1, object);
            String suffix = JFieldReflector.getObjectFromUnspecificField(scoreboardTeamClass, String.class, (i) -> 2, object);

            this.displayName.setMessage(displayName);
            this.displayName.setJsonMessage(IChatBaseComponent.defaultSimpleFormat.replace("%text%", displayName));

            this.prefix.setMessage(prefix);
            this.prefix.setJsonMessage(IChatBaseComponent.defaultSimpleFormat.replace("%text%", prefix));

            this.suffix.setMessage(suffix);
            this.suffix.setJsonMessage(IChatBaseComponent.defaultSimpleFormat.replace("%text%", suffix));
        }

        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_8)) {
            this.nameTagVisibility = NMSEnum.getEnum(EnumNameTagVisibility.class, JFieldReflector.getObjectFromUnspecificField(scoreboardTeamClass, EnumNameTagVisibility.enumNameTagVisibilityClass, object));
            this.deathMessageVisibility = NMSEnum.getEnum(EnumNameTagVisibility.class, JFieldReflector.getObjectFromUnspecificField(scoreboardTeamClass, EnumNameTagVisibility.enumNameTagVisibilityClass, (i) -> 1, object));

            this.color = NMSEnum.getEnum(EnumChatFormat.class, JFieldReflector.getObjectFromUnspecificField(scoreboardTeamClass, EnumChatFormat.enumChatFormat, object));
        }

        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_9)) {
            this.teamPush = NMSEnum.getEnum(EnumTeamPush.class, JFieldReflector.getObjectFromUnspecificField(scoreboardTeamClass, EnumTeamPush.enumTeamPushClass, object));
        }
    }


    @Override
    public Class<?> getParsingClass() {
        return scoreboardTeamClass;
    }

    @Override
    public boolean canParse(Object object) {
        return scoreboardTeamClass.equals(object.getClass());
    }

    @Override
    public NMSObject copy() {
        return new ScoreboardTeam(this.scoreboardPack, this.name, new HashSet<String>(this.playerNameSet), (IChatBaseComponent) this.displayName.copy(), (IChatBaseComponent) this.prefix.copy(), (IChatBaseComponent) this.suffix.copy(), this.color, this.friendlyFire, this.seeFriendlyInvisibles, this.nameTagVisibility, this.deathMessageVisibility, this.teamPush);
    }

    @Override
    public String toString() {
        return "ScoreboardTeam{" +
                "scoreboardPack=" + scoreboardPack +
                ", name='" + name + '\'' +
                ", playerNameSet=" + playerNameSet +
                ", displayName=" + displayName +
                ", prefix=" + prefix +
                ", suffix=" + suffix +
                ", color=" + color +
                ", friendlyFire=" + friendlyFire +
                ", seeFriendlyInvisibles=" + seeFriendlyInvisibles +
                ", nameTagVisibility=" + nameTagVisibility +
                ", deathMessageVisibility=" + deathMessageVisibility +
                ", teamPush=" + teamPush +
                '}';
    }

    public int getFlags() {
        int flagValue = 0;
        if (this.friendlyFire) {
            flagValue |= 1;
        }

        if (this.seeFriendlyInvisibles) {
            flagValue |= 2;
        }

        return flagValue;
    }
}
