package jahongir.kun_uz.service.sms;

import jahongir.kun_uz.dto.sms.SmsRequestDto;
import jahongir.kun_uz.exp.AppBadException;
import jahongir.kun_uz.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SmsSenderService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SmsTokenService smsTokenService;

    @Autowired
    private SmsHistoryService smsHistoryService;

    public void sendRegistrationSms(String phone){
        Integer smsCode = RandomUtil.fiveDigit();
        String body = "<#>G'iybat.uz partali. Ro'yxatdan o'tish uchun tasdiqlash kodi (code) : " + smsCode;

        try {
            sendSms(phone, body);
            smsHistoryService.save(phone, body, String.valueOf(smsCode));
        }catch (Exception e){
            e.printStackTrace();
            throw new AppBadException("Something went wrong");
        }
    }

    public void sendSms(String phone, String body){
        SmsRequestDto smsRequestDto = new SmsRequestDto();
        smsRequestDto.setMobile_phone(phone);
        smsRequestDto.setMessage(body);

        String url = "https://notify.eskiz.uz/api/message/sms/send";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + smsTokenService.getToken());

        RequestEntity<SmsRequestDto> request = RequestEntity
                .post(url)
                .headers(headers)
                .body(smsRequestDto);

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        System.out.println(response.getBody());
    }
}
