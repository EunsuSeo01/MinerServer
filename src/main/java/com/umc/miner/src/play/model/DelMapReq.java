package com.umc.miner.src.play.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class DelMapReq {
    private int mapIdx;
    private String nickName;
    private String mapName;

    private int editorIdx;
}
