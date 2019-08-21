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
package ru.juniperbot.common.persistence.entity.base;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;

@MappedSuperclass
@Getter
@Setter
public abstract class MemberMessageEntity extends MemberEntity {

    private static final long serialVersionUID = -3480359468078209437L;

    @Size(max = 2048) // MessageEmbed.TEXT_MAX_LENGTH
    @Basic
    protected String message;

    @Column(name = "channel_id")
    protected String channelId;

}
