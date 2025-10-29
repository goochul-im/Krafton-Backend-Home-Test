package krafton.bookmark.application.service;

import jakarta.transaction.Transactional;
import krafton.bookmark.domain.bookmark.Bookmark;
import krafton.bookmark.domain.bookmark.BookmarkRepository;
import krafton.bookmark.application.dto.BookmarkQuery;
import krafton.bookmark.application.dto.BookmarkResponse;
import krafton.bookmark.application.dto.BookmarkSaveRequest;
import krafton.bookmark.application.dto.BookmarkUpdateRequest;
import krafton.bookmark.application.exception.NotFoundEntityException;
import krafton.bookmark.domain.member.Member;
import krafton.bookmark.domain.tag.Tag;
import krafton.bookmark.domain.tag.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public BookmarkResponse update(BookmarkUpdateRequest request) {
        Bookmark find = getBookmark(request.tagId(), request.author());

        Tag tag = null;
        if (request.tagId() != null) {
            tag = getTag(request.tagId(), request.author());
        }

        find.update(request.url(), request.title(), request.memo(), tag);
        return find.toDto();
    }

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

    public List<BookmarkResponse> findAll(Member author) {
        return bookmarkRepository.findAllByAuthor(author).stream().map(Bookmark::toDto).toList();
    }

    @Transactional
    public void deleteOne(Long id, Member author) {
        Bookmark bookmark = getBookmark(id, author);
        bookmarkRepository.delete(bookmark);
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
