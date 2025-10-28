package krafton.bookmark.application.service;

import krafton.bookmark.application.exception.NotFoundEntityException;
import krafton.bookmark.domain.member.Member;
import krafton.bookmark.application.exception.AlreadyExistException;
import krafton.bookmark.application.dto.TagResponse;
import krafton.bookmark.application.dto.TagSaveRequest;
import krafton.bookmark.application.dto.TagUpdateRequest;
import krafton.bookmark.domain.tag.Tag;
import krafton.bookmark.domain.tag.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @InjectMocks
    private TagService tagService;

    @Mock
    private TagRepository tagRepository;

    String username = "user33";

    Member testAuthor = new Member(
            username,
            null
    );

    String testTagName = "tagName";
    TagSaveRequest req = new TagSaveRequest(
            testTagName,
            testAuthor
    );

    Tag testTag = new Tag(
            testTagName,
            testAuthor
    );

    @Test
    void 없는_태그를_찾으면_예외를_던진다(){
        //given
        given(tagRepository.findByIdAndAuthor(any(), any())).willReturn(Optional.empty());

        //when & then
        assertThrows(NotFoundEntityException.class, () -> tagService.findOne(testAuthor, 30020L));
    }

    @Test
    void 태그는_정확하게_생성되어야한다(){



        //given
        given(tagRepository.save(any())).willReturn(testTag);

        //when
        Tag saved = tagService.save(req);

        //then
        assertThat(saved.getName()).isEqualTo(testTagName);
        assertThat(saved.getAuthor().getUsername()).isEqualTo(testAuthor.getUsername());

        then(tagRepository).should(times(1)).save(any());
    }

    @Test
    void 같은_회원이_같은_이름을_가진_태그를_생성할때_예외가_발생해야_한다(){
        //given
        given(tagRepository.countByAuthorAndName(testAuthor, testTagName)).willReturn(1);

        //when & then
        assertThrows(AlreadyExistException.class, () -> tagService.save(req));
        then(tagRepository).should(times(1)).countByAuthorAndName(testAuthor, testTagName);
        then(tagRepository).should(times(0)).save(any());
    }

    @Test
    void 회원이_생성한_태그만_모두_가져와야_한다() {
        //GIVEN
        List<Tag> tags = List.of(new Tag("tag1", testAuthor), new Tag("tag2", testAuthor), new Tag("tag3", testAuthor));
        given(tagRepository.findAllByAuthor(testAuthor)).willReturn(tags);

        //WHEN
        List<TagResponse> finds = tagService.findAll(testAuthor);

        //THEN
        assertThat(finds.size()).isEqualTo(3);
        assertThat(finds).extracting("name").containsExactlyInAnyOrder("tag1", "tag2", "tag3");
        then(tagRepository).should(times(1)).findAllByAuthor(testAuthor);
    }

    @Test
    void 존재하지_않는_태그는_삭제할수_없다(){
        //given
        Long testId = 302L;
        given(tagRepository.existsByIdAndAuthor(testId, testAuthor)).willReturn(false);

        //when & then
        assertThrows(NotFoundEntityException.class, () -> tagService.delete(testAuthor, testId));
    }

    @Test
    void 태그_업데이트는_이름_외에는_아무것도_바뀌지_않는다(){
        Tag tempTag = new Tag(
                "temp",
                testAuthor
        );
        //given
        String updateName = "update complete";
        given(tagRepository.findByIdAndAuthor(any(), eq(testAuthor))).willReturn(Optional.of(tempTag));

        //when
        tagService.updateName(new TagUpdateRequest(
                tempTag.getId(),
                testAuthor,
                updateName
        ));

        //then
        assertThat(tempTag.getName()).isEqualTo(updateName);
        assertThat(tempTag.getAuthor()).isEqualTo(testAuthor);
    }

    @Test
    void 태그_업데이트_시_태그를_찾지_못하면_예외를_던진다(){
        //given
        given(tagRepository.findByIdAndAuthor(any(), eq(testAuthor))).willReturn(Optional.empty());

        //when & then
        assertThrows(NotFoundEntityException.class, () -> tagService.updateName(new TagUpdateRequest(
                100200L,
                testAuthor,
                "없는 태그"
        )));
    }
}
