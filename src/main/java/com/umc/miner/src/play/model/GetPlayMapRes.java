package com.umc.miner.src.play.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetPlayMapRes {
    private String mapName;
    private String mapInfo;
    private int mapSize;    // 0, 1, 2 = 작음 중간 큼
    private int mapPassword;
    private int playCount;
    private String status;  // active, inactive
    private String createAt;
    private String updateAt;
}
