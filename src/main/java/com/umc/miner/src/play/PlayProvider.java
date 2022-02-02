package com.umc.miner.src.play;

import com.umc.miner.config.BaseException;
import com.umc.miner.src.play.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.umc.miner.config.BaseResponseStatus.*;

@Service
public class PlayProvider {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PlayDao playDao;

    @Autowired
    public PlayProvider(PlayDao playDao) {
        this.playDao = playDao;
    }

    // 공유 중지할 맵의 플레이정보 있는지 확인
    public int checkPlayTime(int mapIdx) throws BaseException {
        try {
            return playDao.checkPlayTime(mapIdx);
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
            throw new BaseException(FAILED_ACCOUNT);
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

    // save할 때 playCount 업데이트
    public void playCount(PatchSavePlayReq patchSavePlayReq) throws BaseException {
        try {
            playDao.playCount(patchSavePlayReq);
        } catch (Exception exception) {
            throw new BaseException(FAILED_PLAYCOUNT);
        }
    }

    // User의 공유한 맵 개수
    public int countMap(PostMapReq postMapReq) throws BaseException {
        try {
            return playDao.countMap(postMapReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    // 공유된 맵이 총 몇 개인지 알려준다.
    public int getTotalNumOfPlayMap(GetPagingReq getPagingReq) throws BaseException {
        try {
            if (getPagingReq.getSearchContent() != null) {
                return playDao.getSearchedNumOfPlayMap(getPagingReq);
            } else {
                return playDao.getTotalNumOfPlayMap();
            }
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
            if (getPagingReq.getSearchContent() != null) {
                return playDao.getSearchPlayMap(getPagingReq, mapNumPerPage);
            }
            return playDao.getPlayMap(getPagingReq, mapNumPerPage);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
