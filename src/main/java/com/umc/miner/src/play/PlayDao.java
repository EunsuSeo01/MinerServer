package com.umc.miner.src.play;

import com.umc.miner.src.play.model.PostLoadPlayReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

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

    public int getMapIdx(PostLoadPlayReq postLoadPlayReq){
        String getNickIdxQuery = "select mapIdx from PlayMap where editroIdx = ? AND mapName = ?";
        return this.jdbcTemplate.queryForObject(getNickIdxQuery, int.class, postLoadPlayReq.getEditorIdx(), postLoadPlayReq.getMapName());
    }
}
