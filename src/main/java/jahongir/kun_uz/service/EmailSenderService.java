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
                "  <meta charset=\"UTF-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "  <title>Kunuz Portal - Tasdiqlash Kodi</title>\n" +
                "  <link href=\"https://fonts.googleapis.com/css2?family=DM+Serif+Display:ital@0;1&family=DM+Sans:wght@300;400;500;600&display=swap\" rel=\"stylesheet\">\n" +
                "  <style>\n" +
                "    * { margin:0; padding:0; box-sizing:border-box; }\n" +
                "    body { font-family:'DM Sans',sans-serif; background:#0d0f14; padding:40px 16px; }\n" +
                "    .wrapper { max-width:600px; margin:0 auto; }\n" +
                "    .brand-bar { text-align:center; margin-bottom:24px; }\n" +
                "    .logo-mark { display:inline-flex; align-items:center; gap:10px; }\n" +
                "    .wordmark { font-family:'DM Serif Display',serif; font-size:22px; color:#e8d5a3; letter-spacing:.5px; }\n" +
                "    .card { background:linear-gradient(160deg,#1a1d26,#141720); border-radius:24px; overflow:hidden; border:1px solid #2a2d3a; }\n" +
                "\n" +
                "    .hero {\n" +
                "      position:relative;\n" +
                "      padding:56px 48px 48px;\n" +
                "      text-align:center;\n" +
                "      background:\n" +
                "        radial-gradient(ellipse 80%% 60%% at 50%% -10%%,rgba(212,175,105,.18),transparent 65%%),\n" +
                "        linear-gradient(180deg,#1e2130,#161924);\n" +
                "      border-bottom:1px solid #2a2d3a;\n" +
                "      overflow:hidden;\n" +
                "    }\n" +
                "\n" +
                "    .hero::before {\n" +
                "      content:'';\n" +
                "      position:absolute;\n" +
                "      inset:0;\n" +
                "      background-image:\n" +
                "        radial-gradient(circle at 20%% 80%%,rgba(212,175,105,.06),transparent 50%%),\n" +
                "        radial-gradient(circle at 80%% 20%%,rgba(168,130,72,.08),transparent 50%%);\n" +
                "    }\n" +
                "\n" +
                "    .hero-eyebrow {\n" +
                "      display:inline-block;\n" +
                "      font-size:11px;\n" +
                "      font-weight:600;\n" +
                "      letter-spacing:2.5px;\n" +
                "      text-transform:uppercase;\n" +
                "      color:#c49a45;\n" +
                "      background:rgba(196,154,69,.12);\n" +
                "      border:1px solid rgba(196,154,69,.25);\n" +
                "      border-radius:100px;\n" +
                "      padding:6px 16px;\n" +
                "      margin-bottom:24px;\n" +
                "      position:relative;\n" +
                "    }\n" +
                "\n" +
                "    .hero h1 {\n" +
                "      font-family:'DM Serif Display',serif;\n" +
                "      font-size:38px;\n" +
                "      color:#f0e6cc;\n" +
                "      line-height:1.15;\n" +
                "      margin-bottom:14px;\n" +
                "      position:relative;\n" +
                "    }\n" +
                "\n" +
                "    .hero h1 em {\n" +
                "      font-style:italic;\n" +
                "      color:#d4a855;\n" +
                "    }\n" +
                "\n" +
                "    .hero-sub {\n" +
                "      font-size:15px;\n" +
                "      color:#7a8099;\n" +
                "      line-height:1.65;\n" +
                "      max-width:360px;\n" +
                "      margin:0 auto;\n" +
                "      position:relative;\n" +
                "    }\n" +
                "\n" +
                "    .body { padding:48px 48px 40px; }\n" +
                "\n" +
                "    .greeting {\n" +
                "      font-size:16px;\n" +
                "      color:#9aa0b8;\n" +
                "      margin-bottom:32px;\n" +
                "      line-height:1.65;\n" +
                "    }\n" +
                "\n" +
                "    .greeting strong {\n" +
                "      color:#d8dde8;\n" +
                "      font-weight:500;\n" +
                "    }\n" +
                "\n" +
                "    .otp-wrapper { margin:0 0 36px; }\n" +
                "\n" +
                "    .otp-label {\n" +
                "      font-size:11px;\n" +
                "      font-weight:600;\n" +
                "      letter-spacing:2px;\n" +
                "      text-transform:uppercase;\n" +
                "      color:#5a6070;\n" +
                "      margin-bottom:14px;\n" +
                "    }\n" +
                "\n" +
                "    .otp-card {\n" +
                "      background:#0d0f14;\n" +
                "      border:1px solid #2a2d3a;\n" +
                "      border-radius:16px;\n" +
                "      padding:32px 24px;\n" +
                "      display:flex;\n" +
                "      align-items:center;\n" +
                "      justify-content:space-between;\n" +
                "      gap:20px;\n" +
                "    }\n" +
                "\n" +
                "    .otp-digits {\n" +
                "      display:flex;\n" +
                "      gap:10px;\n" +
                "      align-items:center;\n" +
                "    }\n" +
                "\n" +
                "    .digit-box {\n" +
                "      width:52px;\n" +
                "      height:64px;\n" +
                "      background:#161924;\n" +
                "      border:1px solid #2e3245;\n" +
                "      border-top:2px solid #c49a45;\n" +
                "      border-radius:10px;\n" +
                "      display:flex;\n" +
                "      align-items:center;\n" +
                "      justify-content:center;\n" +
                "      font-family:'DM Serif Display',serif;\n" +
                "      font-size:30px;\n" +
                "      color:#f0e6cc;\n" +
                "    }\n" +
                "\n" +
                "    .timer-badge {\n" +
                "      display:inline-flex;\n" +
                "      align-items:center;\n" +
                "      gap:7px;\n" +
                "      background:rgba(196,154,69,.08);\n" +
                "      border:1px solid rgba(196,154,69,.2);\n" +
                "      border-radius:100px;\n" +
                "      padding:8px 16px;\n" +
                "      font-size:13px;\n" +
                "      color:#c49a45;\n" +
                "      font-weight:500;\n" +
                "    }\n" +
                "\n" +
                "    .divider {\n" +
                "      border:none;\n" +
                "      border-top:1px solid #1e2130;\n" +
                "      margin:36px 0;\n" +
                "    }\n" +
                "\n" +
                "    .steps-label {\n" +
                "      font-size:11px;\n" +
                "      font-weight:600;\n" +
                "      letter-spacing:2px;\n" +
                "      text-transform:uppercase;\n" +
                "      color:#5a6070;\n" +
                "      margin-bottom:18px;\n" +
                "    }\n" +
                "\n" +
                "    .steps {\n" +
                "      display:flex;\n" +
                "      flex-direction:column;\n" +
                "      gap:14px;\n" +
                "    }\n" +
                "\n" +
                "    .step {\n" +
                "      display:flex;\n" +
                "      align-items:flex-start;\n" +
                "      gap:14px;\n" +
                "    }\n" +
                "\n" +
                "    .step-num {\n" +
                "      width:26px;\n" +
                "      height:26px;\n" +
                "      flex-shrink:0;\n" +
                "      background:rgba(196,154,69,.1);\n" +
                "      border:1px solid rgba(196,154,69,.25);\n" +
                "      border-radius:50%%;\n" +
                "      display:flex;\n" +
                "      align-items:center;\n" +
                "      justify-content:center;\n" +
                "      font-size:12px;\n" +
                "      font-weight:600;\n" +
                "      color:#c49a45;\n" +
                "      margin-top:1px;\n" +
                "    }\n" +
                "\n" +
                "    .step-text {\n" +
                "      font-size:14px;\n" +
                "      color:#7a8099;\n" +
                "      line-height:1.6;\n" +
                "    }\n" +
                "\n" +
                "    .step-text strong {\n" +
                "      color:#aab0c4;\n" +
                "      font-weight:500;\n" +
                "    }\n" +
                "\n" +
                "    .warning {\n" +
                "      margin-top:32px;\n" +
                "      background:rgba(220,80,60,.05);\n" +
                "      border:1px solid rgba(220,80,60,.15);\n" +
                "      border-left:3px solid #dc503c;\n" +
                "      border-radius:0 10px 10px 0;\n" +
                "      padding:14px 18px;\n" +
                "      font-size:13px;\n" +
                "      color:#8a6a68;\n" +
                "      line-height:1.6;\n" +
                "    }\n" +
                "\n" +
                "    .warning strong {\n" +
                "      color:#c46660;\n" +
                "      font-weight:500;\n" +
                "    }\n" +
                "\n" +
                "    .footer {\n" +
                "      background:#0d0f14;\n" +
                "      border-top:1px solid #1e2130;\n" +
                "      padding:32px 48px;\n" +
                "    }\n" +
                "\n" +
                "    .footer-inner {\n" +
                "      display:flex;\n" +
                "      justify-content:space-between;\n" +
                "      align-items:flex-start;\n" +
                "      gap:24px;\n" +
                "    }\n" +
                "\n" +
                "    .footer-brand {\n" +
                "      font-family:'DM Serif Display',serif;\n" +
                "      font-size:18px;\n" +
                "      color:#c49a45;\n" +
                "      margin-bottom:4px;\n" +
                "    }\n" +
                "\n" +
                "    .footer-address {\n" +
                "      font-size:12px;\n" +
                "      color:#3e4255;\n" +
                "      line-height:1.7;\n" +
                "    }\n" +
                "\n" +
                "    .footer-links {\n" +
                "      text-align:right;\n" +
                "      font-size:12px;\n" +
                "      color:#3e4255;\n" +
                "      line-height:2;\n" +
                "    }\n" +
                "\n" +
                "    .footer-links a {\n" +
                "      color:#5a6070;\n" +
                "      text-decoration:none;\n" +
                "    }\n" +
                "\n" +
                "    .footer-note {\n" +
                "      margin-top:24px;\n" +
                "      padding-top:20px;\n" +
                "      border-top:1px solid #1a1d26;\n" +
                "      font-size:11px;\n" +
                "      color:#2e3245;\n" +
                "      text-align:center;\n" +
                "      line-height:1.8;\n" +
                "    }\n" +
                "\n" +
                "    @media(max-width:500px){\n" +
                "      .hero{padding:40px 24px 36px;}\n" +
                "      .hero h1{font-size:28px;}\n" +
                "      .body{padding:36px 24px 32px;}\n" +
                "      .footer{padding:28px 24px;}\n" +
                "      .footer-inner{flex-direction:column;}\n" +
                "      .footer-links{text-align:left;}\n" +
                "      .otp-card{flex-direction:column;align-items:flex-start;}\n" +
                "      .digit-box{width:44px;height:56px;font-size:24px;}\n" +
                "      .timer-badge{width:100%%;justify-content:center;}\n" +
                "    }\n" +
                "  </style>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "<div class=\"wrapper\">\n" +
                "\n" +
                "  <div class=\"brand-bar\">\n" +
                "    <div class=\"logo-mark\">\n" +
                "      <span class=\"wordmark\">Kunuz Portal</span>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "\n" +
                "  <div class=\"card\">\n" +
                "\n" +
                "    <div class=\"hero\">\n" +
                "      <div class=\"hero-eyebrow\">Tasdiqlash xati</div>\n" +
                "      <h1>Xush kelibsiz,<br><em>aziz foydalanuvchi!</em></h1>\n" +
                "      <p class=\"hero-sub\">Hisobingizni tasdiqlash uchun quyidagi bir martalik kodni kiriting.</p>\n" +
                "    </div>\n" +
                "\n" +
                "    <div class=\"body\">\n" +
                "\n" +
                "      <p class=\"greeting\">\n" +
                "        Siz Kunuz Portalida <strong>ro&#39;yxatdan o&#39;tishni</strong> boshlagansiz.\n" +
                "        Jarayonni yakunlash uchun quyidagi tasdiqlash kodini sahifaga kiriting.\n" +
                "      </p>\n" +
                "\n" +
                "      <div class=\"otp-wrapper\">\n" +
                "        <div class=\"otp-label\">Tasdiqlash kodi</div>\n" +
                "\n" +
                "        <div class=\"otp-card\">\n" +
                "          <div class=\"otp-digits\">\n" +
                "            {{DIGITS}}\n" +
                "          </div>\n" +
                "\n" +
                "          <div>\n" +
                "            <span class=\"timer-badge\">\n" +
                "              ⏳ 10 daqiqa\n" +
                "            </span>\n" +
                "          </div>\n" +
                "        </div>\n" +
                "      </div>\n" +
                "\n" +
                "      <hr class=\"divider\">\n" +
                "\n" +
                "      <div class=\"steps-label\">Qanday foydalanish kerak</div>\n" +
                "\n" +
                "      <div class=\"steps\">\n" +
                "        <div class=\"step\">\n" +
                "          <div class=\"step-num\">1</div>\n" +
                "          <p class=\"step-text\">Brauzeringizdagi <strong>Kunuz Portal</strong> sahifasiga qayting.</p>\n" +
                "        </div>\n" +
                "\n" +
                "        <div class=\"step\">\n" +
                "          <div class=\"step-num\">2</div>\n" +
                "          <p class=\"step-text\">Yuqoridagi <strong>6 xonali kodni</strong> tasdiqlash maydoniga kiriting.</p>\n" +
                "        </div>\n" +
                "\n" +
                "        <div class=\"step\">\n" +
                "          <div class=\"step-num\">3</div>\n" +
                "          <p class=\"step-text\">Kod <strong>10 daqiqa</strong> ichida amal qiladi.</p>\n" +
                "        </div>\n" +
                "      </div>\n" +
                "\n" +
                "      <div class=\"warning\">\n" +
                "        <strong>Muhim:</strong> Agar siz bu so&#39;rovni yubormagan bo&#39;lsangiz,\n" +
                "        ushbu xatni e&#39;tiborsiz qoldiring.\n" +
                "      </div>\n" +
                "\n" +
                "    </div>\n" +
                "\n" +
                "    <div class=\"footer\">\n" +
                "      <div class=\"footer-inner\">\n" +
                "        <div>\n" +
                "          <div class=\"footer-brand\">Kunuz Portal</div>\n" +
                "          <div class=\"footer-address\">Toshkent shahri, O&#39;zbekiston</div>\n" +
                "        </div>\n" +
                "      </div>\n" +
                "\n" +
                "      <div class=\"footer-note\">\n" +
                "        Ushbu xat avtomatik tarzda yuborilgan.<br>\n" +
                "        &copy; 2025 Kunuz Portal.\n" +
                "      </div>\n" +
                "    </div>\n" +
                "\n" +
                "  </div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";

        String otp = String.format("%06d", smsCode);

        StringBuilder digits = new StringBuilder();

        for (char c : otp.toCharArray()) {
            digits.append("<div class=\"digit-box\">")
                    .append(c)
                    .append("</div>");
        }

        body = body.replace("{{DIGITS}}", digits.toString());

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
