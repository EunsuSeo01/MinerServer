package com.umc.miner.src.play.model;

import lombok.*;

import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlayTimeInfo {
    private int userIdx;
    private Time playTime;
}
