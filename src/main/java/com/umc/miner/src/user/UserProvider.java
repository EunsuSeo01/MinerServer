package com.umc.miner.src.user;

import com.umc.miner.config.BaseException;
import com.umc.miner.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.umc.miner.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service    // [Business Layer에서 Service를 명시하기 위해서 사용] 비즈니스 로직이나 respository layer 호출하는 함수에 사용된다.
// [Business Layer]는 컨트롤러와 데이터 베이스를 연결
/**
 * Provider란?
 * Controller에 의해 호출되어 실제 비즈니스 로직과 트랜잭션을 처리: Read의 비즈니스 로직 처리
 * 요청한 작업을 처리하는 관정을 하나의 작업으로 묶음
 * dao를 호출하여 DB CRUD를 처리 후 Controller로 반환
 */
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    // 핸드폰 번호 가입여부 확인.
    public int checkPhoneNum(String phoneNum) throws BaseException {
        try {
            return userDao.checkPhoneNum(phoneNum);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 유저 인덱스 가져옴.
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

