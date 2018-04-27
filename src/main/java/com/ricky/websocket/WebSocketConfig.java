package com.ricky.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * socket核心配置容器
 * 通过EnableWebSocketMessageBroker 开启使用STOMP协议来传输基于代理(message broker)的消息,此时浏览器支持使用@MessageMapping 就像支持@RequestMapping一样
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {


    /**
     * 注册协议节点
     *
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ricky-websocket").setAllowedOrigins("*").withSockJS();//注册一个Stomp 协议的endpoint,并指定 SockJS协议。
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // 设置服务器广播消息的基础路径
        config.setApplicationDestinationPrefixes("/ws");// 设置客户端订阅消息的基础路径
        config.setUserDestinationPrefix("/user/");     //设置前缀  默认是user 可以修改  点对点时使用
        //	config.setPathMatcher(new AntPathMatcher("."));// 可以已“.”来分割路径，看看类级别的@messageMapping和方法级别的@messageMapping
    }

    @Bean
    public WebSocketSessionRegistry SocketSessionRegistry(){
        return new WebSocketSessionRegistry();
    }

    @Bean
    public STOMPConnectEventListener STOMPConnectEventListener(){
        return new STOMPConnectEventListener();
    }
}