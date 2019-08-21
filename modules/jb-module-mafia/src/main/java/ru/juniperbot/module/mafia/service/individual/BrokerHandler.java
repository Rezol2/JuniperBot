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
package ru.juniperbot.module.mafia.service.individual;

import net.dv8tion.jda.api.entities.PrivateChannel;
import org.springframework.stereotype.Component;
import ru.juniperbot.module.mafia.model.*;

@Component
public class BrokerHandler extends IndividualHandler<DoctorHandler> {

    public BrokerHandler() {
        super(MafiaRole.BROKER, MafiaState.NIGHT_BROKER);
    }

    @Override
    protected void choiceAction(MafiaInstance instance, MafiaPlayer target, PrivateChannel channel) {
        instance.getDailyActions().put(MafiaActionType.BROKER_DAMAGE, target);
        channel.sendMessage(messageService.getMessage("mafia.broker.choice.selected", target.getName())).queue();
    }

    @Override
    protected Class<DoctorHandler> getNextType() {
        return DoctorHandler.class;
    }
}
