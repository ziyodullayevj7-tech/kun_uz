package jahongir.kun_uz.controller;

import jahongir.kun_uz.dto.CategoryByLangDto;
import jahongir.kun_uz.dto.CategoryDto;
import jahongir.kun_uz.dto.RegionByLangDto;
import jahongir.kun_uz.dto.RegionDto;
import jahongir.kun_uz.exp.ItemNotFoundException;
import jahongir.kun_uz.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("")
    public ResponseEntity<CategoryDto> create(@RequestBody CategoryDto dto){
        CategoryDto result = categoryService.create(dto);
        return ResponseEntity.ok(result);
    }

    @ExceptionHandler({IllegalArgumentException.class, ItemNotFoundException.class})
    public ResponseEntity<String> handle(RuntimeException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Boolean> update(@PathVariable Integer id,
                                          @RequestBody CategoryDto dto){
        Boolean result = categoryService.update(id, dto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Integer id){
        Boolean result = categoryService.deleteById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<CategoryDto>> getAll(){
        List<CategoryDto> result = categoryService.getAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get-by-language/{lang}")
    public ResponseEntity<List<CategoryByLangDto>> getAllByLang(@PathVariable String lang){
        List<CategoryByLangDto> result = categoryService.getAllByLang(lang);
        return ResponseEntity.ok(result);
    }
}
