package com.umc.miner.src.play;


import com.umc.miner.config.BaseException;
import com.umc.miner.config.BaseResponse;
import com.umc.miner.src.user.UserProvider;
import com.umc.miner.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.umc.miner.src.play.model.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/miner/playmaps")
public class PlayController {
    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log를 남기기: 일단은 모르고 넘어가셔도 무방합니다.


    @Autowired
    private final PlayProvider playProvider;
    @Autowired
    private final PlayService playService;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final UserProvider userProvider;

    public PlayController(PlayProvider playProvider, PlayService playService, JwtService jwtService, UserProvider userProvider) {
        this.playProvider = playProvider;
        this.playService = playService;
        this.jwtService = jwtService;
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
            PostMapRes postMapRes = playService.postMap(postMapReq);
            return new BaseResponse<>(postMapRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 공유된 맵 수정 API
     * [PATCH] /playmaps/modify
     */
    @ResponseBody
    @PostMapping("/modify")
    public BaseResponse<String> modifyMap(@RequestBody PatchMapReq patchMapReq) {
        try {
            patchMapReq.setEditorIdx(userProvider.getEditorIdx(patchMapReq.getNickName())); //patchStatusReq 속 editorIdx에 값 set함
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
    public BaseResponse<String> stopShareMap(@RequestBody PatchMapReq patchMapReq) {
        try {
            patchMapReq.setEditorIdx(userProvider.getEditorIdx(patchMapReq.getNickName())); //patchStatusReq 속 editorIdx에 값 set함
            playService.stopShareMap(patchMapReq);

            String result = "공유 삭제가 완료되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
