package com.umc.miner.src.user;

import com.umc.miner.src.sms.SmsService;
import com.umc.miner.src.sms.SmsProvider;
import com.umc.miner.src.sms.model.*;
import com.umc.miner.src.user.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.umc.miner.config.BaseException;
import com.umc.miner.config.BaseResponse;
import com.umc.miner.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.umc.miner.config.BaseResponseStatus.*;
import static com.umc.miner.utils.ValidationRegex.isRegexEmail;
import static com.umc.miner.utils.ValidationRegex.isRegexPassword;

@RestController
@RequestMapping("/miner/users")
public class UserController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final SmsProvider smsProvider;
    @Autowired
    private final SmsService smsService;


    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService, SmsProvider smsProvider, SmsService smsService) {
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
        this.smsProvider = smsProvider;
        this.smsService = smsService;
    }


    /**
     * 로그인 API
     * [POST] /users/logIn
     */
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq) {

        if (postLoginReq.getEmail() == null) {
            return new BaseResponse<>(USERS_EMPTY_USER_EMAIL);
        }


        if (postLoginReq.getStatus() == "inactive") {
            return new BaseResponse<>(USERS_INACTIVE_USER_EMAIL);
        }

        try {
            PostLoginRes postLoginRes = userProvider.logIn(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * 회원가입 API
     * [POST] /users/signup
     */

    @ResponseBody
    @PostMapping("/signup")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        if (postUserReq.getEmail() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }

        // Email 형식
        if (!isRegexEmail(postUserReq.getEmail())) {
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }

        if (postUserReq.getPassword() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
        }

        // Password 형식
        if (!isRegexPassword(postUserReq.getPassword())) {
            return new BaseResponse<>(POST_USERS_INVALID_PASSWORD);
        }

        try {
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * [인증번호 확인 API]
     * 회원가입 때 인증번호 일치하는지 안 하는지 확인하는 API
     * [GET} /miner/signup/auth
     */
    @GetMapping("/signup/auth")
    public BaseResponse<GetAuthRes> checkAuthNum(@RequestBody GetAuthReq getEmailReq) {
        try {
            // 인증번호가 일치하지 않은 경우.
            if (smsProvider.checkAuthNum(getEmailReq) == 0) {
                return new BaseResponse<>(NOT_MATCHED_AUTH);
            }

            // 일치함 -> SmsAuth 테이블에서 row 제거.
            GetAuthRes getAuthRes = new GetAuthRes(smsService.deleteAuth(getEmailReq));
            return new BaseResponse<>(getAuthRes);  // 인증번호 일치하게 작성한 유저 인덱스 리턴.
        } catch(BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * [아이디(=이메일) 찾기]
     * 일치하는 phoneNum이 있는지 확인하는 API
     * [GET] /miner/users/phoneNum
     */
    @GetMapping("/phoneNum")
    public BaseResponse<GetUserIdxRes> postMessage(@RequestBody GetUserIdxReq getUserIdxReq) {
        try {
            String phoneNum = getUserIdxReq.getPhoneNum();
            // DB내에 일치하는 phoneNum이 있는지 확인.
            if (userProvider.checkPhoneNum(phoneNum) == 0) {
                return new BaseResponse<>(NOT_REGISTERED_PHONE_NUMBER);
            }

            // 있으면 그 핸드폰 번호의 유저 인덱스를 가져온다.
            GetUserIdxRes getUserIdxRes = new GetUserIdxRes(userProvider.getUserIdx(phoneNum));
            return new BaseResponse<>(getUserIdxRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * [아이디(=이메일) 찾기]
     * 이메일 보여주는 API
     * 입력한 네자리 숫자가 전송한 인증번호와 같은지 확인 -> 같으면 가려진 이메일을 보여준다.
     * [GET] /miner/users/find-email
     */
    @GetMapping("/find-email")
    public BaseResponse<GetEmailRes> getUserEmail(@RequestBody GetAuthReq getEmailReq) {
        try {
            // 인증번호가 일치하지 않은 경우.
            if (smsProvider.checkAuthNum(getEmailReq) == 0) {
                return new BaseResponse<>(NOT_MATCHED_AUTH);
            }

            // 일치함 -> SmsAuth 테이블에서 row 제거 & 일치하게 입력한 유저 인덱스 리턴.
            int permittedUserIdx = smsService.deleteAuth(getEmailReq);

            // 이메일 가져옴.
            GetEmailRes getEmailRes = new GetEmailRes(userProvider.getUserEmail(permittedUserIdx));
            return new BaseResponse<>(getEmailRes);
        } catch(BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}

