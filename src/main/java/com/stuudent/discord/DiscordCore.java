package com.stuudent.discord;

import com.stuudent.discord.commands.AuthCommand;
import com.stuudent.discord.commands.NormalCommand;
import com.stuudent.discord.data.AllData;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;

public final class DiscordCore extends JavaPlugin {

    public static JDA api;
    public static FileConfiguration cf;
    public static DiscordCore instance;

    static {
        api = null;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        cf = getConfig();
        JDABuild();
        Bukkit.getConsoleSender().sendMessage("§6ST§f-§9Discord §ev" + getDescription().getVersion() + " §a플러그인이 활성화 되었습니다. §f(created by STuuDENT, Discord 민제#5894)");
    }

    @Override
    public void onDisable() {
        if(api != null) {
            api.shutdown();
            AllData allData = DiscordAPI.getData();
            allData.save();
        }
        Bukkit.getConsoleSender().sendMessage("§6ST§f-§9Discord §ev" + getDescription().getVersion() + " §c플러그인이 비활성화 되었습니다. §f(created by STuuDENT, Discord 민제#5894)");
    }

    public void JDABuild() {
        try {
            api = JDABuilder.createDefault(cf.getString("BotToken")).build();
            api.getPresence().setActivity(Activity.watching(cf.getString("CommandPrefix") + " 글내려..."));
            api.addEventListener(new AuthCommand());
            api.addEventListener(new NormalCommand());
        } catch (LoginException e) {
            api = null;
            Bukkit.getConsoleSender().sendMessage("§6ST§f-§9Discord §ev" + getDescription().getVersion() + " §cJDA를 빌드하지 못했습니다. §f(created by STuuDENT, Discord 민제#5894)");
            Bukkit.getPluginManager().disablePlugin(instance);
        }
    }
}
