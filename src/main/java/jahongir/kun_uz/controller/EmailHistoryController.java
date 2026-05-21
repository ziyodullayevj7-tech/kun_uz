package jahongir.kun_uz.controller;

import jahongir.kun_uz.dto.EmailHistoryResponseDto;
import jahongir.kun_uz.service.EmailHistoryService;
import jahongir.kun_uz.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/email-history")
public class EmailHistoryController {
    @Autowired
    private EmailHistoryService emailHistoryService;

    @GetMapping("/get-email-history-by-email/{email}")
    public ResponseEntity<List<EmailHistoryResponseDto>> getEmailHistoryByEmail(@PathVariable String email){
        return ResponseEntity.ok(emailHistoryService.getEmailHistoryByEmail(email));
    }

    @GetMapping("/get-email-history-by-date")
    public ResponseEntity<List<EmailHistoryResponseDto>> getEmailHistoryByEmail(@RequestParam LocalDate date){
        return ResponseEntity.ok(emailHistoryService.getEmailHistoryByGivenDate(date));
    }

    @GetMapping("/pagination")
    public ResponseEntity<PageImpl<EmailHistoryResponseDto>> pagination(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size){
        return ResponseEntity.ok(emailHistoryService.pagination(PageUtil.page(page), size));
    }
}
