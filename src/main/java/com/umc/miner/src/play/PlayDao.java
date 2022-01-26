package com.umc.miner.src.play;

import com.umc.miner.src.play.model.*;
import com.umc.miner.src.user.UserDao;
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

    private final UserDao userDao;

    @Autowired
    public PlayDao(UserDao userDao) {
        this.userDao = userDao;
    }

    // 공유된 맵이 총 몇 개인지 알려준다.
    public int getTotalNumOfPlayMap() {
        String getTotalNumQuery = "select count(mapIdx) from PlayMap where status = ?";
        return this.jdbcTemplate.queryForObject(getTotalNumQuery, int.class,"active");
    }

    // 검색 조건에 맞는 맵이 총 몇 개인지 알려준다.
    public int getSearchedNumOfPlayMap(GetPagingReq getPagingReq) {
        String getSearchedNumQuery;
        // 미로명 검색.
        if (getPagingReq.getSearchType() == 1) {
            getSearchedNumQuery = "select count(mapIdx) from PlayMap where status = ? and mapName = ?";
            return this.jdbcTemplate.queryForObject(getSearchedNumQuery, int.class,"active", getPagingReq.getSearchContent());
        }
        // 닉네임명 검색.
        else {
            getSearchedNumQuery = "select count(mapIdx) from PlayMap where status = ? and editorIdx = ?";
            return this.jdbcTemplate.queryForObject(getSearchedNumQuery, int.class,"active", userDao.getEditorIdx(getPagingReq.getSearchContent()));
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

    // 공유된 맵들을 페이징 처리해서 보여준다. -> 공유 중지된 맵은 조회 X.
    public List<GetPlayMapRes> getSearchPlayMap(GetPagingReq getPagingReq, int mapNumPerPage) {
        // 최신순 정렬.
        if (getPagingReq.getOrderType() == 1) {
            // 닉네임명 검색.
            if (getPagingReq.getSearchType() == 0) {
                return this.jdbcTemplate.query("select * from PlayMap where status = ? and editorIdx = ? order by updateAt desc limit ? offset ?",
                        (rs, rowNum) -> new GetPlayMapRes(
                                rs.getString("mapName"),
                                rs.getString("mapInfo"),
                                rs.getInt("mapSize"),
                                rs.getInt("mapPassword"),
                                rs.getInt("playCount"),
                                rs.getString("status"),
                                rs.getString("createAt"),
                                rs.getString("updateAt")
                        ), "active", userDao.getEditorIdx(getPagingReq.getSearchContent()), mapNumPerPage, (getPagingReq.getPageNo() - 1) * mapNumPerPage);
            }
            // 미로명 검색.
            else {
                return this.jdbcTemplate.query("select * from PlayMap where status = ? and mapName = ? order by updateAt desc limit ? offset ?",
                        (rs, rowNum) -> new GetPlayMapRes(
                                rs.getString("mapName"),
                                rs.getString("mapInfo"),
                                rs.getInt("mapSize"),
                                rs.getInt("mapPassword"),
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
                return this.jdbcTemplate.query("select * from PlayMap where status = ? and editorIdx = ? order by playCount desc limit ? offset ?",
                        (rs, rowNum) -> new GetPlayMapRes(
                                rs.getString("mapName"),
                                rs.getString("mapInfo"),
                                rs.getInt("mapSize"),
                                rs.getInt("mapPassword"),
                                rs.getInt("playCount"),
                                rs.getString("status"),
                                rs.getString("createAt"),
                                rs.getString("updateAt")
                        ), "active", userDao.getEditorIdx(getPagingReq.getSearchContent()), mapNumPerPage, (getPagingReq.getPageNo() - 1) * mapNumPerPage);
            }
            // 미로명 검색.
            else {
                return this.jdbcTemplate.query("select * from PlayMap where status = ? and mapName = ? order by playCount desc limit ? offset ?",
                        (rs, rowNum) -> new GetPlayMapRes(
                                rs.getString("mapName"),
                                rs.getString("mapInfo"),
                                rs.getInt("mapSize"),
                                rs.getInt("mapPassword"),
                                rs.getInt("playCount"),
                                rs.getString("status"),
                                rs.getString("createAt"),
                                rs.getString("updateAt")
                        ), "active", getPagingReq.getSearchContent(), mapNumPerPage, (getPagingReq.getPageNo() - 1) * mapNumPerPage);
            }

        }
    }
}
