package com.umc.miner.src.user.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class PostLoginReq {
    private int userIdx;
    private String email;
    private String password;
    private String nickName;
    private String status;
}
