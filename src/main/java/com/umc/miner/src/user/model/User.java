package com.umc.miner.src.user.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor

public class User {
    private int userIdx;
    private String email;
    private String password;
    private String phoneNum;
    private String nickName;
}
