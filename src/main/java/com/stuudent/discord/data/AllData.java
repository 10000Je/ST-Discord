package com.stuudent.discord.data;

import com.stuudent.discord.DiscordAPI;
import com.stuudent.discord.DiscordCore;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class AllData {

    public static File configFile;
    public static File tempFile;
    public static YamlConfiguration cf;
    public static YamlConfiguration tempCf;

    static {
        configFile = new File("plugins/" + DiscordCore.instance.getName() + "/BTData.yml");
        tempFile = new File("plugins/" + DiscordCore.instance.getName() + "/tempData.yml");
        cf = YamlConfiguration.loadConfiguration(configFile);
        tempCf = YamlConfiguration.loadConfiguration(tempFile);
    }

    public void save() {
        try {
            cf.save(configFile);
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage("§6ST§f-§9BOT §ev" + DiscordCore.instance.getDescription().getVersion() + " §c데이터를 저장하지 못했습니다.");
        }
    }
    
    // BTUser 전용 메소드

    public void setUserSync(User user, boolean state) {
        cf.set(user.getId() + ".SYNC", state);
    }

    public void setUserSyncedPlayer(User user, Player targetPlayer) {
        cf.set(user.getId() + ".SYNCEDPLAYER", targetPlayer.getUniqueId().toString());
    }

    public boolean isUserSynced(User user) {
        return cf.getBoolean(user.getId() + ".SYNC", false);
    }

    public Player getUserSyncedPlayer(User user) {
        try {
            UUID uuid = UUID.fromString(cf.getString(user.getId() + ".SYNCEDPLAYER"));
            return Bukkit.getPlayer(uuid);
        } catch(IllegalArgumentException e) {
            return null;
        }
    }

    public void setUserSyncing(User user, Player targetPlayer) {
        tempCf.set(user.getId() + ".SYNCING", true);
        tempCf.set(user.getId() + ".ATTEMPT", 0);
        tempCf.set(user.getId() + ".ACCOUNT", targetPlayer.getUniqueId().toString());
        tempCf.set(user.getId() + ".AUTHCODE", DiscordAPI.getAuthCodeManager().getAuthCode());
    }

    public void resetSyncing(User user) {
        tempCf.set(user.getId() + ".SYNCING", null);
        tempCf.set(user.getId() + ".ATTEMPT", null);
        tempCf.set(user.getId() + ".ACCOUNT", null);
        tempCf.set(user.getId() + ".AUTHCODE", null);
        tempCf.set(user.getId() + ".LASTATTEMPT", null);
    }

    public boolean isUserSyncing(User user) {
        return tempCf.getBoolean(user.getId() + ".SYNCING", false);
    }

    public Player getUserSyncingPlayer(User user) {
        try {
            UUID uuid = UUID.fromString(tempCf.getString(user.getId() + ".ACCOUNT"));
            return Bukkit.getPlayer(uuid);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public String getUserAuthCode(User user) {
        return tempCf.getString(user.getId() + ".AUTHCODE");
    }

    public void addSyncAttempt(User user) {
        tempCf.set(user.getId() + ".ATTEMPT", tempCf.getInt(user.getId() + ".ATTEMPT", 0) + 1);
    }

    public int getSyncAttempt(User user) {
        return tempCf.getInt(user.getId() + ".ATTEMPT", 0);
    }

    public boolean isSyncAttemptOver(User user) {
        return tempCf.getInt(user.getId() + ".ATTEMPT", 0) > 2;
    }

    public void setSyncCoolTime(User user) {
        tempCf.set(user.getId() + ".LASTATTEMPT", Instant.now().getEpochSecond());
    }

    public long getSyncCoolTime(User user) {
        return DiscordCore.cf.getLong("AuthCoolTime") - (Instant.now().getEpochSecond() - tempCf.getLong(user.getId() + ".LASTATTEMPT", 0));
    }

    public boolean isSyncCool(User user) {
        return getSyncCoolTime(user) > 0;
    }
    
    // BTPlayer 전용 메소드

    public User getSyncedUser(Player targetPlayer) {
        for(String id : cf.getKeys(false)) {
            if(getUserSyncedPlayer(Objects.requireNonNull(DiscordCore.api.getUserById(id))) == targetPlayer) {
                return DiscordCore.api.getUserById(id);
            }
        }
        return null;
    }

    public User getSyncingUser(Player targetPlayer) {
        for(String id : cf.getKeys(false)) {
            if(getUserSyncingPlayer(Objects.requireNonNull(DiscordCore.api.getUserById(id))) == targetPlayer) {
                return DiscordCore.api.getUserById(id);
            }
        }
        return null;
    }

    public boolean isAlreadySynced(Player targetPlayer) {
        return getSyncedUser(targetPlayer) != null;
    }

    public boolean isAlreadySyncing(Player targetPlayer) {
        return getSyncingUser(targetPlayer) != null;
    }

}
