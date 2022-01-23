package com.umc.miner.src.sms.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Request {
    private String recipientPhoneNumber;
    private String content;
}
