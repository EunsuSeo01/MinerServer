package com.umc.miner.src.user;

import com.umc.miner.config.BaseException;
import com.umc.miner.config.secret.Secret;

import static com.umc.miner.config.BaseResponseStatus.*;

import com.umc.miner.src.user.model.*;
import com.umc.miner.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;

    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;
    }


    public void modifyPw(PatchChangePwReq patchChangePwReq) throws BaseException {
        String encodingPw;
        try {
            encodingPw = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(patchChangePwReq.getPassword());
            patchChangePwReq.setPassword(encodingPw);

            int result = userDao.modifyPw(patchChangePwReq); // 해당 과정이 무사히 수행되면 True(1), 그렇지 않으면 False(0)입니다.
            if (result == 0) { // result값이 0이면 과정이 실패한 것이므로 에러 메서지를 보냅니다.
                throw new BaseException(MODIFY_FAIL_PW);
            }

        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // email 중복확인
    public GetEmailRes getEmail(GetEmailReq getEmailReq) throws BaseException {
        if (userProvider.checkEmail(getEmailReq.getEmail()) == 1) {
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }
        throw new BaseException(POST_USERS_AVAILABLE_EMAIL);
    }

    // nickName 중복확인
    public GetNameRes getNickName(GetNameReq getNameReq) throws BaseException {
        if (userProvider.checkNickName(getNameReq.getNickName()) == 1) {
            throw new BaseException(POST_USERS_EXISTS_NAME);
        }
        throw new BaseException(POST_USERS_AVAILABLE_NAME);
    }


    // 회원가입 (POST)
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        // password 암호화
        try {
            String pwd;
            pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postUserReq.getPassword()); // 암호화코드
            postUserReq.setPassword(pwd);
        } catch (Exception ignored) { // 암호화가 실패하였을 경우 에러 발생
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        try {
            int userIdx = userDao.createUser(postUserReq);
            String jwt = jwtService.createJwt(userIdx); //jwt 발급
            return new PostUserRes(userIdx, jwt);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }
}
