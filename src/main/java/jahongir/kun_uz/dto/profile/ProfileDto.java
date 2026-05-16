package jahongir.kun_uz.dto.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import jahongir.kun_uz.enums.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDto {
    private Integer id;
    @Size(min = 3, message = "Name is too short")
    @NotBlank(message = "Name is required")
    private String name;
    @Size(min = 3, message = "Surname is too short")
    @NotBlank(message = "Surname is required")
    private String surname;
    @NotBlank(message = "Email is required")
    @Email(message = "Wrong email")
    private String username;
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "At least 8 characters should be written")
    private String password;
    private String photo_id;
    @NotEmpty(message = "Roles cannot be empty")
    private List<Roles> roles;
    private Boolean visible;
    private LocalDate createdDate;
}
