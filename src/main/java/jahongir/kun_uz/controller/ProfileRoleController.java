package jahongir.kun_uz.controller;

import jahongir.kun_uz.service.ProfileRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile_role")
public class ProfileRoleController {
    @Autowired
    private ProfileRoleService profileRoleService;

}
