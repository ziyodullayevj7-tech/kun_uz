package jahongir.kun_uz.dto.sms;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsTokenProviderResponseDto {
    private String message;
    private Data data;
    private String token_type;

    @Getter
    @Setter
    public static class Data {
        private String token;
    }
}
