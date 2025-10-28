package krafton.bookmark.application.dto;

import java.time.LocalDateTime;

public record TagResponse(
        Long id,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
