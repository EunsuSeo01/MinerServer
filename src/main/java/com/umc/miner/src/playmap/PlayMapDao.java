package com.umc.miner.src.playmap;

import com.umc.miner.src.playmap.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PlayMapDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    // 공유된 맵이 총 몇 개인지 알려준다.
    public int getTotalNumOfPlayMap() {
        String getTotalNumQuery = "select count(mapIdx) from PlayMap where status = ?";
        return this.jdbcTemplate.queryForObject(getTotalNumQuery, int.class,"active");
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
