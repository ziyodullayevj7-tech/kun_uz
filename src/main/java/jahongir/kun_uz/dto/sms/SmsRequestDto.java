package jahongir.kun_uz.dto.sms;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsRequestDto {
    private String mobile_phone;
    private String message;
}
