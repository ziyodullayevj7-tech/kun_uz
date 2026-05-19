package jahongir.kun_uz.controller;

import jahongir.kun_uz.dto.auth.AuthorizationDto;
import jahongir.kun_uz.dto.auth.RegistrationDto;
import jahongir.kun_uz.dto.auth.RegistrationResendDto;
import jahongir.kun_uz.dto.profile.ProfileDto;
import jahongir.kun_uz.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@Valid @RequestBody RegistrationDto dto) {
        return ResponseEntity.ok(authService.registration(dto));
    }

    @GetMapping("/registration/email/verification/{username}/{smsCode}")
    public ResponseEntity<String> registration(@PathVariable("username") String username, @PathVariable("smsCode") Integer smsCode) {
        return ResponseEntity.ok(authService.regEmailVerification(username, smsCode));
    }

    @PostMapping("/login")
    public ResponseEntity<ProfileDto> login(@Valid @RequestBody AuthorizationDto dto){
        return ResponseEntity.ok(authService.login(dto));
    }

    @GetMapping("/registration//resend")
    public ResponseEntity<String> registration(@RequestBody RegistrationResendDto dto){
        return ResponseEntity.ok(authService.regResend(dto));
    }
}
