package jahongir.kun_uz.controller;

import jahongir.kun_uz.dto.article.ArticleDto;
import jahongir.kun_uz.dto.article.ArticleUpdateDto;
import jahongir.kun_uz.service.ArticleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping("")
    public ResponseEntity<ArticleDto> create(@Valid @RequestBody ArticleDto dto){
        return ResponseEntity.ok(articleService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDto> getAll(@PathVariable("id") Integer id){
        return ResponseEntity.ok(articleService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleUpdateDto> update(@PathVariable Integer id,
                                                   @RequestBody ArticleUpdateDto dto){
        return ResponseEntity.ok(articleService.update(id, dto));
    }
}
