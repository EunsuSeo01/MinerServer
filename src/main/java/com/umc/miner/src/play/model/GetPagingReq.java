package com.umc.miner.src.play.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetPagingReq {
    private int orderType;  // 인기순인지(0) 최신순인지(1).
    private int searchType; // 닉네임 검색인지(0), 미로명 검색인지(1).
    private String searchContent;   // 검색 내용.
    private int pageNo; // 선택한 페이지가 몇 페이지인지.
}