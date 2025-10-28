package krafton.bookmark.domain.bookmark;

import jakarta.transaction.Transactional;
import krafton.bookmark.domain.bookmark.dto.BookmarkQuery;
import krafton.bookmark.domain.bookmark.dto.BookmarkResponse;
import krafton.bookmark.domain.bookmark.dto.BookmarkSaveRequest;
import krafton.bookmark.domain.bookmark.dto.BookmarkUpdateRequest;
import krafton.bookmark.domain.exception.NotFoundEntityException;
import krafton.bookmark.domain.member.Member;
import krafton.bookmark.domain.tag.Tag;
import krafton.bookmark.domain.tag.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final TagRepository tagRepository;

    public Bookmark save(BookmarkSaveRequest req) {

        return bookmarkRepository.save(req.toEntity());
    }

    @Transactional
    public void update(BookmarkUpdateRequest request) {
        Bookmark find = getBookmark(request.id(), request.author());

        Tag tag = null;
        if (request.tagId() != null) {
            tag = getTag(request.tagId(), request.author());
        }

        find.update(request.url(), request.title(), request.memo(), tag);
    }

    @Transactional
    public BookmarkResponse findOne(Member author, Long id) {
        Bookmark bookmark = getBookmark(id, author);

        return bookmark.toDto();
    }

    public Page<BookmarkResponse> findBookmarkPagesByQuery(BookmarkQuery query, Member author, Pageable pageable) {
        Tag tag = null;
        if (query.tagId() != null) {
           tag = getTag(query.tagId(), author);
        }

        return bookmarkRepository.findBookmarkPagesByQuery(query.title(), query.url(), tag, author, pageable).map(Bookmark::toDto);
    }

    private Tag getTag(Long tagId, Member author) {
        Tag tag;
        tag = tagRepository.findByIdAndAuthor(tagId, author).orElseThrow(() -> {
            log.error("없는 태그 id 입니다. id = {}, author = {}", tagId, author.getId());
            return new NotFoundEntityException("태그를 찾을 수 없습니다!");
        });
        return tag;
    }

    private Bookmark getBookmark(Long id, Member author) {
        return bookmarkRepository.findWithTag(id, author).orElseThrow(() -> {
            log.error("없는 북마크 id 입니다. id = {}, author = {}", id, author.getId());
            return new NotFoundEntityException("북마크를 찾을 수 없습니다!");
        });
    }
}
