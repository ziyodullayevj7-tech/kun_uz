package jahongir.kun_uz.dto.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileUpdatePasswordDto {
    @NotBlank(message = "Current password is required")
    private String currentPassword;
    @NotBlank(message = "New password required")
    private String newPassword;
}
