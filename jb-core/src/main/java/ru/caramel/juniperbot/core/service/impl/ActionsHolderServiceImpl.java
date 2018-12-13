package ru.caramel.juniperbot.core.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.dv8tion.jda.core.entities.Message;
import org.springframework.stereotype.Service;
import ru.caramel.juniperbot.core.service.ActionsHolderService;

import java.util.concurrent.TimeUnit;

@Service
public class ActionsHolderServiceImpl implements ActionsHolderService {

    private Cache<String, Boolean> leaveNotified = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.SECONDS)
            .build();

    private Cache<String, Boolean> messageDeleted = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    @Override
    public boolean isLeaveNotified(long guildId, long userId) {
        return Boolean.TRUE.equals(leaveNotified.getIfPresent(getMemberKey(guildId, userId)));
    }

    @Override
    public boolean tryLeaveNotified(long guildId, long userId) {
        if (!isLeaveNotified(guildId, userId)) {
            setLeaveNotified(guildId, userId);
            return true;
        }
        return false;
    }

    @Override
    public void setLeaveNotified(long guildId, long userId) {
        leaveNotified.put(getMemberKey(guildId, userId), true);
    }

    @Override
    public void markAsDeleted(Message message) {
        messageDeleted.cleanUp();
        messageDeleted.put(geMessageKey(message.getChannel().getId(), message.getId()), true);
    }

    @Override
    public boolean isOwnDeleted(String channelId, String messageId) {
        return Boolean.TRUE.equals(messageDeleted.getIfPresent(geMessageKey(channelId, messageId)));
    }

    private static String getMemberKey(long guildId, long userId) {
        return String.format("%s_%s", guildId, userId);
    }

    private static String geMessageKey(String channelId, String messageId) {
        return String.format("%s_%s", channelId, messageId);
    }
}
