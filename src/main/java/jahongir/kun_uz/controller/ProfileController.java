package jahongir.kun_uz.controller;

import jahongir.kun_uz.dto.profile.*;
import jahongir.kun_uz.service.ProfileService;
import jahongir.kun_uz.util.PageUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping("")
    public ResponseEntity<ProfileDto> create(@Valid @RequestBody ProfileDto dto) {
        return ResponseEntity.ok(profileService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileDto> update(@PathVariable("id") Integer id,
                                             @Valid @RequestBody ProfileUpdateDto dto) {
        return ResponseEntity.ok(profileService.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileDto> byId(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(profileService.getDtoById(id));
    }

    @PutMapping("/detail")
    public ResponseEntity<ProfileDto> updateDetail(
            @RequestHeader("ProfileId") Integer currentProfileId,
            @Valid @RequestBody ProfileUpdateDetailDto dto) {
        return ResponseEntity.ok(profileService.updateDetail(currentProfileId, dto));
    }

    @GetMapping("/pagination")
    public ResponseEntity<PageImpl<ProfileDto>> pagination(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size){
        return ResponseEntity.ok(profileService.pagination(PageUtil.page(page), size));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable("id") Integer id){
        return ResponseEntity.ok(profileService.deleteById(id));
    }

    @PutMapping("/photo")
    public ResponseEntity<Boolean> updatePhotoId(@RequestHeader("ProfileId") Integer currentProfileId,
                                                 @Valid @RequestBody ProfileUpdatePhototDto dto){
        return ResponseEntity.ok(profileService.updatePhotoId(currentProfileId, dto));
    }

    @PutMapping("/password")
    public ResponseEntity<Boolean> password(@RequestHeader("ProfileId") Integer currenProfileId,
                                            @Valid @RequestBody ProfileUpdatePasswordDto dto){
        return ResponseEntity.ok(profileService.updatePassword(currenProfileId, dto));
    }
}
