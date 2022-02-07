package com.umc.miner.src.sms;

import com.umc.miner.config.BaseException;
import com.umc.miner.config.BaseResponse;
import com.umc.miner.src.sms.model.*;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

import static com.umc.miner.config.BaseResponseStatus.*;
import static com.umc.miner.utils.ValidationRegex.isRegexPhoneNum;


@RestController
@RequestMapping("/miner/sms")
public class SmsController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SmsProvider smsProvider;
    private final SmsService smsService;

    @Autowired
    public SmsController(SmsProvider smsProvider, SmsService smsService) {
        this.smsProvider = smsProvider;
        this.smsService = smsService;
    }

    /**
     * [인증번호 전송]: CoolSMS 사용.ver - 서리
     * 인증문자 전송 & 해당 인증번호 저장 API
     * [POST] /miner/sms
     */
    @PostMapping("")
    @Transactional
    public BaseResponse<String> sendSms(@RequestBody SmsReq smsReq) {
        try {
            String recipientPhoneNum = smsReq.getPhoneNum();

            // 핸드폰 번호 형식 확인. 010XXXXXXXX
            if (!isRegexPhoneNum(recipientPhoneNum)) {
                return new BaseResponse<>(INVALID_PHONENUM);
            }

            // 난수 4자리 생성 -> 인증번호.
            Random rand = new Random();
            String authNum = "";
            for (int i = 0; i < 4; i++) {
                String ran = Integer.toString(rand.nextInt(10));
                authNum += ran;
            }

            // 문자 전송.
            JSONObject sendRes = smsService.sendSms(recipientPhoneNum, authNum);

            // error 있으면 실패 메세지 띄우기.
            if (!sendRes.get("error_count").toString().equals("0")) {
                System.out.println("ERROR COUNT: " + sendRes.get("error_count").toString().getClass().getSimpleName());
                throw new Exception();
            }

            // 확인.
            System.out.println("수신자 번호 : " + recipientPhoneNum);
            System.out.println("인증번호 : " + authNum);

            try {
                // 해당 전화번호의 유저가 이전에 수신받은 인증번호가 있는지 확인.
                if (smsProvider.checkPrevAuthNum(recipientPhoneNum) == 1) {
                    // 있으면 이전에 보내진 인증번호니까 지운다.
                    smsService.deletePrevAuth(recipientPhoneNum);
                }

                // DB에 전송한 인증번호랑 문자를 받은 유저 인덱스 저장.
                PostSmsAuthReq postSmsAuthReq = new PostSmsAuthReq(recipientPhoneNum, authNum);
                smsService.postSmsAuth(postSmsAuthReq);
            } catch(BaseException exception) {
                return new BaseResponse<>((exception.getStatus()));
            }

            return new BaseResponse<>("메세지 전송이 완료되었습니다.");
        } catch (Exception e) {
            return new BaseResponse<>(FAILED_TO_MSG);
        }
    }

}
