package jahongir.kun_uz.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
public class RegistrationResendDto {
    @NotBlank(message = "Email is required")
    private String username;
}
