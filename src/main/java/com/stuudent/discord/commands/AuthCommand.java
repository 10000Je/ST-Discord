package com.stuudent.discord.commands;

import com.stuudent.discord.DiscordCore;
import com.stuudent.discord.DiscordAPI;
import com.stuudent.discord.data.PlayerData;
import com.stuudent.discord.data.UserData;
import com.stuudent.discord.utils.EmbedManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Objects;

public class AuthCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        if(e.getAuthor().isBot()) return;
        String[] args = e.getMessage().getContentRaw().split(" ");
        if(!args[0].equals(DiscordCore.cf.getString("AuthCommand"))) return;
        EmbedManager eb = DiscordAPI.getEmbedManager();
        e.getMessage().delete().queue();
        if(!e.getChannel().getId().equals(DiscordCore.cf.getString("AuthChannel"))) {
            e.getAuthor().openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage(DiscordCore.cf.getString("NotAuthChannel").
                        replace("\\n", "\n")).queue();
            });
            return;
        }
        UserData userData = DiscordAPI.getUser(e.getAuthor());
        if(userData.isSynced()) {
            e.getAuthor().openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage(DiscordCore.cf.getString("AlreadyAuthed").
                        replace("\\n", "\n")).queue();
            });
            return;
        }
        if(userData.isSyncing()) {
            e.getAuthor().openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage(DiscordCore.cf.getString("AlreadyAuthing").
                        replace("\\n", "\n")).queue();
            });
            return;
        }
        if(userData.isSyncCool()) {
            e.getAuthor().openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage(DiscordCore.cf.getString("CannotAuthNow").
                        replace("[COOLTIME]", String.valueOf(userData.getSyncCoolTime())).
                        replace("\\n", "\n")).queue();
            });
            return;
        }
        if(args.length == 1) {
            e.getAuthor().openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage(DiscordCore.cf.getString("InputPlayer").
                        replace("[PREFIX]", DiscordCore.cf.getString("AuthCommand")).
                        replace("\\n", "\n")).queue();
            });
            return;
        }
        if(Bukkit.getPlayer(args[1]) == null) {
            e.getAuthor().openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage(DiscordCore.cf.getString("PlayerOffline").
                        replace("\\n", "\n")).queue();
            });
            return;
        }
        Player player = Bukkit.getPlayer(args[1]);
        PlayerData playerData = DiscordAPI.getPlayer(player);
        if(playerData.isAlreadySynced()) {
            e.getAuthor().openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage(DiscordCore.cf.getString("PlayerAlreadyAuthed").
                        replace("\\n", "\n")).queue();
            });
            return;
        }
        if(playerData.isAlreadySyncing()) {
            e.getAuthor().openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage(DiscordCore.cf.getString("SomeoneAuthingPlayer").
                        replace("[USER]", playerData.getSyncingUser().getAsMention()).
                        replace("\\n", "\n")).queue();
            });
            return;
        }
        userData.setSyncing(player);
        for(String text : DiscordCore.cf.getStringList("AuthMessages"))
            userData.getSyncingPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', text.replace("[AUTHCODE]", userData.getAuthCode())));
        e.getAuthor().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage(eb.SyncHelpMessage(e.getAuthor(), userData.getSyncingPlayer())).queue();
        });
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent e) {
        if(e.getAuthor().isBot()) return;
        String[] args = e.getMessage().getContentRaw().split(" ");
        if(!args[0].equals(DiscordCore.cf.getString("AuthCommand"))) return;
        EmbedManager eb = DiscordAPI.getEmbedManager();
        UserData userData = DiscordAPI.getUser(e.getAuthor());
        if(userData.isSynced()) {
            e.getChannel().sendMessage(DiscordCore.cf.getString("AlreadyAuthed").
                    replace("\\n", "\n")).queue();
            return;
        }
        if(!userData.isSyncing()) {
            e.getChannel().sendMessage(DiscordCore.cf.getString("NotAuthStarted").
                    replace("\\n", "\n")).queue();
            return;
        }
        if(args.length == 1) {
            e.getChannel().sendMessage(DiscordCore.cf.getString("InputAuthCode").
                    replace("\\n", "\n")).queue();
            return;
        }
        if(!args[1].equals(userData.getAuthCode())) {
            userData.addSyncAttempt();
            if(userData.isSyncAttemptOver()) {
                userData.resetSyncing();
                userData.setSyncCoolTime();
                e.getChannel().sendMessage(DiscordCore.cf.getString("CannotAuthNow").
                        replace("[COOLTIME]", String.valueOf(userData.getSyncCoolTime())).
                        replace("\\n", "\n")).queue();
            } else {
                e.getChannel().sendMessage(DiscordCore.cf.getString("WrongAuthCode").
                        replace("[LEFTCHANCE]", String.valueOf(3 - userData.getSyncAttempt())).
                        replace("[COOLTIME]", String.valueOf(DiscordCore.cf.getInt("AuthCoolTime"))).
                        replace("\\n", "\n")).queue();
            }
            return;
        }
        userData.setSync(true);
        userData.setSyncedPlayer(userData.getSyncingPlayer());
        userData.resetSyncing();
        Guild guild = DiscordCore.api.getGuildById(DiscordCore.cf.getString("DiscordGuild"));
        Role role = DiscordCore.api.getRoleById(DiscordCore.cf.getString("VerifiedRole"));
        Objects.requireNonNull(guild).addRoleToMember(e.getAuthor().getId(), Objects.requireNonNull(role)).queue();
        e.getChannel().sendMessage(eb.SyncCompleteMessage(e.getAuthor())).queue();
    }
}
