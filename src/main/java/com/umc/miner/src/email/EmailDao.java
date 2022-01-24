package com.umc.miner.src.email;

import com.umc.miner.src.email.model.PostAuthNumReq;
import com.umc.miner.src.email.model.PostCompareAuthReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class EmailDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired //readme 참고
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 인증문자 관련 정보를 테이블에 추가한다.
    public int saveEmailAuthNum(PostAuthNumReq postAuthNumReq) {
        String postEmailAuthQuery = "insert into EmailAuthNum (userIdx, emailAuthNum) values (?,?)";
        Object[] postEmailAuthParams = new Object[]{
                postAuthNumReq.getUserIdx(), postAuthNumReq.getEmailAuthNum()
        };

        this.jdbcTemplate.update(postEmailAuthQuery, postEmailAuthParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int compareAuth(PostCompareAuthReq postCompareAuthReq) {
        String compareAuthQuery = "select exists(select authIdx from EmailAuthNum where emailAuthNum = ? )";
        return this.jdbcTemplate.queryForObject(compareAuthQuery, int.class, postCompareAuthReq.getAuthNum()); // 쿼리문의 결과(존재하지 않음(False,0),존재함(True, 1))를 int형(0,1)으로 반환됩니다.
    }

    public int deleteAuth(PostCompareAuthReq postCompareAuthReq) {
        String deleteAuthQuery = "delete from EmailAuthNum where userIdx = ? and emailAuthNum = ?";
        this.jdbcTemplate.update(deleteAuthQuery, postCompareAuthReq.getUserIdx(), postCompareAuthReq.getAuthNum());
        return postCompareAuthReq.getUserIdx();
    }
}
