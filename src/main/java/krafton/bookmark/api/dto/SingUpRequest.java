package krafton.bookmark.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record SingUpRequest(
        @Schema(description = "사용자 이름", example = "testUser")
        @NotBlank(message = "username은 비어있을 수 없습니다.")
        String username,
        @Schema(description = "비밀번호", example = "password123")
        @NotBlank(message = "비밀번호는 비어있을 수 없습니다.")
        String password
) {
}
