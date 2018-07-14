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
package ru.caramel.juniperbot.web.controller.api.priv.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.caramel.juniperbot.web.common.aspect.GuildId;
import ru.caramel.juniperbot.web.controller.api.base.BaseRestController;
import ru.caramel.juniperbot.web.dao.api.CommandsDao;
import ru.caramel.juniperbot.web.dto.api.config.CommandDto;
import ru.caramel.juniperbot.web.dto.api.config.CommandGroupDto;

import java.util.List;

@RestController
public class CommandsController extends BaseRestController {

    @Autowired
    private CommandsDao commandsDao;

    @RequestMapping(value = "/commands/{guildId}", method = RequestMethod.GET)
    @ResponseBody
    public List<CommandGroupDto> load(@GuildId @PathVariable long guildId) {
        return commandsDao.get(guildId);
    }

    @RequestMapping(value = "/commands/{guildId}", method = RequestMethod.POST)
    public void save(@GuildId @PathVariable long guildId,
                     @RequestBody @Validated List<CommandGroupDto> dto) {
        commandsDao.saveAll(dto, guildId, false);
    }

    @RequestMapping(value = "/commands/{guildId}/command", method = RequestMethod.POST)
    public void save(@GuildId @PathVariable long guildId,
                     @RequestBody @Validated CommandDto dto) {
        commandsDao.save(dto, guildId);
    }

    @RequestMapping(value = "/commands/{guildId}/group", method = RequestMethod.POST)
    public void save(@GuildId @PathVariable long guildId,
                     @RequestBody @Validated CommandGroupDto dto) {
        commandsDao.save(dto, guildId);
    }
}
