package krafton.bookmark.domain.bookmark.entity;

import jakarta.persistence.*;
import krafton.bookmark.common.BaseEntity;
import krafton.bookmark.domain.tag.Tag;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@IdClass(TaggedBookmarkId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaggedBookmark extends BaseEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookmark_id")
    private Bookmark bookmark;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

}
