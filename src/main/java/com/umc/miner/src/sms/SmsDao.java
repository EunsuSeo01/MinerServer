package com.umc.miner.src.sms;

import com.umc.miner.src.sms.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository //  [Persistence Layer에서 DAO를 명시하기 위해 사용]

/**
 * DAO란?
 * 데이터베이스 관련 작업을 전담하는 클래스
 * 데이터베이스에 연결하여, 입력 , 수정, 삭제, 조회 등의 작업을 수행
 */
public class SmsDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 인증문자 관련 정보를 테이블에 추가한다.
    public int postSmsAuth(PostSmsAuthReq postSmsAuthReq) {
        String postSmsAuthQuery = "insert into SmsAuth (userIdx, authNum) values (?,?)";
        Object[] postSmsAuthParams = new Object[]{
                postSmsAuthReq.getUserIdx(), postSmsAuthReq.getAuthNum()
        };

        this.jdbcTemplate.update(postSmsAuthQuery, postSmsAuthParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    // 인증번호가 일치하는지 확인.
    public int checkAuthNum(GetAuthReq getAuthReq) {
        String checkAuthNumQuery = "select exists(select authIdx from SmsAuth where userIdx = ? AND authNum = ?)";

        return this.jdbcTemplate.queryForObject(checkAuthNumQuery, int.class,
                getAuthReq.getUserIdx(), getAuthReq.getAuthNum()); // 쿼리문의 결과(존재하지 않음(False,0),존재함(True, 1))를 int형(0,1)으로 반환됩니다.
    }

    // SmsAuth 테이블에서 인증된 row 제거.
    public void deleteAuth(GetAuthReq getAuthReq) {
        String deleteAuthQuery = "delete from SmsAuth where userIdx = ? and authNum = ?";
        this.jdbcTemplate.update(deleteAuthQuery, getAuthReq.getUserIdx(), getAuthReq.getAuthNum());
    }
}
