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
import org.springframework.web.bind.annotation.*;

import static com.umc.miner.config.BaseResponseStatus.FAILED_TO_SHARE_MAP;

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
     * 맵 공유하기 API
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
            }

            else {
                PostMapRes postMapRes = playService.postMap(postMapReq);
                return new BaseResponse<>(postMapRes);
            }

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 공유된 맵 수정 API
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
     * 맵 공유 중지 API
     * [PATCH] /playmaps/stop
     */
    @ResponseBody
    @PatchMapping("/stop")
    public BaseResponse<String> stopShareMap(@RequestBody DelMapReq delMapReq) {
        try {
            delMapReq.setEditorIdx(userProvider.getEditorIdx(delMapReq.getNickName())); //delMapReq 속 editorIdx에 값 set함

            playService.stopShareMap(delMapReq);

            String result = "공유 삭제가 완료되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
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
            paging.pageInfo(playProvider.getTotalNumOfPlayMap(getPagingReq));

            GetPagingRes result = new GetPagingRes(playProvider.getPlayMap(getPagingReq, mapNumPerPage), paging);
            return new BaseResponse(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 검색 결과를 페이징 처리해서 보여주는 API
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
     * @param postLoadPlayReq
     * @return
     */
    /*@ResponseBody
    @PostMapping("/loadPlayInfo")
    public BaseResponse<String> loadPlayInfo(@RequestBody PostLoadPlayReq postLoadPlayReq) {
        PostLoadPlayRes postLoadPlayRes = new PostLoadPlayRes();

        try {
            int gegegege = playProvider.loadPlayInfo(postLoadPlayReq);
            System.out.println(gegegege + "어떻게 나오나용");

            // 빈칸일 시 -> 그럴일은 없을 듯 하긴 함
            if (postLoadPlayReq.getMapName() == null || postLoadPlayReq.getEditorName() == null) {

            }
            // userIdx set
            postLoadPlayReq.setEditorIdx(userProvider.getEditorIdx(postLoadPlayReq.getEditorName()));

            // mapIdx가 없을 때
            if (playProvider.getMapIdx(postLoadPlayReq) == 0) {

            }
            // mapIdx set
            postLoadPlayReq.setMapIdx(playProvider.getMapIdx(postLoadPlayReq));


            // playTimeIdx로 playMap을 찾을 수 없는 거
            if (playProvider.loadPlayInfo(postLoadPlayReq) == 0) {

            }

            String result = "쿠쿠루 삥뽕빵";
            return new BaseResponse<>(result);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    */
}
