package com.ricky.websocket;

import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 用户session注册类
 */
public class WebSocketSessionRegistry {
    //存储客户端session
    private final ConcurrentMap<String, Set<String>> clients = new ConcurrentHashMap();
    //锁
    private final Object lock = new Object();

    public WebSocketSessionRegistry() {}

    /**
     *
     * 获取单一用户sessionId
     * @param user
     * @return
     */
    public String getSessionId(String user) {
        String sessionId = this.clients.get(user).stream().findFirst().get();
        return sessionId != null?sessionId: null;
    }

    /**
     *
     * 获取单一用户sessionIds 集合
     * @param user
     * @return
     */
    public Set<String> getSessionIds(String user) {
        Set set = (Set)this.clients.get(user);
        return set != null?set: Collections.emptySet();
    }

    /**
     * 获取用户所有session
     * @return
     */
    public ConcurrentMap<String, Set<String>> getAllSessionIds() {
        return this.clients;
    }


    /**
     * 注册 session
     * @param user
     * @param sessionId
     */
    public void registerSessionId(String user, String sessionId) {
        Assert.notNull(user, "User must not be null");
        Assert.notNull(sessionId, "Session ID must not be null");
        synchronized(this.lock) {
            Object set = (Set)this.clients.get(user);
            if(set == null) {
                set = new CopyOnWriteArraySet();
                this.clients.put(user, (Set<String>) set);
            }
            ((Set)set).add(sessionId);
        }
    }

    /**
     * 移除 session
     * @param user
     * @param sessionId
     */
    public void unregisterSessionId(String user, String sessionId) {
        Assert.notNull(user, "User Name must not be null");
        Assert.notNull(sessionId, "Session ID must not be null");
        synchronized(this.lock) {
            Set set = (Set)this.clients.get(user);
//            if(set != null && set.remove(sessionId) && set.isEmpty()) {
//                this.clients.remove(user);
//            }
            if(set != null && set.remove(sessionId)) {
                this.clients.remove(user);
            }
        }
    }
}
