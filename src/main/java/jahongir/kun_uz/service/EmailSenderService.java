package jahongir.kun_uz.service;

import jahongir.kun_uz.util.RandomUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    @Value("jziyodullayev678@gmail.com")
    private String fromAccount;
    @Value("${server.url}")
    private String serverUrl;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private EmailHistoryService emailHistoryService;

    public void sendRegistrationStyledEmail(String toAccount) {
        Integer smsCode = RandomUtil.fiveDigit();
        String body = "<!DOCTYPE html>\n" +
                "<html lang=\"uz\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Kunuz Portal - Tasdiqlash Kodi</title>\n" +
                "    <style>\n" +
                "        body { font-family: 'Segoe UI', Arial, sans-serif; background-color: #f0f4f8; margin: 0; padding: 20px; }\n" +
                "        .container { max-width: 620px; margin: 0 auto; background-color: #ffffff; border-radius: 16px; overflow: hidden; box-shadow: 0 8px 25px rgba(0, 0, 0, 0.08); }\n" +
                "        .header { background: linear-gradient(135deg, #1e2937, #334155); color: white; padding: 45px 20px; text-align: center; }\n" +
                "        .content { padding: 45px 35px; text-align: center; }\n" +
                "        .code-box { background-color: #f8fafc; border: 4px solid #3b82f6; border-radius: 14px; padding: 28px 20px; margin: 30px auto; max-width: 320px; font-size: 38px; font-weight: 700; letter-spacing: 12px; color: #1e40af; box-shadow: 0 4px 15px rgba(59, 130, 246, 0.15); }\n" +
                "        h1 { margin: 0 0 10px 0; font-size: 29px; font-weight: 600; }\n" +
                "        h2 { color: #1e2937; margin-bottom: 12px; font-size: 22px; }\n" +
                "        p { color: #475569; line-height: 1.7; }\n" +
                "        .footer { background-color: #f1f5f9; padding: 28px; text-align: center; font-size: 14px; color: #64748b; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h1>Kunuz Portaliga</h1>\n" +
                "            <p style=\"margin: 12px 0 0 0; font-size: 18px; opacity: 0.95;\">Xush kelibsiz!</p>\n" +
                "        </div>\n" +
                "\n" +
                "        <div class=\"content\">\n" +
                "            <h2>Ro‘yxatdan o‘tishni yakunlash uchun</h2>\n" +
                "            <p>Tasdiqlash kodingiz:</p>\n" +
                "            \n" +
                "            <div class=\"code-box\">\n" +
                "                %d\n" +
                "            </div>\n" +
                "\n" +
                "            <p><strong>Bu kod 10 daqiqa davomida amal qiladi.</strong></p>\n" +
                "            \n" +
                "            <p style=\"margin-top: 35px; font-size: 15px; color: #64748b;\">\n" +
                "                Agar siz bu so‘rovni yubormagan bo‘lsangiz, iltimos, ushbu xatni e’tiborsiz qoldiring.\n" +
                "            </p>\n" +
                "        </div>\n" +
                "\n" +
                "        <div class=\"footer\">\n" +
                "            <p><strong>Kunuz Portal</strong><br>\n" +
                "            Tashkent, Uzbekistan</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";

        body = String.format(body, smsCode);
        //send
        sendMimeMessage("Registration complete", body, toAccount);
        //save to db
        emailHistoryService.create(body, smsCode, toAccount);
    }

    private String senSimpleMessage(String subject, String body, String toAccount){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(fromAccount);
        msg.setTo(toAccount);
        msg.setSubject(subject);
        msg.setText(body);
        javaMailSender.send(msg);

        return "Mail was sent";
    }

    private String sendMimeMessage(String subject, String body, String toAccount){
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            msg.setFrom(fromAccount);

            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setTo(toAccount);
            helper.setSubject(subject);
            helper.setText(body, true);
            javaMailSender.send(msg);
        }catch (MessagingException e){
            throw new RuntimeException(e);
        }
        return "Mail was send";
    }
}
