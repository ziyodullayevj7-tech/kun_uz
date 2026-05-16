package jahongir.kun_uz.dto.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleShortInfoDto {
    private Integer id;
    private String title;
    private String description;
    private String content;
}
