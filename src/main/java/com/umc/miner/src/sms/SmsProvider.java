package com.umc.miner.src.sms;

import com.umc.miner.config.BaseException;
import com.umc.miner.src.sms.model.GetAuthReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.umc.miner.config.BaseResponseStatus.*;

@Service
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
