package org.acme.utils;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class BanCacheService {

    private Cache<Long, Boolean> bannedUsersCache;

    @PostConstruct
    void init() {
        bannedUsersCache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build();
    }

    public void banUser(Long userId) {
        bannedUsersCache.put(userId, true);
    }

    public boolean isUserBanned(Long userId) {
        return bannedUsersCache.getIfPresent(userId) != null;
    }

    public void unbanUser(Long userId) {
        bannedUsersCache.invalidate(userId);
    }
}
