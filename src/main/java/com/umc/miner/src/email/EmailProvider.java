package com.umc.miner.src.email;

import com.umc.miner.config.BaseException;
import com.umc.miner.src.email.model.PostCompareAuthReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.umc.miner.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class EmailProvider {

    private final EmailDao emailDao;


    @Autowired
    public EmailProvider(EmailDao emailDao) {
        this.emailDao = emailDao;
    }

    public int compareAuth(PostCompareAuthReq postCompareAuthReq) throws BaseException {
        try {
            return emailDao.compareAuth(postCompareAuthReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int deleteAuth(PostCompareAuthReq postCompareAuthReq) throws BaseException {
        try {
            System.out.println(emailDao.deleteAuth(postCompareAuthReq) + "뭐하냐");
            return emailDao.deleteAuth(postCompareAuthReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
