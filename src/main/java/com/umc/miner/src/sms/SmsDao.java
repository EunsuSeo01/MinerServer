package com.umc.miner.src.sms;

import com.umc.miner.src.sms.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class SmsDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 인증문자 관련 정보를 테이블에 추가한다.
    public void postSmsAuth(PostSmsAuthReq postSmsAuthReq) {
        String postSmsAuthQuery = "insert into SmsAuth (phoneNum, authNum) values (?,?)";
        Object[] postSmsAuthParams = new Object[]{
                postSmsAuthReq.getPhoneNum(), postSmsAuthReq.getAuthNum()
        };

        this.jdbcTemplate.update(postSmsAuthQuery, postSmsAuthParams);
    }

    // 인증번호가 일치하는지 확인.
    public int checkRightAuthNum(GetAuthReq getAuthReq) {
        String checkAuthNumQuery = "select exists(select authIdx from SmsAuth where phoneNum = ? AND authNum = ?)";

        return this.jdbcTemplate.queryForObject(checkAuthNumQuery, int.class,
                getAuthReq.getPhoneNum(), getAuthReq.getAuthNum()); // 쿼리문의 결과(존재하지 않음(False,0),존재함(True, 1))를 int형(0,1)으로 반환됩니다.
    }

    // 이전에 수신받은 인증번호가 있는지 확인.
    public int checkPrevAuthNum(String phoneNum) {
        String checkAuthNumQuery = "select exists(select authIdx from SmsAuth where phoneNum = ?)";

        return this.jdbcTemplate.queryForObject(checkAuthNumQuery, int.class,
                phoneNum); // 쿼리문의 결과(존재하지 않음(False,0),존재함(True, 1))를 int형(0,1)으로 반환됩니다.
    }

    // SmsAuth 테이블에서 인증된 row 제거.
    public void deleteRightAuth(GetAuthReq getAuthReq) {
        String deleteAuthQuery = "delete from SmsAuth where phoneNum = ? and authNum = ?";
        this.jdbcTemplate.update(deleteAuthQuery, getAuthReq.getPhoneNum(), getAuthReq.getAuthNum());
    }

    // 완료되지 못한, 이전의 인증번호 정보들을 SmsAuth 테이블에서 제거.
    public void deletePrevAuth(String phoneNum) {
        String deleteAuthQuery = "delete from SmsAuth where phoneNum = ?";
        this.jdbcTemplate.update(deleteAuthQuery, phoneNum);
    }

}
