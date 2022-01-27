package com.umc.miner.src.play;

import com.umc.miner.src.play.model.*;
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

    // 설계한 맵 플레이로 공유
    public int postMap(PostMapReq postMapReq) {
        String postMapQuery = "insert into PlayMap (mapName, mapInfo, mapSize, mapPassword, editorIdx, playCount) VALUES (?,?,?,?,?,?)";
        Object[] postMapParams = new Object[]{postMapReq.getMapName(), postMapReq.getMapInfo(), postMapReq.getMapSize(), postMapReq.getMapPassword(), postMapReq.getEditorIdx(), postMapReq.getPlayCount()};
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

    // 공유 중지 (status 변경)
    public int stopShareMap(PatchMapReq patchMapReq) {
        String stopShareMapQuery = "update PlayMap set status 'inactive' where (editorIdx = ?) and (mapName = ?)";
        Object[] stopShareMapParams = new Object[]{patchMapReq.getEditorIdx(), patchMapReq.getMapName()};

        return this.jdbcTemplate.update(stopShareMapQuery, stopShareMapParams);
    }

}

