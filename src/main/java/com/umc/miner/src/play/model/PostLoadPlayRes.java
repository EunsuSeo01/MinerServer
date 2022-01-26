package com.umc.miner.src.play.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostLoadPlayRes {
    private int mapPassword;
    private int mapSize;

    private int userIdx;
    private int playTime;

    private int avgPlayTime;
}
