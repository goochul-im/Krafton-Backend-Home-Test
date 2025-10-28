package krafton.bookmark.domain.tag.dto;

import krafton.bookmark.domain.member.Member;

public record TagSaveRequest(
        String tagName,
        Member member
) {
}
