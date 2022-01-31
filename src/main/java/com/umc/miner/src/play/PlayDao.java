package com.umc.miner.src.play;


import com.umc.miner.src.play.model.PostLoadPlayReq;

import com.umc.miner.src.play.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PlayDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
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
        String loadPlayTimeQuery = "select userIdx, playTime from PlayTime where mapIdx = ? ";

        return this.jdbcTemplate.query(loadPlayTimeQuery,
                (rs, rowNum) -> new PlayTimeInfo(
                        rs.getInt("userIdx"),
                        rs.getTime("playTime")
                ), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                postLoadPlayReq.getMapIdx()
        );
    }

    // playInfo 저장하기
    public int savePlayInfo(PatchSavePlayReq patchSavePlayReq) {
        System.out.println(patchSavePlayReq.getPlayerIdx() + " 플레이어 ");
        System.out.println(patchSavePlayReq.getMapIdx() + " 맵네임");
        System.out.println(patchSavePlayReq.getPlayTime() + " 왜애애애애애애애애ㅐ");

        String savePlayQuery = "insert into PlayTime (userIdx, mapIdx, playTime) VALUES (?,?,?)";
        Object[] savePlayParams = new Object[]{patchSavePlayReq.getPlayerIdx(), patchSavePlayReq.getMapIdx(), patchSavePlayReq.getPlayTime()};
        this.jdbcTemplate.update(savePlayQuery, savePlayParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    // player 정보가 존재하는지 확인
    public int checkPlayerInfo(PatchSavePlayReq patchSavePlayReq) {
        String checkPlayQuery = "select exists(select userIdx, mapIdx from PlayTime where userIdx = ? AND mapIdx = ?)";
        Object[] checkPlayParams = new Object[]{patchSavePlayReq.getPlayerIdx(), patchSavePlayReq.getMapIdx()};
        return this.jdbcTemplate.queryForObject(checkPlayQuery, int.class, checkPlayParams);
    }

    // player 정보 update
    public int updatePlayerInfo(PatchSavePlayReq patchSavePlayReq) {
        System.out.println(patchSavePlayReq.getPlayerIdx() + " 플레이어 ");
        System.out.println(patchSavePlayReq.getMapIdx() + " 맵네임");
        System.out.println(patchSavePlayReq.getPlayTime() + " 왜애애애애애애애애ㅐ");

        String updatePlayQuery = "update PlayTime set playTime = ? where (userIdx = ?) AND (mapIdx = ?)";
        Object[] updatePlayParams = new Object[]{patchSavePlayReq.getPlayTime(), patchSavePlayReq.getPlayerIdx(), patchSavePlayReq.getMapIdx()};
        return this.jdbcTemplate.update(updatePlayQuery, updatePlayParams);
    }

    // 공유된 맵이 총 몇 개인지 알려준다.
    public int getTotalNumOfPlayMap() {
        String getTotalNumQuery = "select count(mapIdx) from PlayMap where status = ?";
        return this.jdbcTemplate.queryForObject(getTotalNumQuery, int.class, "active");
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
                            rs.getInt("playCount"),
                            rs.getString("status"),
                            rs.getString("createAt"),
                            rs.getString("updateAt")
                    ), "active", mapNumPerPage, (getPagingReq.getPageNo() - 1) * mapNumPerPage);
        }
    }
}
