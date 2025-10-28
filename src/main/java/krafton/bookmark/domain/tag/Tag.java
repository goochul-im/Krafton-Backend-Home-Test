package krafton.bookmark.domain.tag;

import jakarta.persistence.*;
import krafton.bookmark.common.BaseEntity;
import krafton.bookmark.domain.member.Member;
import krafton.bookmark.domain.tag.dto.TagResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Member author;

    public Tag(String name, Member author) {
        this.name = name;
        this.author = author;
    }

    public TagResponse toDto() {
        return new TagResponse(
                this.id,
                this.name,
                this.getCreateAt(),
                this.getUpdateAt()
        );
    }

    public void updateName(String name) {
        this.name = name;
    }
}
