package krafton.bookmark.domain.bookmark;

import krafton.bookmark.domain.member.Member;
import krafton.bookmark.domain.member.MemberRepository;
import krafton.bookmark.domain.tag.Tag;
import krafton.bookmark.domain.tag.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookmarkRepositoryTest {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TagRepository tagRepository;

    private Member author;
    private Tag tag1;
    private Tag tag2;

    final String testUsername = "test@test.com";
    final String testPassword = "password";
    final String tag1Name = "tag1";
    final String tag2Name = "tag2";

    final String title1 = "title1";
    final String title2 = "title2";
    final String memo1 = "memo1";
    final String memo2 = "memo2";
    final String memo3 = "memo3";

    final String url1 = "http://url1.com";
    final String url2 = "http://url2.com";
    final String url3 = "http://url3.com";

    @BeforeEach
    void setUp() {
        author = memberRepository.save(new Member(testUsername, testPassword));
        tag1 = tagRepository.save(new Tag(tag1Name, author));
        tag2 = tagRepository.save(new Tag(tag2Name, author));

        Bookmark bookmark1 = new Bookmark(title1, url1, memo1, author);
        bookmark1.update(null, null, null, tag1);
        bookmarkRepository.save(bookmark1);

        Bookmark bookmark2 = new Bookmark(title2, url2, memo2, author);
        bookmark2.update(null, null, null, tag1);
        bookmarkRepository.save(bookmark2);

        Bookmark bookmark3 = new Bookmark(title1, url3, memo3, author);
        bookmark3.update(null, null, null, tag2);
        bookmarkRepository.save(bookmark3);
    }

    @DisplayName("모든 조건이 null일 때 모든 북마크를 페이지네이션하여 조회한다")
    @Test
    void findBookmarkPagesByQuery_withNoConditions() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 5);

        // when
        Page<Bookmark> result = bookmarkRepository.findBookmarkPagesByQuery(null, null, null, author, pageRequest);

        // then
        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getContent()).hasSize(3);
    }

    @DisplayName("제목으로 북마크를 필터링하여 조회한다")
    @Test
    void findBookmarkPagesByQuery_withTitle() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 5);

        // when
        Page<Bookmark> result = bookmarkRepository.findBookmarkPagesByQuery(title1, null, null, author, pageRequest);

        // then
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).extracting("title").containsOnly(title1); // 리스트를 순회하며 title에 오직 "title1"만 들엇는지 확인
    }

    @DisplayName("URL로 북마크를 필터링하여 조회한다")
    @Test
    void findBookmarkPagesByQuery_withUrl() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 5);

        // when
        Page<Bookmark> result = bookmarkRepository.findBookmarkPagesByQuery(null, url2, null, author, pageRequest);

        // then
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getUrl()).isEqualTo(url2);
    }

    @DisplayName("Tag 객체로 북마크를 필터링하여 조회한다")
    @Test
    void findBookmarkPagesByQuery_withTag() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 5);

        // when
        Page<Bookmark> result = bookmarkRepository.findBookmarkPagesByQuery(null, null, tag1, author, pageRequest);

        // then
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).extracting("tag").containsOnly(tag1);
    }

    @DisplayName("여러 조건을 조합하여 북마크를 필터링하여 조회한다")
    @Test
    void findBookmarkPagesByQuery_withMultipleConditions() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 5);

        // when
        Page<Bookmark> result = bookmarkRepository.findBookmarkPagesByQuery(title1, null, tag2, author, pageRequest);

        // then
        assertThat(result.getTotalElements()).isEqualTo(1);
        Bookmark found = result.getContent().get(0);
        assertThat(found.getTitle()).isEqualTo(title1);
        assertThat(found.getTag()).isEqualTo(tag2);
    }

    @DisplayName("페이지네이션이 올바르게 동작하는지 확인한다")
    @Test
    void findBookmarkPagesByQuery_withPagination() {
        // given
        PageRequest pageRequest = PageRequest.of(1, 2); // 2번째 페이지, 페이지 당 2개

        // when
        Page<Bookmark> result = bookmarkRepository.findBookmarkPagesByQuery(null, null, null, author, pageRequest);

        // then
        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getTotalPages()).isEqualTo(2);
        assertThat(result.getContent()).hasSize(1);
    }

    @DisplayName("검색 결과가 없을 때 빈 페이지를 반환한다")
    @Test
    void findBookmarkPagesByQuery_withNoResults() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 5);

        // when
        Page<Bookmark> result = bookmarkRepository.findBookmarkPagesByQuery("non-existing-title", null, null, author, pageRequest);

        // then
        assertThat(result.getTotalElements()).isEqualTo(0);
        assertThat(result.getContent()).isEmpty();
    }
}
