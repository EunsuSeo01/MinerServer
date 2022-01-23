package com.umc.miner.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class PostLoginRes {
    private int userIdx;
    private String jwt;
    private String nickName;
}
