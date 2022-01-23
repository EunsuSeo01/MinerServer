package com.umc.miner.src.user;

import com.umc.miner.src.sms.SmsService;
import com.umc.miner.src.sms.SmsProvider;
import com.umc.miner.src.sms.model.GetAuthReq;
import com.umc.miner.src.user.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.umc.miner.config.BaseException;
import com.umc.miner.config.BaseResponse;
import com.umc.miner.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.umc.miner.config.BaseResponseStatus.*;

@RestController // Rest API 또는 WebAPI를 개발하기 위한 어노테이션. @Controller + @ResponseBody 를 합친것.
// @Controller      [Presentation Layer에서 Contoller를 명시하기 위해 사용]
//  [Presentation Layer?] 클라이언트와 최초로 만나는 곳으로 데이터 입출력이 발생하는 곳
//  Web MVC 코드에 사용되는 어노테이션. @RequestMapping 어노테이션을 해당 어노테이션 밑에서만 사용할 수 있다.
// @ResponseBody    모든 method의 return object를 적절한 형태로 변환 후, HTTP Response Body에 담아 반환.
@RequestMapping("/miner/users")
// method가 어떤 HTTP 요청을 처리할 것인가를 작성한다.
// 요청에 대해 어떤 Controller, 어떤 메소드가 처리할지를 맵핑하기 위한 어노테이션
// URL(/app/users)을 컨트롤러의 메서드와 매핑할 때 사용
/**
 * Controller란?
 * 사용자의 Request를 전달받아 요청의 처리를 담당하는 Service, Prodiver 를 호출
 */
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

            // 일치함 -> SmsAuth 테이블에서 row 제거 & 이메일 알려준다.
            smsService.deleteAuth(getEmailReq);

            // 이메일 가져옴.
            GetEmailRes getEmailRes = new GetEmailRes(userProvider.getUserEmail(getEmailReq.getUserIdx()));
            return new BaseResponse<>(getEmailRes);
        } catch(BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}

