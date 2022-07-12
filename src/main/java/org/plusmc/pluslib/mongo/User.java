package org.plusmc.pluslib.mongo;

import org.jetbrains.annotations.Nullable;
import org.mongodb.morphia.annotations.*;
import org.plusmc.pluslib.reflect.bungeespigot.player.IPlayer;
import org.plusmc.pluslib.util.StringFormatter;

import java.util.TimeZone;
import java.util.UUID;

@Entity(value = "users", noClassnameStored = true)
public class User {

    @Id
    private String uuid;
    @Indexed
    private String name;
    @Indexed
    private long discordId;
    @Indexed
    private String discordName;

    private String timeZone = "";

    private long browniePoints;
    private long totalPoints;
    private long totalSpent;

    private long pointsForNextLevel;
    private int level;

    private long firstLogin;
    private long lastLogin;

    private String banReason = null;
    private long banTime = 0;

    @Transient
    private IPlayer player;

    @Embedded
    private UserSR userSR;

    public User() {
    }

    public User(String uuid, String name, boolean newUser) {
        this.name = name;
        this.uuid = uuid;
        if (newUser) {
            this.firstLogin = System.currentTimeMillis();
            this.lastLogin = System.currentTimeMillis();
            this.pointsForNextLevel = 100;
            this.level = 1;
            this.browniePoints = 0;
            this.totalPoints = 0;
            this.totalSpent = 0;
        }
    }

    public void setDiscord(long id, String name) {
        this.discordId = id;
        this.discordName = name;
    }

    public void addPoints(long points) {
        this.browniePoints += points;
        this.totalPoints += points;
        int oldLevel = this.level;
        while (this.pointsForNextLevel <= this.totalPoints) {
            this.pointsForNextLevel += this.pointsForNextLevel * 0.75;
            this.level++;
        }
        if (oldLevel < this.level) {
            String message = "§6§lLevel Up! " + oldLevel + " -> " + this.level;
            if (this.getPlayer() != null) {
                this.getPlayer().sendMessage(message);
            }
        }
    }

    @Nullable
    public IPlayer getPlayer() {
        try {
            if (this.player == null)
                this.player = IPlayer.getPlayer(UUID.fromString(this.uuid));
        } catch (IllegalArgumentException e) {
            return null;
        }
        return this.player;
    }

    public TimeZone getTimeZone() {
        return TimeZone.getTimeZone(this.timeZone);
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone.getID();
    }

    public void addPoints(long points, String reason) {
        this.browniePoints += points;
        this.totalPoints += points;
        int oldLevel = this.level;
        while (this.pointsForNextLevel <= this.totalPoints) {
            this.pointsForNextLevel += this.pointsForNextLevel * 0.75;
            this.level++;
        }
        String pointMessage = reason + "§6§l +" + points + " Brownie Points!";
        if (this.getPlayer() != null)
            this.getPlayer().sendMessage(pointMessage);

        if (oldLevel < this.level) {
            String levelUpMessage = "§6§lLevel Up! " + oldLevel + " -> " + this.level;
            if (this.getPlayer() != null) {
                this.getPlayer().sendMessage(levelUpMessage);
                this.getPlayer().playSound("entity.player.levelup", 1.0F, 2.0F);
            }
        }
    }

    public boolean spendPoints(long points) {
        if (this.browniePoints >= points) {
            this.browniePoints -= points;
            this.totalSpent += points;
            return true;
        }
        return false;
    }

    public long getPoints() {
        return this.browniePoints;
    }

    public long getTotalSpent() {
        return this.totalSpent;
    }

    public int getLevel() {
        return this.level;
    }

    public void ban(String reason, long time) {
        this.banReason = reason;
        this.banTime = time + System.currentTimeMillis();
        String reasonMessage = reason.isBlank() ? "" : ", Reason: " + reason;
        String message = "§cYou've been banned for " + StringFormatter.formatTime(time) + reasonMessage;
        if (this.getPlayer() != null)
            this.getPlayer().kickPlayer(message);
    }

    public void unban() {
        this.banReason = null;
        this.banTime = 0;
    }

    public UserSR getUserMH() {
        if (this.userSR == null)
            this.userSR = new UserSR();

        return this.userSR;
    }

    public boolean isBanned() {
        return this.banTime > System.currentTimeMillis();
    }

    public String getBanReason() {
        return this.banReason;
    }

    public long getBanTime() {
        return this.banTime;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUUID() {
        return UUID.fromString(this.uuid);
    }

    public long getDiscordId() {
        return this.discordId;
    }

    public String getDiscordName() {
        return this.discordName;
    }


    public long getFirstLogin() {
        return this.firstLogin;
    }

    public long getLastLogin() {
        return this.lastLogin;
    }

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }


}
