package krafton.bookmark.domain.tag;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import krafton.bookmark.domain.exception.AlreadyExistException;
import krafton.bookmark.domain.exception.NotFoundEntityException;
import krafton.bookmark.domain.member.Member;
import krafton.bookmark.domain.tag.dto.TagResponse;
import krafton.bookmark.domain.tag.dto.TagSaveRequest;
import krafton.bookmark.domain.tag.dto.TagUpdateReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public Tag save(TagSaveRequest req) {
        if (tagRepository.countByAuthorAndName(req.member(), req.tagName()) > 0) {
            log.error("이미 존재하는 태그 생성 시도. name = {}", req.tagName());
            throw new AlreadyExistException("이미 존재하는 태그 이름입니다.");
        }

        return tagRepository.save(new Tag(req.tagName(), req.member()));
    }

    public List<TagResponse> findAll(Member author) {

        List<Tag> list = tagRepository.findAllByAuthor(author);
        List<TagResponse> result = new ArrayList<>();

        for (Tag tag : list) {
            result.add(tag.toDto());
        }

        return result;
    }

    @Transactional
    public void delete(Member author, Long id) {
        if (!tagRepository.existsByIdAndAuthor(id, author)) {
            log.error("태그 삭제 실패! id : {}, author = {}", id, author.getId());
            throw new NotFoundEntityException("해당 id의 태그가 없습니다.");
        }

        tagRepository.deleteById(id);
    }

    @Transactional
    public void updateName(TagUpdateReq req) {
        Tag find = tagRepository.findByIdAndAuthor(req.id(), req.author()).orElseThrow(
                () -> {
                    log.error("태그 업데이트 실패! id : {}, author = {}", req.id(), req.author().getId());
                    return new NotFoundEntityException("업데이트할 태그를 찾지 못했습니다");
                }
        );
        find.updateName(req.updateName());
    }

    public Tag findOne(Member author, Long id) {
        return tagRepository.findByIdAndAuthor(id, author).orElseThrow(() -> {
            log.error("엔티티를 찾을 수 없습니다. id = {}, author = {}", id, author.getId());
            return new NotFoundEntityException("해당 엔티티가 없습니다");
        });
    }
}
