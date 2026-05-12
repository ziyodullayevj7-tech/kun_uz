package jahongir.kun_uz.controller;

import jahongir.kun_uz.dto.ProfileRoleDto;
import jahongir.kun_uz.exp.ItemNotFoundException;
import jahongir.kun_uz.service.ProfileRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile_role")
public class ProfileRoleController {
    @Autowired
    private ProfileRoleService profileRoleService;

    @GetMapping("/{profile_id}")
    public ResponseEntity<ProfileRoleDto> getByProfileId(@PathVariable Integer profile_id){
        ProfileRoleDto result = profileRoleService.getByProfileId(profile_id);
        return ResponseEntity.ok(result);
    }

    @ExceptionHandler({IllegalArgumentException.class, ItemNotFoundException.class})
    public ResponseEntity<String> handle(RuntimeException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
