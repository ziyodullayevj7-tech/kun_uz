package jahongir.kun_uz.service;

import jahongir.kun_uz.dto.EmailHistoryResponseDto;
import jahongir.kun_uz.entity.EmailHistoryEntity;
import jahongir.kun_uz.exp.AppBadException;
import jahongir.kun_uz.repository.EmailHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class EmailHistoryService {
    @Autowired
    private EmailHistoryRepository emailHistoryRepository;

    public void create(String body, Integer smsCode, String toAccount) {
        EmailHistoryEntity entity = new EmailHistoryEntity();
        entity.setBody(body);
        entity.setCode(smsCode);
        entity.setToAccount(toAccount);
        emailHistoryRepository.save(entity);
    }


    public boolean isSmsSentToAccount(String username, Integer smsCode) {
        Optional<EmailHistoryEntity> optional = emailHistoryRepository.findLastByUsername(username);
        if (optional.isEmpty()){
            return false;
        }
        EmailHistoryEntity entity = optional.get();
        if (!entity.getCode().equals(smsCode)){
            return false;
        }
        //20:32:40 = 20:30:40 + 0:2:00
        LocalDateTime time = entity.getCreatedDateAndTime().plusMinutes(2);
        //now 20:31:30 > 20:32:40 | now 20:35:30 > 20:32:40
        if (LocalDateTime.now().isAfter(time)){
            return false;
        }
        return true;
    }

    public List<EmailHistoryResponseDto> getEmailHistoryByEmail(String email){
        Optional<List<EmailHistoryEntity>> optional = emailHistoryRepository.getEmailHistoryEntitiesByToAccount(email);
        if (optional.isEmpty()){
            throw new AppBadException("No history is found with this email");
        }
        List<EmailHistoryResponseDto> response = new LinkedList<>();
        optional.get().forEach(entity -> {
            EmailHistoryResponseDto dto = new EmailHistoryResponseDto();
            dto.setId(entity.getId());
            dto.setEmail(entity.getToAccount());
            dto.setBody(entity.getBody());
            dto.setCreatedDateAndTime(entity.getCreatedDateAndTime());
            response.add(dto);
        });
        return response;
    }

    public List<EmailHistoryResponseDto> getEmailHistoryByGivenDate(LocalDate date){
        Optional<List<EmailHistoryEntity>> optional = emailHistoryRepository.getEmailHistoryEntitiesByGivenDate(date);
        if (optional.isEmpty()){
            throw new AppBadException("No history is found with this date");
        }
        List<EmailHistoryResponseDto> response = new LinkedList<>();
        optional.get().forEach(entity -> {
            EmailHistoryResponseDto dto = new EmailHistoryResponseDto();
            dto.setId(entity.getId());
            dto.setEmail(entity.getToAccount());
            dto.setBody(entity.getBody());
            dto.setCreatedDateAndTime(entity.getCreatedDateAndTime());
            response.add(dto);
        });
        return response;
    }

    public PageImpl<EmailHistoryResponseDto> pagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDateAndTime").descending());
        Page<EmailHistoryEntity> result = emailHistoryRepository.findAll(pageable);

        List<EmailHistoryResponseDto> dtos = new LinkedList<>();
        for (EmailHistoryEntity entity : result.getContent()) {
            dtos.add(toDtoFromEntity(entity));
        }
        return new PageImpl<>(dtos, pageable, result.getTotalElements());
    }

    public EmailHistoryResponseDto toDtoFromEntity(EmailHistoryEntity entity){
        EmailHistoryResponseDto dto = new EmailHistoryResponseDto();
        dto.setId(entity.getId());
        dto.setEmail(entity.getToAccount());
        dto.setBody(entity.getBody());
        dto.setCreatedDateAndTime(entity.getCreatedDateAndTime());
        return dto;
    }
}
