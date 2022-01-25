package com.umc.miner.src.playmap;

import com.umc.miner.config.BaseException;
import com.umc.miner.config.BaseResponse;
import com.umc.miner.src.playmap.model.*;
import com.umc.miner.utils.Pagination;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/miner/playmaps")
public class PlayMapController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PlayMapProvider playMapProvider;

    @Autowired
    public PlayMapController(PlayMapProvider playMapProvider) {
        this.playMapProvider = playMapProvider;
    }

    /**
     * 공유된 맵을 페이징 처리해서 보여주는 API
     * [POST] /miner/playmaps
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<GetPagingRes> getPlayMap(@RequestBody @NotNull GetPagingReq getPagingReq) {
        try {
            int mapNumPerPage = 4;  // 한 페이지에 4개씩 조회되도록. mapNumPerPage = 한 페이지당 맵의 개수.

            Pagination paging = new Pagination(getPagingReq.getPageNo(), mapNumPerPage);
            paging.pageInfo(playMapProvider.getTotalNumOfPlayMap());

            GetPagingRes result = new GetPagingRes(playMapProvider.getPlayMap(getPagingReq, mapNumPerPage), paging);
            return new BaseResponse(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
