package com.umc.miner.src.user;

import com.umc.miner.config.BaseException;
import com.umc.miner.config.BaseResponse;
import com.umc.miner.src.user.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.umc.miner.src.sms.SmsService;
import com.umc.miner.src.sms.SmsProvider;
import com.umc.miner.src.sms.model.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import static com.umc.miner.config.BaseResponseStatus.*;
import static com.umc.miner.utils.ValidationRegex.isRegexEmail;
import static com.umc.miner.utils.ValidationRegex.isRegexPassword;
import static com.umc.miner.utils.ValidationRegex.isRegexNickName;

@RestController
@RequestMapping("/miner/users")
public class UserController {

    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log를 남기기: 일단은 모르고 넘어가셔도 무방합니다.


    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final SmsProvider smsProvider;
    @Autowired
    private final SmsService smsService;


    public UserController(UserProvider userProvider, UserService userService, SmsProvider smsProvider, SmsService smsService) {
        this.userProvider = userProvider;
        this.userService = userService;
        this.smsProvider = smsProvider;
        this.smsService = smsService;
    }

    /**
     * 로그인 API - 루시
     * [POST] /users/logIn
     */
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq) {
        try {
            if (postLoginReq.getEmail() == null) {
                return new BaseResponse<>(USERS_EMPTY_USER_EMAIL);
            }

            // 이메일 확인
            String email = postLoginReq.getEmail();
            if (userProvider.getUser(email) == 0) {
                return new BaseResponse<>(FAILED_TO_LOGIN);
            }

            // 계정 활성화 확인
            if (postLoginReq.getStatus() == "inactive") {
                return new BaseResponse<>(USERS_INACTIVE_USER_EMAIL);
            }

            PostLoginRes postLoginRes = userProvider.logIn(postLoginReq);
            return new BaseResponse<>(postLoginRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 이메일 중복확인 API - 루시
     * [POST] /miner/users/email
     */
    @ResponseBody
    @PostMapping("/email")
    public BaseResponse checkEmail(@RequestBody GetEmailReq getEmailReq) {
        if (getEmailReq.getEmail() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }

        // Email 형식
        if (!isRegexEmail(getEmailReq.getEmail())) {
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }

        try {
            GetEmailRes getEmailRes = userService.getEmail(getEmailReq);
            return new BaseResponse<>(getEmailRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 닉네임 중복확인 API - 루시
     * [POST] /miner/users/name
     */
    @ResponseBody
    @PostMapping("/name")
    public BaseResponse checkNickName(@RequestBody GetNameReq getNameReq) {
        if (getNameReq.getNickName() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_NAME);
        }

        // NickName 형식
        if (!isRegexNickName(getNameReq.getNickName())) {
            return new BaseResponse<>(POST_USERS_INVALID_NAME);
        }

        try {
            GetNameRes getNameRes = userService.getNickName(getNameReq);
            return new BaseResponse<>(getNameRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 회원가입 API - 루시
     * [POST] /users/signup
     */
    @ResponseBody
    @PostMapping("/signup")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
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
     * [인증번호 확인 API] - 서리
     * 회원가입 때 인증번호 일치하는지 안 하는지 확인하는 API
     * [POST] /miner/signup/auth
     */
    @ResponseBody
    @PostMapping("/signup/auth")
    public BaseResponse<String> checkAuthNum(@RequestBody GetAuthReq getAuthReq) {
        try {
            // 인증번호가 일치하지 않은 경우.
            if (smsProvider.checkRightAuthNum(getAuthReq) == 0) {
                return new BaseResponse<>(NOT_MATCHED_AUTH);
            }

            // 일치함 -> SmsAuth 테이블에서 row 제거.
            smsService.deleteRightAuth(getAuthReq);
            return new BaseResponse<>("인증이 완료되었습니다.");
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * [아이디(=이메일) 찾기] - 서리
     * 가입된 phoneNum인지 확인하는 API -> 문자 인증을 가입할 때랑 아이디 찾기 모두에서 쓰기 때문에 구분을 위해 분리해놓은 것.
     * [POST] /miner/users/phoneNum
     */
    @ResponseBody
    @PostMapping("/phoneNum")
    public BaseResponse<GetUserIdxRes> checkPhoneNum(@RequestBody GetUserIdxReq getUserIdxReq) {
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
     * [아이디(=이메일) 찾기] - 서리
     * 이메일 보여주는 API
     * 입력한 네자리 숫자가 전송한 인증번호와 같은지 확인 -> 같으면 가려진 이메일을 보여준다.
     * [POST] /miner/users/find-email
     */
    @Transactional
    @ResponseBody
    @PostMapping("/find-email")
    public BaseResponse<GetEmailRes> getUserEmail(@RequestBody GetAuthReq getAuthReq) {
        try {
            // 인증번호가 일치하지 않은 경우.
            if (smsProvider.checkRightAuthNum(getAuthReq) == 0) {
                return new BaseResponse<>(NOT_MATCHED_AUTH);
            }

            // 일치함 -> SmsAuth 테이블에서 row 제거 & 일치하게 입력한 유저 인덱스 리턴.
            smsService.deleteRightAuth(getAuthReq);
            String permittedPhoneNum = getAuthReq.getPhoneNum();

            // 이메일 가져옴.
            GetEmailRes getEmailRes = new GetEmailRes(userProvider.getUserEmail(permittedPhoneNum));
            return new BaseResponse<>(getEmailRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 비밀번호 찾기 API - 릴라
     * [POST] /users/findPw
     */
    @ResponseBody
    @PostMapping("/findPw")
    public BaseResponse<PostFindPwRes> findPw(@RequestBody PostFindPwReq postFindPwReq) {
        try {
            String userEmail = postFindPwReq.getEmail();

            // 빈칸일 때
            if (userEmail == null) {
                return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
            }
            // 정규화 형식 아닐 때
            if (!isRegexEmail(userEmail)) {
                return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
            }
            // DB에 이메일이 없을 때
            if (userProvider.checkEmail(userEmail) == 0) {
                return new BaseResponse<>(POST_USERS_NOT_EXISTS_EMAIL);
            }

            PostFindPwRes postFindPwRes = new PostFindPwRes(userProvider.eGetUserIdx(userEmail));
            return new BaseResponse<>(postFindPwRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * 비밀번호 변경 API - 릴라
     * [PATCH] /users/modifyPw
     */
    @ResponseBody
    @PatchMapping("/modifyPw")
    public BaseResponse<String> modifyPw(@RequestBody PatchChangePwReq patchChangePwReq) {
        try {
            String pw = patchChangePwReq.getPassword();

            // 빈칸일 시
            if (pw == null) {
                return new BaseResponse<>(PATCH_USERS_EMPTY_EMAIL);
            }

            // 비밀번호 정규식
            if (!isRegexPassword(pw)) {
                return new BaseResponse<>(PATCH_USERS_INVALID_PASSWORD);
            }

            userService.modifyPw(patchChangePwReq);
            String result = "비밀번호가 변경되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}

