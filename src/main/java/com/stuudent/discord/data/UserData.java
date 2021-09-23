package com.stuudent.discord.data;

import com.stuudent.discord.DiscordAPI;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.entity.Player;

public class UserData {

    public User user;
    public AllData allData;

    public UserData(User user) {
        this.user = user;
        allData = DiscordAPI.getData();
    }

    public User getUser() {
        return this.user;
    }

    public void setSync(boolean state) {
        this.allData.setUserSync(this.user, state);
    }

    public void setSyncedPlayer(Player targetPlayer) {
        this.allData.setUserSyncedPlayer(this.user, targetPlayer);
    }

    public Player getSyncedPlayer() {
        return this.allData.getUserSyncedPlayer(this.user);
    }

    public boolean isSynced() {
        return this.allData.isUserSynced(this.user);
    }

    public void setSyncing(Player syncPlayer) {
        this.allData.setUserSyncing(this.user, syncPlayer);
    }

    public void resetSyncing() {
        this.allData.resetSyncing(this.user);
    }

    public boolean isSyncing() {
        return this.allData.isUserSyncing(this.user);
    }

    public Player getSyncingPlayer() {
        return this.allData.getUserSyncingPlayer(this.user);
    }

    public String getAuthCode() {
        return this.allData.getUserAuthCode(this.user);
    }

    public void addSyncAttempt() {
        this.allData.addSyncAttempt(this.user);
    }

    public boolean isSyncAttemptOver() {
        return this.allData.isSyncAttemptOver(this.user);
    }

    public int getSyncAttempt() {
        return this.allData.getSyncAttempt(this.user);
    }

    public void setSyncCoolTime() {
        this.allData.setSyncCoolTime(this.user);
    }

    public long getSyncCoolTime() {
        return this.allData.getSyncCoolTime(this.user);
    }

    public boolean isSyncCool() {
        return this.allData.isSyncCool(this.user);
    }

}
