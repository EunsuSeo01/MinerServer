package com.umc.miner.src.sms;

import com.umc.miner.config.BaseException;
import com.umc.miner.src.sms.model.*;
import net.nurigo.java_sdk.api.Message;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;

import static com.umc.miner.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@Transactional
public class SmsService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SmsDao smsDao;

    @Autowired
    public SmsService(SmsDao smsDao) {
        this.smsDao = smsDao;
    }

    @Value("${sms.api_key}")
    private String api_key;
    @Value("${sms.api_secret}")
    private String api_secret;
    @Value("${sms.senderPhoneNum}")
    private String senderPhoneNum;


    // 인증문자 전송.
    public JSONObject sendSms(String recipientPhoneNum, String authNum) throws BaseException {
        try {
            Message coolsms = new Message(api_key, api_secret);
        
            // 4 params(to, from, type, text) are mandatory. must be filled
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("to", recipientPhoneNum);    // 수신전화번호
            params.put("from", senderPhoneNum);    // 발신전화번호.
            params.put("type", "SMS");
            params.put("text", "[Miner] 인증번호는 " + authNum + "입니다.");
            params.put("app_version", "miner 1.0.1"); // application name and version

            JSONObject obj = coolsms.send(params);
            System.out.println(obj.toString());
            return obj;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    // 인증문자 정보 저장.
    public void postSmsAuth(PostSmsAuthReq postSmsAuthReq) throws BaseException {
        try {
            smsDao.postSmsAuth(postSmsAuthReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 인증번호 일치함 -> SmsAuth 테이블에서 row 제거.
    public void deleteRightAuth(GetAuthReq getAuthReq) throws BaseException {
        try {
            smsDao.deleteRightAuth(getAuthReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 인증번호 일치 여부 확인 전에 인증번호를 더 수신받았을 때 이전의 인증번호 정보들을 SmsAuth 테이블에서 제거.
    public void deletePrevAuth(String phoneNum) throws BaseException {
        try {
            smsDao.deletePrevAuth(phoneNum);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
