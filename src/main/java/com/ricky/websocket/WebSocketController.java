package com.ricky.websocket;

import com.ricky.bean.InMessage;
import com.ricky.bean.OutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * websocket controller
 */
@Controller
public class WebSocketController {
    /**
     * session操作类
     */
    @Autowired
    WebSocketSessionRegistry sessionRegistry;
    /**
     * 消息发送工具
     */
    @Autowired
    private SimpMessagingTemplate template;

    @RequestMapping(value = "/index")
    public String index() {
        return "/index";
    }

    @RequestMapping(value = "/msg/message")
    public String ToMessage() {
        return "/message";
    }

    @RequestMapping(value = "/msg/messaget2")
    public String ToMessaget2() {
        return "/messaget2";
    }

    @RequestMapping(value = "/msg/trendPrice")
    public String price() {
        return "/messaget3";
    }

    /**
     * 广播 普通http请求
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/msg/sendcommuser")
    @ResponseBody
    public OutMessage SendToCommUserMessage(HttpServletRequest request) {
        List<String> keys = sessionRegistry.getAllSessionIds().entrySet()
                .stream().map(Map.Entry::getKey)
                .collect(Collectors.toList());
        Date date = new Date();
        keys.forEach(x -> {
            String sessionId = sessionRegistry.getSessionIds(x).stream().findFirst().get().toString();
            template.convertAndSendToUser(sessionId, "/topic/greetings", new OutMessage("commmsg：allsend, " + "send  comm" + date.getTime() + "!"), createHeaders(sessionId));

        });
        return new OutMessage("sendcommuser, " + new Date() + "!");
    }



    /**
     * 广播 http请求方式
     *
     * @return
     */

    @GetMapping(value = "/msg/sendAllHttp")
    @ResponseBody
    public String sendAllHttp() {
        template.convertAndSend("/topic/greetings", "大家好 http方式");
        return "success";
    }

    /**
     * 广播 ws版本 http不能请求
     *
     * @return
     */

    @MessageMapping("/msg/sendAll")
    @SendTo("/topic/greetings")
    public String SendAllWS() {
        return "大家好 ws请求方式";
    }


    /**
     * 单播 ws版本 http不能请求
     *
     * @param message
     * @return
     * @throws Exception
     */
    @MessageMapping("/msg/sendSingle")
    public void greeting2(InMessage message) throws Exception {
        //这里没做校验
        String sessionId = sessionRegistry.getSessionIds(message.getToken()).stream().findFirst().get();
        template.convertAndSendToUser(sessionId, "/topic/greetings", new OutMessage("single send to：" + message.getToken() + ", from:" + message.getName() + "!"), createHeaders(sessionId));
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }




    /**
     * 接收不同的topic信息
     *
     * @return
     * @throws Exception
     */
    @MessageMapping("/msg/{topic}/{token}")
    public void trendPrice(@DestinationVariable("topic") String topic, @DestinationVariable("token") String token, String crypto) throws Exception {

        if (StringUtils.isBlank(topic)) {
            return;
        }
        System.out.println("topic:" + topic + " token:" + token + " crypto:" + crypto);

    }


    /**
     * 断开连接
     */
    @MessageMapping("/disconnect")
    public void disconnect(InMessage message) throws Exception {
        String token = message.getToken();
        String sessionId = sessionRegistry.getSessionIds(message.getToken()).stream().findFirst().get();
        sessionRegistry.unregisterSessionId(token,sessionId);
    }

}
