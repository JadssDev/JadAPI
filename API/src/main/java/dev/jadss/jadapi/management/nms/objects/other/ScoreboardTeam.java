package dev.jadss.jadapi.management.nms.objects.other;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
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
import dev.jadss.jadapi.utils.JReflection;
import org.bukkit.ChatColor;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class ScoreboardTeam implements NMSObject, NMSBuildable, NMSParsable, NMSCopyable {

    public static final Class<?> scoreboardTeamClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.scores" : "server." + JReflection.getNMSVersion()) + ".ScoreboardTeam");
    public static final Class<?> scoreboardClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.scores" : "server." + JReflection.getNMSVersion()) + ".Scoreboard");
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
        Object object = JReflection.executeConstructor(scoreboardTeamClass, new Class[]{scoreboardClass, String.class}, (scoreboardPack == null ? null : this.scoreboardPack.getObject()), this.name);

        //Global (same places for each version)
        JReflection.setUnspecificField(scoreboardTeamClass, boolean.class, 0, object, friendlyFire);
        JReflection.setUnspecificField(scoreboardTeamClass, boolean.class, 1, object, seeFriendlyInvisibles);

        //No casting, or Mr. Exception will pop here.
        Set set = JReflection.getUnspecificFieldObject(scoreboardTeamClass, Set.class, object);
        set.clear();
        set.addAll(playerNameSet);

        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13)) {
            JReflection.setUnspecificField(scoreboardTeamClass, IChatBaseComponent.iChatBaseComponentClass, 0, object, displayName.build());
            JReflection.setUnspecificField(scoreboardTeamClass, IChatBaseComponent.iChatBaseComponentClass, 1, object, prefix.build());
            JReflection.setUnspecificField(scoreboardTeamClass, IChatBaseComponent.iChatBaseComponentClass, 2, object, suffix.build());
        } else {
            JReflection.setUnspecificField(scoreboardTeamClass, String.class, 0, object, displayName.getMessage());
            JReflection.setUnspecificField(scoreboardTeamClass, String.class, 1, object, prefix.getMessage());
            JReflection.setUnspecificField(scoreboardTeamClass, String.class, 2, object, suffix.getMessage());
        }

        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_8)) {
            JReflection.setUnspecificField(scoreboardTeamClass, EnumNameTagVisibility.enumNameTagVisibilityClass, 0, object, nameTagVisibility.getNMSObject());
            JReflection.setUnspecificField(scoreboardTeamClass, EnumNameTagVisibility.enumNameTagVisibilityClass, 1, object, deathMessageVisibility.getNMSObject());
            JReflection.setUnspecificField(scoreboardTeamClass, EnumChatFormat.enumChatFormat, 0, object, color.getNMSObject());
        }

        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_9)) {
            JReflection.setUnspecificField(scoreboardTeamClass, EnumTeamPush.enumTeamPushClass, 0, object, teamPush.getNMSObject());
        }

        return object;
    }

    @Override
    public void parse(Object object) {
        if (object == null) return;
        if (!canParse(object)) throw new NMSException("Cannot parse this object.");

        //Global (same places for each version)
        this.name = JReflection.getUnspecificFieldObject(scoreboardTeamClass, String.class, 0, object);

        this.friendlyFire = JReflection.getUnspecificFieldObject(scoreboardTeamClass, boolean.class, 0, object);
        this.seeFriendlyInvisibles = JReflection.getUnspecificFieldObject(scoreboardTeamClass, boolean.class, 1, object);

        //No casting, or Mr. Exception will pop here.
        Set set = JReflection.getUnspecificFieldObject(scoreboardTeamClass, Set.class, object);
        this.playerNameSet.addAll(set);

        this.displayName = new IChatBaseComponent();
        this.prefix = new IChatBaseComponent();
        this.suffix = new IChatBaseComponent();

        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13)) {
            this.displayName.parse(JReflection.getUnspecificFieldObject(scoreboardTeamClass, IChatBaseComponent.iChatBaseComponentClass, 0, object));
            this.prefix.parse(JReflection.getUnspecificFieldObject(scoreboardTeamClass, IChatBaseComponent.iChatBaseComponentClass, 1, object));
            this.suffix.parse(JReflection.getUnspecificFieldObject(scoreboardTeamClass, IChatBaseComponent.iChatBaseComponentClass, 2, object));
        } else {
            String displayName = JReflection.getUnspecificFieldObject(scoreboardTeamClass, String.class, 0, object);
            String prefix = JReflection.getUnspecificFieldObject(scoreboardTeamClass, String.class, 1, object);
            String suffix = JReflection.getUnspecificFieldObject(scoreboardTeamClass, String.class, 2, object);

            this.displayName.setMessage(displayName);
            this.displayName.setJsonMessage(IChatBaseComponent.defaultSimpleFormat.replace("%text%", displayName));

            this.prefix.setMessage(prefix);
            this.prefix.setJsonMessage(IChatBaseComponent.defaultSimpleFormat.replace("%text%", prefix));

            this.suffix.setMessage(suffix);
            this.suffix.setJsonMessage(IChatBaseComponent.defaultSimpleFormat.replace("%text%", suffix));
        }

        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_8)) {
            this.nameTagVisibility = NMSEnum.getEnum(EnumNameTagVisibility.class, (Enum<?>) JReflection.getUnspecificFieldObject(scoreboardTeamClass, EnumNameTagVisibility.enumNameTagVisibilityClass, 0, object));
            this.deathMessageVisibility = NMSEnum.getEnum(EnumNameTagVisibility.class, (Enum<?>) JReflection.getUnspecificFieldObject(scoreboardTeamClass, EnumNameTagVisibility.enumNameTagVisibilityClass, 1, object));

            this.color = NMSEnum.getEnum(EnumChatFormat.class, (Enum<?>) JReflection.getUnspecificFieldObject(scoreboardTeamClass, EnumChatFormat.enumChatFormat, 0, object));
        }

        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_9)) {
            this.teamPush = NMSEnum.getEnum(EnumTeamPush.class, (Enum<?>) JReflection.getUnspecificFieldObject(scoreboardTeamClass, EnumTeamPush.enumTeamPushClass, 0, object));
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
