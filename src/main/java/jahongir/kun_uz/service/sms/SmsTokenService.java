package jahongir.kun_uz.service.sms;

import jahongir.kun_uz.dto.sms.SmsProviderTokenDto;
import jahongir.kun_uz.dto.sms.SmsTokenProviderResponseDto;
import jahongir.kun_uz.entity.sms.SmsTokenEntity;
import jahongir.kun_uz.repository.sms.SmsTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class SmsTokenService {

    @Autowired
    private SmsTokenRepository smsTokenRepository;

    @Autowired
    private RestTemplate restTemplate;
    @Value("royxatdan-otilgan-email")
    private String email;
    @Value("taqdim-etilgan-password")
    private String password;
    @Value("https://notigy.eskiz.uz/")
    private String url;

    public String getToken() {
        Optional<SmsTokenEntity> optional = smsTokenRepository.findTopByOrderByCreatedDateTimeDesc();
        if (optional.isPresent()) {
            SmsTokenEntity entity = optional.get();
            LocalDateTime tokenDate = entity.getCreatedDateAndTime();
            LocalDateTime now = LocalDateTime.now();
            long days = Duration.between(tokenDate, now).toDaysPart();

            if (days >= 30) {
                //create new and return token
                return createToken();
            } else if (days == 29) {
                //refresh token
                return refreshToken(entity.getToken());
            } else {
                return entity.getToken();
            }
        }
        //if token not exists create new one
        return createToken();
    }

    private String createToken() {
        SmsProviderTokenDto smsProviderTokenDto = new SmsProviderTokenDto();
        smsProviderTokenDto.setEmail(email);
        smsProviderTokenDto.setPassword(password);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        RequestEntity<SmsProviderTokenDto> request = RequestEntity
                .post(url + "auth/login")
                .headers(headers)
                .body(smsProviderTokenDto);

        var response = restTemplate.exchange(request, SmsTokenProviderResponseDto.class);
        String token = response.getBody().getData().getToken();

        SmsTokenEntity entity = new SmsTokenEntity();
        entity.setToken(token);
        entity.setCreatedDateAndTime(LocalDateTime.now());
        smsTokenRepository.save(entity);
        return token;
    }

    public String refreshToken(String oldToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + oldToken);
        headers.set("Content-Type", "application/json");

        RequestEntity<Void> request = RequestEntity
                .patch(url + "auht/refresh")
                .headers(headers)
                .build();

        var response = restTemplate.exchange(request, SmsTokenProviderResponseDto.class);
        String newToken = response.getBody().getData().getToken();

        SmsTokenEntity entity = new SmsTokenEntity();
        entity.setToken(newToken);
        entity.setCreatedDateAndTime(LocalDateTime.now());
        smsTokenRepository.save(entity);

        return newToken;
    }
}
