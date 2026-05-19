package jahongir.kun_uz.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EmailHistoryResponseDto {
    private String id;
    private String email;
    private LocalDateTime createdDateAndTime;
    @JsonProperty("body")
    private String body;
}
