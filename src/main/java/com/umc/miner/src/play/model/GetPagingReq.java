package com.umc.miner.src.play.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetPagingReq {
    private int orderType;  // 인기순인지(0) 최신순인지(1). -> Q. 안드에서 이렇게 줄 수 있나?
    private int pageNo; // 선택한 페이지가 몇 페이지인지.
}
