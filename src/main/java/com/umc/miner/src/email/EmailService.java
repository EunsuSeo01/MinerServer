package com.umc.miner.src.email;

import com.umc.miner.config.BaseException;
import com.umc.miner.src.email.model.Email;
import com.umc.miner.src.email.model.PostAuthNumReq;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

import static com.umc.miner.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@AllArgsConstructor
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    private static final String FROM_ADDRESS = "miner20212022@gmail.com";
    private final EmailDao emailDao;
    private final EmailProvider emailProvider;

    private String randomAuthNum;

    @Autowired
    public EmailService(EmailDao emailDao, EmailProvider emailProvider) {
        this.emailDao = emailDao;
        this.emailProvider = emailProvider;
    }


    public void mailSend(Email email) {
        SimpleMailMessage message = new SimpleMailMessage();


        String emailTitle = "[Miner] 비밀번호 변경";
        String emailMessage = "인증번호는 " + randomAuthNum + " 입니다";

        message.setTo(email.getAddress());
        message.setFrom(EmailService.FROM_ADDRESS);
        message.setSubject(emailTitle);
        message.setText(emailMessage);

        mailSender.send(message);
    }

    public int saveEmailAuthNum(PostAuthNumReq postAuthNumReq) throws BaseException {
        try {
            randomAuthNum = makeRandomAuthNum();
            postAuthNumReq.setEmailAuthNum(randomAuthNum);
            emailDao.saveEmailAuthNum(postAuthNumReq);
            return postAuthNumReq.getUserIdx();
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String makeRandomAuthNum() {
        Random rd = new Random();
        String randomAuthNum = "";

        for (int i = 0; i < 4; i++) {
            randomAuthNum += Integer.toString(rd.nextInt(10));
        }
        return randomAuthNum;
    }
}
