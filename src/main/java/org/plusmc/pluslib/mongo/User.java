package org.plusmc.pluslib.mongo;

import org.mongodb.morphia.annotations.*;
import org.plusmc.pluslib.mongo.util.MinecraftUtil;
import org.plusmc.pluslib.mongo.util.OtherUtil;

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

    private double browniePoints;
    private double totalBrowniePoints;
    private double totalSpent;

    private double pointsForNextLevel;
    private int level;

    private long firstLogin;
    private long lastLogin;

    private boolean isBanned = false;
    private String banReason = null;
    private long banTime = 0;

    @Embedded
    private UserMH userMH;

    public User() {
    }

    public Object getPlayer() {
        return MinecraftUtil.getPlayer(uuid);
    }

    public User(String uuid, String name, boolean newUser) {
        this.name = name;
        this.uuid = uuid;
        if(newUser) {
            this.firstLogin = System.currentTimeMillis();
            this.lastLogin = System.currentTimeMillis();
            this.pointsForNextLevel = 100;
            this.level = 1;
            this.browniePoints = 0;
            this.totalBrowniePoints = 0;
            this.totalSpent = 0;
        }
    }

    public void setDiscord(long id, String name) {
        this.discordId = id;
        this.discordName = name;
    }

    public void addPoints(double points) {
        this.browniePoints += points;
        this.totalBrowniePoints += points;
        int oldLevel = this.level;
        while (this.pointsForNextLevel <= this.totalBrowniePoints) {
            this.pointsForNextLevel += this.pointsForNextLevel * 0.75;
            this.level++;
        }
        if(oldLevel < this.level) {
            String message = "§6§lLevel Up! " + oldLevel + " -> " + this.level;
            MinecraftUtil.sendMessage(this.getPlayer(), message);
        }
    }

    public void addPoints(double points, String reason) {
        this.browniePoints += points;
        this.totalBrowniePoints += points;
        int oldLevel = this.level;
        while (this.pointsForNextLevel <= this.totalBrowniePoints) {
            this.pointsForNextLevel += this.pointsForNextLevel * 0.75;
            this.level++;
        }
        String pointMessage = reason + "§6§l " + points + " Brownie Points!";
        MinecraftUtil.sendMessage(this.getPlayer(), pointMessage);
        if(oldLevel < this.level) {
            String levelUpMessage = "§6§lLevel Up! " + oldLevel + " -> " + this.level;
            MinecraftUtil.sendMessage(this.getPlayer(), levelUpMessage);
        }
    }

    public boolean spendPoints(double points) {
        if (this.browniePoints >= points) {
            this.browniePoints -= points;
            this.totalSpent += points;
            return true;
        }
        return false;
    }

    public double getPoints() {
        return this.browniePoints;
    }

    public double getTotalSpent() {
        return this.totalSpent;
    }

    public int getLevel() {
        return this.level;
    }

    public void ban(String reason, long time) {
        this.isBanned = true;
        this.banReason = reason;
        this.banTime = time;
        String message = "§cYou have been banned for " + OtherUtil.formatTime(time) + ", reason: " + reason;
        MinecraftUtil.kickPlayer(this.getPlayer(), message);
    }

    public void unban() {
        this.isBanned = false;
        this.banReason = null;
        this.banTime = 0;
    }

    public UserMH getUserMH() {
        if(this.userMH == null)
            this.userMH = new UserMH();

        return this.userMH;
    }

    public boolean isBanned() {
        return this.isBanned;
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

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }

    public long getLastLogin() {
        return this.lastLogin;
    }




}
