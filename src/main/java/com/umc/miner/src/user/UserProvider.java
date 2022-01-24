package com.umc.miner.src.user;

import com.umc.miner.config.BaseException;
import com.umc.miner.src.user.model.PostFindPwReq;
import com.umc.miner.src.user.model.PostFindPwRes;
import com.umc.miner.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.umc.miner.config.BaseResponseStatus.DATABASE_ERROR;
import static com.umc.miner.config.BaseResponseStatus.USERS_NOT_EXISTS_EMAIL;

@Service
public class UserProvider {
    private final UserDao userDao;
    private final JwtService jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired //readme 참고
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    // 해당 이메일이 이미 User Table에 존재하는지 확인
    public int checkEmail(String email) throws BaseException {
        try {
            return userDao.checkEmail(email);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int getUserIdx(String email) throws BaseException {
        try {
            return userDao.EGetUserIdx(email);
        } catch (Exception exception) {
            throw new BaseException(USERS_NOT_EXISTS_EMAIL);
        }
    }
}
