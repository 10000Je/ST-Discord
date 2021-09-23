package com.stuudent.discord;

import com.stuudent.discord.data.AllData;
import com.stuudent.discord.data.PlayerData;
import com.stuudent.discord.data.UserData;
import com.stuudent.discord.utils.AuthCodeManager;
import com.stuudent.discord.utils.EmbedManager;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.entity.Player;

public class DiscordAPI {

    public static UserData getUser(User user) {
        return new UserData(user);
    }

    public static AllData getData() {
        return new AllData();
    }

    public static PlayerData getPlayer(Player targetPlayer) {
        return new PlayerData(targetPlayer);
    }

    public static EmbedManager getEmbedManager() {
        return new EmbedManager();
    }

    public static AuthCodeManager getAuthCodeManager() {
        return new AuthCodeManager();
    }

}
