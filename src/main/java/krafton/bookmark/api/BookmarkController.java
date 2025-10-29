package krafton.bookmark.api;

import krafton.bookmark.api.dto.BookmarkSaveApiRequest;
import krafton.bookmark.api.dto.BookmarkUpdateApiRequest;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping
    public ResponseEntity<?> saveBookmark(@RequestBody BookmarkSaveApiRequest request, @AuthenticationPrincipal CustomUserDetails details) {

        Bookmark save = bookmarkService.save(new BookmarkSaveRequest(
                details.getMember(), request.title(), request.url(), request.memo()
        ));

        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> findAll(@AuthenticationPrincipal CustomUserDetails details) {

        List<BookmarkResponse> result = bookmarkService.findAll(details.getMember());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> findOne(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails details) {
        BookmarkResponse result = bookmarkService.findOne(details.getMember(), id);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateBookmark(
            @PathVariable Long id,
            @RequestBody BookmarkUpdateApiRequest request,
            @AuthenticationPrincipal CustomUserDetails details) {

        BookmarkResponse update = bookmarkService.update(new BookmarkUpdateRequest(
                details.getMember(), id, request.title(), request.url(), request.memo(), request.tagId()
        ));

        return new ResponseEntity<>(update, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteBookmark(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails details) {

        bookmarkService.deleteOne(id, details.getMember());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/query")
    public ResponseEntity<?> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String url,
            @RequestParam(required = false) Long tagId,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "20") int size,
            @AuthenticationPrincipal CustomUserDetails details) {

        Page<BookmarkResponse> result = bookmarkService.findBookmarkPagesByQuery(new BookmarkQuery(
                title, url, tagId
        ), details.getMember(), PageRequest.of(page - 1, size));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
