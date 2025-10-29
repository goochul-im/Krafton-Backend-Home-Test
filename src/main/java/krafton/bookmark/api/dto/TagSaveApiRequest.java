package krafton.bookmark.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record TagSaveApiRequest(
        @Schema(description = "태그 이름", example = "개발")
        @NotNull
        String tagName
) {
}
