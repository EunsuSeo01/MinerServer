package com.umc.miner.src.user.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor // 생성자를 자동으로 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 해당 클래스의 파라미터가 없는 생성자를 생성, 접근 제한자를 PROTECTED로 설정.
public class PostFindPwReq {
    private String email;
}
