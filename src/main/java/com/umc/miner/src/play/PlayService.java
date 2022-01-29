package com.umc.miner.src.play;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.umc.miner.config.BaseException;
import com.umc.miner.src.play.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import static com.umc.miner.config.BaseResponseStatus.*;

@Service
public class PlayService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PlayDao playDao;
    private final PlayProvider playProvider;

    @Autowired
    public PlayService(PlayDao playDao, PlayProvider playProvider) {

        this.playDao = playDao;
        this.playProvider = playProvider;
    }

    // 공유
    public PostMapRes postMap(PostMapReq postMapReq) throws BaseException {
        try {
            int mapIdx = playDao.postMap(postMapReq);
            return new PostMapRes(mapIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 공유 수정
    public void modifyMap(PatchMapReq patchStatusReq) throws BaseException {
        try {
            int result = playDao.modifyMap(patchStatusReq);
            if (result == 0) {
                throw new BaseException(FAILED_TO_MODIFY_SHARED_MAP);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    // 공유 중지
    public void stopShareMap(PatchMapReq patchStatusReq) throws BaseException {
        try {
            int result = playDao.stopShareMap(patchStatusReq);
            if (result == 0) {
                throw new BaseException(FAILED_TO_DELETE_SHARED_MAP);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
