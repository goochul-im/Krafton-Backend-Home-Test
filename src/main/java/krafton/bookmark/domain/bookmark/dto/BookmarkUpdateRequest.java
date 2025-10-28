package krafton.bookmark.domain.bookmark.dto;

import krafton.bookmark.domain.member.Member;

public record BookmarkUpdateRequest(
        Member author,
        Long id,
        String title,
        String url,
        String memo,
        Long tagId
) {
}
