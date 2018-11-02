/*
 * This file is part of JuniperBotJ.
 *
 * JuniperBotJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * JuniperBotJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with JuniperBotJ. If not, see <http://www.gnu.org/licenses/>.
 */
package ru.caramel.juniperbot.core.listeners;

import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.guild.GuildBanEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberNickChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.caramel.juniperbot.core.audit.ModerationAuditForwardProvider;
import ru.caramel.juniperbot.core.model.DiscordEvent;
import ru.caramel.juniperbot.core.model.enums.AuditActionType;
import ru.caramel.juniperbot.core.persistence.entity.LocalMember;
import ru.caramel.juniperbot.core.service.MemberService;
import ru.caramel.juniperbot.core.service.ModerationService;

import java.util.Objects;
import java.util.stream.Collectors;

@DiscordEvent(priority = 0)
public class MemberListener extends DiscordEventListener {

    @Autowired
    private MemberService memberService;

    @Autowired
    private ModerationService moderationService;

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        if (event.getMember().getUser().isBot()) {
            return;
        }
        LocalMember member = memberService.getOrCreate(event.getMember());
        moderationService.refreshMute(event.getMember());
        getAuditService().log(event.getGuild(), AuditActionType.MEMBER_JOIN)
                .withUser(member)
                .save();
    }

    @Override
    public void onGuildBan(GuildBanEvent event) {
        if (event.getUser().isBot()) {
            return;
        }
        if (!moderationService.isLeaveNotified(event.getGuild().getIdLong(), event.getUser().getIdLong())) {
            moderationService.setLeaveNotified(event.getGuild().getIdLong(), event.getUser().getIdLong());
            event.getGuild().getBan(event.getUser()).queue(e -> {
                getAuditService().log(event.getGuild(), AuditActionType.MEMBER_BAN)
                        .withTargetUser(event.getUser())
                        .withAttribute(ModerationAuditForwardProvider.REASON_ATTR, e.getReason())
                        .save();
            });
        }
    }

    @Override
    @Transactional
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        if (event.getMember().getUser().isBot()) {
            return;
        }
        LocalMember member = memberService.get(event.getMember());
        if (member == null) {
            return;
        }
        member.setLastKnownRoles( event.getMember().getRoles().stream()
                .map(Role::getIdLong).collect(Collectors.toList()));
        memberService.save(member);

        if (!moderationService.isLeaveNotified(event.getGuild().getIdLong(), event.getUser().getIdLong())) {
            getAuditService().log(event.getGuild(), AuditActionType.MEMBER_LEAVE)
                    .withUser(member)
                    .save();
        }
    }

    @Override
    @Transactional
    public void onGuildMemberNickChange(GuildMemberNickChangeEvent event) {
        LocalMember member = memberService.get(event.getMember());
        if (member != null && !Objects.equals(event.getMember().getEffectiveName(), member.getEffectiveName())) {
            member.setEffectiveName(event.getMember().getEffectiveName());
            memberService.save(member);
        }
    }
}