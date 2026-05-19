package jahongir.kun_uz.controller;

import jahongir.kun_uz.dto.CategoryByLangDto;
import jahongir.kun_uz.dto.SectionDto;
import jahongir.kun_uz.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/section")
public class SectionController {
    @Autowired
    private SectionService sectionService;

    @PostMapping("")
    public ResponseEntity<SectionDto> create(@RequestBody SectionDto dto){
        SectionDto result = sectionService.create(dto);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Boolean> update(@PathVariable Integer id,
                                          @RequestBody SectionDto dto){
        Boolean result = sectionService.update(id, dto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Integer id){
        Boolean result = sectionService.deleteById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<SectionDto>> getAll(){
        List<SectionDto> result = sectionService.getAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get-by-language/{lang}")
    public ResponseEntity<List<CategoryByLangDto>> getAllByLang(@PathVariable String lang){
        List<CategoryByLangDto> result = sectionService.getAllByLang(lang);
        return ResponseEntity.ok(result);
    }
}
