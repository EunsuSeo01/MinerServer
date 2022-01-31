package com.umc.miner.src.play.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
@NoArgsConstructor
public class PatchSavePlayReq {
    private String editorName;
    private String mapName;
    private String playerName;

    private int editorIdx;
    private int playerIdx;

    private int mapIdx;

    private String playTime;
}
