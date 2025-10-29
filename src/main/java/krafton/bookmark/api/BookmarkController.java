package krafton.bookmark.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import krafton.bookmark.api.dto.BookmarkSaveApiRequest;
import krafton.bookmark.api.dto.BookmarkUpdateApiRequest;
import krafton.bookmark.api.dto.PageResponse;
import krafton.bookmark.application.dto.BookmarkQuery;
import krafton.bookmark.application.dto.BookmarkResponse;
import krafton.bookmark.application.dto.BookmarkSaveRequest;
import krafton.bookmark.application.dto.BookmarkUpdateRequest;
import krafton.bookmark.application.service.BookmarkService;
import krafton.bookmark.common.security.details.CustomUserDetails;
import krafton.bookmark.domain.bookmark.Bookmark;
import krafton.bookmark.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Bookmark", description = "북마크 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @Operation(summary = "북마크 저장", description = "새로운 북마크를 저장합니다.")
    @ApiResponse(responseCode = "201", description = "북마크 저장 성공",
            content = @Content(schema = @Schema(implementation = Bookmark.class)))
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    @PostMapping
    public ResponseEntity<?> saveBookmark(
            @Valid @RequestBody BookmarkSaveApiRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails details) {

        Bookmark save = bookmarkService.save(new BookmarkSaveRequest(
                details.getMember(), request.title(), request.url(), request.memo()
        ));

        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }

    @Operation(summary = "모든 북마크 조회", description = "현재 사용자의 모든 북마크를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "북마크 목록 조회 성공",
            content = @Content(schema = @Schema(implementation = BookmarkResponse.class)))
    @GetMapping
    public ResponseEntity<?> findAll(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails details) {

        List<BookmarkResponse> result = bookmarkService.findAll(details.getMember());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "단일 북마크 조회", description = "특정 ID의 북마크를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "북마크 조회 성공",
            content = @Content(schema = @Schema(implementation = BookmarkResponse.class)))
    @ApiResponse(responseCode = "404", description = "북마크를 찾을 수 없음")
    @GetMapping("{id}")
    public ResponseEntity<?> findOne(
            @Parameter(description = "북마크 ID") @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails details) {
        BookmarkResponse result = bookmarkService.findOne(details.getMember(), id);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "북마크 수정", description = "특정 ID의 북마크를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "북마크 수정 성공",
            content = @Content(schema = @Schema(implementation = BookmarkResponse.class)))
    @ApiResponse(responseCode = "404", description = "북마크를 찾을 수 없음")
    @PutMapping("{id}")
    public ResponseEntity<?> updateBookmark(
            @Parameter(description = "북마크 ID") @PathVariable Long id,
            @Valid @RequestBody BookmarkUpdateApiRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails details) {

        BookmarkResponse update = bookmarkService.update(new BookmarkUpdateRequest(
                details.getMember(), request.title(), request.url(), request.memo(), id
        ));

        return new ResponseEntity<>(update, HttpStatus.OK);
    }

    @Operation(summary = "북마크 삭제", description = "특정 ID의 북마크를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "북마크 삭제 성공")
    @ApiResponse(responseCode = "404", description = "북마크를 찾을 수 없음")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteBookmark(
            @Parameter(description = "북마크 ID") @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails details) {

        bookmarkService.deleteOne(id, details.getMember());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "북마크 검색", description = "조건에 따라 북마크를 검색하고 페이지네이션하여 반환합니다.")
    @GetMapping("/query")
    public ResponseEntity<PageResponse<BookmarkResponse>> search(
            @Parameter(description = "검색할 북마크 제목 (부분 일치)") @RequestParam(required = false) String title,
            @Parameter(description = "검색할 북마크 URL (부분 일치)") @RequestParam(required = false) String url,
            @Parameter(description = "검색할 태그 ID") @RequestParam(required = false) Long tagId,
            @Parameter(description = "페이지 번호 (1부터 시작)") @RequestParam(required = false, defaultValue = "1") int page,
            @Parameter(description = "페이지당 항목 수") @RequestParam(required = false, defaultValue = "20") int size,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails details) {

        Page<BookmarkResponse> result = bookmarkService.findBookmarkPagesByQuery(new BookmarkQuery(
                title, url, tagId
        ), details.getMember(), PageRequest.of(page - 1, size));

        PageResponse<BookmarkResponse> pageResponse = new PageResponse<>(
                result.getContent(),
                result.getTotalPages(),
                result.getNumber() + 1,
                result.hasNext()
        );

        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

}
