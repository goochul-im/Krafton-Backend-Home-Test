package krafton.bookmark.domain.bookmark.entity.repository;

import krafton.bookmark.domain.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
}
