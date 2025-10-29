package krafton.bookmark.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record TagUpdateApiRequest(
        @Schema(description = "수정할 태그 이름", example = "새로운 태그 이름")
        String updateName
) {
}
