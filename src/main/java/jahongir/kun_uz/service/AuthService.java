package jahongir.kun_uz.service;

import jahongir.kun_uz.dto.auth.AuthorizationDto;
import jahongir.kun_uz.dto.auth.RegistrationResendDto;
import jahongir.kun_uz.dto.profile.ProfileDto;
import jahongir.kun_uz.dto.auth.RegistrationDto;
import jahongir.kun_uz.entity.ProfileEntity;
import jahongir.kun_uz.enums.Roles;
import jahongir.kun_uz.enums.Status;
import jahongir.kun_uz.exp.AppBadException;
import jahongir.kun_uz.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private ProfileRoleService profileRoleService;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private EmailHistoryService emailHistoryService;

    public String registration(RegistrationDto dto){
        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleIsTrue(dto.getUsername());
        if (optional.isPresent()){
            ProfileEntity existsProfile = optional.get();
            if (existsProfile.getStatus().equals(Status.INACTIVE)){
                profileRoleService.deleteRolesByProfileId(existsProfile.getId());
                profileRepository.deleteById(existsProfile.getId());
            }else {
                throw new AppBadException("Username already exists");
            }
        }
        // create new profile
        ProfileEntity profile = new ProfileEntity();
        profile.setName(dto.getName());
        profile.setSurname(dto.getSurname());
        profile.setUsername(dto.getUsername());
        profile.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        profile.setStatus(Status.INACTIVE);
        profileRepository.save(profile);
        //create profile roles
        profileRoleService.merge(profile.getId(), List.of(Roles.USER));
        //send verification code
        //email send
        emailSenderService.sendRegistrationStyledEmail(profile.getUsername());
        // send sms to phone
        // response
        return "Confirmation code has been sent";
    }

    public String regEmailVerification(String username, Integer smsCode){
        Optional<ProfileEntity> existOptional = profileRepository.findByUsernameAndVisibleIsTrue(username);
        if (existOptional.isEmpty()){
            throw new AppBadException("Username not found");
        }
        ProfileEntity profile = existOptional.get();
        if (!profile.getStatus().equals(Status.INACTIVE)){
            throw new AppBadException("Username in wrong status");
        }
        // check sms code and time
        if (emailHistoryService.isSmsSentToAccount(username, smsCode)){
            profile.setStatus(Status.ACTIVE);
            profileRepository.save(profile);
            return "Verification successfully completed";
        }
        throw new AppBadException("Not completed");
    }

    public ProfileDto login(AuthorizationDto dto){
        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleIsTrue(dto.getUsername());
        if (optional.isEmpty()){
            throw new AppBadException("Username or password is wrong");
        }
        ProfileEntity entity = optional.get();
        if (!bCryptPasswordEncoder.matches(dto.getPassword(), entity.getPassword())){
            throw new AppBadException("Username or password is wrong");
        }
        if (!entity.getStatus().equals(Status.ACTIVE)){
            throw new AppBadException("Status is not active");
        }
        ProfileDto response = new ProfileDto();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setSurname(entity.getSurname());
        response.setUsername(entity.getUsername());
        response.setRoles(profileRoleService.getByProfileId(entity.getId()));
        return response;
    }

    public String regResend(RegistrationResendDto dto) {
        Optional<ProfileEntity> optional = profileRepository.findInActiveByUserName(dto.getUsername());
        if (optional.isEmpty()){
            throw new AppBadException("The user is not incomplete session");
        }
        emailSenderService.sendRegistrationStyledEmail(dto.getUsername());
        return "Code has been sent to you via email";
    }
}
