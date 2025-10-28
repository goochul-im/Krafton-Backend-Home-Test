package krafton.bookmark.domain.tag.dto;

import krafton.bookmark.domain.member.Member;

public record TagUpdateReq(
        Long id,
        Member author,
        String updateName
) {
}
