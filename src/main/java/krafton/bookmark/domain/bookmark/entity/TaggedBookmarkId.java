package krafton.bookmark.domain.bookmark.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TaggedBookmarkId implements Serializable {

    private Long bookmark;
    private Long tag;

}
