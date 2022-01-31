package com.umc.miner.src.play.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostLoadPlayReq {
    // 첨에 들어오는 거
    private String mapName;
    private String editorName;

    // 찾아낸 userIdx
    private int editorIdx;
    // 찾아낸 mapIdx
    private int mapIdx;

    // res주기 전 잠깐 담기
    private int mapPassword;
    private int mapSize;

}
