package jahongir.kun_uz.controller;

import jahongir.kun_uz.dto.article.*;
import jahongir.kun_uz.enums.ArticleStatus;
import jahongir.kun_uz.service.ArticleService;
import jahongir.kun_uz.service.ProfileService;
import jahongir.kun_uz.util.PageUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ProfileService profileService;

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Integer id){
        return ResponseEntity.ok(articleService.deleteById(id));
    }

    @PutMapping("/change-status")
    public ResponseEntity<Boolean> changeStatusById(@RequestBody ArticleStatusDto dto){
        return ResponseEntity.ok(articleService.changeStatusById(dto));
    }

    @GetMapping("/pagination-by-section-id/{sectionId}")
    public ResponseEntity<PageImpl<ArticleShortInfoDto>> paginationBySectionId(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @PathVariable Integer sectionId){
        return ResponseEntity.ok(articleService.paginationBySectionId(PageUtil.page(page), size, sectionId));
    }

    @GetMapping("/pagination-except-ids/")
    public ResponseEntity<PageImpl<ArticleShortInfoDto>> paginationExceptForIds(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestBody ArticleExceptIdsDto dto){
        return ResponseEntity.ok(articleService.paginationExceptForIds(PageUtil.page(page), dto));
    }

    @GetMapping("/pagination-by-category-id/{categoryId}")
    public ResponseEntity<PageImpl<ArticleShortInfoDto>> paginationByCategoryId(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @PathVariable Integer categoryId){
        return ResponseEntity.ok(articleService.paginationByCategoryId(PageUtil.page(page), size, categoryId));
    }

    @GetMapping("/pagination-by-regiod-id/{regionId}")
    public ResponseEntity<PageImpl<ArticleShortInfoDto>> paginationByRegionId(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @PathVariable Integer regionId){
        return ResponseEntity.ok(articleService.paginationByRegionId(PageUtil.page(page), size, regionId));
    }
}
