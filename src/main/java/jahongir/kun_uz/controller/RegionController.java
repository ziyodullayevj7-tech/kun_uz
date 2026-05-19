package jahongir.kun_uz.controller;

import jahongir.kun_uz.dto.RegionByLangDto;
import jahongir.kun_uz.dto.RegionDto;
import jahongir.kun_uz.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/region")
public class RegionController {
    @Autowired
    private RegionService regionService;

    @PostMapping("")
    public ResponseEntity<RegionDto> create(@RequestBody RegionDto dto){
        RegionDto result = regionService.create(dto);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Boolean> update(@PathVariable Integer id,
                                          @RequestBody RegionDto dto){
        Boolean result = regionService.update(id, dto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Integer id){
        Boolean result = regionService.deleteById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<RegionDto>> getAll(){
        List<RegionDto> result = regionService.getAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get-by-language/{lang}")
    public ResponseEntity<List<RegionByLangDto>> getAllByLang(@PathVariable String lang){
        List<RegionByLangDto> result = regionService.getAllByLang(lang);
        return ResponseEntity.ok(result);
    }
}
