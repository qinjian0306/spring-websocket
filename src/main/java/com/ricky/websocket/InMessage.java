package com.ricky.websocket;

import lombok.Data;
import lombok.ToString;

/**
 * 消息接收实体
 */
@Data
@ToString
public class InMessage {

    private String name;
    private String token;
    private String time;
    private String crypto;

}
