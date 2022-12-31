package f3f.dev1.domain.member.application;

import f3f.dev1.domain.member.dto.MemberDTO.CodeConfirmDto;
import f3f.dev1.domain.member.dto.MemberDTO.EmailConfirmCodeDto;
import f3f.dev1.domain.member.exception.EmailCertificationExpireException;
import f3f.dev1.domain.member.exception.InvalidCertificationCodeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static f3f.dev1.global.common.constants.EmailConstants.EMIAL_CERTIFICATION_TIME;
import static javax.mail.Message.RecipientType.TO;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailCertificationService {

    private final JavaMailSender emailSender;


    private final RedisTemplate<String, String> redisTemplate;

    // 인증 번호
    private String ePw;

    public MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(TO, to);
        message.setSubject("COKIRI 이메일 인증"); // 제목

        String msg = "";
        msg += "<div style = 'margin:100px;'>";
        msg += "<h1> 안녕하세요 </h1>";
        msg += "<h1> 물물 교환 플랫폼 COKIRI 입니다.</h1>";
        msg += "<br>";
        msg += "<p>아래 코드를 앱으로 돌아가서 입력해주세요</p>";
        msg += "<br>";
        msg += "<div align='center' style = 'border:1px solid black; font-family:verdana';>";
        msg += "<h3 style = 'color:blue;'>회원가입 인증 코드입니다.</h3>";
        msg += "<div style='font-style:130%'>";
        msg += "CODE: <strong>";
        msg += ePw + "</strong><div><br/>";
        msg += "</div>";
        message.setText(msg, "utf-8", "html");
        message.setFrom(new InternetAddress("cokiri_dev_team@naver.com", "COKIRI_admin"));
        return message;
    }

    public String createKey() {
        StringBuffer key = new StringBuffer();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(3);

            switch (index) {
                case 0:
                    key.append((char) random.nextInt(26) + 97);
                    break;
                case 1:
                    key.append((char) random.nextInt(26) + 65);
                    break;
                case 2:
                    key.append((random.nextInt(10)));
                    break;
            }
        }
        return key.toString();
    }

    // 메일 발송
    public void sendSimpleMessage(String to) throws Exception {
        ePw = createKey();
        MimeMessage message = createMessage(to);
        try {
            emailSender.send(message);
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            valueOperations.set(to, ePw);
            redisTemplate.expire(to, EMIAL_CERTIFICATION_TIME, TimeUnit.MILLISECONDS);
        } catch (MailException es) {
            es.printStackTrace();
            throw new IllegalArgumentException("메일 전송중 오류 발생");
        }


    }

    // 코드 검증
    public CodeConfirmDto confirmCode(EmailConfirmCodeDto emailConfirmCodeDto) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String code = valueOperations.get(emailConfirmCodeDto.getEmail());
        if (code == null) {
            throw new EmailCertificationExpireException();
        }
        if (!code.equals(emailConfirmCodeDto.getCode())) {
            return CodeConfirmDto.builder().matches(false).build();
        }
        return CodeConfirmDto.builder().matches(true).build();

    }

}
