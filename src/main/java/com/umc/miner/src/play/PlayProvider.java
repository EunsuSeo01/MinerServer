package com.umc.miner.src.play;

import com.umc.miner.config.BaseException;

import static com.umc.miner.config.BaseResponseStatus.*;
import com.umc.miner.src.play.model.*;

import com.umc.miner.utils.JwtService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayProvider {
    private final PlayDao playDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public PlayProvider(PlayDao playDao, JwtService jwtService) {
        this.playDao = playDao;
        this.jwtService = jwtService;
    }

    // 설계 map 가져오기
    public int postMap(PostMapReq postMapReq) throws BaseException {
        try {

            return playDao.postMap(postMapReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 공유 수정
    public int modifyMap(PatchMapReq patchMapReq) throws BaseException {
        try {
            return playDao.modifyMap(patchMapReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    // 공유 중지
    public int stopShareMap(PatchMapReq patchMapReq) throws BaseException {
        try {
            return playDao.stopShareMap(patchMapReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }



}
