package com.umc.miner.src.user;

import com.umc.miner.config.BaseException;

import com.umc.miner.config.secret.Secret;
import static com.umc.miner.config.BaseResponseStatus.*;
import com.umc.miner.src.user.model.*;
import com.umc.miner.utils.AES128;

import com.umc.miner.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




@Service
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    // 1. 로그인 (password 검사)
    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException {
        User user = userDao.getPwd(postLoginReq);
        String password;
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(user.getPassword()); // 암호화
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if (postLoginReq.getPassword().equals(password)) { //비말번호가 일치한다면 userIdx를 가져온다.
            int userIdx = userDao.getPwd(postLoginReq).getUserIdx();
            String jwt = jwtService.createJwt(userIdx);
            String nickName = userDao.getPwd(postLoginReq).getNickName();
            return new PostLoginRes(userIdx, jwt, nickName);

        } else { // 비밀번호가 다르다면 에러메세지를 출력한다.
            throw new BaseException(FAILED_TO_LOGIN);
        }
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


    // 핸드폰 번호 가입여부 확인.
    public int checkPhoneNum(String phoneNum) throws BaseException {
        try {
            return userDao.checkPhoneNum(phoneNum);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 이메일 USERIDX 가져오기
    public int eGetUserIdx(String email) throws BaseException {
        try {
            return userDao.EGetUserIdx(email);
        } catch (Exception exception) {
            throw new BaseException(USERS_NOT_EXISTS_EMAIL);
        }
    }

    // PhoneNum 유저 인덱스 가져옴.
    public int getUserIdx(String phoneNum) throws BaseException {
        try {
            return userDao.getUserIdx(phoneNum);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 이메일 가져옴 -> 이메일 가려줌.
    public String getUserEmail(int userIdx) throws BaseException {
        try {
            return hideEmailByAsterisk(userDao.getUserEmail(userIdx));
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // Asterisk로 이메일 앞 뒤 가리기.
    private static final String HIDE_CHAR = "*";

    public static String hideEmailByAsterisk(String email) {
        int atSignIndex = email.indexOf("@");

        StringBuilder stringBuilder = new StringBuilder(email);
        String emailName = email.substring(0, atSignIndex);
        String emailRoar = email.substring(atSignIndex + 1, email.length());

        stringBuilder.replace(2, atSignIndex, makeAsterisk(emailName.length()));
        stringBuilder.replace(atSignIndex + 3, email.length(), makeAsterisk(emailRoar.length()));

        return stringBuilder.toString();
    }

    // 필요한 만큼 Asterisk 만들기.
    public static String makeAsterisk(int length) {

        String addStr = "";

        if (length > 4) {
            for (int i = 0; i < length - 2; i++) {
                addStr += HIDE_CHAR;
            }
            return addStr;
        }
        else {
            for (int i = 0; i < 3; i++) {
                addStr += HIDE_CHAR;
            }
            return addStr;
        }
    }
}
