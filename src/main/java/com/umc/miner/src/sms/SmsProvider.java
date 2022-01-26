package com.umc.miner.src.sms;

import com.umc.miner.config.BaseException;
import com.umc.miner.src.sms.model.GetAuthReq;
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
public class SmsProvider {
    private final SmsDao smsDao;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public SmsProvider(SmsDao smsDao) {
        this.smsDao = smsDao;
    }

    // 인증번호 일치 여부 확인.
    public int checkRightAuthNum(GetAuthReq getAuthReq) throws BaseException {
        try {
            return smsDao.checkRightAuthNum(getAuthReq);
        } catch(Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 이전에 수신받은 인증번호가 있는지 확인.
    public int checkPrevAuthNum(String phoneNum) throws BaseException {
        try {
            return smsDao.checkPrevAuthNum(phoneNum);
        } catch(Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
