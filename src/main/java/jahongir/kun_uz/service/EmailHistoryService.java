package jahongir.kun_uz.service;

import jahongir.kun_uz.entity.EmailHistoryEntity;
import jahongir.kun_uz.repository.EmailHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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


}
