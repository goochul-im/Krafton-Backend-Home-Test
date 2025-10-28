package krafton.bookmark.domain.bookmark.entity.repository;

import krafton.bookmark.domain.bookmark.entity.TaggedBookmark;
import krafton.bookmark.domain.bookmark.entity.TaggedBookmarkId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaggedBookmarkRepository extends JpaRepository<TaggedBookmark, TaggedBookmarkId> {
}
