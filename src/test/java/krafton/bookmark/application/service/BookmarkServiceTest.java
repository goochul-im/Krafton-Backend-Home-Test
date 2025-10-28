package krafton.bookmark.application.service;

import krafton.bookmark.application.dto.BookmarkQuery;
import krafton.bookmark.application.dto.BookmarkResponse;
import krafton.bookmark.application.dto.BookmarkSaveRequest;
import krafton.bookmark.application.dto.BookmarkUpdateRequest;
import krafton.bookmark.application.exception.NotFoundEntityException;
import krafton.bookmark.domain.bookmark.Bookmark;
import krafton.bookmark.domain.bookmark.BookmarkRepository;
import krafton.bookmark.domain.member.Member;
import krafton.bookmark.domain.tag.Tag;
import krafton.bookmark.domain.tag.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

    @InjectMocks
    private BookmarkService bookmarkService;

    @Mock
    private BookmarkRepository bookmarkRepository;

    @Mock
    private TagRepository tagRepository;

    String testTitle = "testTitle";
    String testUrl = "testUrl";
    String testMemo = "testMemo";
    Member testAuthor = new Member("testAuthor", "1234");

    Bookmark testBookmark = new Bookmark(testTitle, testUrl, testMemo, testAuthor);
    BookmarkSaveRequest testReq = new BookmarkSaveRequest(
            testAuthor,
            testTitle,
            testUrl,
            testMemo
    );

    @Test
    void 북마크는_정확하게_저장되어야_한다() {
        //given
        given(bookmarkRepository.save(any())).willReturn(testBookmark);

        //when
        Bookmark saved = bookmarkService.save(testReq);

        //then
        assertThat(saved.getTitle()).isEqualTo(testTitle);
        assertThat(saved.getUrl()).isEqualTo(testUrl);
        assertThat(saved.getMemo()).isEqualTo(testMemo);
        assertThat(saved.getAuthor().getUsername()).isEqualTo(testAuthor.getUsername());
        then(bookmarkRepository).should(times(1)).save(any());
    }

    @Test
    void 북마크_업데이트_시_변경이_정확하게_일어나야_한다() {
        //given
        String updateTitle = "update complete";
        String updateUrl = "update complete";
        String updateMemo = "update complete";
        Tag testTag = new Tag("testTag", testAuthor);
        Long testTagId = 1L;
        given(tagRepository.findByIdAndAuthor(testTagId, testAuthor)).willReturn(Optional.of(testTag));
        given(bookmarkRepository.findWithTag(any(), eq(testAuthor))).willReturn(Optional.of(testBookmark));

        //when
        bookmarkService.update(new BookmarkUpdateRequest(
                testAuthor,
                testBookmark.getId(),
                updateTitle,
                updateUrl,
                updateMemo,
                testTagId
        ));

        //then
        assertThat(testBookmark.getTitle()).isEqualTo(updateTitle);
        assertThat(testBookmark.getMemo()).isEqualTo(updateMemo);
        assertThat(testBookmark.getUrl()).isEqualTo(updateUrl);
        assertThat(testBookmark.getTag().getName()).isEqualTo(testTag.getName());

        then(bookmarkRepository).should(times(1)).findWithTag(any(), eq(testAuthor));
        then(tagRepository).should(times(1)).findByIdAndAuthor(testTagId, testAuthor);
    }

    @Test
    void 북마크_업데이트_시_null로_전달받은_필드는_업데이트하지_않는다() {
        //given
        String updateUrl = "update complete";
        given(bookmarkRepository.findWithTag(any(), eq(testAuthor))).willReturn(Optional.of(testBookmark));

        //when
        bookmarkService.update(new BookmarkUpdateRequest(
                testAuthor,
                testBookmark.getId(),
                null,
                updateUrl,
                null,
                null
        ));

        //then
        assertThat(testBookmark.getTitle()).isEqualTo(testTitle);
        assertThat(testBookmark.getMemo()).isEqualTo(testMemo);
        assertThat(testBookmark.getUrl()).isEqualTo(updateUrl);
        assertThat(testBookmark.getTag()).isNull();

        then(bookmarkRepository).should(times(1)).findWithTag(any(), eq(testAuthor));
    }

    @Test
    void 북마크_업데이트_시_없는_태그를_전달받으면_예외를_던진다() {
        //GIVEN
        given(bookmarkRepository.findWithTag(any(), any())).willReturn(Optional.empty());

        //WHEN & then
        assertThrows(NotFoundEntityException.class, () -> bookmarkService.update(new BookmarkUpdateRequest(
                testAuthor,
                testBookmark.getId(),
                null,
                null,
                null,
                null
        )));
    }

    @Test
    void 북마크_업데이트_시_없는_북마크를_전달받으면_예외를_던진다() {
        //GIVEN
        given(bookmarkRepository.findWithTag(any(), any())).willReturn(Optional.empty());

        //WHEN & then
        assertThrows(NotFoundEntityException.class, () -> bookmarkService.update(new BookmarkUpdateRequest(
                testAuthor,
                testBookmark.getId(),
                null,
                null,
                null,
                null
        )));
    }

    @Test
    void 북마크_하나를_찾을_때_정확한_응답을_보내야한다() {
        //given
        Long testId = 1L;
        given(bookmarkRepository.findWithTag(testId, testAuthor)).willReturn(Optional.of(testBookmark));

        //when
        BookmarkResponse response = bookmarkService.findOne(testAuthor, testId);

        //then
        assertThat(response.url()).isEqualTo(testBookmark.getUrl());
        assertThat(response.title()).isEqualTo(testBookmark.getTitle());
        assertThat(response.memo()).isEqualTo(testBookmark.getMemo());
        assertThat(response.tag()).isNull();
    }

    @Test
    void 없는_북마크를_찾으면_예외가_발생한다() {
        //given
        Long testId = 1L;
        given(bookmarkRepository.findWithTag(testId, testAuthor)).willReturn(Optional.empty());

        //when & then
        assertThrows(NotFoundEntityException.class, () -> bookmarkService.findOne(testAuthor, testId));
    }

    @Test
    void 북마크를_검색할_때_정확한_페이지가_반환되어야_한다() {
        //given
        Long tagId = 1L;
        String testSearchTitle = "testTitle";
        BookmarkQuery query = new BookmarkQuery(testSearchTitle, null, tagId);
        Pageable pageable = PageRequest.of(0, 10);
        Tag mockTag = new Tag("testTag", testAuthor);
        List<Bookmark> bookmarks = List.of(new Bookmark(testSearchTitle, "testUrl", "memo", testAuthor));
        Page<Bookmark> mockPage = new PageImpl<>(bookmarks, pageable, bookmarks.size());

        given(tagRepository.findByIdAndAuthor(tagId, testAuthor)).willReturn(Optional.of(mockTag));
        given(bookmarkRepository.findBookmarkPagesByQuery(query.title(), query.url(), mockTag, testAuthor, pageable))
                .willReturn(mockPage);

        //when
        Page<BookmarkResponse> result = bookmarkService.findBookmarkPagesByQuery(query, testAuthor, pageable);

        //then
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).title()).isEqualTo(testSearchTitle);
        then(tagRepository).should(times(1)).findByIdAndAuthor(tagId, testAuthor);
        then(bookmarkRepository).should(times(1)).findBookmarkPagesByQuery(query.title(), query.url(), mockTag, testAuthor, pageable);
    }

    @Test
    void 북마크_검색_결과가_없을_때_빈_페이지를_반환해야_한다() {
        //given
        BookmarkQuery query = new BookmarkQuery("no-such-title", null, null);
        Pageable pageable = PageRequest.of(0, 10);
        given(bookmarkRepository.findBookmarkPagesByQuery(query.title(), query.url(), null, testAuthor, pageable))
                .willReturn(Page.empty(pageable));

        //when
        Page<BookmarkResponse> result = bookmarkService.findBookmarkPagesByQuery(query, testAuthor, pageable);

        //then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void 북마크_검색_시_없는_태그ID를_사용하면_예외를_던져야_한다() {
        //given
        Long nonExistentTagId = 3020L;
        BookmarkQuery query = new BookmarkQuery(null, null, nonExistentTagId);
        Pageable pageable = PageRequest.of(0, 10);
        given(tagRepository.findByIdAndAuthor(nonExistentTagId, testAuthor)).willReturn(Optional.empty());

        //when & then
        assertThrows(NotFoundEntityException.class, () -> {
            bookmarkService.findBookmarkPagesByQuery(query, testAuthor, pageable);
        });
        then(bookmarkRepository).should(never()).findBookmarkPagesByQuery(any(), any(), any(), any(), any());
    }
}
