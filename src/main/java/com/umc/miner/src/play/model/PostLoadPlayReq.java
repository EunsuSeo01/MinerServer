package com.umc.miner.src.play.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostLoadPlayReq {
    private String mapName;
    private String editorName;

    private int editorIdx;

    private int mapIdx;

    private int playTimeIdx;
}
