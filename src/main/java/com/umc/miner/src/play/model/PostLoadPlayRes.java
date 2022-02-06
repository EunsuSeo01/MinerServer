package com.umc.miner.src.play.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostLoadPlayRes {
    private int mapPassword;
    private int mapSize;

    private int avgPlayTime;

    private List<PlayTimeInfo> playInfoList;

}
