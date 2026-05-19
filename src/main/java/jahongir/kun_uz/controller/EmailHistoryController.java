package jahongir.kun_uz.controller;

import jahongir.kun_uz.dto.EmailHistoryResponseDto;
import jahongir.kun_uz.service.EmailHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
