package krafton.bookmark.common;

import krafton.bookmark.domain.bookmark.Bookmark;
import krafton.bookmark.infrastructure.BookmarkRepository;
import krafton.bookmark.domain.member.Member;
import krafton.bookmark.infrastructure.MemberRepository;
import krafton.bookmark.domain.tag.Tag;
import krafton.bookmark.infrastructure.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;
    private final TagRepository tagRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (memberRepository.count() == 0) {
            makeUser1();
            makerUser2();
        }
    }

    private void makeUser1() {
        Member user = memberRepository.save(new Member("user", passwordEncoder.encode("1111")));

        Tag tag1 = tagRepository.save(new Tag("개발", user));
        Tag tag2 = tagRepository.save(new Tag("일상", user));
        Tag tag3 = tagRepository.save(new Tag("뉴스", user));

        Bookmark bookmark1 = new Bookmark("구글", "https://www.google.com", "검색 엔진", user);
        bookmark1.update(null, null, null, tag1);
        bookmarkRepository.save(bookmark1);

        Bookmark bookmark2 = new Bookmark("네이버", "https://www.naver.com", "포털 사이트", user);
        bookmark2.update(null, null, null, tag2);
        bookmarkRepository.save(bookmark2);

        Bookmark bookmark3 = new Bookmark("다음", "https://www.daum.net", "포털 사이트", user);
        bookmark3.update(null, null, null, tag3);
        bookmarkRepository.save(bookmark3);

        Bookmark bookmark4 = new Bookmark("스프링 공식 문서", "https://spring.io/docs", "스프링 프레임워크 문서", user);
        bookmark4.update(null, null, null, tag1);
        bookmarkRepository.save(bookmark4);

        Bookmark bookmark5 = new Bookmark("유튜브", "https://www.youtube.com", "동영상 플랫폼", user);
        bookmark5.update(null, null, null, tag2);
        bookmarkRepository.save(bookmark5);
    }

    private void makerUser2() {
        Member user2 = memberRepository.save(new Member("user2", passwordEncoder.encode("1111")));

        Tag user2Tag1 = tagRepository.save(new Tag("업무", user2));
        Tag user2Tag2 = tagRepository.save(new Tag("취미", user2));

        Bookmark user2Bookmark1 = new Bookmark("GitHub", "https://github.com", "코드 저장소", user2);
        user2Bookmark1.update(null, null, null, user2Tag1);
        bookmarkRepository.save(user2Bookmark1);

        Bookmark user2Bookmark2 = new Bookmark("Reddit", "https://www.reddit.com", "커뮤니티", user2);
        user2Bookmark2.update(null, null, null, user2Tag2);
        bookmarkRepository.save(user2Bookmark2);

        Bookmark user2Bookmark3 = new Bookmark("Stack Overflow", "https://stackoverflow.com", "개발자 Q&A", user2);
        user2Bookmark3.update(null, null, null, user2Tag1);
        bookmarkRepository.save(user2Bookmark3);

        Bookmark user2Bookmark4 = new Bookmark("Netflix", "https://www.netflix.com", "스트리밍 서비스", user2);
        bookmarkRepository.save(user2Bookmark4);
    }
}
