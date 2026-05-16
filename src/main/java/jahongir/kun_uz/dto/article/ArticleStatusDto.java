package jahongir.kun_uz.dto.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import jahongir.kun_uz.enums.ArticleStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleStatusDto {
    private Integer articleId;
    private ArticleStatus status;
}
