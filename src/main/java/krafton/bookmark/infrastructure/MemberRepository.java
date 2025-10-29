package krafton.bookmark.infrastructure;

import krafton.bookmark.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUsername(String username);
    int countByUsername(String username);
}
