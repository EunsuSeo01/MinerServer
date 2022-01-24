package com.umc.miner.src.user;


import com.umc.miner.config.BaseException;
import com.umc.miner.config.secret.Secret;
import com.umc.miner.src.user.model.PostUserReq;
import com.umc.miner.src.user.model.PostUserRes;
import com.umc.miner.utils.AES128;
import com.umc.miner.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import static com.umc.miner.config.BaseResponseStatus.*;


@Service

public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;

    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;
    }

<<<<<<< Updated upstream
        // 회원가입 (POST)
        public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
            // email 중복 확인
            if (userProvider.checkEmail(postUserReq.getEmail()) == 1) {
                throw new BaseException(POST_USERS_EXISTS_EMAIL);
            }

            // nickName 중복 확인
            if (userProvider.checkNickName(postUserReq.getNickName()) == 1) {
                throw new BaseException(POST_USERS_EXISTS_NICKNAME);
            }

            // password 암호화
            try {
                String pwd;
                pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postUserReq.getPassword()); // 암호화코드
                postUserReq.setPassword(pwd);
            } catch (Exception ignored) { // 암호화가 실패하였을 경우 에러 발생
                throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
            }

            try {
                int userIdx = userDao.createUser(postUserReq);
                String jwt = jwtService.createJwt(userIdx); //jwt 발급
                return new PostUserRes(userIdx, jwt);
            }catch (Exception exception) {
                throw new BaseException(DATABASE_ERROR);
            }
=======
    // email 중복확인
    public GetEmailRes getEmail(GetEmailReq getEmailReq) throws BaseException {
        if (userProvider.checkEmail(getEmailReq.getEmail()) == 1) {
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }
        throw new BaseException(POST_USERS_AVAILABLE_EMAIL);
    }

    // nickName 중복확인
    public GetNameRes getNickName(GetNameReq getNameReq) throws BaseException {
        if (userProvider.checkNickName(getNameReq.getNickName()) == 1) {
            throw new BaseException(POST_USERS_EXISTS_NAME);
        }
        throw new BaseException(POST_USERS_AVAILABLE_NAME);
    }


    // 회원가입 (POST)
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        // password 암호화
        try {
            String pwd;
            pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postUserReq.getPassword()); // 암호화코드
            postUserReq.setPassword(pwd);
        } catch (Exception ignored) { // 암호화가 실패하였을 경우 에러 발생
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        try {
            int userIdx = userDao.createUser(postUserReq);
            String jwt = jwtService.createJwt(userIdx); //jwt 발급
            return new PostUserRes(userIdx, jwt);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
>>>>>>> Stashed changes
        }

}