package com.ricky.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectEvent;

/**
 * STOMP监听类
 * 用于session注册 以及key值获取
 */
public class STOMPConnectEventListener  implements ApplicationListener<SessionConnectEvent> {

    @Autowired
    WebSocketSessionRegistry sessionRegistry;

//    @Override
//    public void onApplicationEvent(SessionConnectEvent event) {
//        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
//        //login get from browser
//        String agentId = sha.getNativeHeader("login").get(0);
//        String sessionId = sha.getSessionId();
//        webAgentSessionRegistry.registerSessionId(agentId,sessionId);
//    }


    /**
     * 自己
     * @param event
     */
    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        //login get from browser
        String token = sha.getNativeHeader("login").get(0);
        String sessionId = sha.getSessionId();
        sessionRegistry.registerSessionId(token,sessionId);
    }
}
