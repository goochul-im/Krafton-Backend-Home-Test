package krafton.bookmark.common.security.dto;

import krafton.bookmark.domain.member.Member;

public record LoginRequest(
        String username,
        String password
) {

    public Member toEntity() {
        return new Member(
                username,
                password
        );
    }

}
