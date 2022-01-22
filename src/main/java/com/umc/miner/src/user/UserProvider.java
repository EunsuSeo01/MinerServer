package com.umc.miner.src.user;

import com.umc.miner.config.BaseException;
import com.umc.miner.config.secret.Secret;
import com.umc.miner.src.user.model.*;
import com.umc.miner.utils.AES128;
import com.umc.miner.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import static com.umc.miner.config.BaseResponseStatus.*;

@Service

public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired //readme 참고
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    // 해당 이메일이 이미 User 테이블에 존재하는지 확인
    public int checkEmail(String email) throws BaseException {
        try {
            return userDao.checkEmail(email);
        } catch (Exception exception) {
            throw new BaseException(UNEXPECTED_ERROR);
        }
    }

    // 해당 닉네임이 이미 User 테이블에 존재하는지 확인
    public int checkNickName(String nickName) throws BaseException {
        try {
            return userDao.checkNickName(nickName);
        } catch (Exception exception) {
            throw new BaseException(UNEXPECTED_ERROR);
        }
    }
}
