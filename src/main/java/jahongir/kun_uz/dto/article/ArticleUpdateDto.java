package jahongir.kun_uz.dto.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleUpdateDto {
    private String title;
    private String description;
    private String content;
    private Integer imageId;
    private Integer regionId;
    private List<Integer> categoryIds;
    private List<Integer> sectionIds;
}
