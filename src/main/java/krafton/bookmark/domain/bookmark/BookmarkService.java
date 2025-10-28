package krafton.bookmark.domain.bookmark;

import krafton.bookmark.domain.bookmark.dto.BookmarkSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    public Bookmark save(BookmarkSaveRequest req) {

        return bookmarkRepository.save(req.toEntity());
    }


}
