/*
 * This file is part of JuniperBot.
 *
 * JuniperBot is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * JuniperBot is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with JuniperBot. If not, see <http://www.gnu.org/licenses/>.
 */
package ru.juniperbot.worker.commands.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import ru.juniperbot.common.persistence.entity.MemberWarning;
import ru.juniperbot.common.utils.CommonUtils;
import ru.juniperbot.common.worker.command.model.BotContext;
import ru.juniperbot.common.worker.command.model.DiscordCommand;

import java.util.List;

@DiscordCommand(key = "discord.command.mod.warns.key",
        description = "discord.command.mod.warns.desc",
        group = {"discord.command.group.moderation", "discord.command.group.utility"},
        priority = 15)
public class WarnsCommand extends ModeratorCommandAsync {

    @Override
    public void doCommandAsync(GuildMessageReceivedEvent event, BotContext context, String query) {
        Member member = getMentioned(event);
        if (member == null) {
            member = event.getMember();
        }

        List<MemberWarning> warningList = moderationService.getWarnings(member);
        if (warningList.isEmpty()) {
            messageService.onEmbedMessage(event.getChannel(), "discord.command.mod.warns.empty");
            return;
        }
        if (warningList.size() > 20) {
            warningList = warningList.subList(0, 20);
        }

        EmbedBuilder builder = messageService.getBaseEmbed();
        builder.setTitle(messageService.getMessage("discord.command.mod.warns.message.title", member.getEffectiveName()));
        final String noReasonMessage = messageService.getMessage("discord.command.mod.warns.noReason");

        DateTimeFormatter formatter = DateTimeFormat
                .shortDateTime()
                .withZone(context.getTimeZone())
                .withLocale(contextService.getLocale());

        int i = 1;
        int length = builder.length();
        for (MemberWarning warning : warningList) {
            String title = String.format("%2s. %s %s (%s)", i++,
                    formatter.print(new DateTime(warning.getDate())),
                    CommonUtils.getUTCOffset(context.getTimeZone()),
                    warning.getModerator().getEffectiveName());
            String reason = warning.getReason();
            if (StringUtils.isEmpty(reason)) {
                reason = noReasonMessage;
            }
            if ((length += title.length() + reason.length()) > MessageEmbed.EMBED_MAX_LENGTH_BOT) {
                break;
            }
            builder.addField(title, reason, true);
        }
        messageService.sendMessageSilent(event.getChannel()::sendMessage, builder.build());
    }

    @Override
    public boolean isAvailable(User user, Member member, Guild guild) {
        return true; // to everyone
    }
}
