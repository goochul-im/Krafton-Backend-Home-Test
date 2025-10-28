package krafton.bookmark.domain.bookmark.dto;

import krafton.bookmark.domain.tag.dto.TagResponse;

import java.time.LocalDateTime;

public record BookmarkResponse(
        Long id,
        String title,
        String url,
        String memo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        TagResponse tag
) {
}
