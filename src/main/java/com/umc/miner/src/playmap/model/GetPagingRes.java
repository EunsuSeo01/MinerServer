package com.umc.miner.src.playmap.model;

import lombok.*;
import java.util.List;
import com.umc.miner.utils.Pagination;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetPagingRes {

    private List<GetPlayMapRes> playMapList;
    private Pagination paging;

    public GetPagingRes(List<GetPlayMapRes> getPlayMapRes, Pagination paging) {
        playMapList = getPlayMapRes;
        this.paging = paging;
    }
}
