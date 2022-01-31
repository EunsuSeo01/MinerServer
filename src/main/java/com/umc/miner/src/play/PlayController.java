package com.umc.miner.src.play;

import com.umc.miner.config.BaseException;
import com.umc.miner.config.BaseResponse;

import com.umc.miner.src.play.model.PostLoadPlayReq;
import com.umc.miner.src.play.model.PostLoadPlayRes;
import com.umc.miner.src.user.UserProvider;

import com.umc.miner.src.play.model.*;
import com.umc.miner.utils.Pagination;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.sql.Time;
import java.util.List;

@RestController
@RequestMapping("/miner/playmaps")
public class PlayController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PlayService playService;
    private final PlayProvider playProvider;
    private final UserProvider userProvider;


    @Autowired
    public PlayController(PlayService playService, PlayProvider playProvider, UserProvider userProvider) {
        this.playProvider = playProvider;
        this.playService = playService;
        this.userProvider = userProvider;
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
            paging.pageInfo(playProvider.getTotalNumOfPlayMap());

            GetPagingRes result = new GetPagingRes(playProvider.getPlayMap(getPagingReq, mapNumPerPage), paging);
            return new BaseResponse(result);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 미로맵 클릭 시 mapPassword, mapSize, user, playTime을 알려준다.
     * [POST] /miner/playmaps/loadPlayInfo
     */
    @ResponseBody
    @PostMapping("/loadPlayInfo")
    public BaseResponse<PostLoadPlayRes> loadPlayInfo(@RequestBody PostLoadPlayReq postLoadPlayReq) {
        PostLoadPlayRes postLoadPlayRes = new PostLoadPlayRes();

        try {
            // userIdx set
            postLoadPlayReq.setEditorIdx(userProvider.getEditorIdx(postLoadPlayReq.getEditorName()));
            // mapIdx set
            postLoadPlayReq.setMapIdx(playProvider.getMapIdx(postLoadPlayReq.getEditorIdx(), postLoadPlayReq.getMapName()));

            // PlayMap
            PlayMapInfo playMapInfo = playProvider.loadPlayMapInfo(postLoadPlayReq);
            postLoadPlayRes.setMapPassword(playMapInfo.getMapPassword());
            postLoadPlayRes.setMapSize(playMapInfo.getMapSize());

            //PlayTime
            List<PlayTimeInfo> playTimeInfoList = playProvider.loadPlayTimeInfo(postLoadPlayReq);
            postLoadPlayRes.setPlayInfoList(playTimeInfoList);

            //PlayTime average
            int totalSec = 0;
            int userCount = 0;
            for (int i = 0; i < playTimeInfoList.size(); i++) {
                String stringTemp = playTimeInfoList.get(i).getPlayTime().toString();

                String[] hourMin = stringTemp.split(":");
                int hour = Integer.parseInt(hourMin[0]);
                int mins = Integer.parseInt(hourMin[1]);
                int sec = Integer.parseInt(hourMin[2]);
                totalSec += (hour * 360) + (mins * 60) + sec;
                userCount++;
            }
            postLoadPlayRes.setAvgPlayTime(totalSec / userCount);
            return new BaseResponse<>(postLoadPlayRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 플레이 정보 저장
     * [PATCH] /miner/playmaps/savePlayInfo
     */
    @ResponseBody
    @PatchMapping("/savePlayInfo")
    public BaseResponse<PatchSavePlayRes> savePlayInfo(@RequestBody PatchSavePlayReq patchSavePlayReq) {
        PatchSavePlayRes patchSavePlayRes = new PatchSavePlayRes();
        try {
            // editorIdx set
            patchSavePlayReq.setEditorIdx(userProvider.getEditorIdx(patchSavePlayReq.getEditorName()));
            // playerIdx set
            patchSavePlayReq.setPlayerIdx(userProvider.getEditorIdx(patchSavePlayReq.getPlayerName()));
            // mapIdx set
            patchSavePlayReq.setMapIdx(playProvider.getMapIdx(patchSavePlayReq.getEditorIdx(), patchSavePlayReq.getMapName()));

            if (playProvider.checkPlayerInfo(patchSavePlayReq) == 0) {
                patchSavePlayRes.setPlayerName(playProvider.savePlayInfo(patchSavePlayReq));
            } else {
                patchSavePlayRes.setPlayerName(playProvider.updatePlayerInfo(patchSavePlayReq));
            }
            return new BaseResponse<>(patchSavePlayRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
