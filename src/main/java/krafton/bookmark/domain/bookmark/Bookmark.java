package krafton.bookmark.domain.bookmark;

import jakarta.persistence.*;
import krafton.bookmark.common.BaseEntity;
import krafton.bookmark.application.dto.BookmarkResponse;
import krafton.bookmark.domain.member.Member;
import krafton.bookmark.domain.tag.Tag;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column
    private String url;
    @Column
    private String memo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Member author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = true)
    private Tag tag;

    public Bookmark(String title, String url, String memo, Member author) {
        this.title = title;
        this.url = url;
        this.memo = memo;
        this.author = author;
    }

    public void update(String url, String title, String memo, Tag tag) {

        if (url != null) {
            this.url = url;
        }

        if (title != null) {
            this.title = title;
        }

        if (tag != null) {
            this.tag = tag;
        }

        if (memo != null) {
            this.memo = memo;
        }

    }

    public BookmarkResponse toDto() {
        return new BookmarkResponse(
                this.id,
                this.title,
                this.url,
                this.memo,
                this.getCreateAt(),
                this.getUpdateAt(),
                this.tag != null ? this.tag.toDto() : null
        );
    }
}
