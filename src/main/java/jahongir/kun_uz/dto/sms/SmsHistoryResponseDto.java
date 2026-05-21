package jahongir.kun_uz.dto.sms;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SmsHistoryResponseDto {
    private String id;
    private String phone;
    @JsonProperty("text")
    private String message;
    private Integer attemptCount;
    private LocalDateTime createdDateTime;
}
