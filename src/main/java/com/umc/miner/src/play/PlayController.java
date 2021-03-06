package com.umc.miner.src.play;

import com.umc.miner.config.BaseException;
import com.umc.miner.config.BaseResponse;

import com.umc.miner.src.user.UserProvider;

import com.umc.miner.src.play.model.*;
import com.umc.miner.utils.Pagination;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static com.umc.miner.config.BaseResponseStatus.*;


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
     * 맵 공유하기 API - 루시
     * [POST] /playmaps/share
     */
    @ResponseBody
    @PostMapping("/share")
    public BaseResponse<PostMapRes> postMap(@RequestBody PostMapReq postMapReq) {
        try {
            postMapReq.setEditorIdx(userProvider.getEditorIdx(postMapReq.getNickName()));
            postMapReq.setEditorName(postMapReq.getNickName());

            // DB에 active 상태인 맵이 {3개인 경우 = 2개 넘어가는 경우} 부터 공유 불가능
            if (playProvider.countMap(postMapReq) > 2) {
                return new BaseResponse<>(FAILED_TO_SHARE_MAP);
            } else {
                PostMapRes postMapRes = playService.postMap(postMapReq);
                return new BaseResponse<>(postMapRes);
            }

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 공유된 맵 수정 API - 루시
     * [PATCH] /playmaps/modify
     */
    @ResponseBody
    @PatchMapping("/modify")
    public BaseResponse<String> modifyMap(@RequestBody PatchMapReq patchMapReq) {
        try {
            patchMapReq.setEditorIdx(userProvider.getEditorIdx(patchMapReq.getNickName())); //patchMapReq 속 editorIdx에 값 set함
            playService.modifyMap(patchMapReq);

            String result = "공유 수정이 완료되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 맵 공유 중지 API - 루시
     * [PATCH] /playmaps/stop
     */
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    @ResponseBody
    @PatchMapping("/stop")
    public BaseResponse<String> stopShareMap(@RequestBody DelMapReq delMapReq){
        try {
            delMapReq.setEditorIdx(userProvider.getEditorIdx(delMapReq.getNickName())); //delMapReq 속 editorIdx에 값 set함
            int mapIdx = playProvider.getMapIdx(delMapReq);

            playService.stopShareMap(delMapReq);

            // 공유 중지할 맵의 플레이 정보가 있는 경우 : 플레이 정보도 삭제
            if (playProvider.checkPlayTime(mapIdx) == 1) {
                playService.delPlayTime(mapIdx);
            }

            String result = "공유 중지가 완료되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 공유된 맵을 페이징 처리해서 보여주는 API - 서리
     * [POST] /miner/playmaps
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<GetPagingRes> getPlayMap(@RequestBody @NotNull GetPagingReq getPagingReq) {
        try {
            int mapNumPerPage = 4;  // 한 페이지에 4개씩 조회되도록. mapNumPerPage = 한 페이지당 맵의 개수.

            Pagination paging = new Pagination(getPagingReq.getPageNo(), mapNumPerPage);
            paging.pageInfo(playProvider.getTotalNumOfPlayMap(getPagingReq));

            GetPagingRes result = new GetPagingRes(playProvider.getPlayMap(getPagingReq, mapNumPerPage), paging);
            return new BaseResponse(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 검색 결과를 페이징 처리해서 보여주는 API - 서리
     * [POST] /miner/playmaps/search
     */
    @ResponseBody
    @PostMapping("/search")
    public BaseResponse<GetPagingRes> getSearch(@RequestBody @NotNull GetPagingReq getPagingReq) {
        try {
            int mapNumPerPage = 4;  // 한 페이지에 4개씩 조회되도록. mapNumPerPage = 한 페이지당 맵의 개수.

            Pagination paging = new Pagination(getPagingReq.getPageNo(), mapNumPerPage);
            paging.pageInfo(playProvider.getTotalNumOfPlayMap(getPagingReq));

            GetPagingRes result = new GetPagingRes(playProvider.getPlayMap(getPagingReq, mapNumPerPage), paging);
            return new BaseResponse(result);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 맵 배열 관련 정보 가져오는 API - 서리
     * [POST] /miner/playmaps/info
     */
    @ResponseBody
    @PostMapping("/info")
    public BaseResponse<List<GetMapInfoRes>> getMapInfo(@RequestBody GetMapInfoReq getMapInfoReq) {
        try {
            // Req에 관한 맵이 존재하는 맵에 대한 정보인지 확인.
            if (playProvider.checkValidMap(getMapInfoReq) == 0) {
                return new BaseResponse<>(NOT_EXISTS_MAP);
            }

            return new BaseResponse<>(playProvider.getMapInfo(getMapInfoReq));
        } catch (BaseException exception) {
        return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 미로맵 클릭 시 mapPassword, mapSize, user, playTime을 알려준다. - 릴라
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
            List<PlayTimeInfo> playTenTimeInfoList = playProvider.loadTenPlayTimeInfo(postLoadPlayReq);
            postLoadPlayRes.setPlayInfoList(playTenTimeInfoList);

            List<PlayTimeInfo> playTotalTimeInfoList = playProvider.loadTotalPlayTimeInfo(postLoadPlayReq);

            if (playTotalTimeInfoList.size() == 0) {
                postLoadPlayRes.setAvgPlayTime(0);
            } else {

                //PlayTime average
                int totalSec = 0;
                int userCount = 0;
                for (int i = 0; i < playTotalTimeInfoList.size(); i++) {
                    String stringTemp = playTotalTimeInfoList.get(i).getPlayTime().toString();

                    String[] hourMin = stringTemp.split(":");
                    int hour = Integer.parseInt(hourMin[0]);
                    int mins = Integer.parseInt(hourMin[1]);
                    int sec = Integer.parseInt(hourMin[2]);
                    totalSec += (hour * 360) + (mins * 60) + sec;
                    userCount++;
                }
                postLoadPlayRes.setAvgPlayTime(totalSec / userCount);
            }
            return new BaseResponse<>(postLoadPlayRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 정보 비교후 플레이 정보 저장, playCount++ - 릴라
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
                patchSavePlayRes.setRank(playProvider.getRank(patchSavePlayReq));
                // play count ++
                playService.playCount(patchSavePlayReq);
            } else {
                patchSavePlayRes.setPlayerName(playService.updatePlayerInfo(patchSavePlayReq));
                patchSavePlayRes.setRank(playProvider.getRank(patchSavePlayReq));
                // play count ++
                playService.playCount(patchSavePlayReq);
            }
            return new BaseResponse<>(patchSavePlayRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
