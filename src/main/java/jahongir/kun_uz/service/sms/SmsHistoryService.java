package jahongir.kun_uz.service.sms;

import jahongir.kun_uz.dto.sms.SmsHistoryResponseDto;
import jahongir.kun_uz.dto.sms.SmsRequestDto;
import jahongir.kun_uz.entity.EmailHistoryEntity;
import jahongir.kun_uz.entity.sms.SmsHistoryEntity;
import jahongir.kun_uz.exp.AppBadException;
import jahongir.kun_uz.repository.sms.SmsHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class SmsHistoryService {
    @Autowired
    private SmsHistoryRepository smsHistoryRepository;

    public void save(String phone, String body, String code) {
        SmsHistoryEntity entity = new SmsHistoryEntity();
        entity.setPhoneNumber(phone);
        entity.setBody(body);
        entity.setCode(code);
        entity.setCreatedDateAndTime(LocalDateTime.now());
        smsHistoryRepository.save(entity);
    }


    public SmsHistoryResponseDto getByPhone(String phone) {
        Optional<SmsHistoryEntity> optional = smsHistoryRepository.findByPhoneNumber(phone);
        if (optional.isEmpty()){
            throw new AppBadException("No history found with this phone number");
        }
        return toDtoFromEntity(optional.get());
    }

    public List<SmsHistoryResponseDto> getByCreatedDate(LocalDate createdDate) {
        Optional<List<SmsHistoryEntity>> optional = smsHistoryRepository.getByCreatedDate(createdDate);
        if (optional.isEmpty()){
            throw new AppBadException("No history found on this day");
        }
        List<SmsHistoryResponseDto> dtos = new LinkedList<>();
        optional.get().forEach(entity -> {
            dtos.add(toDtoFromEntity(entity));
        });
        return dtos;
    }

    public SmsHistoryResponseDto toDtoFromEntity(SmsHistoryEntity entity){
        SmsHistoryResponseDto dto = new SmsHistoryResponseDto();
        dto.setPhone(entity.getPhoneNumber());
        dto.setMessage(entity.getBody());
        dto.setAttemptCount(entity.getAttemptCount());
        dto.setCreatedDateTime(entity.getCreatedDateAndTime());
        dto.setId(entity.getId());
        return dto;
    }

    public PageImpl<SmsHistoryResponseDto> pagination(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDateAndTime").descending());
        Page<SmsHistoryEntity> result = smsHistoryRepository.findAll(pageable);

        List<SmsHistoryResponseDto> dtos = new LinkedList<>();
        for (SmsHistoryEntity entity : result.getContent()) {
            dtos.add(toDtoFromEntity(entity));
        }
        return new PageImpl<>(dtos, pageable, result.getTotalElements());
    }
}
