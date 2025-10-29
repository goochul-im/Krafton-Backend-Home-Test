package krafton.bookmark.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record SingUpRequest(
        @Schema(description = "사용자 이름", example = "testUser")
        String username,
        @Schema(description = "비밀번호", example = "password123")
        String password
) {
}
