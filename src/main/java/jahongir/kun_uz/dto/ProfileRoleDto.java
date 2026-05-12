package jahongir.kun_uz.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProfileRoleDto {
    private Integer id;
    private String profileName;
    private List<String> roles;
}
