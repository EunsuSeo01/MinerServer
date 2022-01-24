package com.umc.miner.src.user;

import com.umc.miner.src.user.model.PatchChangePwReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired //readme 참고
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 이메일 확인
    public int checkEmail(String email) {
        String checkEmailQuery = "select exists(select email from User where email = ?)"; // User Table에 해당 email 값을 갖는 유저 정보가 존재하는가?
        String checkEmailParams = email; // 해당(확인할) 이메일 값
        return this.jdbcTemplate.queryForObject(checkEmailQuery, int.class, checkEmailParams);
        // checkEmailQuery, checkEmailParams를 통해 가져온 값(intgud)을 반환한다. -> 쿼리문의 결과(존재하지 않음(False,0),존재함(True, 1))를 int형(0,1)으로 반환됩니다.
    }

    // Email -> user idx 가져오기
    public int EGetUserIdx(String email) {
        String getUserIdxQuery = "select userIdx from User where email = ?";
        return this.jdbcTemplate.queryForObject(getUserIdxQuery, int.class, email);
    }

    // 비밀번호 변경
    public int modifyPw(PatchChangePwReq patchChangePwReq) {
        String modifyPwQuery = "update User set password = ? where userIdx = ? "; // 해당 userIdx를 만족하는 User를 해당 nickname으로 변경한다.
        Object[] modifyPwParams = new Object[]{patchChangePwReq.getPassword(), patchChangePwReq.getUserIdx()};

        return this.jdbcTemplate.update(modifyPwQuery, modifyPwParams); // 대응시켜 매핑시켜 쿼리 요청(생성했으면 1, 실패했으면 0)
    }

}
