package com.umc.miner.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchDeleteUserInfoReq {
    private String email;
    private String password;

    private int userIdx;
    private int mapIdx;
}
