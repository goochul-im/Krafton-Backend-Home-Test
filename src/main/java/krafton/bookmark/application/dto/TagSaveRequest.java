package krafton.bookmark.application.dto;

import krafton.bookmark.domain.member.Member;

public record TagSaveRequest(
        String tagName,
        Member member
) {
}
