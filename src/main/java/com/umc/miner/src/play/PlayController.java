package com.umc.miner.src.play;

import com.umc.miner.config.BaseException;
import com.umc.miner.config.BaseResponse;

import com.umc.miner.src.play.model.PostLoadPlayReq;
import com.umc.miner.src.play.model.PostLoadPlayRes;
import com.umc.miner.src.user.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/miner/play")
public class PlayController {

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
     * @param postLoadPlayReq
     * @return
     */
    @ResponseBody
    @PostMapping("/loadPlayInfo")
    public BaseResponse<String> loadPlayInfo(@RequestBody PostLoadPlayReq postLoadPlayReq) {
        PostLoadPlayRes postLoadPlayRes = new PostLoadPlayRes();

        try {
//            int gegegege = playProvider.loadPlayInfo(postLoadPlayReq);
//            System.out.println(gegegege + "어떻게 나오나용");

            // 빈칸일 시 -> 그럴일은 없을 듯 하긴 함
            if(postLoadPlayReq.getMapName() == null || postLoadPlayReq.getEditorName() == null){

            }
            // userIdx set
            postLoadPlayReq.setEditorIdx(userProvider.getEditorIdx(postLoadPlayReq.getEditorName()));

            // mapIdx가 없을 때
            if(playProvider.getMapIdx(postLoadPlayReq) == 0){

            }
            // mapIdx set
            postLoadPlayReq.setMapIdx(playProvider.getMapIdx(postLoadPlayReq));







            // playTimeIdx로 playMap을 찾을 수 없는 거
//            if (playProvider.loadPlayInfo(postLoadPlayReq) == 0) {
//
//            }

            String result = "쿠쿠루 삥뽕빵";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
