package krafton.bookmark.application.dto;

import krafton.bookmark.domain.bookmark.Bookmark;
import krafton.bookmark.domain.member.Member;

public record BookmarkSaveRequest(
        Member author,
        String title,
        String url,
        String memo
) {

    public Bookmark toEntity() {
        return new Bookmark(
                title,
                url,
                memo,
                author
        );
    }

}
