package krafton.bookmark.domain.bookmark;

import krafton.bookmark.domain.member.Member;
import krafton.bookmark.domain.tag.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @Query("SELECT b FROM Bookmark b " +
            "LEFT JOIN FETCH b.tag t " +
            "WHERE b.id = :id AND b.author = :author")
    Optional<Bookmark> findWithTag(@Param("id") Long id, @Param("author") Member author);

    @Query(
            value = "select b from Bookmark b " +
                    "left join fetch b.tag t " +
                    "where b.author = :author " +
                    "and (:title is null or LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
                    "and (:url is null or LOWER(b.url) LIKE LOWER(CONCAT('%', :url, '%'))) " +
                    "and (:tag is null or b.tag = :tag)",

            countQuery = "select count(b.id) from Bookmark b " +
                    "where b.author = :author " +
                    "and (:title is null or LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
                    "and (:url is null or LOWER(b.url) LIKE LOWER(CONCAT('%', :url, '%'))) " +
                    "and (:tag is null or b.tag = :tag)"
    )
    Page<Bookmark> findBookmarkPagesByQuery(
            @Param("title") String title,
            @Param("url") String url,
            @Param("tag") Tag tag,
            @Param("author") Member author,
            Pageable pageable);

    Optional<Bookmark> findByIdAndAuthor(Long id, Member author);

    @Query("select b from Bookmark b " +
            "left join fetch b.tag t " +
            "where b.author = :author " +
            "order by b.id desc")
    List<Bookmark> findAllByAuthor(Member author);

}
