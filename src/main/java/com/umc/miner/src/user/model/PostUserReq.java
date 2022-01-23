package com.umc.miner.src.user.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class PostUserReq {
    private String email;
    private String password;
    private String phoneNum;
    private String nickName;
    private int isChecked;
}
