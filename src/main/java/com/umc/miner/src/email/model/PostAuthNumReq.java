package com.umc.miner.src.email.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@NoArgsConstructor
public class PostAuthNumReq {
    private int userIdx;
    private String EmailAuthNum;
}
