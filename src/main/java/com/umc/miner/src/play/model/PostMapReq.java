package com.umc.miner.src.play.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class PostMapReq {
    private String mapName;
    private String mapInfo;
    private int mapSize;
    private int mapPassword;
    private int editorIdx;
    private String editorName;
    private int playCount;
    private String status;

    private String nickName;
}
