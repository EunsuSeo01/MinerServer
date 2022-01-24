package com.umc.miner.src.email.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostCompareAuthReq {
    private String AuthNum;
    private int userIdx;
}
