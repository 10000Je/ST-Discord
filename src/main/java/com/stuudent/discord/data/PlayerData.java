package com.stuudent.discord.data;

import com.stuudent.discord.DiscordAPI;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.entity.Player;

public class PlayerData {

    public Player targetPlayer;
    public AllData allData;

    public PlayerData(Player targetPlayer) {
        this.targetPlayer = targetPlayer;
        this.allData = DiscordAPI.getData();
    }

    public Player getPlayer() {
        return this.targetPlayer;
    }

    public User getSyncedUser() {
        return this.allData.getSyncedUser(this.targetPlayer);
    }

    public User getSyncingUser() {
        return this.allData.getSyncingUser(this.targetPlayer);
    }

    public boolean isAlreadySynced() {
        return this.allData.isAlreadySynced(this.targetPlayer);
    }

    public boolean isAlreadySyncing() {
        return this.allData.isAlreadySyncing(this.targetPlayer);
    }

}
