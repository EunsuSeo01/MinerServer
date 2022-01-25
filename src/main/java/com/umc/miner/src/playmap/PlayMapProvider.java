package com.umc.miner.src.playmap;

import com.umc.miner.config.BaseException;
import com.umc.miner.src.playmap.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.umc.miner.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class PlayMapProvider {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PlayMapDao playMapDao;

    @Autowired
    public PlayMapProvider(PlayMapDao playMapDao) {
        this.playMapDao = playMapDao;
    }

    // 공유된 맵이 총 몇 개인지 알려준다.
    public int getTotalNumOfPlayMap() throws BaseException {
        try {
            return playMapDao.getTotalNumOfPlayMap();
        } catch(Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 공유된 맵들을 페이징 처리해서 보여준다.
    public List<GetPlayMapRes> getPlayMap(GetPagingReq getPagingReq, int mapNumPerPage) throws BaseException {
        try {
            return playMapDao.getPlayMap(getPagingReq, mapNumPerPage);
        } catch(Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    
}
