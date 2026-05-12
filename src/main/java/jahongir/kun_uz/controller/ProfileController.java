package jahongir.kun_uz.controller;

import jahongir.kun_uz.dto.ProfileDto;
import jahongir.kun_uz.dto.ProfileRoleDto;
import jahongir.kun_uz.exp.ItemNotFoundException;
import jahongir.kun_uz.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping("")
    public ResponseEntity<ProfileDto> create(@RequestBody ProfileDto dto){
        ProfileDto result = profileService.create(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("")
    public ResponseEntity<List<ProfileDto>> getAll(){
        List<ProfileDto> result = profileService.getAll();
        return ResponseEntity.ok(result);
    }

    @PutMapping("/udpate/{id}")
    public ResponseEntity<Boolean> update(@RequestBody ProfileDto dto,
                                          @PathVariable Integer id){
        Boolean result = profileService.update(dto, id);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id){
        Boolean result = profileService.delete(id);
        return ResponseEntity.ok(result);
    }

    @ExceptionHandler({IllegalArgumentException.class, ItemNotFoundException.class})
    public ResponseEntity<String> handle(RuntimeException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<ProfileDto> getById(@PathVariable Integer id){
        ProfileDto result = profileService.getById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get-by-id/{name}")
    public ResponseEntity<List<ProfileRoleDto>> getById(@PathVariable String name){
        List<ProfileRoleDto> result = profileService.getByName(name);
        return ResponseEntity.ok(result);
    }
}
