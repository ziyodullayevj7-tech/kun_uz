package jahongir.kun_uz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleDto {
    private Integer id;
    @NotBlank(message = "Title is required")
    @Size(min = 3,message = "Title is too short")
    private String title;
    @NotBlank(message = "Description is required")
    @Size(min = 3,message = "Description is too short")
    private String description;
    @NotBlank(message = "Content is required")
    @Size(min = 3,message = "Content is too short")
    private String content;
    private Integer imageId;
    private Integer regionId;
    private List<Integer> categoryIds;
    private List<Integer> sectionIds;
}
