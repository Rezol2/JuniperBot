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
package ru.juniperbot.api.dto.config;

import lombok.Getter;
import lombok.Setter;
import ru.juniperbot.api.common.validation.DiscordEntity;
import ru.juniperbot.api.common.validation.DiscordEntityType;
import ru.juniperbot.common.model.AuditActionType;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class AuditConfigDto implements Serializable {

    private static final long serialVersionUID = 1373520739258476656L;

    private boolean enabled;

    private boolean forwardEnabled;

    @DiscordEntity(DiscordEntityType.TEXT_CHANNEL)
    private String forwardChannelId;

    private List<AuditActionType> forwardActions;
}
