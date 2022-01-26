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


    public int loadPlayInfo(PostLoadPlayReq postLoadPlayReq) {
        String loadPlayMapQuery = "select mapPassword, mapSize from playMap where mapIdx = ? "; // User Table에 해당 email 값을 갖는 유저 정보가 존재하는가?
        return this.jdbcTemplate.queryForObject(loadPlayMapQuery, int.class, postLoadPlayReq.getMapIdx()); // checkEmailQuery, checkEmailParams를 통해 가져온 값(intgud)을 반환한다. -> 쿼리문의 결과(존재하지 않음(False,0),존재함(True, 1))를 int형(0,1)으로 반환됩니다.
    }

    public int getMapIdx(PostLoadPlayReq postLoadPlayReq) {
        String getNickIdxQuery = "select mapIdx from PlayMap where editroIdx = ? AND mapName = ?";
        return this.jdbcTemplate.queryForObject(getNickIdxQuery, int.class, postLoadPlayReq.getEditorIdx(), postLoadPlayReq.getMapName());
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
