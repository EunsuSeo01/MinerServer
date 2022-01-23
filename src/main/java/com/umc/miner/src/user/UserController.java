package com.umc.miner.src.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.umc.miner.config.BaseException;
import com.umc.miner.config.BaseResponse;
import com.umc.miner.src.user.model.*;
import com.umc.miner.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService) {
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
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
}

