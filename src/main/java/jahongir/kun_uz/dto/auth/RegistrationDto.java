package jahongir.kun_uz.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegistrationDto {
    private String name;
    private String surname;
    private String username;
    private String password;
}
