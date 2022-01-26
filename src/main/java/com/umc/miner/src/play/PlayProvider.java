package com.umc.miner.src.play;

import com.umc.miner.config.BaseException;

import com.umc.miner.src.play.model.PostLoadPlayReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.umc.miner.src.play.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;

import static com.umc.miner.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class PlayProvider {

    final Logger logger = LoggerFactory.getLogger(this.getClass());


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

    // 공유된 맵이 총 몇 개인지 알려준다.
    public int getTotalNumOfPlayMap() throws BaseException {
        try {
            return playDao.getTotalNumOfPlayMap();
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

    // 공유된 맵들을 페이징 처리해서 보여준다.
    public List<GetPlayMapRes> getPlayMap(GetPagingReq getPagingReq, int mapNumPerPage) throws BaseException {
        try {
            return playDao.getPlayMap(getPagingReq, mapNumPerPage);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
