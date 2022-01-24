package com.umc.miner.src.sms.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Message {
    private String to;
    private String content;
}