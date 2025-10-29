package krafton.bookmark.common.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import krafton.bookmark.domain.member.Member;

@Schema(description = "로그인 요청")
public record LoginRequest(
        @Schema(description = "유저 이름", example = "testUser")
        String username,
        @Schema(description = "비밀번호", example = "1111")
        String password
) {

    public Member toEntity() {
        return new Member(
                username,
                password
        );
    }

}
