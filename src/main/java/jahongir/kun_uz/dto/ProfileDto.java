package jahongir.kun_uz.dto;

import jahongir.kun_uz.enums.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
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
    private Status status;
    private String photo_id;
    @NotEmpty(message = "Roles cannot be empty")
    @Size(min = 1, max = 3, message = "Minimum role is 1 and the max is 3")
    private List<@Size(min = 4, max = 9, message = "Characters should be at 4 and 9 at most") @NotBlank(message = "Role cannot be blank") String> roles;
    private Boolean visible;
}
