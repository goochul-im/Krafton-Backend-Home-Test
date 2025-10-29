package krafton.bookmark.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "페이지 응답")
public record PageResponse<T>(
        @Schema(description = "북마크 페이지")
        List<T> items,
        @Schema(description = "전체 페이지 수")
        int totalPage,
        @Schema(description = "현재 페이지")
        int currentPage,
        @Schema(description = "다음 페이지 여부")
        boolean hasNextPage
) {
}
