package com.umc.miner.src.user;

import com.umc.miner.config.BaseException;
import com.umc.miner.config.BaseResponse;
import com.umc.miner.config.BaseResponseStatus;
import com.umc.miner.src.user.model.PatchChangePwReq;
import com.umc.miner.src.user.model.PatchChangePwRes;
import com.umc.miner.src.user.model.PostFindPwReq;
import com.umc.miner.src.user.model.PostFindPwRes;
import com.umc.miner.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.umc.miner.config.BaseResponseStatus.*;
import static com.umc.miner.utils.ValidationRegex.isRegexEmail;
import static com.umc.miner.utils.ValidationRegex.isRegexPw;

@Controller
@RequestMapping("/miner/users")
public class UserController {

    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log를 남기기: 일단은 모르고 넘어가셔도 무방합니다.

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;


    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService) {
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * 비밀번호 찾기 API
     * [POST] /users/findPw
     */
    @ResponseBody
    @PostMapping("/findPw")
    public BaseResponse<PostFindPwRes> findPw(@RequestBody PostFindPwReq postFindPwReq) {
        try {
            // TODO: 로그인 값들에 대한 형식적인 validatin 처리해주셔야합니다.
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

            PostFindPwRes postFindPwRes = new PostFindPwRes(userProvider.getUserIdx(userEmail));
            return new BaseResponse<>(postFindPwRes);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 비밀번호 변경 API
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
            if (!isRegexPw(pw)) {
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
