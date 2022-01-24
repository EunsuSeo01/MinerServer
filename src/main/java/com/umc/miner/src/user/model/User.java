package com.umc.miner.src.user.model;

import lombok.*;


@Getter // 해당 클래스에 대한 접근자 생성
@Setter // 해당 클래스에 대한 설정자 생성
@AllArgsConstructor

public class User {
    private int userIdx;
    private String email;
    private String password;
    private String nickName;
    private String status;
}
