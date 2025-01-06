package com.teamname.projectname.Util;
//util 또는 service에 작성
//util시 component 선언, service는 service 선언
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

//Gmail에서 이메일 전송하도록 설정
// 이메일 비밀키 발급
//임시 비밀번호를 이메일로 발송

@Component
@RequiredArgsConstructor
public class GMailUtil {

    private final JavaMailSender javaMailSender;

    //받은사람 주소, 제목, 내용
    public void sendEmail(String to, String subject, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        String from = "운영자<본인메일주소>";

        message.setFrom(from); //보내는 사람
        message.setTo(to); //받은 사람
        message.setSubject(subject); //제목
        message.setText(text); //내용

        try{
            javaMailSender.send(message); //java에서 메일 전송
        } catch (MailException e) {
            //메일 보내기 실패시
        }
    }
}

