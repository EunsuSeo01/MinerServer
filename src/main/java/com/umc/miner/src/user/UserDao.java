package com.umc.miner.src.user;

import com.umc.miner.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 1. 로그인 ?email=
    public User getPwd(PostLoginReq postLoginReq) {
        String getPwdQuery = "select userIdx, email, password, nickName, status from User where email = ?"; // 해당 email을 만족하는 User의 정보들을 조회한다.
        String getPwdParams = postLoginReq.getEmail(); // 주입될 email값을 클라이언트의 요청에서 주어진 정보를 통해 가져온다.

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs, rowNum) -> new User(
                        rs.getInt("userIdx"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("nickName"),
                        rs.getString("status")
                ), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                getPwdParams
        ); // 한 개의 회원정보를 얻기 위한 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }

    // 이메일 중복확인
    public int checkEmail(String email) {
        String checkEmailQuery = "select exists(select email from User where email = ?)"; // User Table에 해당 email 값을 갖는 유저 정보가 존재하는가?
        String checkEmailParams = email; // 해당(확인할) 이메일 값
        return this.jdbcTemplate.queryForObject(checkEmailQuery, int.class, checkEmailParams);
    }

    // 닉네임 중복확인
    public int checkNickName(String nickName) {
        String checkNickNameQuery = "select exists(select nickName from User where nickName  = ?)"; // User Table에 해당 nickName 값을 갖는 유저 정보가 존재하는가?
        String checkNickNameParams = nickName; // 해당(확인할) 닉네임 값
        return this.jdbcTemplate.queryForObject(checkNickNameQuery, int.class, checkNickNameParams);
    }

    // 회원가입
    public int createUser(PostUserReq postUserReq) {
        String createUserQuery = "insert into User (email, password, phoneNum, nickName, isChecked) VALUES (?,?,?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getEmail(), postUserReq.getPassword(), postUserReq.getPhoneNum(), postUserReq.getNickName(), postUserReq.getIsChecked()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
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
