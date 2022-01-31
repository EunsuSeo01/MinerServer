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

    // PlayMap 정보 가져오기 (pw, size)
    public PlayMapInfo loadPlayMapInfo(PostLoadPlayReq postLoadPlayReq) throws BaseException {
        try {
            return playDao.loadPlayMapInfo(postLoadPlayReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // mapIdx로 playTime가져오기
    public List<PlayTimeInfo> loadPlayTimeInfo(PostLoadPlayReq postLoadPlayReq) throws BaseException {
        try {
            return playDao.loadPlayTimeInfo(postLoadPlayReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // save 전 Player정보가 있는지  확인
    public int checkPlayerInfo(PatchSavePlayReq patchSavePlayReq) throws BaseException {
        try {
            return playDao.checkPlayerInfo(patchSavePlayReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // playInfo 저장
    public String savePlayInfo(PatchSavePlayReq patchSavePlayReq) throws BaseException {
        try {
            playDao.savePlayInfo(patchSavePlayReq);
            return patchSavePlayReq.getPlayerName();
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // playInfo update
    public String updatePlayerInfo(PatchSavePlayReq patchSavePlayReq) throws BaseException {
        try {
            playDao.updatePlayerInfo(patchSavePlayReq);
            return patchSavePlayReq.getPlayerName();
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


    public int getMapIdx(int editorIdx, String mapName) throws BaseException {
        try {
            return playDao.getMapIdx(editorIdx, mapName);
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
