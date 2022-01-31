package com.umc.miner.src.play;

import com.umc.miner.config.BaseException;
import com.umc.miner.src.play.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    // User의 공유한 맵 개수
    public int countMap(PostMapReq postMapReq) throws BaseException {
        try {
            return playDao.countMap(postMapReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // mapIdx 찾기
    public int getMapIdx(DelMapReq delMapReq) throws BaseException {
        try {
            return playDao.getMapIdx(delMapReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    // 공유된 맵이 총 몇 개인지 알려준다.
    public int getTotalNumOfPlayMap(GetPagingReq getPagingReq) throws BaseException {
        try {
            if (getPagingReq.getSearchContent() != null) {
                return playDao.getSearchedNumOfPlayMap(getPagingReq);
            }
            else {
                return playDao.getTotalNumOfPlayMap();
            }
        } catch(Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 공유된 맵들을 페이징 처리해서 보여준다.
    public List<GetPlayMapRes> getPlayMap(GetPagingReq getPagingReq, int mapNumPerPage) throws BaseException {
        try {
            if (getPagingReq.getSearchContent() != null) {
                return playDao.getSearchPlayMap(getPagingReq, mapNumPerPage);
            }

            return playDao.getPlayMap(getPagingReq, mapNumPerPage);
        } catch(Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    
}
