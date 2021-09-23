package com.stuudent.discord.utils;

import com.stuudent.discord.DiscordCore;
import com.stuudent.discord.DiscordAPI;
import com.stuudent.discord.data.UserData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.entity.Player;

import java.awt.*;

public class EmbedManager {

    public MessageEmbed helpMessage(User user) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("안녕하세요 " + user.getAsTag() + " 님!");
        eb.setThumbnail(DiscordCore.api.getSelfUser().getAvatarUrl());
        StringBuilder helpBuild = new StringBuilder("```");
        for(String text : DiscordCore.cf.getStringList("HelpMessages")) {
            helpBuild.append(text.replace("[PREFIX]", DiscordCore.cf.getString("CommandPrefix")));
            helpBuild.append("\n");
        }
        helpBuild.append("```");
        String helpMessage = helpBuild.toString();
        eb.addField("명령어!", helpMessage, false);
        eb.setColor(Color.GREEN);
        return eb.build();
    }

    public MessageEmbed SyncHelpMessage(User user, Player syncPlayer) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("안녕하세요 " + user.getAsTag() + " 님!");
        eb.setThumbnail("https://mc-heads.net/avatar/" + syncPlayer.getName());
        eb.addField("플레이어 " +syncPlayer.getName() + " 님에게 인증코드를 전송하였습니다!", "확인 후 \"!인증 [인증코드]\" 를 입력하세요!", false);
        eb.setColor(Color.GREEN);
        return eb.build();
    }

    public MessageEmbed SyncCompleteMessage(User user) {
        UserData userData = DiscordAPI.getUser(user);
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("인증에 성공하였습니다!");
        eb.setThumbnail("https://mc-heads.net/avatar/" + userData.getSyncedPlayer().getName());
        eb.addField("플레이어 정보!", userData.getSyncedPlayer().getName(), true);
        eb.addField("유저 정보!", user.getAsMention(), true);
        eb.setFooter("이제 정상적으로 디스코드 서버를 이용하실 수 있습니다.");
        eb.setColor(Color.GREEN);
        return eb.build();
    }

}
