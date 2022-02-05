package com.umc.miner.src.play;

import com.umc.miner.src.play.model.*;

import com.umc.miner.src.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PlayDao {
    private JdbcTemplate jdbcTemplate;
    private final UserDao userDao;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public PlayDao(UserDao userDao) {
        this.userDao = userDao;
    }


    // userIdx, mapName으로 mapIdx가져오기
    public int getMapIdx(int editorIdx, String mapName) {
        String getNickIdxQuery = "select mapIdx from PlayMap where editorIdx = ? AND mapName = ?";
        return this.jdbcTemplate.queryForObject(getNickIdxQuery, int.class, editorIdx, mapName);
    }

    // 맵 정보 불러오기
    public PlayMapInfo loadPlayMapInfo(PostLoadPlayReq postLoadPlayReq) {
        String loadPlayMapQuery = "select mapPassword, mapSize from PlayMap where mapIdx = ? ";

        return this.jdbcTemplate.queryForObject(loadPlayMapQuery,
                (rs, rowNum) -> new PlayMapInfo(
                        rs.getInt("mapPassword"),
                        rs.getInt("mapSize")
                ), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                postLoadPlayReq.getMapIdx()
        );
    }

    // PlayTimeInfo 불러오기
    public List<PlayTimeInfo> loadPlayTimeInfo(PostLoadPlayReq postLoadPlayReq) {
        String loadPlayTimeQuery = "select userIdx, playerName, playTime from PlayTime where mapIdx = ? ";

        return this.jdbcTemplate.query(loadPlayTimeQuery,
                (rs, rowNum) -> new PlayTimeInfo(
                        rs.getInt("userIdx"),
                        rs.getString("playerName"),
                        rs.getTime("playTime")
                ), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                postLoadPlayReq.getMapIdx()
        );
    }

    // playInfo 저장하기
    public int savePlayInfo(PatchSavePlayReq patchSavePlayReq) {
        String savePlayQuery = "insert into PlayTime (userIdx, playerName, mapIdx, playTime) VALUES (?,?,?,?)";
        Object[] savePlayParams = new Object[]{patchSavePlayReq.getPlayerIdx(), patchSavePlayReq.getPlayerName(), patchSavePlayReq.getMapIdx(), patchSavePlayReq.getPlayTime()};
        this.jdbcTemplate.update(savePlayQuery, savePlayParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }


    // 설계 -> 플레이 공유
    public int postMap(PostMapReq postMapReq) {
        String postMapQuery = "insert into PlayMap (mapName, mapInfo, mapSize, mapPassword, editorIdx, editorName, playCount) VALUES (?,?,?,?,?,?,?)";
        Object[] postMapParams = new Object[]{postMapReq.getMapName(), postMapReq.getMapInfo(), postMapReq.getMapSize(), postMapReq.getMapPassword(), postMapReq.getEditorIdx(), postMapReq.getEditorName(), postMapReq.getPlayCount()};
        this.jdbcTemplate.update(postMapQuery, postMapParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    // 공유 수정 (mapInfo 변경) - body에 mapInfo, nickName, mapName 입력
    public int modifyMap(PatchMapReq patchMapReq) {
        String modifyMapQuery = "update PlayMap set mapInfo = ? where (editorIdx = ?) and (mapName = ?)";
        Object[] modifyMapParams = new Object[]{patchMapReq.getMapInfo(), patchMapReq.getEditorIdx(), patchMapReq.getMapName()};

        return this.jdbcTemplate.update(modifyMapQuery, modifyMapParams);
    }

    // 공유 중지 (DB에서 맵 삭제)
    public int stopShareMap(DelMapReq delMapReq) {
        String stopShareMapQuery = "delete from PlayMap where (editorIdx = ?) and (mapName = ?)";
        Object[] stopShareMapParams = new Object[]{delMapReq.getEditorIdx(), delMapReq.getMapName()};

        return this.jdbcTemplate.update(stopShareMapQuery, stopShareMapParams);
    }

    // mapIdx 찾기
    public int getMapIdx(DelMapReq delMapReq) {
        String getMapIdxQuery = "select mapIdx from PlayMap where (editorIdx = ?) and (mapName = ?)";
        Object[] getMapIdxParams = new Object[]{delMapReq.getEditorIdx(), delMapReq.getMapName()};

        return this.jdbcTemplate.queryForObject(getMapIdxQuery, int.class, getMapIdxParams);
    }

    // 공유 중지할 맵 플레이정보 존재 확인
    public int checkPlayTime(int mapIdx) {
        String checkPlayTimeQuery = "select exists(select playTimeIdx from PlayTime where mapIdx = ?)";
        return this.jdbcTemplate.queryForObject(checkPlayTimeQuery, int.class, mapIdx);
    }

    // 공유 중지한 맵 플레이정보 삭제
    public int delPlayTime(int mapIdx) {
        String delPlayTimeQuery = "delete from PlayTime where mapIdx = ?";
        return this.jdbcTemplate.update(delPlayTimeQuery, mapIdx);
    }


    // player 정보가 존재하는지 확인
    public int checkPlayerInfo(PatchSavePlayReq patchSavePlayReq) {
        String checkPlayQuery = "select exists(select userIdx, mapIdx from PlayTime where userIdx = ? AND mapIdx = ?)";
        Object[] checkPlayParams = new Object[]{patchSavePlayReq.getPlayerIdx(), patchSavePlayReq.getMapIdx()};
        return this.jdbcTemplate.queryForObject(checkPlayQuery, int.class, checkPlayParams);
    }

    // player 정보 update
    public int updatePlayerInfo(PatchSavePlayReq patchSavePlayReq) {
        String updatePlayQuery = "update PlayTime set playTime = ? where (userIdx = ?) AND (mapIdx = ?)";
        Object[] updatePlayParams = new Object[]{patchSavePlayReq.getPlayTime(), patchSavePlayReq.getPlayerIdx(), patchSavePlayReq.getMapIdx()};
        return this.jdbcTemplate.update(updatePlayQuery, updatePlayParams);
    }

    // playCount update
    public void playCount(PatchSavePlayReq patchSavePlayReq) {
        String countPlayQuery = "update PlayMap set playCount = playCount + 1 where mapIdx = ?";
        Object[] countPlayParams = new Object[]{patchSavePlayReq.getMapIdx()};
        this.jdbcTemplate.update(countPlayQuery, countPlayParams);
    }

    // 각 유저가 공유했던 맵 개수 세기
    public int countMap(PostMapReq postMapReq) {
        String countMapQuery = "select count(case when editorIdx = ? and status='active' then 1 end) from PlayMap";
        int countMapParams = postMapReq.getEditorIdx();
        return this.jdbcTemplate.queryForObject(countMapQuery, int.class, countMapParams);

    }

    // 공유된 맵이 총 몇 개인지 알려준다.
    public int getTotalNumOfPlayMap() {
        String getTotalNumQuery = "select count(mapIdx) from PlayMap where status = ?";
        return this.jdbcTemplate.queryForObject(getTotalNumQuery, int.class, "active");
    }

    // 검색 조건에 맞는 맵이 총 몇 개인지 알려준다.
    public int getSearchedNumOfPlayMap(GetPagingReq getPagingReq) {
        String getSearchedNumQuery;
        // 미로명 검색.
        if (getPagingReq.getSearchType() == 1) {
            getSearchedNumQuery = "select count(mapIdx) from PlayMap where status = ? and mapName = ?";
            return this.jdbcTemplate.queryForObject(getSearchedNumQuery, int.class, "active", getPagingReq.getSearchContent());
        }
        // 닉네임명 검색.
        else {
            getSearchedNumQuery = "select count(mapIdx) from PlayMap where status = ? and editorName = ?";
            return this.jdbcTemplate.queryForObject(getSearchedNumQuery, int.class, "active", getPagingReq.getSearchContent());
        }

    }

    // 공유된 맵들을 페이징 처리해서 보여준다. -> 공유 중지된 맵은 조회 X.
    public List<GetPlayMapRes> getPlayMap(GetPagingReq getPagingReq, int mapNumPerPage) {
        // 최신순 정렬.
        if (getPagingReq.getOrderType() == 1) {
            return this.jdbcTemplate.query("select * from PlayMap where status = ? order by updateAt desc limit ? offset ?",
                    (rs, rowNum) -> new GetPlayMapRes(
                            rs.getString("mapName"),
                            rs.getString("mapInfo"),
                            rs.getInt("mapSize"),
                            rs.getInt("mapPassword"),
                            rs.getString("editorName"),
                            rs.getInt("playCount"),
                            rs.getString("status"),
                            rs.getString("createAt"),
                            rs.getString("updateAt")
                    ), "active", mapNumPerPage, (getPagingReq.getPageNo() - 1) * mapNumPerPage);
        }
        // 인기순 정렬.
        else {
            return this.jdbcTemplate.query("select * from PlayMap where status = ? order by playCount desc limit ? offset ?",
                    (rs, rowNum) -> new GetPlayMapRes(
                            rs.getString("mapName"),
                            rs.getString("mapInfo"),
                            rs.getInt("mapSize"),
                            rs.getInt("mapPassword"),
                            rs.getString("editorName"),
                            rs.getInt("playCount"),
                            rs.getString("status"),
                            rs.getString("createAt"),
                            rs.getString("updateAt")
                    ), "active", mapNumPerPage, (getPagingReq.getPageNo() - 1) * mapNumPerPage);
        }
    }

    // 공유된 맵들을 페이징 처리해서 보여준다. -> 공유 중지된 맵은 조회 X.
    public List<GetPlayMapRes> getSearchPlayMap(GetPagingReq getPagingReq, int mapNumPerPage) {
        // 최신순 정렬.
        if (getPagingReq.getOrderType() == 1) {
            // 닉네임명 검색.
            if (getPagingReq.getSearchType() == 0) {
                return this.jdbcTemplate.query("select * from PlayMap where status = ? and editorName = ? order by updateAt desc limit ? offset ?",
                        (rs, rowNum) -> new GetPlayMapRes(
                                rs.getString("mapName"),
                                rs.getString("mapInfo"),
                                rs.getInt("mapSize"),
                                rs.getInt("mapPassword"),
                                rs.getString("editorName"),
                                rs.getInt("playCount"),
                                rs.getString("status"),
                                rs.getString("createAt"),
                                rs.getString("updateAt")
                        ), "active", getPagingReq.getSearchContent(), mapNumPerPage, (getPagingReq.getPageNo() - 1) * mapNumPerPage);
            }
            // 미로명 검색.
            else {
                return this.jdbcTemplate.query("select * from PlayMap where status = ? and mapName = ? order by updateAt desc limit ? offset ?",
                        (rs, rowNum) -> new GetPlayMapRes(
                                rs.getString("mapName"),
                                rs.getString("mapInfo"),
                                rs.getInt("mapSize"),
                                rs.getInt("mapPassword"),
                                rs.getString("editorName"),
                                rs.getInt("playCount"),
                                rs.getString("status"),
                                rs.getString("createAt"),
                                rs.getString("updateAt")
                        ), "active", getPagingReq.getSearchContent(), mapNumPerPage, (getPagingReq.getPageNo() - 1) * mapNumPerPage);
            }

        }
        // 인기순 정렬.
        else {
            // 닉네임명 검색.
            if (getPagingReq.getSearchType() == 0) {
                return this.jdbcTemplate.query("select * from PlayMap where status = ? and editorName = ? order by playCount desc limit ? offset ?",
                        (rs, rowNum) -> new GetPlayMapRes(
                                rs.getString("mapName"),
                                rs.getString("mapInfo"),
                                rs.getInt("mapSize"),
                                rs.getInt("mapPassword"),
                                rs.getString("editorName"),
                                rs.getInt("playCount"),
                                rs.getString("status"),
                                rs.getString("createAt"),
                                rs.getString("updateAt")
                        ), "active", getPagingReq.getSearchContent(), mapNumPerPage, (getPagingReq.getPageNo() - 1) * mapNumPerPage);
            }
            // 미로명 검색.
            else {
                return this.jdbcTemplate.query("select * from PlayMap where status = ? and mapName = ? order by playCount desc limit ? offset ?",
                        (rs, rowNum) -> new GetPlayMapRes(
                                rs.getString("mapName"),
                                rs.getString("mapInfo"),
                                rs.getInt("mapSize"),
                                rs.getInt("mapPassword"),
                                rs.getString("editorName"),
                                rs.getInt("playCount"),
                                rs.getString("status"),
                                rs.getString("createAt"),
                                rs.getString("updateAt")
                        ), "active", getPagingReq.getSearchContent(), mapNumPerPage, (getPagingReq.getPageNo() - 1) * mapNumPerPage);
            }

        }
    }

    // Req에 관한 맵이 존재하는 맵에 대한 정보인지 확인.
    public int checkValidMap(GetMapInfoReq getMapInfoReq) {
        String checkValidMapQuery = "select exists(select mapIdx from PlayMap where editorName = ? AND mapName = ?)";
        Object[] checkValidMapParams = new Object[]{getMapInfoReq.getMapName(), getMapInfoReq.getMapName()};
        return this.jdbcTemplate.queryForObject(checkValidMapQuery, int.class, checkValidMapParams);
    }

    // 맵 배열 정보 & 사이즈 정보를 가져온다.
    public List<GetMapInfoRes> getMapInfo(GetMapInfoReq getMapInfoReq) {
        return this.jdbcTemplate.query("select mapInfo, mapSize from PlayMap where editorName = ? AND mapName = ?",
                (rs, rowNum) -> new GetMapInfoRes(
                        rs.getString("mapInfo"),
                        rs.getInt("mapSize")
                ), getMapInfoReq.getEditorName(), getMapInfoReq.getMapName());
    }
}
