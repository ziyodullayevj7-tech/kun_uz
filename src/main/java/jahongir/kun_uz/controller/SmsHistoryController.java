package jahongir.kun_uz.controller;

import jahongir.kun_uz.dto.sms.SmsHistoryResponseDto;
import jahongir.kun_uz.service.sms.SmsHistoryService;
import jahongir.kun_uz.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/sms_controller")
public class SmsHistoryController {
    @Autowired
    private SmsHistoryService smsHistoryService;

    @GetMapping("/by-phone/{phone}")
    public ResponseEntity<SmsHistoryResponseDto> getSmsHistoryByPhone(@PathVariable String phone){
        SmsHistoryResponseDto result = smsHistoryService.getByPhone(phone);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/list-by-created-date-time")
    public ResponseEntity<List<SmsHistoryResponseDto>> getByCreatedDateAndTime(@RequestParam LocalDate createdDate){
        List<SmsHistoryResponseDto> result = smsHistoryService.getByCreatedDate(createdDate);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/pagination")
    public ResponseEntity<PageImpl<SmsHistoryResponseDto>> pagination(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size){
        return ResponseEntity.ok(smsHistoryService.pagination(PageUtil.page(page), size));
    }
}
