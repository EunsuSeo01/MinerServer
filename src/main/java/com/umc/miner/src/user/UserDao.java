package com.umc.miner.src.user;

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
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 핸드폰 번호 가입여부 확인.
    public int checkPhoneNum(String phoneNum) {
        String checkPhoneNumQuery = "select exists(select userIdx from User where phoneNum = ?)"; // User Table에 해당 phoneNum를 갖는 유저가 존재하는가?

        return this.jdbcTemplate.queryForObject(checkPhoneNumQuery, int.class, phoneNum); // 쿼리문의 결과(존재하지 않음(False,0),존재함(True, 1))를 int형(0,1)으로 반환됩니다.
    }

    // 유저 인덱스 전달.
    public int getUserIdx(String phoneNum) {
        String getUserIdxQuery = "select userIdx from User where phoneNum = ?";
        return this.jdbcTemplate.queryForObject(getUserIdxQuery, int.class, phoneNum);
    }

    // 이메일 알려준다.
    public String getUserEmail(int userIdx) {
        String getEmailQuery = "select email from User where userIdx = ?";
        return this.jdbcTemplate.queryForObject(getEmailQuery, String.class, userIdx);
    }

}

