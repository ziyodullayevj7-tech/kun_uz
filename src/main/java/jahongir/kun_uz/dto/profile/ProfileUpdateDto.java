package jahongir.kun_uz.dto.profile;

import jahongir.kun_uz.enums.Roles;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProfileUpdateDto {
    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Surname cannot be empty")
    private String surname;

    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotEmpty(message = "Role cannot be empty")
    private List<Roles> rolesList;
}
