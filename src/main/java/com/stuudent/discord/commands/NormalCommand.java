package com.stuudent.discord.commands;

import com.stuudent.discord.DiscordCore;
import com.stuudent.discord.utils.EmbedManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class NormalCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        if(e.getAuthor().isBot()) return;
        String[] args = e.getMessage().getContentRaw().split(" ");
        if(!args[0].equals(DiscordCore.cf.getString("CommandPrefix"))) return;
        EmbedManager eb = new EmbedManager();
        if(args.length == 1) {
            e.getAuthor().openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage(eb.helpMessage(e.getAuthor())).queue();
            });
            return;
        }
        if(args[1].equals("글내려")) {
            e.getChannel().sendMessage("^^7;").queue();
            return;
        }
    }

}
