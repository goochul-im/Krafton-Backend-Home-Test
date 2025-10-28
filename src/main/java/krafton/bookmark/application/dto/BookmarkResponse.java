package krafton.bookmark.application.dto;

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
