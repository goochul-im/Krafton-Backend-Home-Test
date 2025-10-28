package krafton.bookmark.domain.bookmark;

import krafton.bookmark.domain.bookmark.dto.BookmarkResponse;
import krafton.bookmark.domain.bookmark.dto.BookmarkSaveRequest;
import krafton.bookmark.domain.bookmark.dto.BookmarkUpdateRequest;
import krafton.bookmark.domain.exception.NotFoundEntityException;
import krafton.bookmark.domain.member.Member;
import krafton.bookmark.domain.tag.Tag;
import krafton.bookmark.domain.tag.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
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
    void 북마크는_정확하게_저장되어야_한다(){
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
    void 북마크_업데이트_시_변경이_정확하게_일어나야_한다(){
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
    void 북마크_업데이트_시_null로_전달받은_필드는_업데이트하지_않는다(){
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
    void 북마크_하나를_찾을_때_정확한_응답을_보내야한다(){
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
    void 없는_북마크를_찾으면_예외가_발생한다(){
        //given
        Long testId = 1L;
        given(bookmarkRepository.findWithTag(testId, testAuthor)).willReturn(Optional.empty());

        //when & then
        assertThrows(NotFoundEntityException.class, () -> bookmarkService.findOne(testAuthor, testId));
    }
}
