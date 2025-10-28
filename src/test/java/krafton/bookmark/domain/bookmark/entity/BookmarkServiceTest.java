package krafton.bookmark.domain.bookmark.entity;

import krafton.bookmark.domain.bookmark.Bookmark;
import krafton.bookmark.domain.bookmark.BookmarkRepository;
import krafton.bookmark.domain.bookmark.BookmarkService;
import krafton.bookmark.domain.bookmark.dto.BookmarkSaveRequest;
import krafton.bookmark.domain.member.Member;
import krafton.bookmark.domain.tag.Tag;
import krafton.bookmark.domain.tag.TagRepository;
import org.assertj.core.api.Assertions;
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
        Tag testTag = new Tag("testTag", testAuthor);
        given(tagRepository.findByIdAndAuthor(any(), eq(testAuthor))).willReturn(Optional.of(testTag));
        given(bookmarkRepository.findByIdAndAuthor(any(), eq(testAuthor))).willReturn(Optional.of(testBookmark));

        //when
        BookmarkResponse updated = bookmarkService.update(new BookmarkUpdateReq());

        //then
    }

}
