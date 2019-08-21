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
package ru.juniperbot.module.steam.service;

import ru.juniperbot.module.steam.model.details.SteamAppDetails;
import ru.juniperbot.module.steam.persistence.entity.SteamApp;

import java.util.List;
import java.util.Locale;

public interface SteamService {

    void rebuildApps();

    List<SteamApp> find(String query);

    SteamApp findOne(String query);

    SteamApp findByAppId(Long appId);

    SteamAppDetails getDetails(SteamApp steamApp, Locale locale);
}
