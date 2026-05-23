package jahongir.kun_uz.controller;

import jahongir.kun_uz.dto.AttachDTO;
import jahongir.kun_uz.service.AttachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/attach")
public class AttachController {
    @Autowired
    private AttachService attachService;

    @PostMapping("/upload")
    public ResponseEntity<AttachDTO> create(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(attachService.upload(file));
    }


    @GetMapping("/open/{fileId}")
    public ResponseEntity<Resource> open(@PathVariable String fileId){
        return attachService.openSimple(fileId);
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> opeN(@PathVariable String fileId){
        return attachService.open(fileId);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable("id") String id){
        return attachService.download(id);
    }
}
