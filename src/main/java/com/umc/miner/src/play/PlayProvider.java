package com.umc.miner.src.play;

import com.umc.miner.config.BaseException;
import com.umc.miner.src.play.model.PostLoadPlayReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.umc.miner.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class PlayProvider {
    private final PlayDao playDao;

    @Autowired
    public PlayProvider(PlayDao playDao) {
        this.playDao = playDao;
    }


    // DB에서 Play Time 정보 가져오기이이
    // try -catch를 통해서 filtering 처리 가능한듯
    public int loadPlayInfo(PostLoadPlayReq postLoadPlayReq) throws BaseException {
        int temp = playDao.loadPlayInfo(postLoadPlayReq);
        try {
            // if temp가 어쩌구저쩌구 ㄱㄱ
            return temp;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int getMapIdx(PostLoadPlayReq postLoadPlayReq) throws BaseException {
        try {
            return playDao.getMapIdx(postLoadPlayReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
