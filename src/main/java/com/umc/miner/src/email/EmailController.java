package com.umc.miner.src.email;

import com.umc.miner.config.BaseException;
import com.umc.miner.config.BaseResponse;
import com.umc.miner.src.email.model.*;
import com.umc.miner.src.user.UserProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.umc.miner.config.BaseResponseStatus.*;
import static com.umc.miner.utils.ValidationRegex.isRegexEmail;

@Controller
@RequestMapping("/miner/email")
public class EmailController {
    private final EmailService emailService;
    private final UserProvider userProvider;
    private final EmailProvider emailProvider;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public EmailController(EmailService emailService, UserProvider userProvider, EmailProvider emailProvider) {
        this.emailService = emailService;
        this.userProvider = userProvider;
        this.emailProvider = emailProvider;
    }

    /**
     * Email 인증번호 전송 API - 릴라
     * [POST] /email/emailSend
     */
    @ResponseBody
    @PostMapping("/emailSend")
    public BaseResponse<PostAuthNumRes> sendMail(@RequestBody Email email) {
        try {
            // 이메일 null
            if (email.getAddress() == null) {
                return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
            }

            // 정규화 형식 아닐 때
            if (!isRegexEmail(email.getAddress())) {
                return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
            }

            // REQUEST 담기
            PostAuthNumReq postAuthNumReq = new PostAuthNumReq();
            postAuthNumReq.setUserIdx(userProvider.eGetUserIdx(email.getAddress()));

            // RESPONSE(AuthNum DB에 임시저장) -> userIdx
            PostAuthNumRes postAuthNumRes = new PostAuthNumRes(emailService.saveEmailAuthNum(postAuthNumReq));

            // 메일 전송
            this.emailService.mailSend(email);

            return new BaseResponse<>(postAuthNumRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 인증번호 비교 API - 릴라
     * [POST] /email/compareAuth
     */
    @ResponseBody
    @PostMapping("/compareAuth")
    public BaseResponse<PostCompareAuthRes> compareAuth(@RequestBody PostCompareAuthReq postCompareAuthReq) {
        try {
            // 인증번호 빈칸
            if (postCompareAuthReq.getAuthNum() == null || postCompareAuthReq.getUserIdx() == 0) {
                return new BaseResponse<>(POST_EMAIL_EMPTY_EMAIL);
            }
            // 인증번호 비교 틀렸을 때
            if (emailProvider.compareAuth(postCompareAuthReq) == 0) {
                return new BaseResponse<>(POST_EMAIL_FAIL_EMAIL);
            }

            PostCompareAuthRes postCompareAuthRes = new PostCompareAuthRes(emailProvider.deleteAuth(postCompareAuthReq));
            return new BaseResponse<>(postCompareAuthRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
