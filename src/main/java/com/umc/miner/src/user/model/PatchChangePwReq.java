package com.umc.miner.src.user.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor // 생성자를 자동으로 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PatchChangePwReq {
    private int userIdx;
    private String password;
}
