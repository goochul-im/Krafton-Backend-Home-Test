package krafton.bookmark.domain.tag;

import krafton.bookmark.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    int countByAuthorAndName(Member author, String name);
    List<Tag> findAllByAuthor(Member author);
    boolean existsByIdAndAuthor(Long id, Member author);
    Optional<Tag> findByIdAndAuthor(Long id, Member author);

}
