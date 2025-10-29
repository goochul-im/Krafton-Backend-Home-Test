package krafton.bookmark.application.dto;

import krafton.bookmark.domain.member.Member;

public record BookmarkUpdateRequest(
        Member author,
        String title,
        String url,
        String memo,
        Long tagId
) {
}
