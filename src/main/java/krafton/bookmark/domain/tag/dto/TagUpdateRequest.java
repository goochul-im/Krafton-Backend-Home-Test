package krafton.bookmark.domain.tag.dto;

import krafton.bookmark.domain.member.Member;

public record TagUpdateRequest(
        Long id,
        Member author,
        String updateName
) {
}
